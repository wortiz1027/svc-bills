package co.com.demo.bills.infraestructure.repository;

import co.com.demo.bills.domain.database.Bill;
import co.com.demo.bills.domain.database.Detail;

import java.util.List;
import java.util.Optional;

public interface BillRepository {

    Optional<Bill> findById(String sequence);
    Optional<List<Detail>> findAllDetail(String id);
    Optional<List<Bill>> findAlls();

}
