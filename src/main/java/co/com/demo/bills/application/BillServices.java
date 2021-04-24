package co.com.demo.bills.application;

import co.com.demo.bills.domain.controller.Client;
import co.com.demo.bills.domain.controller.Product;
import co.com.demo.bills.domain.database.Bill;
import co.com.demo.bills.domain.database.Detail;
import co.com.demo.bills.infraestructure.repository.BillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BillServices implements BillBehavior {

    private final BillRepository repository;

    @Override
    public Optional<co.com.demo.bills.domain.controller.Bill> getBillById(String sequence) {

        Optional<Bill> bill = this.repository.findById(sequence);
        // TODO lanzar exception
        Optional<List<Detail>> details = this.repository.findAllDetail(bill.get().getId());
        // TODO lanzar exception

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
        return Optional.empty();
    }
}
