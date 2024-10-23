package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.CustomerAlpha;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.OrderAlpha;
import xyz.jhmapstruct.domain.PaymentAlpha;
import xyz.jhmapstruct.domain.ShipmentAlpha;
import xyz.jhmapstruct.service.dto.CustomerAlphaDTO;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.OrderAlphaDTO;
import xyz.jhmapstruct.service.dto.PaymentAlphaDTO;
import xyz.jhmapstruct.service.dto.ShipmentAlphaDTO;

/**
 * Mapper for the entity {@link OrderAlpha} and its DTO {@link OrderAlphaDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderAlphaMapper extends EntityMapper<OrderAlphaDTO, OrderAlpha> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentAlphaId")
    @Mapping(target = "shipment", source = "shipment", qualifiedByName = "shipmentAlphaId")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerAlphaEmail")
    OrderAlphaDTO toDto(OrderAlpha s);

    @Named("paymentAlphaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentAlphaDTO toDtoPaymentAlphaId(PaymentAlpha paymentAlpha);

    @Named("shipmentAlphaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ShipmentAlphaDTO toDtoShipmentAlphaId(ShipmentAlpha shipmentAlpha);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("customerAlphaEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    CustomerAlphaDTO toDtoCustomerAlphaEmail(CustomerAlpha customerAlpha);
}
