package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCustomerAlpha;
import xyz.jhmapstruct.domain.NextOrderAlpha;
import xyz.jhmapstruct.domain.NextPaymentAlpha;
import xyz.jhmapstruct.domain.NextShipmentAlpha;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCustomerAlphaDTO;
import xyz.jhmapstruct.service.dto.NextOrderAlphaDTO;
import xyz.jhmapstruct.service.dto.NextPaymentAlphaDTO;
import xyz.jhmapstruct.service.dto.NextShipmentAlphaDTO;

/**
 * Mapper for the entity {@link NextOrderAlpha} and its DTO {@link NextOrderAlphaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextOrderAlphaMapper extends EntityMapper<NextOrderAlphaDTO, NextOrderAlpha> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "nextPaymentAlphaId")
    @Mapping(target = "shipment", source = "shipment", qualifiedByName = "nextShipmentAlphaId")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "nextCustomerAlphaEmail")
    NextOrderAlphaDTO toDto(NextOrderAlpha s);

    @Named("nextPaymentAlphaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextPaymentAlphaDTO toDtoNextPaymentAlphaId(NextPaymentAlpha nextPaymentAlpha);

    @Named("nextShipmentAlphaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextShipmentAlphaDTO toDtoNextShipmentAlphaId(NextShipmentAlpha nextShipmentAlpha);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("nextCustomerAlphaEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    NextCustomerAlphaDTO toDtoNextCustomerAlphaEmail(NextCustomerAlpha nextCustomerAlpha);
}
