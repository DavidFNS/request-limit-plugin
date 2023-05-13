package uz.devops.ratelimit.config;

import io.github.bucket4j.ConsumptionProbe;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import uz.devops.ratelimit.service.LimitService;
import uz.devops.ratelimit.service.PricingPlanService;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Configuration
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {
    private final PricingPlanService pricingPlanService;
    private final LimitService limitService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String userToken = request.getHeader("X-api-key");
        String limitPlan = request.getHeader("X-limit-plan");
        if (userToken == null || userToken.isEmpty()) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), "Missing Header: X-api-key");
            return false;
        }

        if (limitPlan == null || limitPlan.isEmpty()) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), "Missing Header: X-limit-plan");
            return false;
        }

        String apiPath = request.getRequestURI();
        ConsumptionProbe probe = pricingPlanService.consumeUserLimit(userToken, limitPlan, apiPath);

        // consume user limit from bucket if exists, otherwise return error Too Many Requests
        if (probe.isConsumed()) {
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            return true;
        } else {
            long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
            response.addHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(waitForRefill));
            response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(),
                    "You have exhausted your API Request Quota");

            // set reset limit time if limit exists
            limitService.updateResetLimitTime(userToken, apiPath, waitForRefill);
            return false;
        }
    }
}