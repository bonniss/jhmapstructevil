package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.ProductAlpha;
import xyz.jhmapstruct.domain.ReviewAlpha;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.ProductAlphaDTO;
import xyz.jhmapstruct.service.dto.ReviewAlphaDTO;

/**
 * Mapper for the entity {@link ReviewAlpha} and its DTO {@link ReviewAlphaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReviewAlphaMapper extends EntityMapper<ReviewAlphaDTO, ReviewAlpha> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productAlphaName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    ReviewAlphaDTO toDto(ReviewAlpha s);

    @Named("productAlphaName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductAlphaDTO toDtoProductAlphaName(ProductAlpha productAlpha);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
