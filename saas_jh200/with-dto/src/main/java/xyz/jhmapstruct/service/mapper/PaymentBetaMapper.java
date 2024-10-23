package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.PaymentBeta;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.PaymentBetaDTO;

/**
 * Mapper for the entity {@link PaymentBeta} and its DTO {@link PaymentBetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentBetaMapper extends EntityMapper<PaymentBetaDTO, PaymentBeta> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    PaymentBetaDTO toDto(PaymentBeta s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
