package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.PaymentVi;
import xyz.jhmapstruct.service.dto.PaymentViDTO;

/**
 * Mapper for the entity {@link PaymentVi} and its DTO {@link PaymentViDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentViMapper extends EntityMapper<PaymentViDTO, PaymentVi> {}
