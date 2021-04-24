package co.com.demo.bills.domain.database;

import lombok.Data;

@Data
public class Detail implements java.io.Serializable {

    private String id;
    private String billId;
    private String productCode;
    private double quantity;
    private double discount;

}