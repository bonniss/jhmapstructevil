package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.PaymentViVi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.PaymentViViDTO;

/**
 * Mapper for the entity {@link PaymentViVi} and its DTO {@link PaymentViViDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentViViMapper extends EntityMapper<PaymentViViDTO, PaymentViVi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    PaymentViViDTO toDto(PaymentViVi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
