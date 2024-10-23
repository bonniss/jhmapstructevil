package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.ProductGamma;
import xyz.jhmapstruct.domain.ReviewGamma;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.ProductGammaDTO;
import xyz.jhmapstruct.service.dto.ReviewGammaDTO;

/**
 * Mapper for the entity {@link ReviewGamma} and its DTO {@link ReviewGammaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReviewGammaMapper extends EntityMapper<ReviewGammaDTO, ReviewGamma> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productGammaName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    ReviewGammaDTO toDto(ReviewGamma s);

    @Named("productGammaName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductGammaDTO toDtoProductGammaName(ProductGamma productGamma);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
