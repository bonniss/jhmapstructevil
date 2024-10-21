package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.CustomerMi;
import xyz.jhmapstruct.domain.OrderMi;
import xyz.jhmapstruct.domain.PaymentMi;
import xyz.jhmapstruct.domain.ShipmentMi;
import xyz.jhmapstruct.service.dto.CustomerMiDTO;
import xyz.jhmapstruct.service.dto.OrderMiDTO;
import xyz.jhmapstruct.service.dto.PaymentMiDTO;
import xyz.jhmapstruct.service.dto.ShipmentMiDTO;

/**
 * Mapper for the entity {@link OrderMi} and its DTO {@link OrderMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderMiMapper extends EntityMapper<OrderMiDTO, OrderMi> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentMiId")
    @Mapping(target = "shipment", source = "shipment", qualifiedByName = "shipmentMiId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerMiEmail")
    OrderMiDTO toDto(OrderMi s);

    @Named("paymentMiId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentMiDTO toDtoPaymentMiId(PaymentMi paymentMi);

    @Named("shipmentMiId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ShipmentMiDTO toDtoShipmentMiId(ShipmentMi shipmentMi);

    @Named("customerMiEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    CustomerMiDTO toDtoCustomerMiEmail(CustomerMi customerMi);
}
