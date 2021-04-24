package co.com.demo.bills.domain.controller;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Bill implements java.io.Serializable {

    private String id;
    private String sequence;
    private LocalDate date;
    private Client client;
    private double total;
    private List<Detail> detail;

}
