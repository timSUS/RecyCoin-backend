package pl.timsus.recycoinbackend.mockblockchain.dao;

import pl.timsus.recycoinbackend.distributor.dao.Client;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "account")
public class Account {

    public Account() {
    }

    public Account(Integer id, BigDecimal recyCoinBalance, Long claimedTokens, BigDecimal tokenBalance) {
        this.id = id;
        this.recyCoinBalance = recyCoinBalance;
        this.claimedTokens = claimedTokens;
        this.tokenBalance = tokenBalance;
    }

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne(mappedBy = "account")
    private Client client;

    @Column(name = "recyCoinBalance", nullable = false)
    private BigDecimal recyCoinBalance;

    @Column(name = "tokenBalance", nullable = false)
    private BigDecimal tokenBalance;

    @Column(name = "claimedTokens", nullable = false)
    private Long claimedTokens;

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

    public BigDecimal getRecyCoinBalance() {
        return recyCoinBalance;
    }

    public void setRecyCoinBalance(BigDecimal balance) {
        this.recyCoinBalance = balance;
    }

    public Long getClaimedTokens() {
        return claimedTokens;
    }

    public void setClaimedTokens(Long claimedTokens) {
        this.claimedTokens = claimedTokens;
    }

    public BigDecimal getTokenBalance() {
        return tokenBalance;
    }

    public void setTokenBalance(BigDecimal tokenBalance) {
        this.tokenBalance = tokenBalance;
    }
}
