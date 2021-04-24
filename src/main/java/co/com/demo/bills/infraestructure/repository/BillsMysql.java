package co.com.demo.bills.infraestructure.repository;

import co.com.demo.bills.domain.database.Bill;
import co.com.demo.bills.domain.database.Detail;
import co.com.demo.bills.infraestructure.repository.mappers.BillRowMapper;
import co.com.demo.bills.infraestructure.repository.mappers.DetailRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BillsMysql implements BillRepository {

    private final JdbcTemplate template;

    @Override
    public Optional<Bill> findById(String sequence) {
        try {
            String sql = "SELECT * " +
                         "FROM BILLS B " +
                         "WHERE BILL_SEQUENCE = ?";

            return template.queryForObject(sql,
                                           new Object[]{sequence},
                                           (rs, rowNum) ->
                                                Optional.of(new Bill(
                                                                rs.getString("BILL_ID"),
                                                                rs.getString("BILL_SEQUENCE"),
                                                                rs.getDate("BILL_DATETIME").toLocalDate(),
                                                                rs.getString("CLIENT_DNI"),
                                                                rs.getBigDecimal("BILL_TOTAL")
                                                                )
                                                )
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<Detail>> findAllDetail(String id) {
        try {
            String sql = "SELECT * " +
                         "FROM BILL_DETAIL B " +
                         "WHERE BILL_ID = ?";

            List<Detail> details = template.query(sql, new Object[]{id}, new DetailRowMapper());

            return Optional.of(details);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<Bill>> findAlls() {
        try {
            String sql = "SELECT * " +
                         "FROM BILLS B " ;

            List<Bill> bills = template.query(sql, new BillRowMapper());
            return Optional.of(bills);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
