package Repository;

import Entity.Tariff;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public interface TariffRepository {

    List<Tariff> findAll();

    Tariff getTariffById(long id)throws SQLException;
}
