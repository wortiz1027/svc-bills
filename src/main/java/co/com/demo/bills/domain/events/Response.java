package co.com.demo.bills.domain.events;

import co.com.demo.bills.domain.controller.Client;
import lombok.Data;

import java.util.List;

@Data
public class Response implements java.io.Serializable {

    private List<Client> clients;

}