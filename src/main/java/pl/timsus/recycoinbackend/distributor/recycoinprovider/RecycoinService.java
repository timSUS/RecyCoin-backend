package pl.timsus.recycoinbackend.distributor.recycoinprovider;

import java.math.BigDecimal;

public interface RecycoinService {
    boolean sendRecyCoin(int id, BigDecimal value);
}
