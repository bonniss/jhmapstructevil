package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.Customer;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.Order;
import xyz.jhmapstruct.domain.Payment;
import xyz.jhmapstruct.domain.Shipment;
import xyz.jhmapstruct.service.dto.CustomerDTO;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.OrderDTO;
import xyz.jhmapstruct.service.dto.PaymentDTO;
import xyz.jhmapstruct.service.dto.ShipmentDTO;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderMapper extends EntityMapper<OrderDTO, Order> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentId")
    @Mapping(target = "shipment", source = "shipment", qualifiedByName = "shipmentId")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerEmail")
    OrderDTO toDto(Order s);

    @Named("paymentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentDTO toDtoPaymentId(Payment payment);

    @Named("shipmentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ShipmentDTO toDtoShipmentId(Shipment shipment);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("customerEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    CustomerDTO toDtoCustomerEmail(Customer customer);
}
