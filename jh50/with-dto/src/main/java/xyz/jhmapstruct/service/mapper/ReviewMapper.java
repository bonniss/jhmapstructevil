package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.Product;
import xyz.jhmapstruct.domain.Review;
import xyz.jhmapstruct.service.dto.ProductDTO;
import xyz.jhmapstruct.service.dto.ReviewDTO;

/**
 * Mapper for the entity {@link Review} and its DTO {@link ReviewDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReviewMapper extends EntityMapper<ReviewDTO, Review> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productName")
    ReviewDTO toDto(Review s);

    @Named("productName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductDTO toDtoProductName(Product product);
}
