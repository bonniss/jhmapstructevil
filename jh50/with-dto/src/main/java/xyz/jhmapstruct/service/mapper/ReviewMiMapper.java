package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.ProductMi;
import xyz.jhmapstruct.domain.ReviewMi;
import xyz.jhmapstruct.service.dto.ProductMiDTO;
import xyz.jhmapstruct.service.dto.ReviewMiDTO;

/**
 * Mapper for the entity {@link ReviewMi} and its DTO {@link ReviewMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReviewMiMapper extends EntityMapper<ReviewMiDTO, ReviewMi> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productMiName")
    ReviewMiDTO toDto(ReviewMi s);

    @Named("productMiName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductMiDTO toDtoProductMiName(ProductMi productMi);
}
