package pl.timsus.recycoinbackend.mockblockchain.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.timsus.recycoinbackend.mockblockchain.transfer.TransferService;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/account")
public class AccountController {

    private final TransferService transferService;

    @Autowired
    public AccountController(TransferService transferService) {
        this.transferService = transferService;
    }

    @GetMapping("{id}")
    @Transactional
    public @ResponseBody
    ResponseEntity<Map<String, String>> getOwnedCoins(@PathVariable("id") int id) {
        Optional<BigDecimal> optionalBigInteger = transferService.getOwnedRecyCoin(id);
        if (optionalBigInteger.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("owned", optionalBigInteger.get().toPlainString()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "bad id"));
        }
    }

}
