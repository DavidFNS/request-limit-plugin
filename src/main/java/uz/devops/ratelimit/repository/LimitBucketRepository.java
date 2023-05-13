package uz.devops.ratelimit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.devops.ratelimit.domain.LimitBucketCache;

@Repository
public interface LimitBucketRepository extends JpaRepository<LimitBucketCache, Long> {
}
