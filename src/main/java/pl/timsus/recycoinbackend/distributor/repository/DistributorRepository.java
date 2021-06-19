package pl.timsus.recycoinbackend.distributor.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.timsus.recycoinbackend.distributor.dao.Distributor;

@Repository
public interface DistributorRepository extends CrudRepository<Distributor, Integer> {
}
