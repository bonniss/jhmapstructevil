package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.CustomerVi;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.OrderVi;
import xyz.jhmapstruct.domain.PaymentVi;
import xyz.jhmapstruct.domain.ShipmentVi;
import xyz.jhmapstruct.service.dto.CustomerViDTO;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.OrderViDTO;
import xyz.jhmapstruct.service.dto.PaymentViDTO;
import xyz.jhmapstruct.service.dto.ShipmentViDTO;

/**
 * Mapper for the entity {@link OrderVi} and its DTO {@link OrderViDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderViMapper extends EntityMapper<OrderViDTO, OrderVi> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentViId")
    @Mapping(target = "shipment", source = "shipment", qualifiedByName = "shipmentViId")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerViEmail")
    OrderViDTO toDto(OrderVi s);

    @Named("paymentViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentViDTO toDtoPaymentViId(PaymentVi paymentVi);

    @Named("shipmentViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ShipmentViDTO toDtoShipmentViId(ShipmentVi shipmentVi);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("customerViEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    CustomerViDTO toDtoCustomerViEmail(CustomerVi customerVi);
}
