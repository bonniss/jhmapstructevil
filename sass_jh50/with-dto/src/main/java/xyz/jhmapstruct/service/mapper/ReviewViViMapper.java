package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.ProductViVi;
import xyz.jhmapstruct.domain.ReviewViVi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.ProductViViDTO;
import xyz.jhmapstruct.service.dto.ReviewViViDTO;

/**
 * Mapper for the entity {@link ReviewViVi} and its DTO {@link ReviewViViDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReviewViViMapper extends EntityMapper<ReviewViViDTO, ReviewViVi> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productViViName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    ReviewViViDTO toDto(ReviewViVi s);

    @Named("productViViName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductViViDTO toDtoProductViViName(ProductViVi productViVi);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}