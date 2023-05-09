package Repository;

import Entity.LoanOrder;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public interface LoanOrderRepository {

    List<LoanOrder> getLoanOrderByUserId(long userId, long tariffId) throws SQLException;

    void save(LoanOrder order);

    LoanOrder getOrderStatus(String orderId);

    LoanOrder selectOrder(long userId, String orderId);

    boolean deleteOrder(long userId, String orderId);

}
