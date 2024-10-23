package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCustomerBeta;
import xyz.jhmapstruct.domain.NextOrderBeta;
import xyz.jhmapstruct.domain.NextPaymentBeta;
import xyz.jhmapstruct.domain.NextShipmentBeta;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCustomerBetaDTO;
import xyz.jhmapstruct.service.dto.NextOrderBetaDTO;
import xyz.jhmapstruct.service.dto.NextPaymentBetaDTO;
import xyz.jhmapstruct.service.dto.NextShipmentBetaDTO;

/**
 * Mapper for the entity {@link NextOrderBeta} and its DTO {@link NextOrderBetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextOrderBetaMapper extends EntityMapper<NextOrderBetaDTO, NextOrderBeta> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "nextPaymentBetaId")
    @Mapping(target = "shipment", source = "shipment", qualifiedByName = "nextShipmentBetaId")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "nextCustomerBetaEmail")
    NextOrderBetaDTO toDto(NextOrderBeta s);

    @Named("nextPaymentBetaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextPaymentBetaDTO toDtoNextPaymentBetaId(NextPaymentBeta nextPaymentBeta);

    @Named("nextShipmentBetaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextShipmentBetaDTO toDtoNextShipmentBetaId(NextShipmentBeta nextShipmentBeta);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("nextCustomerBetaEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    NextCustomerBetaDTO toDtoNextCustomerBetaEmail(NextCustomerBeta nextCustomerBeta);
}
