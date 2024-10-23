package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextPaymentGamma;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextPaymentGammaDTO;

/**
 * Mapper for the entity {@link NextPaymentGamma} and its DTO {@link NextPaymentGammaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextPaymentGammaMapper extends EntityMapper<NextPaymentGammaDTO, NextPaymentGamma> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextPaymentGammaDTO toDto(NextPaymentGamma s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
