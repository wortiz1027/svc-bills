package co.com.demo.bills.infraestructure.repository.mappers;

import co.com.demo.bills.domain.database.Bill;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BillRowMapper implements RowMapper<Bill> {

    @Override
    public Bill mapRow(ResultSet rs, int i) throws SQLException {
        return new Bill(
                rs.getString("BILL_ID"),
                rs.getString("BILL_SEQUENCE"),
                rs.getDate("BILL_DATETIME").toLocalDate(),
                rs.getString("CLIENT_DNI"),
                rs.getBigDecimal("BILL_TOTAL"));
    }
}
