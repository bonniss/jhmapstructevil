package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.CustomerMiMi;
import xyz.jhmapstruct.domain.OrderMiMi;
import xyz.jhmapstruct.domain.PaymentMiMi;
import xyz.jhmapstruct.domain.ShipmentMiMi;
import xyz.jhmapstruct.service.dto.CustomerMiMiDTO;
import xyz.jhmapstruct.service.dto.OrderMiMiDTO;
import xyz.jhmapstruct.service.dto.PaymentMiMiDTO;
import xyz.jhmapstruct.service.dto.ShipmentMiMiDTO;

/**
 * Mapper for the entity {@link OrderMiMi} and its DTO {@link OrderMiMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderMiMiMapper extends EntityMapper<OrderMiMiDTO, OrderMiMi> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentMiMiId")
    @Mapping(target = "shipment", source = "shipment", qualifiedByName = "shipmentMiMiId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerMiMiEmail")
    OrderMiMiDTO toDto(OrderMiMi s);

    @Named("paymentMiMiId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentMiMiDTO toDtoPaymentMiMiId(PaymentMiMi paymentMiMi);

    @Named("shipmentMiMiId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ShipmentMiMiDTO toDtoShipmentMiMiId(ShipmentMiMi shipmentMiMi);

    @Named("customerMiMiEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    CustomerMiMiDTO toDtoCustomerMiMiEmail(CustomerMiMi customerMiMi);
}
