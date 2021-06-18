package pl.timsus.recycoinbackend.dao;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "distributor")
public class Distributor {

    public Distributor() {
    }

    public Distributor(Integer id, Boolean isActive, Long maxTokensPerDay) {
        this.id = id;
        this.isActive = isActive;
        this.maxTokensPerDay = maxTokensPerDay;
    }

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "isActive", nullable = false)
    private Boolean isActive;

    @Column(name = "maxTokensPerDay", nullable = false)
    private Long maxTokensPerDay;

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

    public Long getMaxTokensPerDay() {
        return maxTokensPerDay;
    }

    public void setMaxTokensPerDay(Long maxTokensPerDay) {
        this.maxTokensPerDay = maxTokensPerDay;
    }
}
