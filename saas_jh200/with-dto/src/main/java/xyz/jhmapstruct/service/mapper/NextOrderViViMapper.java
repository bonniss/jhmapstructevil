package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCustomerViVi;
import xyz.jhmapstruct.domain.NextOrderViVi;
import xyz.jhmapstruct.domain.NextPaymentViVi;
import xyz.jhmapstruct.domain.NextShipmentViVi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCustomerViViDTO;
import xyz.jhmapstruct.service.dto.NextOrderViViDTO;
import xyz.jhmapstruct.service.dto.NextPaymentViViDTO;
import xyz.jhmapstruct.service.dto.NextShipmentViViDTO;

/**
 * Mapper for the entity {@link NextOrderViVi} and its DTO {@link NextOrderViViDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextOrderViViMapper extends EntityMapper<NextOrderViViDTO, NextOrderViVi> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "nextPaymentViViId")
    @Mapping(target = "shipment", source = "shipment", qualifiedByName = "nextShipmentViViId")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "nextCustomerViViEmail")
    NextOrderViViDTO toDto(NextOrderViVi s);

    @Named("nextPaymentViViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextPaymentViViDTO toDtoNextPaymentViViId(NextPaymentViVi nextPaymentViVi);

    @Named("nextShipmentViViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextShipmentViViDTO toDtoNextShipmentViViId(NextShipmentViVi nextShipmentViVi);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("nextCustomerViViEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    NextCustomerViViDTO toDtoNextCustomerViViEmail(NextCustomerViVi nextCustomerViVi);
}
