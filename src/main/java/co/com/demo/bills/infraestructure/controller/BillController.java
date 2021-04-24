package co.com.demo.bills.infraestructure.controller;

import co.com.demo.bills.application.BillBehavior;
import co.com.demo.bills.domain.controller.Bill;
import co.com.demo.bills.exceptions.bussines.BillNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BillController {

    private final BillBehavior services;

    @GetMapping("/bills")
    public ResponseEntity<List<Bill>> detail() {
        Optional<List<Bill>> bills = this.services.getAllBills();

        if (bills.isEmpty()) throw new BillNotFoundException("There are not bills informations!");

        return new ResponseEntity<>(bills.get(), HttpStatus.OK);
    }

    @GetMapping("/bills/{sequence}")
    public ResponseEntity<Bill> bills(@PathVariable(required = true) String sequence) {
        Optional<Bill> bills = this.services.getBillById(sequence);

        if (!bills.isPresent()) throw new BillNotFoundException(String.format("There is no information for bill with sequence: [%s]", sequence));

        return new ResponseEntity<>(bills.get(), HttpStatus.OK);
    }

}
