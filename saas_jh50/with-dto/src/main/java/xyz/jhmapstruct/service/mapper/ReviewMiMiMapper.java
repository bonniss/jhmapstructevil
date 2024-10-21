package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.ProductMiMi;
import xyz.jhmapstruct.domain.ReviewMiMi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.ProductMiMiDTO;
import xyz.jhmapstruct.service.dto.ReviewMiMiDTO;

/**
 * Mapper for the entity {@link ReviewMiMi} and its DTO {@link ReviewMiMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReviewMiMiMapper extends EntityMapper<ReviewMiMiDTO, ReviewMiMi> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productMiMiName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    ReviewMiMiDTO toDto(ReviewMiMi s);

    @Named("productMiMiName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductMiMiDTO toDtoProductMiMiName(ProductMiMi productMiMi);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
