package pl.timsus.recycoinbackend.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.timsus.recycoinbackend.dao.Machine;

@Repository
public interface MachineRepository extends CrudRepository<Machine, Integer> {
}
