package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.ProductMi;
import xyz.jhmapstruct.domain.ReviewMi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.ProductMiDTO;
import xyz.jhmapstruct.service.dto.ReviewMiDTO;

/**
 * Mapper for the entity {@link ReviewMi} and its DTO {@link ReviewMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReviewMiMapper extends EntityMapper<ReviewMiDTO, ReviewMi> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productMiName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    ReviewMiDTO toDto(ReviewMi s);

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
