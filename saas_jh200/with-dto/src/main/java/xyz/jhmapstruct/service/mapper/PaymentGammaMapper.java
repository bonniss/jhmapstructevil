package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.PaymentGamma;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.PaymentGammaDTO;

/**
 * Mapper for the entity {@link PaymentGamma} and its DTO {@link PaymentGammaDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentGammaMapper extends EntityMapper<PaymentGammaDTO, PaymentGamma> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    PaymentGammaDTO toDto(PaymentGamma s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
