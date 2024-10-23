package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextReviewMi;
import xyz.jhmapstruct.domain.ProductMi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextReviewMiDTO;
import xyz.jhmapstruct.service.dto.ProductMiDTO;

/**
 * Mapper for the entity {@link NextReviewMi} and its DTO {@link NextReviewMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextReviewMiMapper extends EntityMapper<NextReviewMiDTO, NextReviewMi> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productMiName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextReviewMiDTO toDto(NextReviewMi s);

    @Named("productMiName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductMiDTO toDtoProductMiName(ProductMi productMi);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
