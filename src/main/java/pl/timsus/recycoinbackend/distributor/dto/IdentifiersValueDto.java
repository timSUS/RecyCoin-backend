package pl.timsus.recycoinbackend.distributor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class IdentifiersValueDto extends IdentifiersDto{
    private final BigDecimal value;

    public IdentifiersValueDto(
            @JsonProperty(required = true) Integer distributorId,
            @JsonProperty(required = true) Integer clientId,
            @JsonProperty(required = true) BigDecimal value) {
        super(distributorId, clientId);
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }
}
