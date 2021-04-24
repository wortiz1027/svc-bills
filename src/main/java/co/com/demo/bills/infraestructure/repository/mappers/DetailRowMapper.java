package co.com.demo.bills.infraestructure.repository.mappers;

import co.com.demo.bills.domain.database.Detail;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DetailRowMapper implements RowMapper<Detail> {

    @Override
    public Detail mapRow(ResultSet resultSet, int i) throws SQLException {
        Detail detail = new Detail();

        detail.setId(resultSet.getString("DETAIL_ID"));
        detail.setBillId(resultSet.getString("BILL_ID"));
        detail.setProductCode(resultSet.getString("PRODUCT_CODE"));
        detail.setQuantity(resultSet.getDouble("QUANTITY"));
        detail.setDiscount(resultSet.getDouble("DISCOUNT"));

        return detail;
    }
}
