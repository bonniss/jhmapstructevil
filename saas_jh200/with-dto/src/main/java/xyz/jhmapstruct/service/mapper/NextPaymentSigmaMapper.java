package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextPaymentSigma;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextPaymentSigmaDTO;

/**
 * Mapper for the entity {@link NextPaymentSigma} and its DTO {@link NextPaymentSigmaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextPaymentSigmaMapper extends EntityMapper<NextPaymentSigmaDTO, NextPaymentSigma> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextPaymentSigmaDTO toDto(NextPaymentSigma s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
