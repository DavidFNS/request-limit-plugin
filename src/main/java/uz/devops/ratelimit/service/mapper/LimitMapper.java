package uz.devops.ratelimit.service.mapper;

import org.mapstruct.*;
import uz.devops.ratelimit.domain.Limit;
import uz.devops.ratelimit.service.dto.LimitDTO;

/**
 * Mapper for the entity {@link Limit} and its DTO {@link LimitDTO}.
 */
@Mapper(componentModel = "spring")
public interface LimitMapper extends EntityMapper<LimitDTO, Limit> {
}
