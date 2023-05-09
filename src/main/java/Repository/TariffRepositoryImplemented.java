package Repository;

import Entity.Tariff;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static javax.swing.text.html.parser.DTDConstants.ID;

@Getter
@Component
@RequiredArgsConstructor
public class TariffRepositoryImplemented implements TariffRepository{

    public static final String TARIFF_NOT_FOUND = "Tariff not found";

    private JdbcTemplate jdbcTemplate;

    private static final String FIND_ALL_TARIFFS = "select * from tariff";

    private static final String FIND_TARIFF_BY_ID = "select * from tariff where id=?";

    @Override
    public List<Tariff> findAll() {
        return jdbcTemplate.query(
                FIND_ALL_TARIFFS,
                this::mapRowToTariff);
    }

    @Override
    public Tariff getTariffById(long id) throws SQLException{
        List<Tariff> rez = jdbcTemplate.query(FIND_TARIFF_BY_ID, this::mapRowToTariff, ID);
        if (rez.size() == 0) throw new SQLException(TARIFF_NOT_FOUND);
        else return rez.get(0);
    }

    private Tariff mapRowToTariff(ResultSet resultSet, int i) throws SQLException {
        return new Tariff(
                resultSet.getLong("id"),
                resultSet.getString("type"),
                resultSet.getString("interest_rate")
        );
    }
}
