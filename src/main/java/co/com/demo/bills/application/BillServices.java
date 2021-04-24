package co.com.demo.bills.application;

import co.com.demo.bills.domain.controller.Client;
import co.com.demo.bills.domain.controller.Product;
import co.com.demo.bills.domain.database.Bill;
import co.com.demo.bills.domain.database.Detail;
import co.com.demo.bills.exceptions.bussines.BillNotFoundException;
import co.com.demo.bills.infraestructure.repository.BillRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BillServices implements BillBehavior {
    private static final Logger LOG = LoggerFactory.getLogger(BillServices.class);
    private final BillRepository repository;

    @Override
    public Optional<co.com.demo.bills.domain.controller.Bill> getBillById(String sequence) {
        Optional<Bill> bill = this.repository.findById(sequence);

        if (!bill.isPresent()) throw new BillNotFoundException(String.format("There is no information for bill with sequence: [%s]", sequence));

        Optional<List<Detail>> details = this.repository.findAllDetail(bill.get().getId());

        if (details.isEmpty()) throw new BillNotFoundException(String.format("There is no products information for bill with sequence: [%s]", sequence));

        // TODO hacer llamado queue informacion cliente

        Client client = new Client();
        client.setId(bill.get().getClientId());
        //client.setDni();
        //client.setName();
        //client.setEmail();
        //client.setPhone();

        List<co.com.demo.bills.domain.controller.Detail> detailList = new ArrayList<>();

        for (Detail row : details.get()) {
            // TODO hacer llamado queue informacion de producto
            Product product = new Product();
            product.setCode(row.getProductCode());

            co.com.demo.bills.domain.controller.Detail item = new co.com.demo.bills.domain.controller.Detail();
            item.setId(row.getId());
            item.setBillId(row.getBillId());
            item.setProduct(product);
            item.setDiscount(row.getDiscount());
            item.setQuantity(row.getQuantity());

            detailList.add(item);
        }

        co.com.demo.bills.domain.controller.Bill response = new co.com.demo.bills.domain.controller.Bill();
        response.setId(bill.get().getId());
        response.setSequence(bill.get().getSequence());
        response.setDate(bill.get().getDate());
        response.setClient(client);
        response.setDetail(detailList);

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
            client.setId(row.getClientId());

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
            item.setDetail(detailList);

            billList.add(item);
        }

        return Optional.of(billList);
    }
}
