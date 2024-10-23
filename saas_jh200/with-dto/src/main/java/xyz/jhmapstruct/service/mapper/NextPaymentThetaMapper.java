package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextPaymentTheta;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextPaymentThetaDTO;

/**
 * Mapper for the entity {@link NextPaymentTheta} and its DTO {@link NextPaymentThetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextPaymentThetaMapper extends EntityMapper<NextPaymentThetaDTO, NextPaymentTheta> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextPaymentThetaDTO toDto(NextPaymentTheta s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
