package uz.devops.ratelimit.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import uz.devops.ratelimit.domain.enumeration.LimitPeriod;
import uz.devops.ratelimit.domain.enumeration.PricingPlan;
import uz.devops.ratelimit.domain.enumeration.Status;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link uz.devops.ratelimit.domain.Limit} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LimitDTO implements Serializable {

    private Long id;

    @NotNull
    private LimitPeriod limitPeriod;

    @NotNull
    private PricingPlan pricingPlan;

    @NotNull
    private String username;

    @NotNull
    private String apiPath;

    @NotNull
    private Long requestCount;

    private Long resetLimitTime;

    @NotNull
    private Status status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LimitPeriod getLimitPeriod() {
        return limitPeriod;
    }

    public void setLimitPeriod(LimitPeriod limitPeriod) {
        this.limitPeriod = limitPeriod;
    }

    public PricingPlan getPricingPlan() {
        return pricingPlan;
    }

    public void setPricingPlan(PricingPlan pricingPlan) {
        this.pricingPlan = pricingPlan;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getApiPath() {
        return apiPath;
    }

    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }

    public Long getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(Long requestCount) {
        this.requestCount = requestCount;
    }

    public Long getResetLimitTime() {
        return resetLimitTime;
    }

    public void setResetLimitTime(Long resetLimitTime) {
        this.resetLimitTime = resetLimitTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LimitDTO)) {
            return false;
        }

        LimitDTO limitDTO = (LimitDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, limitDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LimitDTO{" +
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
