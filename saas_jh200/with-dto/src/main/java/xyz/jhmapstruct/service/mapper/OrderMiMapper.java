package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCustomerMi;
import xyz.jhmapstruct.domain.NextPaymentMi;
import xyz.jhmapstruct.domain.NextShipmentMi;
import xyz.jhmapstruct.domain.OrderMi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCustomerMiDTO;
import xyz.jhmapstruct.service.dto.NextPaymentMiDTO;
import xyz.jhmapstruct.service.dto.NextShipmentMiDTO;
import xyz.jhmapstruct.service.dto.OrderMiDTO;

/**
 * Mapper for the entity {@link OrderMi} and its DTO {@link OrderMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderMiMapper extends EntityMapper<OrderMiDTO, OrderMi> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "nextPaymentMiId")
    @Mapping(target = "shipment", source = "shipment", qualifiedByName = "nextShipmentMiId")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "nextCustomerMiEmail")
    OrderMiDTO toDto(OrderMi s);

    @Named("nextPaymentMiId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextPaymentMiDTO toDtoNextPaymentMiId(NextPaymentMi nextPaymentMi);

    @Named("nextShipmentMiId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextShipmentMiDTO toDtoNextShipmentMiId(NextShipmentMi nextShipmentMi);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("nextCustomerMiEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    NextCustomerMiDTO toDtoNextCustomerMiEmail(NextCustomerMi nextCustomerMi);
}
