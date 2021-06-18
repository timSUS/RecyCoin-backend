package pl.timsus.recycoinbackend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.timsus.recycoinbackend.dao.Client;
import pl.timsus.recycoinbackend.dao.Machine;
import pl.timsus.recycoinbackend.dao.Token;
import pl.timsus.recycoinbackend.data.ClientRepository;
import pl.timsus.recycoinbackend.data.MachineRepository;
import pl.timsus.recycoinbackend.data.TokenRepository;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import java.util.Set;


@Service
public class MainService {

    private final ClientRepository clientRepository;
    private final MachineRepository machineRepository;
    private final TokenRepository tokenRepository;

    private final int tokenLimit;

    public MainService(
            ClientRepository clientRepository,
            MachineRepository machineRepository,
            TokenRepository tokenRepository,
            @Value("${pl.timsus.recycoin.tokenLimit}") int tokenLimit
    ) {
        this.clientRepository = clientRepository;
        this.machineRepository = machineRepository;
        this.tokenRepository = tokenRepository;
        this.tokenLimit = tokenLimit;
    }

    //przy założeniu, że user i machine istnieje
    @Transactional
    public Optional<String> generateCode(int userId, int machineId) {
        Client client = clientRepository.findById(userId).get();
        Machine machine = machineRepository.findById(machineId).get();

        Set<Token> tokens = client.getTokens();

        long tokensToday = tokens.stream().filter(
                token -> {
                    ZoneId z = ZoneId.of( "Europe/Warsaw" );
                    LocalDate ld = LocalDate.ofInstant( token.getGenerated() , z );
                    return ld.isEqual(LocalDate.now());
                }
        ).count();

        System.out.println(tokensToday);

        if (tokensToday >= tokenLimit) {
            return Optional.empty();
        }

        Token token = new Token();
        token.setClient(client);
        token.setMachine(machine);
        token.setGenerated(Instant.now());
        token.setUsed(false);

        token = tokenRepository.save(token);

        return Optional.of(token.getId());
    }


}
