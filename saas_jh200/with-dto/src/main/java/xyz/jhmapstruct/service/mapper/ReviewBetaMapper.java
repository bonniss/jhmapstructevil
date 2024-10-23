package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.ProductBeta;
import xyz.jhmapstruct.domain.ReviewBeta;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.ProductBetaDTO;
import xyz.jhmapstruct.service.dto.ReviewBetaDTO;

/**
 * Mapper for the entity {@link ReviewBeta} and its DTO {@link ReviewBetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReviewBetaMapper extends EntityMapper<ReviewBetaDTO, ReviewBeta> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productBetaName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    ReviewBetaDTO toDto(ReviewBeta s);

    @Named("productBetaName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductBetaDTO toDtoProductBetaName(ProductBeta productBeta);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
