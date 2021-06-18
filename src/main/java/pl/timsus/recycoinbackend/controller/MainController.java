package pl.timsus.recycoinbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.timsus.recycoinbackend.dao.Client;
import pl.timsus.recycoinbackend.dao.Machine;
import pl.timsus.recycoinbackend.data.ClientRepository;
import pl.timsus.recycoinbackend.data.MachineRepository;
import pl.timsus.recycoinbackend.dto.Identifiers;

import java.util.Optional;

@RestController
@RequestMapping(path = "/machine")
public class MainController {

    private final MachineRepository machineRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public MainController(MachineRepository machineRepository, ClientRepository clientRepository) {
        this.machineRepository = machineRepository;
        this.clientRepository = clientRepository;
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

}
