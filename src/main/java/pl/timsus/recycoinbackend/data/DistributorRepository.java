package pl.timsus.recycoinbackend.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.timsus.recycoinbackend.dao.Distributor;

@Repository
public interface DistributorRepository extends CrudRepository<Distributor, Integer> {
}
