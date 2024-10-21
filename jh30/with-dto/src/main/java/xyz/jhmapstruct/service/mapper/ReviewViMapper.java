package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.ProductVi;
import xyz.jhmapstruct.domain.ReviewVi;
import xyz.jhmapstruct.service.dto.ProductViDTO;
import xyz.jhmapstruct.service.dto.ReviewViDTO;

/**
 * Mapper for the entity {@link ReviewVi} and its DTO {@link ReviewViDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReviewViMapper extends EntityMapper<ReviewViDTO, ReviewVi> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productViName")
    ReviewViDTO toDto(ReviewVi s);

    @Named("productViName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductViDTO toDtoProductViName(ProductVi productVi);
}
