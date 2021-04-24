package co.com.demo.bills.domain.controller;

import lombok.Data;

@Data
public class Client implements java.io.Serializable {

    private String id;
    private String dni;
    private String name;
    private String email;
    private String phone;

}