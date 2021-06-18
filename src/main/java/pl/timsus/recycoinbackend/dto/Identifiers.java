package pl.timsus.recycoinbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Identifiers {
    private final Integer machineId;
    private final Integer clientId;

    public Identifiers(
            @JsonProperty(required = true) Integer machineId,
            @JsonProperty(required = true) Integer clientId
    ) {
        this.machineId = machineId;
        this.clientId = clientId;
    }

    public Integer getMachineId() {
        return machineId;
    }

    public Integer getClientId() {
        return clientId;
    }
}
