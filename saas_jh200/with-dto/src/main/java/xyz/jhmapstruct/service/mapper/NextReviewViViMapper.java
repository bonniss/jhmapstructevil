package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextProductViVi;
import xyz.jhmapstruct.domain.NextReviewViVi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextProductViViDTO;
import xyz.jhmapstruct.service.dto.NextReviewViViDTO;

/**
 * Mapper for the entity {@link NextReviewViVi} and its DTO {@link NextReviewViViDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextReviewViViMapper extends EntityMapper<NextReviewViViDTO, NextReviewViVi> {
    @Mapping(target = "product", source = "product", qualifiedByName = "nextProductViViName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextReviewViViDTO toDto(NextReviewViVi s);

    @Named("nextProductViViName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    NextProductViViDTO toDtoNextProductViViName(NextProductViVi nextProductViVi);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
