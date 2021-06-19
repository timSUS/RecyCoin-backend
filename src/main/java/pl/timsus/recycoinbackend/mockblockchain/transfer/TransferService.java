package pl.timsus.recycoinbackend.mockblockchain.transfer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.timsus.recycoinbackend.distributor.dao.Client;
import pl.timsus.recycoinbackend.distributor.recycoinprovider.RecycoinService;
import pl.timsus.recycoinbackend.distributor.repository.ClientRepository;
import pl.timsus.recycoinbackend.mockblockchain.dao.Account;
import pl.timsus.recycoinbackend.mockblockchain.repository.AccountRepository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class TransferService implements RecycoinService {

    private final Logger logger = LoggerFactory.getLogger(TransferService.class);
    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;
    private TransferService transferService;

    @Autowired
    public TransferService(ClientRepository clientRepository, AccountRepository accountRepository) {
        this.clientRepository = clientRepository;
        this.accountRepository = accountRepository;
    }

    @Autowired
    public void setTransferService(TransferService transferService) {
        this.transferService = transferService;
    }

    @Override
    public boolean sendRecyCoin(int id, BigDecimal value) {
        Optional<Client> client = clientRepository.findById(id);
        if (!client.isPresent()) {
            return false;
        }
        Account account = client.get().getAccount();

        account.setTokenBalance(account.getTokenBalance().add(value));

        updateCoinsForAccount(account.getId());
        return true;
    }

    public void updateCoinsForAccount(Integer id) {
        while (true) {
            Account account = accountRepository.findById(id).get();
            if (Math.floor(account.getTokenBalance().doubleValue()) > account.getClaimedTokens()) {
                logger.info("Consuming tokens for {}", id);
                transferService.giveCoinTo(id);
                account.setClaimedTokens(account.getClaimedTokens() + 1);
                accountRepository.save(account);
            } else {
                return;
            }
        }
    }

    @Transactional
    public void giveCoinTo(Integer id) {
        long distributedTokens = getDistributedTokens();
        double recyCoins = getRecyCoinsReceivedFor(distributedTokens);
        logger.info("Created {} recyCoins for {}", recyCoins, id);
        Account account = accountRepository.findById(id).get();
        account.setRecyCoinBalance(account.getRecyCoinBalance().add(BigDecimal.valueOf(recyCoins)));
    }

    public double getRecyCoinsReceivedFor(long tokensInExistence) {
        return 1 / miningCost(tokensInExistence);
    }

    public double miningCost(long tokensInExistence) {
        long tokensCalculator = tokensInExistence + 1;
        return 1 + Math.log10(tokensCalculator);
    }


    @Transactional
    public long getDistributedTokens() {
        return StreamSupport.stream(accountRepository.findAll().spliterator(), false)
                .map(Account::getClaimedTokens)
                .reduce(0L, this::sum);
    }


    private long sum(long a1, long a2) {
        return a1 + a2;
    }
}
