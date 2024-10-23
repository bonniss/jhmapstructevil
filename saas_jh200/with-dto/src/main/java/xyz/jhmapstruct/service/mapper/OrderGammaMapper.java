package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.CustomerGamma;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.OrderGamma;
import xyz.jhmapstruct.domain.PaymentGamma;
import xyz.jhmapstruct.domain.ShipmentGamma;
import xyz.jhmapstruct.service.dto.CustomerGammaDTO;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.OrderGammaDTO;
import xyz.jhmapstruct.service.dto.PaymentGammaDTO;
import xyz.jhmapstruct.service.dto.ShipmentGammaDTO;

/**
 * Mapper for the entity {@link OrderGamma} and its DTO {@link OrderGammaDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderGammaMapper extends EntityMapper<OrderGammaDTO, OrderGamma> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentGammaId")
    @Mapping(target = "shipment", source = "shipment", qualifiedByName = "shipmentGammaId")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerGammaEmail")
    OrderGammaDTO toDto(OrderGamma s);

    @Named("paymentGammaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentGammaDTO toDtoPaymentGammaId(PaymentGamma paymentGamma);

    @Named("shipmentGammaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ShipmentGammaDTO toDtoShipmentGammaId(ShipmentGamma shipmentGamma);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("customerGammaEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    CustomerGammaDTO toDtoCustomerGammaEmail(CustomerGamma customerGamma);
}
