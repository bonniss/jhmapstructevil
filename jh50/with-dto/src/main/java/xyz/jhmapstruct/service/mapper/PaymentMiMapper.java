package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.PaymentMi;
import xyz.jhmapstruct.service.dto.PaymentMiDTO;

/**
 * Mapper for the entity {@link PaymentMi} and its DTO {@link PaymentMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMiMapper extends EntityMapper<PaymentMiDTO, PaymentMi> {}
