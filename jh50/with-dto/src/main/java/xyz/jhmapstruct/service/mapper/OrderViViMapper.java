package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.CustomerViVi;
import xyz.jhmapstruct.domain.OrderViVi;
import xyz.jhmapstruct.domain.PaymentViVi;
import xyz.jhmapstruct.domain.ShipmentViVi;
import xyz.jhmapstruct.service.dto.CustomerViViDTO;
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

    @Named("customerViViEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    CustomerViViDTO toDtoCustomerViViEmail(CustomerViVi customerViVi);
}
