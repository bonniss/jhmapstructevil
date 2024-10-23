package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.CustomerMi;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextOrderMi;
import xyz.jhmapstruct.domain.PaymentMi;
import xyz.jhmapstruct.domain.ShipmentMi;
import xyz.jhmapstruct.service.dto.CustomerMiDTO;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextOrderMiDTO;
import xyz.jhmapstruct.service.dto.PaymentMiDTO;
import xyz.jhmapstruct.service.dto.ShipmentMiDTO;

/**
 * Mapper for the entity {@link NextOrderMi} and its DTO {@link NextOrderMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextOrderMiMapper extends EntityMapper<NextOrderMiDTO, NextOrderMi> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentMiId")
    @Mapping(target = "shipment", source = "shipment", qualifiedByName = "shipmentMiId")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerMiEmail")
    NextOrderMiDTO toDto(NextOrderMi s);

    @Named("paymentMiId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentMiDTO toDtoPaymentMiId(PaymentMi paymentMi);

    @Named("shipmentMiId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ShipmentMiDTO toDtoShipmentMiId(ShipmentMi shipmentMi);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("customerMiEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    CustomerMiDTO toDtoCustomerMiEmail(CustomerMi customerMi);
}
