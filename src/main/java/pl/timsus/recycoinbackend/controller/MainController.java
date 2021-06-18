package pl.timsus.recycoinbackend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/machine")
public class MainController {

    @PostMapping("")
    public String doA() {
        return "a";
    }

}
