package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.CustomerBeta;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.OrderBeta;
import xyz.jhmapstruct.domain.PaymentBeta;
import xyz.jhmapstruct.domain.ShipmentBeta;
import xyz.jhmapstruct.service.dto.CustomerBetaDTO;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.OrderBetaDTO;
import xyz.jhmapstruct.service.dto.PaymentBetaDTO;
import xyz.jhmapstruct.service.dto.ShipmentBetaDTO;

/**
 * Mapper for the entity {@link OrderBeta} and its DTO {@link OrderBetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderBetaMapper extends EntityMapper<OrderBetaDTO, OrderBeta> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentBetaId")
    @Mapping(target = "shipment", source = "shipment", qualifiedByName = "shipmentBetaId")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerBetaEmail")
    OrderBetaDTO toDto(OrderBeta s);

    @Named("paymentBetaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentBetaDTO toDtoPaymentBetaId(PaymentBeta paymentBeta);

    @Named("shipmentBetaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ShipmentBetaDTO toDtoShipmentBetaId(ShipmentBeta shipmentBeta);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("customerBetaEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    CustomerBetaDTO toDtoCustomerBetaEmail(CustomerBeta customerBeta);
}
