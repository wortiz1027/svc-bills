package co.com.demo.bills.domain.controller;

import lombok.Data;

@Data
public class Product implements java.io.Serializable {

    private String id;
    private String Code;
    private String name;
    private String description;
    private double price;

}
