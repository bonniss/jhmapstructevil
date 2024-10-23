package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCustomerSigma;
import xyz.jhmapstruct.domain.NextOrderSigma;
import xyz.jhmapstruct.domain.NextPaymentSigma;
import xyz.jhmapstruct.domain.NextShipmentSigma;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCustomerSigmaDTO;
import xyz.jhmapstruct.service.dto.NextOrderSigmaDTO;
import xyz.jhmapstruct.service.dto.NextPaymentSigmaDTO;
import xyz.jhmapstruct.service.dto.NextShipmentSigmaDTO;

/**
 * Mapper for the entity {@link NextOrderSigma} and its DTO {@link NextOrderSigmaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextOrderSigmaMapper extends EntityMapper<NextOrderSigmaDTO, NextOrderSigma> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "nextPaymentSigmaId")
    @Mapping(target = "shipment", source = "shipment", qualifiedByName = "nextShipmentSigmaId")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "nextCustomerSigmaEmail")
    NextOrderSigmaDTO toDto(NextOrderSigma s);

    @Named("nextPaymentSigmaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextPaymentSigmaDTO toDtoNextPaymentSigmaId(NextPaymentSigma nextPaymentSigma);

    @Named("nextShipmentSigmaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextShipmentSigmaDTO toDtoNextShipmentSigmaId(NextShipmentSigma nextShipmentSigma);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("nextCustomerSigmaEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    NextCustomerSigmaDTO toDtoNextCustomerSigmaEmail(NextCustomerSigma nextCustomerSigma);
}
