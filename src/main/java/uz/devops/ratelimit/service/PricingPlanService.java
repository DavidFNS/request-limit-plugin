package uz.devops.ratelimit.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.devops.ratelimit.domain.LimitBucketCache;
import uz.devops.ratelimit.domain.enumeration.LimitPeriod;
import uz.devops.ratelimit.domain.enumeration.PricingPlan;
import uz.devops.ratelimit.domain.enumeration.Status;
import uz.devops.ratelimit.repository.LimitBucketRepository;
import uz.devops.ratelimit.service.dto.LimitDTO;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class PricingPlanService {
    private final LimitService limitService;
    private static final LimitPeriod DEFAULT_LIMIT_PERIOD = LimitPeriod.MINUTE;
    private final LimitBucketRepository limitBucketRepository;
    private final Map<String, Bucket> limitCache = new ConcurrentHashMap<>();

    @PostConstruct
    private void addLimitCacheFromDB () {
        log.debug("Started method addLimitCacheFromDB");
        List<LimitBucketCache> limitBucketCaches = limitBucketRepository.findAll();
        if (limitBucketCaches.size() == 0) return;

        limitBucketCaches
                .stream()
                .filter(limitBucketCache -> limitBucketCache.getAvailableTokens() != 0)
                .forEach(limitBucketCache -> {
                                Bandwidth bandwidth = Bandwidth.classic(
                                        limitBucketCache.getAvailableTokens(),
                                        Refill.intervally(limitBucketCache.getAvailableTokens(), Duration.ofMinutes(1))
                                );
                                Bucket limitBucket = Bucket.builder()
                                        .addLimit(bandwidth)
                                        .build();
                                limitCache.putIfAbsent(limitBucketCache.getApiKey(), limitBucket);
                            }
                        );
        // delete all limitBuckets after putting to map of limitCache
        limitBucketRepository.deleteAll();

        log.debug("Result completed limitCache map: {}", limitCache);
    }
    @PreDestroy
    private void saveLimitCacheToDB() {
        log.debug("Started method saveLimitCacheToDB");
        List<LimitBucketCache> limitBucketCaches = new ArrayList<>();
        limitCache.forEach( (userToken, limitBucket) ->
                limitBucketCaches.add(new LimitBucketCache(userToken, limitBucket.getAvailableTokens()))
        );
        limitBucketRepository.saveAll(limitBucketCaches);
    }

    public PricingPlan getUserTariff(String apiKey) {
        log.debug("request to get user tariff: {}", apiKey);
        return PricingPlan.resolvePlanFromApiKey(apiKey);
    }

    public Bucket resolveBucketLimit(String apiKey) {
        log.debug("Request to get user limit from bucketLimit");
        return limitCache.computeIfAbsent(apiKey, this::createNewBucket);
    }

    public Bucket createNewBucket(String apiKey) {
        log.debug("Request to get user limit from token bucket");

        PricingPlan pricingPlan = getUserTariff(apiKey);
        return Bucket.builder()
                .addLimit(pricingPlan.getLimit(DEFAULT_LIMIT_PERIOD, PricingPlan.getLimitCount(pricingPlan)))
                .build();
    }

    /**
     * Check user limit if exists, otherwise save new request limit
     *
     * @param userToken - the header of request X-api-key.
     * @param limitPlan - the header of request X-limit-plan.
     * @param apiPath - the apiPath of RequestURI.
     *
     * @return the remaining limitBucket.
     */
    public ConsumptionProbe consumeUserLimit(String userToken, String limitPlan, String apiPath) {
        log.debug(
                "Start calculate user limit. UserToken: {} | limitPlan: {} | apiPath: {}",
                userToken, limitPlan, apiPath
        );

        // full request header
        String apiKey = limitPlan + apiPath + "/" + userToken;

        Bucket limitBucket = resolveBucketLimit(apiKey);
        PricingPlan pricingPlan = getUserTariff(apiKey);
        LimitDTO limit = limitService.findByUserNameAndApiPath(userToken, apiPath);
        return getConsumptionProbe(limit, userToken, apiPath, limitBucket, pricingPlan);
    }

    private ConsumptionProbe getConsumptionProbe(LimitDTO limit, String userToken, String apiPath, Bucket limitBucket, PricingPlan pricingPlan) {
        log.debug("Start check user limit: {} by userToken: {}", limit, userToken);

        ConsumptionProbe probe = limitBucket.tryConsumeAndReturnRemaining(1);
        if (limit == null) {
            long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
            LimitDTO limitDTO = LimitDTO.builder()
                    .pricingPlan(pricingPlan)
                    .limitPeriod(LimitPeriod.MINUTE)
                    .status(Status.ACTIVE)
                    .apiPath(apiPath)
                    .resetLimitTime(waitForRefill)
                    .requestCount(limitBucket.getAvailableTokens())
                    .username(userToken)
                    .build();
            limitService.save(limitDTO);

            log.debug("Result consumptionProbe of limit: {}", probe);
             return probe;
        }
        // update user limitCount
        limit.setRequestCount(limitBucket.getAvailableTokens());
        limit.setPricingPlan(pricingPlan);
        limit.setResetLimitTime(0L);
        limitService.save(limit);

        log.debug("Result consumptionProbe of limit: {}", probe);
        return probe;
    }
}
