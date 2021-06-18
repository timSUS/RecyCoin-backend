package pl.timsus.recycoinbackend.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.timsus.recycoinbackend.dao.Client;

@Repository
public interface ClientRepository extends CrudRepository<Client, Integer> {
}
