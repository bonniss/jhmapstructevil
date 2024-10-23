package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextPayment;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextPaymentDTO;

/**
 * Mapper for the entity {@link NextPayment} and its DTO {@link NextPaymentDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextPaymentMapper extends EntityMapper<NextPaymentDTO, NextPayment> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextPaymentDTO toDto(NextPayment s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
