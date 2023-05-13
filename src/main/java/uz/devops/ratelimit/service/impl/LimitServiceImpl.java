package uz.devops.ratelimit.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.devops.ratelimit.domain.Limit;
import uz.devops.ratelimit.domain.enumeration.PricingPlan;
import uz.devops.ratelimit.repository.LimitRepository;
import uz.devops.ratelimit.service.LimitService;
import uz.devops.ratelimit.service.dto.LimitDTO;
import uz.devops.ratelimit.service.dto.ResponseDTO;
import uz.devops.ratelimit.service.mapper.LimitMapper;


import java.util.List;
import java.util.Optional;
/**
 * Service Implementation for managing {@link Limit}.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class LimitServiceImpl implements LimitService {

    private final Logger log = LoggerFactory.getLogger(LimitServiceImpl.class);

    private final LimitRepository limitRepository;

    private final LimitMapper limitMapper;
    @Override
    public LimitDTO save(LimitDTO limitDTO) {
        log.debug("Request to save Limit : {}", limitDTO);
        Limit limit = limitMapper.toEntity(limitDTO);
        limit = limitRepository.save(limit);
        return limitMapper.toDto(limit);
    }

    @Override
    public LimitDTO update(LimitDTO limitDTO) {
        log.debug("Request to update Limit : {}", limitDTO);
        Limit limit = limitMapper.toEntity(limitDTO);
        limit = limitRepository.save(limit);
        return limitMapper.toDto(limit);
    }
    @Override
    @Transactional(readOnly = true)
    public Page<LimitDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Limits");
        return limitRepository.findAll(pageable).map(limitMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LimitDTO> findOne(Long id) {
        log.debug("Request to get Limit : {}", id);
        return limitRepository.findById(id).map(limitMapper::toDto);
    }
    @Override
    public List<LimitDTO> getAllLimits() {
        log.debug("Request to get all Limits");
        return limitRepository.findAll().stream()
                .map(limitMapper::toDto)
                .toList();
    }

    @Override
    public ResponseDTO<List<LimitDTO>> getAllLimitsCurrentUser(String username) {
        log.debug("Request to get all Limits by username: {}", username);
        List<Limit> userLimits = limitRepository.findAllByUsername(username);
        if (userLimits.size() == 0) {
            return ResponseDTO.<List<LimitDTO>>builder()
                    .message("Limit not found by username: " + username)
                    .success(false)
                    .build();
        }

        return ResponseDTO.<List<LimitDTO>>builder()
                .success(true)
                .data(limitMapper.toDto(userLimits))
                .message("OK")
                .build();
    }

    @Override
    public LimitDTO findByUserNameAndApiPath(String userToken, String apiPath) {
        log.debug("Request to get Limits by apiPath: {} and username: {}", apiPath, userToken);
        Optional<Limit> optionalLimit = limitRepository.findByApiPathAndUsernameAndLimitPlan(apiPath, userToken);
        if (optionalLimit.isEmpty()) {
            log.warn("Limit not found by userToken: {}", userToken);
            return null;
        }

        return limitMapper.toDto(optionalLimit.get());
    }

    @Override
    public void updateResetLimitTime(String userToken, String apiPath, Long waitRefill) {
        log.debug("Request to update limit count by username: {} with LimitCount: {}", userToken, waitRefill);
        limitRepository.updateResetLimitTime(waitRefill, userToken, apiPath);
    }
}
