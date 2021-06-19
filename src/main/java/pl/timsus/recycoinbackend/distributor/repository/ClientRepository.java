package pl.timsus.recycoinbackend.distributor.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.timsus.recycoinbackend.distributor.dao.Client;

@Repository
public interface ClientRepository extends CrudRepository<Client, Integer> {
}
