package pl.timsus.recycoinbackend.distributor.distributor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.timsus.recycoinbackend.distributor.dao.Client;
import pl.timsus.recycoinbackend.distributor.dao.Distributor;
import pl.timsus.recycoinbackend.distributor.dao.Token;
import pl.timsus.recycoinbackend.distributor.repository.ClientRepository;
import pl.timsus.recycoinbackend.distributor.repository.DistributorRepository;
import pl.timsus.recycoinbackend.distributor.repository.TokenRepository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import java.util.Set;


@Service
public class DistributorService {

    private final Logger logger = LoggerFactory.getLogger(DistributorService.class);

    private final ClientRepository clientRepository;
    private final DistributorRepository distributorRepository;
    private final TokenRepository tokenRepository;

    private final double tokenUserLimit;

    public DistributorService(
            ClientRepository clientRepository,
            DistributorRepository distributorRepository,
            TokenRepository tokenRepository,
            @Value("${pl.timsus.recycoin.tokenLimit}") double tokenUserLimit
    ) {
        this.clientRepository = clientRepository;
        this.distributorRepository = distributorRepository;
        this.tokenRepository = tokenRepository;
        this.tokenUserLimit = tokenUserLimit;
    }

    //przy założeniu, że user i machine istnieje
    @Transactional
    public Optional<String> generateCode(int userId, int machineId, BigDecimal value) {
        Client client = clientRepository.findById(userId).get();
        Distributor distributor = distributorRepository.findById(machineId).get();

        Set<Token> clientTokens = client.getTokens();
        Set<Token> distributorTokens = distributor.getTokens();

        BigDecimal tokensTodayClient = getValueOfActiveTodaysTokens(clientTokens);

        BigDecimal tokensTodayDistributor = getValueOfActiveTodaysTokens(distributorTokens);

        logger.info(
                "Request of {} token(s) generation for user: {}, distributor: {}, tokens generated for user today: {}, tokens distributed by distributor: {}/{}",
                value,
                userId,
                machineId,
                tokensTodayClient,
                tokensTodayDistributor,
                distributor.getMaxTokensPerDay()
        );

        BigDecimal tokensLeftTodayClient = getTokensLeftToday(client);
        if (tokensLeftTodayClient.compareTo(value) < 0) {
            return Optional.empty();
        }

        BigDecimal tokensLeftTodayDistributor = getTokensLeftToday(distributor);
        if (tokensLeftTodayDistributor.compareTo(value) < 0) {
            return Optional.empty();
        }

        Token token = new Token();
        token.setClient(client);
        token.setDistributor(distributor);
        token.setGenerated(Instant.now());
        token.setValue(value);
        token.setUsed(false);

        token = tokenRepository.save(token);

        String tokenId = token.getId();

        logger.info(
                "Generated token for user: {}, distributor: {}, token: {}",
                userId,
                machineId,
                tokenId
        );

        return Optional.of(tokenId);
    }

    public BigDecimal getTokensLeftToday(Distributor distributor) {
        return BigDecimal.valueOf(distributor.getMaxTokensPerDay()).subtract(getValueOfActiveTodaysTokens(distributor.getTokens()));
    }

    public BigDecimal getTokensLeftToday(Client client) {
        return BigDecimal.valueOf(tokenUserLimit).subtract((getValueOfActiveTodaysTokens(client.getTokens())));
    }

    public BigDecimal getValueOfActiveTodaysTokens(Set<Token> tokens) {
        return tokens
                .stream()
                .filter(this::todaysToken)
                .map(Token::getValue)
                .reduce(BigDecimal.ZERO, this::sum);
    }

    private BigDecimal sum(BigDecimal a1, BigDecimal a2) {
        return a1.add(a2);
    }

    private boolean todaysToken(Token token) {
        ZoneId z = ZoneId.of( "Europe/Warsaw" );
        LocalDate ld = LocalDate.ofInstant( token.getGenerated() , z );
        return ld.isEqual(LocalDate.now());
    }


}
