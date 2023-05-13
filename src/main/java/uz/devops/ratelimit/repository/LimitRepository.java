package uz.devops.ratelimit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.devops.ratelimit.domain.Limit;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Limit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LimitRepository extends JpaRepository<Limit, Long> {
    @Query("select l from Limit l where l.apiPath = ?1 and l.username = ?2 and l.status = 'ACTIVE'")
    Optional<Limit> findByApiPathAndUsernameAndLimitPlan(String apiPath, String username);
    List<Limit> findAllByUsername(String username);

    @Modifying
    @Query("update Limit l set l.resetLimitTime = ?1 where l.username = ?2 and l.apiPath = ?3")
    void updateResetLimitTime(Long waitRefill, String userToken, String apiPath);
}
