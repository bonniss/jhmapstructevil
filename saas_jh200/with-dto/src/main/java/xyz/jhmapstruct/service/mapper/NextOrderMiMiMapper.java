package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCustomerMiMi;
import xyz.jhmapstruct.domain.NextOrderMiMi;
import xyz.jhmapstruct.domain.NextPaymentMiMi;
import xyz.jhmapstruct.domain.NextShipmentMiMi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCustomerMiMiDTO;
import xyz.jhmapstruct.service.dto.NextOrderMiMiDTO;
import xyz.jhmapstruct.service.dto.NextPaymentMiMiDTO;
import xyz.jhmapstruct.service.dto.NextShipmentMiMiDTO;

/**
 * Mapper for the entity {@link NextOrderMiMi} and its DTO {@link NextOrderMiMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextOrderMiMiMapper extends EntityMapper<NextOrderMiMiDTO, NextOrderMiMi> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "nextPaymentMiMiId")
    @Mapping(target = "shipment", source = "shipment", qualifiedByName = "nextShipmentMiMiId")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "nextCustomerMiMiEmail")
    NextOrderMiMiDTO toDto(NextOrderMiMi s);

    @Named("nextPaymentMiMiId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextPaymentMiMiDTO toDtoNextPaymentMiMiId(NextPaymentMiMi nextPaymentMiMi);

    @Named("nextShipmentMiMiId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextShipmentMiMiDTO toDtoNextShipmentMiMiId(NextShipmentMiMi nextShipmentMiMi);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("nextCustomerMiMiEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    NextCustomerMiMiDTO toDtoNextCustomerMiMiEmail(NextCustomerMiMi nextCustomerMiMi);
}
