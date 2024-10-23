package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextPaymentBeta;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextPaymentBetaDTO;

/**
 * Mapper for the entity {@link NextPaymentBeta} and its DTO {@link NextPaymentBetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextPaymentBetaMapper extends EntityMapper<NextPaymentBetaDTO, NextPaymentBeta> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextPaymentBetaDTO toDto(NextPaymentBeta s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
