package pl.timsus.recycoinbackend.distributor.dao;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "token")
public class Token {

    public Token() {
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "isUsed", nullable = false)
    private Boolean isUsed;

    @ManyToOne
    @JoinColumn(name="client", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name="distributor", nullable = false)
    private Distributor distributor;

    @Column(name = "generated", nullable = false)
    private Instant generated;

    @Column(name = "consumed")
    private Instant consumed;

    @Column(name = "value")
    private Double value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Instant getGenerated() {
        return generated;
    }

    public void setGenerated(Instant instant) {
        this.generated = instant;
    }

    public Instant getConsumed() {
        return consumed;
    }

    public void setConsumed(Instant consumed) {
        this.consumed = consumed;
    }


    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Distributor getDistributor() {
        return distributor;
    }

    public void setDistributor(Distributor distributor) {
        this.distributor = distributor;
    }
}
