package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.InvoiceMiMi;
import xyz.jhmapstruct.service.dto.InvoiceMiMiDTO;

/**
 * Mapper for the entity {@link InvoiceMiMi} and its DTO {@link InvoiceMiMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface InvoiceMiMiMapper extends EntityMapper<InvoiceMiMiDTO, InvoiceMiMi> {}
