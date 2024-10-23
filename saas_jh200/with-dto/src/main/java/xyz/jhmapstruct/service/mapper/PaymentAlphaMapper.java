package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.PaymentAlpha;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.PaymentAlphaDTO;

/**
 * Mapper for the entity {@link PaymentAlpha} and its DTO {@link PaymentAlphaDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentAlphaMapper extends EntityMapper<PaymentAlphaDTO, PaymentAlpha> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    PaymentAlphaDTO toDto(PaymentAlpha s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
