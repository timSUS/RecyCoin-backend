package pl.timsus.recycoinbackend.distributor.recycoinprovider;

import java.math.BigDecimal;
import java.util.Optional;

public interface RecycoinService {
    boolean sendRecyCoin(int id, BigDecimal value);
    Optional<BigDecimal> getOwnedRecyCoin(int id);
}
