package pl.timsus.recycoinbackend.distributor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IdentifiersValueDto extends IdentifiersDto{
    private final Double value;

    public IdentifiersValueDto(
            @JsonProperty(required = true) Integer distributorId,
            @JsonProperty(required = true) Integer clientId,
            @JsonProperty(required = true) Double value) {
        super(distributorId, clientId);
        this.value = value;
    }

    public Double getValue() {
        return value;
    }
}
