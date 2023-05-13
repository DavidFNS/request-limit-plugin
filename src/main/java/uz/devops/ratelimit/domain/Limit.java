package uz.devops.ratelimit.domain;

import uz.devops.ratelimit.domain.enumeration.LimitPeriod;
import uz.devops.ratelimit.domain.enumeration.PricingPlan;
import uz.devops.ratelimit.domain.enumeration.Status;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A Limit.
 */
@Entity
@Table(name = "request_limits")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Limit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "limit_sequence")
    @SequenceGenerator(name = "limit_sequence", sequenceName = "limit_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "limit_period", nullable = false)
    private LimitPeriod limitPeriod;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "pricing_plan", nullable = false)
    private PricingPlan pricingPlan;

    @NotNull
    @Column(name = "username", nullable = false)
    private String username;

    @NotNull
    @Column(name = "api_path", nullable = false)
    private String apiPath;

    @NotNull
    @Column(name = "request_count", nullable = false)
    private Long requestCount;

    @Column(name = "reset_limit_time")
    private Long resetLimitTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;
    public Long getId() {
        return this.id;
    }

    public Limit id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LimitPeriod getLimitPeriod() {
        return this.limitPeriod;
    }

    public Limit limitPeriod(LimitPeriod limitPeriod) {
        this.setLimitPeriod(limitPeriod);
        return this;
    }

    public void setLimitPeriod(LimitPeriod limitPeriod) {
        this.limitPeriod = limitPeriod;
    }

    public PricingPlan getPricingPlan() {
        return this.pricingPlan;
    }

    public Limit pricingPlan(PricingPlan pricingPlan) {
        this.setPricingPlan(pricingPlan);
        return this;
    }

    public void setPricingPlan(PricingPlan pricingPlan) {
        this.pricingPlan = pricingPlan;
    }

    public String getUsername() {
        return this.username;
    }

    public Limit username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getApiPath() {
        return this.apiPath;
    }

    public Limit apiPath(String apiPath) {
        this.setApiPath(apiPath);
        return this;
    }

    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }

    public Long getRequestCount() {
        return this.requestCount;
    }

    public Limit requestCount(Long requestCount) {
        this.setRequestCount(requestCount);
        return this;
    }

    public void setRequestCount(Long requestCount) {
        this.requestCount = requestCount;
    }

    public Long getResetLimitTime() {
        return this.resetLimitTime;
    }

    public Limit resetLimitTime(Long resetLimitTime) {
        this.setResetLimitTime(resetLimitTime);
        return this;
    }

    public void setResetLimitTime(Long resetLimitTime) {
        this.resetLimitTime = resetLimitTime;
    }

    public Status getStatus() {
        return this.status;
    }

    public Limit status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Limit)) {
            return false;
        }
        return id != null && id.equals(((Limit) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Limit{" +
            "id=" + getId() +
            ", limitPeriod='" + getLimitPeriod() + "'" +
            ", pricingPlan='" + getPricingPlan() + "'" +
            ", username='" + getUsername() + "'" +
            ", apiPath='" + getApiPath() + "'" +
            ", requestCount=" + getRequestCount() +
            ", resetLimitTime=" + getResetLimitTime() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
