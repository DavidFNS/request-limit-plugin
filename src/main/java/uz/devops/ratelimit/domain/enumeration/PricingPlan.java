package uz.devops.ratelimit.domain.enumeration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;

import java.time.Duration;
import java.util.Map;

import static uz.devops.ratelimit.domain.enumeration.LimitPeriod.*;


/**
 * The PricingPlan enumeration.
 */
public enum PricingPlan {
    FREE {
        Bandwidth getFreeLimit(LimitPeriod limitPeriod) {
            return getLimit(limitPeriod, 1);
        }
    },
    BASIC {
        Bandwidth getBasicLimit(LimitPeriod limitPeriod) {
            return getLimit(limitPeriod, 10);
        }
    },
    PROFESSIONAL {
        Bandwidth getBasicLimit(LimitPeriod limitPeriod) {
            return getLimit(limitPeriod, 100);
        }
    };

    public static PricingPlan resolvePlanFromApiKey(String apiKey) {
        if (apiKey == null || apiKey.isEmpty()) {
            return FREE;
        }
        if (apiKey.startsWith("PT001")) {
            return PROFESSIONAL;
        }
        if (apiKey.startsWith("BT001")) {
            return BASIC;
        }
        return FREE;
    }

    public static Integer getLimitCount(PricingPlan pricingPlan) {
        if (pricingPlan.equals(FREE)) return 1;
        if (pricingPlan.equals(BASIC)) return 10;
        if (pricingPlan.equals(PROFESSIONAL)) return 100;

        return 1;
    }

    public Bandwidth getLimit(LimitPeriod limitPeriod, Integer limitCount) {
        Map<LimitPeriod, Bandwidth> basicLimits = Map.of(
                MINUTE, Bandwidth.classic(limitCount, Refill.intervally(limitCount, Duration.ofMinutes(1))),
                HOUR, Bandwidth.classic(limitCount * 10, Refill.intervally(limitCount * 10, Duration.ofHours(1))),
                DAY, Bandwidth.classic(limitCount * 100, Refill.intervally(limitCount* 100, Duration.ofDays(1))),
                WEEK, Bandwidth.classic(limitCount * 1000, Refill.intervally(limitCount * 1000, Duration.ofMinutes(1))),
                MONTH, Bandwidth.classic(limitCount * 10000, Refill.intervally(limitCount * 10000, Duration.ofMinutes(1)))
        );
        return basicLimits.get(limitPeriod);
    }
}
