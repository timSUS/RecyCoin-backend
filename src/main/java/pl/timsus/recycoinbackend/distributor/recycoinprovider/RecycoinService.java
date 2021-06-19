package pl.timsus.recycoinbackend.distributor.recycoinprovider;

import java.math.BigDecimal;

public interface RecycoinService {
    void sendRecyCoin(int id, BigDecimal value);
}
