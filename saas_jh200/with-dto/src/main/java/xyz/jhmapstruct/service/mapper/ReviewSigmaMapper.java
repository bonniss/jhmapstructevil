package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.ProductSigma;
import xyz.jhmapstruct.domain.ReviewSigma;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.ProductSigmaDTO;
import xyz.jhmapstruct.service.dto.ReviewSigmaDTO;

/**
 * Mapper for the entity {@link ReviewSigma} and its DTO {@link ReviewSigmaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReviewSigmaMapper extends EntityMapper<ReviewSigmaDTO, ReviewSigma> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productSigmaName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    ReviewSigmaDTO toDto(ReviewSigma s);

    @Named("productSigmaName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductSigmaDTO toDtoProductSigmaName(ProductSigma productSigma);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
