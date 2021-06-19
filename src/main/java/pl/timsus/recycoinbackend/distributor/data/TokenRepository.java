package pl.timsus.recycoinbackend.distributor.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.timsus.recycoinbackend.distributor.dao.Token;

@Repository
public interface TokenRepository extends CrudRepository<Token, String> {
}
