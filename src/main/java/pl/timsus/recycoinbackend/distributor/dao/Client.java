package pl.timsus.recycoinbackend.distributor.dao;

import pl.timsus.recycoinbackend.mockblockchain.dao.Account;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "client")
public class Client {

    public Client() {
    }

    public Client(Integer id, Boolean isActive, Account account) {
        this.id = id;
        this.isActive = isActive;
        this.account = account;
    }

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "isActive", nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy="client")
    private Set<Token> tokens;

    @OneToOne
    @JoinColumn(name = "account", referencedColumnName = "id", nullable = false)
    private Account account;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Set<Token> getTokens() {
        return tokens;
    }

    public void setTokens(Set<Token> tokens) {
        this.tokens = tokens;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
