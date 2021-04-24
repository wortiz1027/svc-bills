package co.com.demo.bills.infraestructure.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BillController {

    @PostMapping("/bills")
    public ResponseEntity<String> detail(@RequestBody(required = true) String data) {
        return null;
    }

    @GetMapping("/bills/{number}")
    public ResponseEntity<String> bills(@RequestBody(required = true) String number) {
        return null;
    }

}
