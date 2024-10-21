package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.CategoryMi;
import xyz.jhmapstruct.service.dto.CategoryMiDTO;

/**
 * Mapper for the entity {@link CategoryMi} and its DTO {@link CategoryMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoryMiMapper extends EntityMapper<CategoryMiDTO, CategoryMi> {}
