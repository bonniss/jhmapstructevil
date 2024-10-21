package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.CategoryVi;
import xyz.jhmapstruct.service.dto.CategoryViDTO;

/**
 * Mapper for the entity {@link CategoryVi} and its DTO {@link CategoryViDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoryViMapper extends EntityMapper<CategoryViDTO, CategoryVi> {}
