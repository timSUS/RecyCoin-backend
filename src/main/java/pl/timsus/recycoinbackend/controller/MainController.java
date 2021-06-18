package pl.timsus.recycoinbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.timsus.recycoinbackend.dao.Client;
import pl.timsus.recycoinbackend.dao.Machine;
import pl.timsus.recycoinbackend.data.ClientRepository;
import pl.timsus.recycoinbackend.data.MachineRepository;
import pl.timsus.recycoinbackend.dto.Identifiers;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/machine")
public class MainController {

    private final MachineRepository machineRepository;
    private final ClientRepository clientRepository;
    private final MainService mainService;

    @Autowired
    public MainController(MachineRepository machineRepository, ClientRepository clientRepository, MainService mainService) {
        this.machineRepository = machineRepository;
        this.clientRepository = clientRepository;
        this.mainService = mainService;
    }


    @PostMapping("")
    public String doA() {
        return "a";
    }

    @PostMapping("authorized")
    public boolean isAuthorized(@RequestBody Identifiers identifiers) {
        Optional<Machine> optionalMachine = machineRepository.findById(identifiers.getMachineId());
        if (optionalMachine.isEmpty()) {
            return false;
        }
        if (!optionalMachine.get().getActive()) {
            return false;
        }
        Optional<Client> optionalClient = clientRepository.findById(identifiers.getClientId());
        if (optionalClient.isEmpty()) {
            return false;
        }
        if (!optionalClient.get().getActive()) {
            return false;
        }
        return true;
    }

    @PostMapping("createToken")
    public @ResponseBody ResponseEntity<Map<String, String>> createToken(@RequestBody Identifiers identifiers) {
        if (!isAuthorized(identifiers)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<String> generateCode = mainService.generateCode(identifiers.getClientId(), identifiers.getMachineId());

        if (generateCode.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("code", generateCode.get()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "out of codes!"));
        }
    }

}
