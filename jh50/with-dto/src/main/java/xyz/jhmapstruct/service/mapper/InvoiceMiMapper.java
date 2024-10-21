package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.InvoiceMi;
import xyz.jhmapstruct.service.dto.InvoiceMiDTO;

/**
 * Mapper for the entity {@link InvoiceMi} and its DTO {@link InvoiceMiDTO}.
 */
@Mapper(componentModel = "spring")
public interface InvoiceMiMapper extends EntityMapper<InvoiceMiDTO, InvoiceMi> {}
