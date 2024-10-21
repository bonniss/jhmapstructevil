package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.CategoryViVi;
import xyz.jhmapstruct.service.dto.CategoryViViDTO;

/**
 * Mapper for the entity {@link CategoryViVi} and its DTO {@link CategoryViViDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoryViViMapper extends EntityMapper<CategoryViViDTO, CategoryViVi> {}
