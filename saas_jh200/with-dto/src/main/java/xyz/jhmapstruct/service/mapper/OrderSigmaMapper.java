package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.CustomerSigma;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.OrderSigma;
import xyz.jhmapstruct.domain.PaymentSigma;
import xyz.jhmapstruct.domain.ShipmentSigma;
import xyz.jhmapstruct.service.dto.CustomerSigmaDTO;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.OrderSigmaDTO;
import xyz.jhmapstruct.service.dto.PaymentSigmaDTO;
import xyz.jhmapstruct.service.dto.ShipmentSigmaDTO;

/**
 * Mapper for the entity {@link OrderSigma} and its DTO {@link OrderSigmaDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderSigmaMapper extends EntityMapper<OrderSigmaDTO, OrderSigma> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentSigmaId")
    @Mapping(target = "shipment", source = "shipment", qualifiedByName = "shipmentSigmaId")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerSigmaEmail")
    OrderSigmaDTO toDto(OrderSigma s);

    @Named("paymentSigmaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentSigmaDTO toDtoPaymentSigmaId(PaymentSigma paymentSigma);

    @Named("shipmentSigmaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ShipmentSigmaDTO toDtoShipmentSigmaId(ShipmentSigma shipmentSigma);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("customerSigmaEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    CustomerSigmaDTO toDtoCustomerSigmaEmail(CustomerSigma customerSigma);
}
