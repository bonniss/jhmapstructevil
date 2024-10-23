package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextProductMiMi;
import xyz.jhmapstruct.domain.NextReviewMiMi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextProductMiMiDTO;
import xyz.jhmapstruct.service.dto.NextReviewMiMiDTO;

/**
 * Mapper for the entity {@link NextReviewMiMi} and its DTO {@link NextReviewMiMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextReviewMiMiMapper extends EntityMapper<NextReviewMiMiDTO, NextReviewMiMi> {
    @Mapping(target = "product", source = "product", qualifiedByName = "nextProductMiMiName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextReviewMiMiDTO toDto(NextReviewMiMi s);

    @Named("nextProductMiMiName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    NextProductMiMiDTO toDtoNextProductMiMiName(NextProductMiMi nextProductMiMi);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
