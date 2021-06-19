package pl.timsus.recycoinbackend.distributor.distributor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.timsus.recycoinbackend.distributor.dao.Client;
import pl.timsus.recycoinbackend.distributor.dao.Distributor;
import pl.timsus.recycoinbackend.distributor.dao.Token;
import pl.timsus.recycoinbackend.distributor.repository.ClientRepository;
import pl.timsus.recycoinbackend.distributor.repository.DistributorRepository;
import pl.timsus.recycoinbackend.distributor.repository.TokenRepository;
import pl.timsus.recycoinbackend.distributor.dto.IdentifiersDto;
import pl.timsus.recycoinbackend.distributor.dto.IdentifiersValueDto;
import pl.timsus.recycoinbackend.distributor.dto.TokenDto;
import pl.timsus.recycoinbackend.distributor.recycoinprovider.RecycoinService;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/distributor")
public class DistributorController {

    private final Logger logger = LoggerFactory.getLogger(DistributorController.class);

    private final DistributorRepository distributorRepository;
    private final ClientRepository clientRepository;
    private final TokenRepository tokenRepository;
    private final DistributorService distributorService;
    private final RecycoinService recycoinService;

    @Autowired
    public DistributorController(DistributorRepository distributorRepository, ClientRepository clientRepository, TokenRepository tokenRepository, DistributorService distributorService, RecycoinService recycoinService) {
        this.distributorRepository = distributorRepository;
        this.clientRepository = clientRepository;
        this.tokenRepository = tokenRepository;
        this.distributorService = distributorService;
        this.recycoinService = recycoinService;
    }


    @PostMapping("")
    public @ResponseBody ResponseEntity<?> test() {
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
    }

    @PostMapping("authorized")
    public @ResponseBody ResponseEntity<Boolean> isAuthorized(@RequestBody IdentifiersDto identifiersDto) {
        if (isAuthorizedInternal(identifiersDto)) {
            return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Boolean.FALSE);
    }

    private boolean isAuthorizedInternal(IdentifiersDto identifiersDto) {
        Optional<Distributor> optionalMachine = distributorRepository.findById(identifiersDto.getDistributorId());
        if (optionalMachine.isEmpty()) {
            return false;
        }
        if (!optionalMachine.get().getActive()) {
            return false;
        }
        Optional<Client> optionalClient = clientRepository.findById(identifiersDto.getClientId());
        if (optionalClient.isEmpty()) {
            return false;
        }
        if (!optionalClient.get().getActive()) {
            return false;
        }
        return true;
    }

    @PostMapping("createToken")
    public @ResponseBody ResponseEntity<Map<String, String>> createToken(@RequestBody IdentifiersValueDto identifiersDto) {
        if (!isAuthorizedInternal(identifiersDto)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<String> generateCode = distributorService.generateCode(identifiersDto.getClientId(), identifiersDto.getDistributorId(), identifiersDto.getValue());

        if (generateCode.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("code", generateCode.get()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status", "out of codes!"));
        }
    }

    @PostMapping("consumeToken")
    @Transactional
    public @ResponseBody ResponseEntity<Map<String, String>> consumeToken(@RequestBody TokenDto tokenDto) {
        Optional<Token> optionalToken = tokenRepository.findById(tokenDto.getToken());

        if (optionalToken.isPresent()) {

            Token token = optionalToken.get();
            if (token.getUsed()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status", "already consumed"));
            }


            boolean sent = recycoinService.sendRecyCoin(token.getClient().getId(), token.getValue());

            if (sent) {
                token.setUsed(true);
                token.setConsumed(Instant.now());
                tokenRepository.save(token);

                return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "consumed"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status", "could not consume"));
    }

    @GetMapping("distributorLimits/{id}")
    @Transactional
    public @ResponseBody ResponseEntity<Map<String, String>> getDistributorLimit(@PathVariable("id") int id) {
        Optional<Distributor> optionalDistributor = distributorRepository.findById(id);
        if (optionalDistributor.isPresent()) {
            BigDecimal valueLeft = distributorService.getTokensLeftToday(optionalDistributor.get());

            return ResponseEntity.status(HttpStatus.OK).body(Map.of("dailyLimitLeft", valueLeft.toPlainString()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "bad id"));
        }
    }

    @GetMapping("clientLimits/{id}")
    @Transactional
    public @ResponseBody ResponseEntity<Map<String, String>> getClientLimit(@PathVariable("id") int id) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        if (optionalClient.isPresent()) {
            BigDecimal valueLeft = distributorService.getTokensLeftToday(optionalClient.get());

            return ResponseEntity.status(HttpStatus.OK).body(Map.of("dailyLimitLeft", valueLeft.toPlainString()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "bad id"));
        }
    }

}
