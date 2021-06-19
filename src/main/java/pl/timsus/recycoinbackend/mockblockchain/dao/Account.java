package pl.timsus.recycoinbackend.mockblockchain.dao;

import pl.timsus.recycoinbackend.distributor.dao.Client;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "account")
public class Account {

    public Account() {
    }

    public Account(Integer id, BigDecimal balance) {
        this.id = id;
        this.balance = balance;
    }

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne(mappedBy = "account")
    private Client client;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
