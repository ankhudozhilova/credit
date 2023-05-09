package Repository;

import Entity.LoanOrder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Getter
@Component
@RequiredArgsConstructor
public class LoanOrderImplemented implements LoanOrderRepository{
    private JdbcTemplate jdbcTemplate;

    public static final String ORDER_NOT_FOUND = "Order not found";
    private static final String FIND_LOAN_ORDER_BY_ID = "select * from loan_order where user_id=? and tariff_id=?";

    private static final String FIND_ALL_LOAN_ORDERS = "select * from loan_order";
    private static final String GET_ORDER_STATUS = "select * from loan_order where order_id=?";

    private static final String GET_ORDER_BY_USER_AND_ORDER_ID = "select * from loan_order where user_id=? and order_id=?";

    private static final String DELETE_ORDER = "delete from loan_order where user_id=? and order_id=?";
    private static final String SAVE_ORDER = "insert into loan_order(order_id, user_id, tariff_id, credit_rating, status, time_insert, time_update) values (?, ?, ?, ?, ?, ?, ?)";

    @Override
    public List<LoanOrder> getLoanOrderByUserId(long userId, long tariffId) throws SQLException{
        return jdbcTemplate.query(FIND_LOAN_ORDER_BY_ID, this::mapRowToLoanOrder, userId, tariffId);
    }

    @Override
    public void save(LoanOrder order) {
        jdbcTemplate.update(SAVE_ORDER,
                order.getOrder_id(),
                order.getUser_id(),
                order.getTariff_id(),
                order.getCredit_rating(),
                order.getStatus(),
                order.getTime_insert(),
                order.getTime_update());
    }

    @Override
    public LoanOrder getOrderStatus(String orderId) {
        List<LoanOrder> res = jdbcTemplate.query(
                GET_ORDER_STATUS,
                this::mapRowToLoanOrder,
                orderId);
        return res.size() == 0 ?
                null :
                res.get(0);
    }

    @Override
    public LoanOrder selectOrder(long userId, String orderId) {
        List<LoanOrder> res = jdbcTemplate.query(
                GET_ORDER_BY_USER_AND_ORDER_ID,
                this::mapRowToLoanOrder,
                userId, orderId);
        return res.size() == 0 ?
                null :
                res.get(0);
    }

    @Override
    public boolean deleteOrder(long userId, String orderId) {
        return jdbcTemplate.update(
                DELETE_ORDER,
                userId,
                orderId
        ) == 1;
    }

    public LoanOrder mapRowToLoanOrder(ResultSet resultSet, int i) throws SQLException {
        return new LoanOrder(
                resultSet.getLong("id"),
                resultSet.getString("order_id"),
                resultSet.getLong("user_id"),
                resultSet.getLong("tariff_id"),
                resultSet.getDouble("credit_rating"),
                resultSet.getString("status"),
                resultSet.getTimestamp("time_insert"),
                resultSet.getTimestamp("status")
        );
    }

}
