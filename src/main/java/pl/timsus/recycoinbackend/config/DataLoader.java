package pl.timsus.recycoinbackend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pl.timsus.recycoinbackend.distributor.dao.Client;
import pl.timsus.recycoinbackend.distributor.dao.Distributor;
import pl.timsus.recycoinbackend.distributor.repository.ClientRepository;
import pl.timsus.recycoinbackend.distributor.repository.DistributorRepository;
import pl.timsus.recycoinbackend.mockblockchain.dao.Account;
import pl.timsus.recycoinbackend.mockblockchain.repository.AccountRepository;

import java.math.BigDecimal;

@Component
public class DataLoader implements ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    private final ClientRepository clientRepository;
    private final DistributorRepository distributorRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public DataLoader(ClientRepository clientRepository, DistributorRepository distributorRepository, AccountRepository accountRepository) {
        this.clientRepository = clientRepository;
        this.distributorRepository = distributorRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (clientRepository.count() == 0 && distributorRepository.count() == 0) {
            logger.info("Database clients and distributor clear, initializing it!");

            accountRepository.save(new Account(1, BigDecimal.ZERO));
            accountRepository.save(new Account(2, BigDecimal.ZERO));
            accountRepository.save(new Account(3, BigDecimal.ZERO));
            accountRepository.save(new Account(4, BigDecimal.ZERO));
            accountRepository.save(new Account(5, BigDecimal.ZERO));

            clientRepository.save(new Client(1, true, accountRepository.findById(1).get()));
            clientRepository.save(new Client(2, false, accountRepository.findById(2).get()));
            clientRepository.save(new Client(3, true, accountRepository.findById(3).get()));
            clientRepository.save(new Client(4, true, accountRepository.findById(4).get()));
            clientRepository.save(new Client(5, false, accountRepository.findById(5).get()));

            distributorRepository.save(new Distributor(1, true, 2L));
            distributorRepository.save(new Distributor(2, true, 20L));
            distributorRepository.save(new Distributor(3, false, 200L));
            distributorRepository.save(new Distributor(4, true, 2000L));
        }
    }
}
