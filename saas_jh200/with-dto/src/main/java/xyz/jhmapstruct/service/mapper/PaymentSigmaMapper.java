package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.PaymentSigma;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.PaymentSigmaDTO;

/**
 * Mapper for the entity {@link PaymentSigma} and its DTO {@link PaymentSigmaDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentSigmaMapper extends EntityMapper<PaymentSigmaDTO, PaymentSigma> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    PaymentSigmaDTO toDto(PaymentSigma s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
