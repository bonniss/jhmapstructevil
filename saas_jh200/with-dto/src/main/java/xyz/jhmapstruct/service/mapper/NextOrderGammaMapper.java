package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCustomerGamma;
import xyz.jhmapstruct.domain.NextOrderGamma;
import xyz.jhmapstruct.domain.NextPaymentGamma;
import xyz.jhmapstruct.domain.NextShipmentGamma;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCustomerGammaDTO;
import xyz.jhmapstruct.service.dto.NextOrderGammaDTO;
import xyz.jhmapstruct.service.dto.NextPaymentGammaDTO;
import xyz.jhmapstruct.service.dto.NextShipmentGammaDTO;

/**
 * Mapper for the entity {@link NextOrderGamma} and its DTO {@link NextOrderGammaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextOrderGammaMapper extends EntityMapper<NextOrderGammaDTO, NextOrderGamma> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "nextPaymentGammaId")
    @Mapping(target = "shipment", source = "shipment", qualifiedByName = "nextShipmentGammaId")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "nextCustomerGammaEmail")
    NextOrderGammaDTO toDto(NextOrderGamma s);

    @Named("nextPaymentGammaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextPaymentGammaDTO toDtoNextPaymentGammaId(NextPaymentGamma nextPaymentGamma);

    @Named("nextShipmentGammaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextShipmentGammaDTO toDtoNextShipmentGammaId(NextShipmentGamma nextShipmentGamma);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("nextCustomerGammaEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    NextCustomerGammaDTO toDtoNextCustomerGammaEmail(NextCustomerGamma nextCustomerGamma);
}
