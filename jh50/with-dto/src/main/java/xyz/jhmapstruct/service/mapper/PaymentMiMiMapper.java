package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.PaymentMiMi;
import xyz.jhmapstruct.service.dto.PaymentMiMiDTO;

/**
 * Mapper for the entity {@link PaymentMiMi} and its DTO {@link PaymentMiMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMiMiMapper extends EntityMapper<PaymentMiMiDTO, PaymentMiMi> {}
