package Controller;

import Entity.LoanOrder;
import Entity.Tariff;
import Repository.LoanOrderRepository;
import Repository.TariffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/loan-service")
public class CreditController {
    public static final String TARIFF_NOT_FOUND = "Tariff not found";
    public static final String LOAN_CONSIDERATION = "LOAN_CONSIDERATION";

    public static final String ORDER_NOT_FOUND = "ORDER_NOT_FOUND";
    public static final String LOAN_ALREADY_APPROVED = "LOAN_ALREADY_APPROVED";
    public static final String TRY_LATER = "TRY_LATER";
    public static final String ORDER_IMPOSSIBLE_TO_DELETE = "ORDER_IMPOSSIBLE_TO_DELETE";
    private final TariffRepository tariffRepository;

    private final LoanOrderRepository loanOrderRepository;

    @GetMapping("/getTariffs")
    public String getTariffs(Model model) {
        List<Tariff> tariff = tariffRepository.findAll();
        if (!tariff.isEmpty()) {
            model.addAttribute("tariff", tariff);
        } else {
            model.addAttribute("tariff", "Подходящих тарифов нет");
        }
        return "tariff";
    }

    @PostMapping("/order")
    public String newOrder(Model model, @PathVariable("userId") long userId, @PathVariable("tariffId") long tariffId) {
        try {
            Tariff tariff = tariffRepository.getTariffById(tariffId);
            List<LoanOrder> orders = loanOrderRepository.getLoanOrderByUserId(userId, tariffId);
            for (LoanOrder order : orders) {
                if (order.getStatus() == "IN_PROGRESS")
                    return LOAN_CONSIDERATION;
                else if (order.getStatus() == "APPROVED")
                    return LOAN_ALREADY_APPROVED;
                else if (order.getStatus() == "REFUSED")
                    return TRY_LATER;
            }
            LoanOrder order = new LoanOrder();
            order.setOrder_id(String.valueOf(UUID.randomUUID()));
            order.setUser_id(userId);
            order.setTariff_id(tariffId);
            Random r = new Random();
            order.setCredit_rating(0.10 + (0.90 - 0.10) * r.nextDouble());
            order.setStatus("IN_PROGRESS");
            Date date = new Date();
            Timestamp ts = new Timestamp(date.getTime());
            order.setTime_insert(ts);
            order.setTime_update(ts);
            loanOrderRepository.save(order);
            return order.getOrder_id();
        } catch (SQLException sqlException) {
            return TARIFF_NOT_FOUND;
        }
    }
    @GetMapping("/getStatusOrder")
    public String getOrderStatus(Model model, @PathVariable String orderId) {
        LoanOrder order = loanOrderRepository.getOrderStatus(orderId);
        if (order == null)
            return ORDER_NOT_FOUND;
        else
            return order.getStatus();
    }

    @GetMapping("/deleteOrder")
    public String deleteOrder(Model model, @PathVariable long userId, @PathVariable String orderId) {
        LoanOrder order = loanOrderRepository.selectOrder(userId, orderId);
        if (order == null)
            return ORDER_NOT_FOUND;
        if (order.getStatus() == "IN_PROGRESS")
            loanOrderRepository.deleteOrder(userId, orderId);
        else
            return ORDER_IMPOSSIBLE_TO_DELETE;
        return "";
    }
}


