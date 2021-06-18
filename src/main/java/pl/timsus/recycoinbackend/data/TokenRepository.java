package pl.timsus.recycoinbackend.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.timsus.recycoinbackend.dao.Token;

@Repository
public interface TokenRepository extends CrudRepository<Token, String> {
}
