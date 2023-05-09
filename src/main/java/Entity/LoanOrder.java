package Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="loan_order")
public class LoanOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String order_id;
    private long user_id;
    private long tariff_id;
    private double credit_rating;
    private String status;
    private Timestamp time_insert;
    private Timestamp time_update;

}
