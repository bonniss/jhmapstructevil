package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCustomer;
import xyz.jhmapstruct.domain.NextOrder;
import xyz.jhmapstruct.domain.NextPayment;
import xyz.jhmapstruct.domain.NextShipment;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCustomerDTO;
import xyz.jhmapstruct.service.dto.NextOrderDTO;
import xyz.jhmapstruct.service.dto.NextPaymentDTO;
import xyz.jhmapstruct.service.dto.NextShipmentDTO;

/**
 * Mapper for the entity {@link NextOrder} and its DTO {@link NextOrderDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextOrderMapper extends EntityMapper<NextOrderDTO, NextOrder> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "nextPaymentId")
    @Mapping(target = "shipment", source = "shipment", qualifiedByName = "nextShipmentId")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "nextCustomerEmail")
    NextOrderDTO toDto(NextOrder s);

    @Named("nextPaymentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextPaymentDTO toDtoNextPaymentId(NextPayment nextPayment);

    @Named("nextShipmentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextShipmentDTO toDtoNextShipmentId(NextShipment nextShipment);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("nextCustomerEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    NextCustomerDTO toDtoNextCustomerEmail(NextCustomer nextCustomer);
}
