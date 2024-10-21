package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.PaymentMi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.PaymentMiDTO;

/**
 * Mapper for the entity {@link PaymentMi} and its DTO {@link PaymentMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMiMapper extends EntityMapper<PaymentMiDTO, PaymentMi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    PaymentMiDTO toDto(PaymentMi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
