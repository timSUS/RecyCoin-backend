package pl.timsus.recycoinbackend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.timsus.recycoinbackend.dao.Client;
import pl.timsus.recycoinbackend.dao.Distributor;
import pl.timsus.recycoinbackend.dao.Token;
import pl.timsus.recycoinbackend.data.ClientRepository;
import pl.timsus.recycoinbackend.data.DistributorRepository;
import pl.timsus.recycoinbackend.data.TokenRepository;
import pl.timsus.recycoinbackend.dto.IdentifiersDto;
import pl.timsus.recycoinbackend.dto.IdentifiersValueDto;
import pl.timsus.recycoinbackend.dto.TokenDto;
import pl.timsus.recycoinbackend.recycoinprovider.RecycoinService;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/machine")
public class MainController {

    private final Logger logger = LoggerFactory.getLogger(MainController.class);

    private final DistributorRepository distributorRepository;
    private final ClientRepository clientRepository;
    private final TokenRepository tokenRepository;
    private final MainService mainService;
    private final RecycoinService recycoinService = new RecycoinService() {
        @Override
        public void sendRecyCoin(int id, double value) {
            logger.info("Sending RecyCoin of value {} to {}", value, id);
        }
    };

    @Autowired
    public MainController(DistributorRepository distributorRepository, ClientRepository clientRepository, TokenRepository tokenRepository, MainService mainService) {
        this.distributorRepository = distributorRepository;
        this.clientRepository = clientRepository;
        this.tokenRepository = tokenRepository;
        this.mainService = mainService;
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

        Optional<String> generateCode = mainService.generateCode(identifiersDto.getClientId(), identifiersDto.getDistributorId(), identifiersDto.getValue());

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

            token.setUsed(true);
            token.setConsumed(Instant.now());
            tokenRepository.save(token);

            recycoinService.sendRecyCoin(token.getClient().getId(), token.getValue());

            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "consumed"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status", "could not consume"));
    }

}
