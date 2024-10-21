package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.PaymentViVi;
import xyz.jhmapstruct.service.dto.PaymentViViDTO;

/**
 * Mapper for the entity {@link PaymentViVi} and its DTO {@link PaymentViViDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentViViMapper extends EntityMapper<PaymentViViDTO, PaymentViVi> {}
