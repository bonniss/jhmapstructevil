package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.CustomerTheta;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.OrderTheta;
import xyz.jhmapstruct.domain.PaymentTheta;
import xyz.jhmapstruct.domain.ShipmentTheta;
import xyz.jhmapstruct.service.dto.CustomerThetaDTO;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.OrderThetaDTO;
import xyz.jhmapstruct.service.dto.PaymentThetaDTO;
import xyz.jhmapstruct.service.dto.ShipmentThetaDTO;

/**
 * Mapper for the entity {@link OrderTheta} and its DTO {@link OrderThetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderThetaMapper extends EntityMapper<OrderThetaDTO, OrderTheta> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentThetaId")
    @Mapping(target = "shipment", source = "shipment", qualifiedByName = "shipmentThetaId")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerThetaEmail")
    OrderThetaDTO toDto(OrderTheta s);

    @Named("paymentThetaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentThetaDTO toDtoPaymentThetaId(PaymentTheta paymentTheta);

    @Named("shipmentThetaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ShipmentThetaDTO toDtoShipmentThetaId(ShipmentTheta shipmentTheta);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("customerThetaEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    CustomerThetaDTO toDtoCustomerThetaEmail(CustomerTheta customerTheta);
}
