package pl.timsus.recycoinbackend.distributor.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pl.timsus.recycoinbackend.distributor.dao.Client;
import pl.timsus.recycoinbackend.distributor.dao.Distributor;
import pl.timsus.recycoinbackend.distributor.data.ClientRepository;
import pl.timsus.recycoinbackend.distributor.data.DistributorRepository;

@Component
public class DataLoader implements ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    private final ClientRepository clientRepository;
    private final DistributorRepository distributorRepository;

    public DataLoader(ClientRepository clientRepository, DistributorRepository distributorRepository) {
        this.clientRepository = clientRepository;
        this.distributorRepository = distributorRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (clientRepository.count() == 0 && distributorRepository.count() == 0) {
            logger.info("Database clients and distributor clear, initializing it!");

            clientRepository.save(new Client(1, true));
            clientRepository.save(new Client(2, false));
            clientRepository.save(new Client(3, true));
            clientRepository.save(new Client(4, true));
            clientRepository.save(new Client(5, false));

            distributorRepository.save(new Distributor(1, true, 2L));
            distributorRepository.save(new Distributor(2, true, 20L));
            distributorRepository.save(new Distributor(3, false, 200L));
            distributorRepository.save(new Distributor(4, true, 2000L));
        }
    }
}
