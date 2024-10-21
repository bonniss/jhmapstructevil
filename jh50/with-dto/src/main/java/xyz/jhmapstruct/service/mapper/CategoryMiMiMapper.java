package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.CategoryMiMi;
import xyz.jhmapstruct.service.dto.CategoryMiMiDTO;

/**
 * Mapper for the entity {@link CategoryMiMi} and its DTO {@link CategoryMiMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoryMiMiMapper extends EntityMapper<CategoryMiMiDTO, CategoryMiMi> {}
