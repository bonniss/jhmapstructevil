package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCustomerVi;
import xyz.jhmapstruct.domain.NextOrderVi;
import xyz.jhmapstruct.domain.NextPaymentVi;
import xyz.jhmapstruct.domain.NextShipmentVi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCustomerViDTO;
import xyz.jhmapstruct.service.dto.NextOrderViDTO;
import xyz.jhmapstruct.service.dto.NextPaymentViDTO;
import xyz.jhmapstruct.service.dto.NextShipmentViDTO;

/**
 * Mapper for the entity {@link NextOrderVi} and its DTO {@link NextOrderViDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextOrderViMapper extends EntityMapper<NextOrderViDTO, NextOrderVi> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "nextPaymentViId")
    @Mapping(target = "shipment", source = "shipment", qualifiedByName = "nextShipmentViId")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "nextCustomerViEmail")
    NextOrderViDTO toDto(NextOrderVi s);

    @Named("nextPaymentViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextPaymentViDTO toDtoNextPaymentViId(NextPaymentVi nextPaymentVi);

    @Named("nextShipmentViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextShipmentViDTO toDtoNextShipmentViId(NextShipmentVi nextShipmentVi);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("nextCustomerViEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    NextCustomerViDTO toDtoNextCustomerViEmail(NextCustomerVi nextCustomerVi);
}
