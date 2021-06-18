package pl.timsus.recycoinbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenDto {
    private final String token;

    public TokenDto(@JsonProperty(required = true, value = "token") String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
