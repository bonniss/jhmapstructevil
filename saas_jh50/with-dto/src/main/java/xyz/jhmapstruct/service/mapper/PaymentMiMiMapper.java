package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.PaymentMiMi;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.PaymentMiMiDTO;

/**
 * Mapper for the entity {@link PaymentMiMi} and its DTO {@link PaymentMiMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMiMiMapper extends EntityMapper<PaymentMiMiDTO, PaymentMiMi> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    PaymentMiMiDTO toDto(PaymentMiMi s);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
