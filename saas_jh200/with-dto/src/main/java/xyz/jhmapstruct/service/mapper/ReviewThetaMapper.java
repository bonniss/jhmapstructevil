package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.ProductTheta;
import xyz.jhmapstruct.domain.ReviewTheta;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.ProductThetaDTO;
import xyz.jhmapstruct.service.dto.ReviewThetaDTO;

/**
 * Mapper for the entity {@link ReviewTheta} and its DTO {@link ReviewThetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReviewThetaMapper extends EntityMapper<ReviewThetaDTO, ReviewTheta> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productThetaName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    ReviewThetaDTO toDto(ReviewTheta s);

    @Named("productThetaName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductThetaDTO toDtoProductThetaName(ProductTheta productTheta);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
