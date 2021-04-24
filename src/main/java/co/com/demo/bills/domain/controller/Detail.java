package co.com.demo.bills.domain.controller;

import lombok.Data;

@Data
public class Detail implements java.io.Serializable {

    private String id;
    private String billId;
    private Product product;
    private double quantity;
    private double discount;

}
