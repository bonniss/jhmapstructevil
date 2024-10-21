package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.Product;
import xyz.jhmapstruct.domain.Review;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.ProductDTO;
import xyz.jhmapstruct.service.dto.ReviewDTO;

/**
 * Mapper for the entity {@link Review} and its DTO {@link ReviewDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReviewMapper extends EntityMapper<ReviewDTO, Review> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    ReviewDTO toDto(Review s);

    @Named("productName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductDTO toDtoProductName(Product product);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
