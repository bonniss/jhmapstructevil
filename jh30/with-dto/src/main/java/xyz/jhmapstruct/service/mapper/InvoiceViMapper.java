package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.InvoiceVi;
import xyz.jhmapstruct.service.dto.InvoiceViDTO;

/**
 * Mapper for the entity {@link InvoiceVi} and its DTO {@link InvoiceViDTO}.
 */
@Mapper(componentModel = "spring")
public interface InvoiceViMapper extends EntityMapper<InvoiceViDTO, InvoiceVi> {}
