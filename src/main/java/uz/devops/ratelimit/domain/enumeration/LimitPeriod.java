package uz.devops.ratelimit.domain.enumeration;

import java.time.Duration;

/**
 * The LimitPeriod enumeration.
 */
public enum LimitPeriod {
    MINUTE {
        Duration getDuration() {
            return Duration.ofMinutes(1);
        }
    },
    HOUR {
        Duration getDuration() {
            return Duration.ofHours(1);
        }
    },
    DAY {
        Duration getDuration() {
            return Duration.ofDays(1);
        }
    },
    WEEK {
        Duration getDuration() {
            return Duration.ofDays(7);
        }
    },
    MONTH {
        Duration getDuration() {
            return Duration.ofDays(30);
        }
    },
}
