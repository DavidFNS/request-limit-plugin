package uz.devops.ratelimit.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.devops.ratelimit.domain.enumeration.PricingPlan;
import uz.devops.ratelimit.service.dto.LimitDTO;
import uz.devops.ratelimit.service.dto.ResponseDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link uz.devops.ratelimit.domain.Limit}.
 */
public interface LimitService {
    /**
     * Save a limit.
     *
     * @param limitDTO the entity to save.
     * @return the persisted entity.
     */
    LimitDTO save(LimitDTO limitDTO);

    /**
     * Updates a limit.
     *
     * @param limitDTO the entity to update.
     * @return the persisted entity.
     */
    LimitDTO update(LimitDTO limitDTO);

    /**
     * Get all the limits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LimitDTO> findAll(Pageable pageable);

    /**
     * Get the "id" limit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LimitDTO> findOne(Long id);

    /**
     * Get all the limits.
     * @return the list of entities.
     */
    List<LimitDTO> getAllLimits();
    ResponseDTO<List<LimitDTO>> getAllLimitsCurrentUser(String username);

    LimitDTO findByUserNameAndApiPath(String userToken, String apiPath);

    void updateResetLimitTime(String userToken, String apiPath, Long waitRefill);
}
