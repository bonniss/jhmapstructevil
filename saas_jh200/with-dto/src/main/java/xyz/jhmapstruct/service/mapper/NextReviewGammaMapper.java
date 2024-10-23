package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextProductGamma;
import xyz.jhmapstruct.domain.NextReviewGamma;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextProductGammaDTO;
import xyz.jhmapstruct.service.dto.NextReviewGammaDTO;

/**
 * Mapper for the entity {@link NextReviewGamma} and its DTO {@link NextReviewGammaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextReviewGammaMapper extends EntityMapper<NextReviewGammaDTO, NextReviewGamma> {
    @Mapping(target = "product", source = "product", qualifiedByName = "nextProductGammaName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextReviewGammaDTO toDto(NextReviewGamma s);

    @Named("nextProductGammaName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    NextProductGammaDTO toDtoNextProductGammaName(NextProductGamma nextProductGamma);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
