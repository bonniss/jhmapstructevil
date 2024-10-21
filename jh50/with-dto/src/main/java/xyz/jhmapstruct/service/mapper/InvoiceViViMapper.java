package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.InvoiceViVi;
import xyz.jhmapstruct.service.dto.InvoiceViViDTO;

/**
 * Mapper for the entity {@link InvoiceViVi} and its DTO {@link InvoiceViViDTO}.
 */
@Mapper(componentModel = "spring")
public interface InvoiceViViMapper extends EntityMapper<InvoiceViViDTO, InvoiceViVi> {}
