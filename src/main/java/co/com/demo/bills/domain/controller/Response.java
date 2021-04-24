package co.com.demo.bills.domain.controller;

import lombok.Data;

import java.util.List;

@Data
public class Response implements java.io.Serializable {

    private List<Product> products;

}
