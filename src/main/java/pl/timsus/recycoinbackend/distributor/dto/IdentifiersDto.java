package pl.timsus.recycoinbackend.distributor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IdentifiersDto {
    private final Integer distributorId;
    private final Integer clientId;

    public IdentifiersDto(
            @JsonProperty(required = true) Integer distributorId,
            @JsonProperty(required = true) Integer clientId
    ) {
        this.distributorId = distributorId;
        this.clientId = clientId;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public Integer getClientId() {
        return clientId;
    }
}
