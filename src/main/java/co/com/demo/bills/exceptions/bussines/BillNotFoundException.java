package co.com.demo.bills.exceptions.bussines;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BillNotFoundException extends RuntimeException {

    public BillNotFoundException() {
        super();
    }

    public BillNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public BillNotFoundException(String message) {
        super(message);
    }

    public BillNotFoundException(Throwable cause) {
        super(cause);
    }
}