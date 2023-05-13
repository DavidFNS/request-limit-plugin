package uz.devops.ratelimit.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uz.devops.ratelimit.service.LimitService;
import uz.devops.ratelimit.service.dto.LimitDTO;
import uz.devops.ratelimit.service.dto.ResponseDTO;
import uz.devops.ratelimit.utils.HeaderUtil;
import uz.devops.ratelimit.utils.PaginationUtil;
import uz.devops.ratelimit.utils.ResponseUtil;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import java.util.Optional;

/**
 * REST controller for managing {@link uz.devops.ratelimit.domain.Limit}.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LimitResource {

    private final Logger log = LoggerFactory.getLogger(LimitResource.class);

    private static final String ENTITY_NAME = "limit";

    @Value("${spring.application.name}")
    private String applicationName;

    private final LimitService limitService;


    /**
     * {@code POST  /limits} : Create a new limit.
     *
     * @param limitDTO the limitDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new limitDTO, or with status {@code 400 (Bad Request)} if the limit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/limits")
    public ResponseEntity<LimitDTO> createLimit(
            @RequestHeader(value = "X-api-key") String apiKey,
            @RequestHeader(value = "X-limit-plan") String limitPlan,
            @Valid @RequestBody LimitDTO limitDTO
    ) throws URISyntaxException {
        log.debug("REST request to save Limit : {}", limitDTO);
        LimitDTO result = limitService.save(limitDTO);
        return ResponseEntity
            .created(new URI("/api/limits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /limits/:id} : Updates an existing limit.
     *
     * @param id the id of the limitDTO to save.
     * @param limitDTO the limitDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated limitDTO,
     * or with status {@code 400 (Bad Request)} if the limitDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the limitDTO couldn't be updated.
     */
    @PutMapping("/limits/{id}")
    public ResponseEntity<LimitDTO> updateLimit(
            @RequestHeader(value = "X-api-key") String apiKey,
            @RequestHeader(value = "X-limit-plan") String limitPlan,
            @PathVariable(value = "id", required = false) final Long id,
            @Valid @RequestBody LimitDTO limitDTO
    ) {
        LimitDTO result = limitService.update(limitDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, limitDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /limits} : get all the limits.
     *
     * @param page, size the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of limits in body.
     */
    @GetMapping("/limits")
    public ResponseEntity<List<LimitDTO>> getAllLimits(
            @RequestHeader(value = "X-api-key") String apiKey,
            @RequestHeader(value = "X-limit-plan") String limitPlan,
            @RequestParam Integer page, @RequestParam Integer size
    ) {
        log.debug("REST request to get a page of Limits. Page: {} | Size: {}", page, size);
        Page<LimitDTO> limitPage = limitService.findAll(PageRequest.of(page, size));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), limitPage);
        return ResponseEntity.ok().headers(headers).body(limitPage.getContent());
    }

    /**
     * {@code GET  /limits/:id} : get the "id" limit.
     *
     * @param id the id of the limitDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the limitDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/limits/{id}")
    public ResponseEntity<LimitDTO> getLimit(
            @RequestHeader(value = "X-api-key") String apiKey,
            @RequestHeader(value = "X-limit-plan") String limitPlan,
            @PathVariable Long id
    ) {
        log.debug("REST request to get Limit : {}", id);
        Optional<LimitDTO> limitDTO = limitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(limitDTO);
    }

    /**
     * {@code GET  /limits} : get all the limits of current user.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of limits in body.
     */
    @GetMapping("/limits/user-limits")
    public ResponseEntity<List<LimitDTO>> getAllLimitsCurrentUser(
            @RequestHeader(value = "X-api-key") String apiKey,
            @RequestHeader(value = "X-limit-plan") String limitPlan,
            @RequestParam String username
    ) {
        log.debug("REST request to get a page of Limits by username: {}", username);
        ResponseDTO<List<LimitDTO>> result = limitService.getAllLimitsCurrentUser(username);
        return ResponseEntity
                .ok()
                .body(result.getData());
    }
}
