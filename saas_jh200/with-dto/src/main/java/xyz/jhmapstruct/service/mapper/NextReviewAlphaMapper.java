package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextProductAlpha;
import xyz.jhmapstruct.domain.NextReviewAlpha;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextProductAlphaDTO;
import xyz.jhmapstruct.service.dto.NextReviewAlphaDTO;

/**
 * Mapper for the entity {@link NextReviewAlpha} and its DTO {@link NextReviewAlphaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextReviewAlphaMapper extends EntityMapper<NextReviewAlphaDTO, NextReviewAlpha> {
    @Mapping(target = "product", source = "product", qualifiedByName = "nextProductAlphaName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextReviewAlphaDTO toDto(NextReviewAlpha s);

    @Named("nextProductAlphaName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    NextProductAlphaDTO toDtoNextProductAlphaName(NextProductAlpha nextProductAlpha);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
