package pl.timsus.recycoinbackend.dao;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "client")
public class Client {

    public Client() {
    }

    public Client(Integer id, Boolean isActive) {
        this.id = id;
        this.isActive = isActive;
    }

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "isActive", nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy="client")
    private Set<Token> tokens;

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
}
