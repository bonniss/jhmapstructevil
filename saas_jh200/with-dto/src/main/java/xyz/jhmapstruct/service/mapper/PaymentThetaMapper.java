package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.PaymentTheta;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.PaymentThetaDTO;

/**
 * Mapper for the entity {@link PaymentTheta} and its DTO {@link PaymentThetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentThetaMapper extends EntityMapper<PaymentThetaDTO, PaymentTheta> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    PaymentThetaDTO toDto(PaymentTheta s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
