package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.CustomerViVi;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.OrderViVi;
import xyz.jhmapstruct.domain.PaymentViVi;
import xyz.jhmapstruct.domain.ShipmentViVi;
import xyz.jhmapstruct.service.dto.CustomerViViDTO;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.OrderViViDTO;
import xyz.jhmapstruct.service.dto.PaymentViViDTO;
import xyz.jhmapstruct.service.dto.ShipmentViViDTO;

/**
 * Mapper for the entity {@link OrderViVi} and its DTO {@link OrderViViDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderViViMapper extends EntityMapper<OrderViViDTO, OrderViVi> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentViViId")
    @Mapping(target = "shipment", source = "shipment", qualifiedByName = "shipmentViViId")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerViViEmail")
    OrderViViDTO toDto(OrderViVi s);

    @Named("paymentViViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentViViDTO toDtoPaymentViViId(PaymentViVi paymentViVi);

    @Named("shipmentViViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ShipmentViViDTO toDtoShipmentViViId(ShipmentViVi shipmentViVi);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("customerViViEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    CustomerViViDTO toDtoCustomerViViEmail(CustomerViVi customerViVi);
}
