package co.com.demo.bills.domain.database;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bill implements java.io.Serializable {

    private String id;
    private String sequence;
    private LocalDate date;
    private String clientId;
    private double total;

}
