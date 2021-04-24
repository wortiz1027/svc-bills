package co.com.demo.bills.application;

import co.com.demo.bills.domain.controller.Bill;

import java.util.List;
import java.util.Optional;

public interface BillBehavior {

    Optional<Bill> getBillById(String sequence);
    Optional<List<Bill>> getAllBills();

}
