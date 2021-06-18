package pl.timsus.recycoinbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IdentifiersValueDto extends IdentifiersDto{
    private final Double value;

    public IdentifiersValueDto(
            @JsonProperty(required = true) Integer machineId,
            @JsonProperty(required = true) Integer clientId,
            @JsonProperty(required = true) Double value) {
        super(machineId, clientId);
        this.value = value;
    }

    public Double getValue() {
        return value;
    }
}
