package pl.timsus.recycoinbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IdentifiersDto {
    private final Integer machineId;
    private final Integer clientId;

    public IdentifiersDto(
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
