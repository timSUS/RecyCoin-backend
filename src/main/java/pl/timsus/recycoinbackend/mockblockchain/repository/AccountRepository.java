package pl.timsus.recycoinbackend.mockblockchain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.timsus.recycoinbackend.mockblockchain.dao.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, Integer> {
}
