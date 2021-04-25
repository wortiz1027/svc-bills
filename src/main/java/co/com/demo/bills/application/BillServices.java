package co.com.demo.bills.application;

import co.com.demo.bills.domain.events.Request;
import co.com.demo.bills.domain.controller.Client;
import co.com.demo.bills.domain.controller.Product;
import co.com.demo.bills.domain.database.Bill;
import co.com.demo.bills.domain.database.Detail;
import co.com.demo.bills.domain.events.Response;
import co.com.demo.bills.exceptions.bussines.BillNotFoundException;
import co.com.demo.bills.infraestructure.repository.BillRepository;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate.RabbitConverterFuture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillServices implements BillBehavior {

    private static final Logger LOG = LoggerFactory.getLogger(BillServices.class);

    @Value("${events.rpc.clients.exchange}")
    private String rpcClientsExchange;

    @Value("${events.rpc.clients.routing-key}")
    private String rpcClientsRoutingKey;

    @Value("${rest.products.uri}")
    private String productsUri;

    @Value("${rest.products.services-id}")
    private String servicesId;

    private final BillRepository repository;
    private final AsyncRabbitTemplate template;
    private final DiscoveryClient discoveryClient;
    private final RestTemplate restTemplate;

    @Override
    public Optional<co.com.demo.bills.domain.controller.Bill> getBillById(String sequence) {
        Optional<Bill> bill = this.repository.findById(sequence);

        if (!bill.isPresent()) throw new BillNotFoundException(String.format("There is no information for bill with sequence: [%s]", sequence));

        Optional<List<Detail>> details = this.repository.findAllDetail(bill.get().getId());

        if (details.isEmpty()) throw new BillNotFoundException(String.format("There is no products information for bill with sequence: [%s]", sequence));

        List<String> dnis = new ArrayList<>();
        dnis.add(bill.get().getClientDni());

        List<Client> clients = callClientsInformation(dnis);

        Client client = new Client();
        client.setId(clients.get(0).getId());
        client.setDni(clients.get(0).getDni());
        client.setName(clients.get(0).getName());
        client.setEmail(clients.get(0).getEmail());
        client.setPhone(clients.get(0).getPhone());

        List<co.com.demo.bills.domain.controller.Detail> detailList = new ArrayList<>();

        String ids = details.get()
                            .stream()
                            .map(Detail::getProductCode)
                            .collect(Collectors.joining(","));

        Optional<List<Product>> products = callProductsInformation(ids);

        for (Detail row : details.get()) {
            co.com.demo.bills.domain.controller.Detail item = new co.com.demo.bills.domain.controller.Detail();
            item.setId(row.getId());
            item.setBillId(row.getBillId());
            item.setProduct(products.get()
                                    .stream()
                                    .filter(product -> product.getCode().equals(row.getProductCode()))
                                    .findAny()
                                    .orElse(null));
            item.setDiscount(row.getDiscount());
            item.setQuantity(row.getQuantity());

            detailList.add(item);
        }

        co.com.demo.bills.domain.controller.Bill response = new co.com.demo.bills.domain.controller.Bill();
        response.setId(bill.get().getId());
        response.setSequence(bill.get().getSequence());
        response.setDate(bill.get().getDate());
        response.setClient(client);
        response.setProducts(detailList);
        response.setTotal(bill.get().getTotal());

        return Optional.of(response);
    }

    @Override
    public Optional<List<co.com.demo.bills.domain.controller.Bill>> getAllBills() {
        Optional<List<Bill>> bills = this.repository.findAlls();

        if (bills.isEmpty()) throw new BillNotFoundException("There is no bills informations");

        List<co.com.demo.bills.domain.controller.Bill> billList = new ArrayList<>();

        for (Bill row : bills.get()) {
            List<co.com.demo.bills.domain.controller.Detail> detailList = new ArrayList<>();

            // TODO consultar informacion de clientes
            Client client = new Client();
            client.setId(row.getClientDni());

            Optional<List<Detail>> details = this.repository.findAllDetail(row.getId());

            LOG.debug("SEQUENCE: {} | PRODUCTS: {}", row.getSequence(), details.get().size());

            for (Detail rowd : details.get()) {
                // TODO hacer llamado queue informacion de producto
                Product product = new Product();
                product.setCode(rowd.getProductCode());

                co.com.demo.bills.domain.controller.Detail item = new co.com.demo.bills.domain.controller.Detail();
                item.setId(rowd.getId());
                item.setBillId(rowd.getBillId());
                item.setProduct(product);
                item.setDiscount(rowd.getDiscount());
                item.setQuantity(rowd.getQuantity());

                detailList.add(item);
            }

            co.com.demo.bills.domain.controller.Bill item = new co.com.demo.bills.domain.controller.Bill();
            item.setId(row.getId());
            item.setSequence(row.getSequence());
            item.setDate(row.getDate());
            item.setClient(client);
            item.setProducts(detailList);

            billList.add(item);
        }

        return Optional.of(billList);
    }

    private List<Client> callClientsInformation(List<String> dnis) {
        String ids = "";

        for (String row : dnis) {
            ids = ids.concat(row).concat(",");
        }
        LOG.info("ID_CLIENTS: {}", ids);
        LOG.info(ids.substring(0, ids.length() - 1));

        Request request = new Request();
        request.setIds(ids.substring(0, ids.length() - 1));

        RabbitConverterFuture<Response> future = this.template.convertSendAndReceiveAsType(
                rpcClientsExchange,
                rpcClientsRoutingKey,
                request,
                new ParameterizedTypeReference<>() {});

        try {
            Response res = future.get();
            LOG.info("Message received: {}", res);

            return res.getClients();
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("Cannot get response.", e);
        }

        return null;
    }

    private Optional<List<Product>> callProductsInformation(String codes) {
        //List<ServiceInstance> instances = discoveryClient.getInstances(servicesId);
        //ServiceInstance serviceInstance = instances.get(0);
        //String url = serviceInstance.getUri().toString();
        String url = String.format("http://%s:8083/%s", servicesId, productsUri);
        //url = url + productsUri;

        ResponseEntity<co.com.demo.bills.domain.controller.Response> response = null;

        UriComponents builder = UriComponentsBuilder.fromHttpUrl(url)
                                                    .queryParam("codes", codes)
                                                    .build();

        try{
            response = restTemplate.exchange(builder.toUriString(),
                                                HttpMethod.POST,
                                                getHeaders(),
                                                co.com.demo.bills.domain.controller.Response.class);
        }catch (Exception ex) {
            LOG.error("Cannot get product response.", ex);
        }

        LOG.debug(">>>>>>>>>>> RESPONSE: {}", response.getBody());
        return Optional.of(response.getBody().getProducts());
    }

    private static HttpEntity<?> getHeaders() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        return new HttpEntity<>(headers);
    }


}
