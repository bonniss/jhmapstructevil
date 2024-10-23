package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCustomerTheta;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCustomerThetaDTO;

/**
 * Mapper for the entity {@link NextCustomerTheta} and its DTO {@link NextCustomerThetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextCustomerThetaMapper extends EntityMapper<NextCustomerThetaDTO, NextCustomerTheta> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextCustomerThetaDTO toDto(NextCustomerTheta s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
