package pl.timsus.recycoinbackend.distributor.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.timsus.recycoinbackend.distributor.dao.Client;
import pl.timsus.recycoinbackend.distributor.dao.Distributor;
import pl.timsus.recycoinbackend.distributor.dao.Token;
import pl.timsus.recycoinbackend.distributor.data.ClientRepository;
import pl.timsus.recycoinbackend.distributor.data.DistributorRepository;
import pl.timsus.recycoinbackend.distributor.data.TokenRepository;

import javax.transaction.Transactional;
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
    public Optional<String> generateCode(int userId, int machineId, double value) {
        Client client = clientRepository.findById(userId).get();
        Distributor distributor = distributorRepository.findById(machineId).get();

        Set<Token> clientTokens = client.getTokens();
        Set<Token> distributorTokens = distributor.getTokens();

        double tokensTodayClient = getValueOfActiveTodaysTokens(clientTokens);

        double tokensTodayDistributor = getValueOfActiveTodaysTokens(distributorTokens);

        logger.info(
                "Request of {} token(s) generation for user: {}, distributor: {}, tokens generated for user today: {}, tokens distributed by distributor: {}/{}",
                value,
                userId,
                machineId,
                tokensTodayClient,
                tokensTodayDistributor,
                distributor.getMaxTokensPerDay()
        );

        double tokensLeftTodayClient = getTokensLeftToday(client);
        if (tokensLeftTodayClient < value) {
            return Optional.empty();
        }

        double tokensLeftTodayDistributor = getTokensLeftToday(distributor);
        if (tokensLeftTodayDistributor < value) {
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

    public double getTokensLeftToday(Distributor distributor) {
        return distributor.getMaxTokensPerDay() - getValueOfActiveTodaysTokens(distributor.getTokens());
    }

    public double getTokensLeftToday(Client client) {
        return tokenUserLimit - getValueOfActiveTodaysTokens(client.getTokens());
    }

    public Double getValueOfActiveTodaysTokens(Set<Token> tokens) {
        return tokens
                .stream()
                .filter(this::todaysToken)
                .map(Token::getValue)
                .reduce(0.0, this::sum);
    }

    private double sum(double a1, double a2) {
        return a1 + a2;
    }

    private boolean todaysToken(Token token) {
        ZoneId z = ZoneId.of( "Europe/Warsaw" );
        LocalDate ld = LocalDate.ofInstant( token.getGenerated() , z );
        return ld.isEqual(LocalDate.now());
    }


}
