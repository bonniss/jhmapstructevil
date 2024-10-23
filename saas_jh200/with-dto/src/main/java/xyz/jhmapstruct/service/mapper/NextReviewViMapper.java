package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextProductVi;
import xyz.jhmapstruct.domain.NextReviewVi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextProductViDTO;
import xyz.jhmapstruct.service.dto.NextReviewViDTO;

/**
 * Mapper for the entity {@link NextReviewVi} and its DTO {@link NextReviewViDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextReviewViMapper extends EntityMapper<NextReviewViDTO, NextReviewVi> {
    @Mapping(target = "product", source = "product", qualifiedByName = "nextProductViName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextReviewViDTO toDto(NextReviewVi s);

    @Named("nextProductViName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    NextProductViDTO toDtoNextProductViName(NextProductVi nextProductVi);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
