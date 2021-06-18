package pl.timsus.recycoinbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.timsus.recycoinbackend.dao.Client;
import pl.timsus.recycoinbackend.dao.Machine;
import pl.timsus.recycoinbackend.dao.Token;
import pl.timsus.recycoinbackend.data.ClientRepository;
import pl.timsus.recycoinbackend.data.MachineRepository;
import pl.timsus.recycoinbackend.data.TokenRepository;
import pl.timsus.recycoinbackend.dto.IdentifiersDto;
import pl.timsus.recycoinbackend.dto.TokenDto;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/machine")
public class MainController {

    private final MachineRepository machineRepository;
    private final ClientRepository clientRepository;
    private final TokenRepository tokenRepository;
    private final MainService mainService;

    @Autowired
    public MainController(MachineRepository machineRepository, ClientRepository clientRepository, TokenRepository tokenRepository, MainService mainService) {
        this.machineRepository = machineRepository;
        this.clientRepository = clientRepository;
        this.tokenRepository = tokenRepository;
        this.mainService = mainService;
    }


    @PostMapping("")
    public String doA() {
        return "a";
    }

    @PostMapping("authorized")
    public boolean isAuthorized(@RequestBody IdentifiersDto identifiersDto) {
        Optional<Machine> optionalMachine = machineRepository.findById(identifiersDto.getMachineId());
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
    public @ResponseBody ResponseEntity<Map<String, String>> createToken(@RequestBody IdentifiersDto identifiersDto) {
        if (!isAuthorized(identifiersDto)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<String> generateCode = mainService.generateCode(identifiersDto.getClientId(), identifiersDto.getMachineId());

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


            //todo give tokens
            token.setUsed(true);
            token.setConsumed(Instant.now());
            tokenRepository.save(token);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "consumed"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status", "could not consume"));
    }

}
