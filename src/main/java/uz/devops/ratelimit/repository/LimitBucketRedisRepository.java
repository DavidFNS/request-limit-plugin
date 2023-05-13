package uz.devops.ratelimit.repository;

import org.springframework.data.repository.CrudRepository;
import uz.devops.ratelimit.domain.LimitBucketRedis;

//@Repository
public interface LimitBucketRedisRepository extends CrudRepository<LimitBucketRedis, Long> {
}
