package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCustomerTheta;
import xyz.jhmapstruct.domain.NextOrderTheta;
import xyz.jhmapstruct.domain.NextPaymentTheta;
import xyz.jhmapstruct.domain.NextShipmentTheta;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextCustomerThetaDTO;
import xyz.jhmapstruct.service.dto.NextOrderThetaDTO;
import xyz.jhmapstruct.service.dto.NextPaymentThetaDTO;
import xyz.jhmapstruct.service.dto.NextShipmentThetaDTO;

/**
 * Mapper for the entity {@link NextOrderTheta} and its DTO {@link NextOrderThetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextOrderThetaMapper extends EntityMapper<NextOrderThetaDTO, NextOrderTheta> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "nextPaymentThetaId")
    @Mapping(target = "shipment", source = "shipment", qualifiedByName = "nextShipmentThetaId")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "nextCustomerThetaEmail")
    NextOrderThetaDTO toDto(NextOrderTheta s);

    @Named("nextPaymentThetaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextPaymentThetaDTO toDtoNextPaymentThetaId(NextPaymentTheta nextPaymentTheta);

    @Named("nextShipmentThetaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextShipmentThetaDTO toDtoNextShipmentThetaId(NextShipmentTheta nextShipmentTheta);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);

    @Named("nextCustomerThetaEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    NextCustomerThetaDTO toDtoNextCustomerThetaEmail(NextCustomerTheta nextCustomerTheta);
}
