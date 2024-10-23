package xyz.jhmapstruct.service.mapper;

import org.mapstruct.*;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextProduct;
import xyz.jhmapstruct.domain.NextReview;
import xyz.jhmapstruct.service.dto.MasterTenantDTO;
import xyz.jhmapstruct.service.dto.NextProductDTO;
import xyz.jhmapstruct.service.dto.NextReviewDTO;

/**
 * Mapper for the entity {@link NextReview} and its DTO {@link NextReviewDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextReviewMapper extends EntityMapper<NextReviewDTO, NextReview> {
    @Mapping(target = "product", source = "product", qualifiedByName = "nextProductName")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "masterTenantId")
    NextReviewDTO toDto(NextReview s);

    @Named("nextProductName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    NextProductDTO toDtoNextProductName(NextProduct nextProduct);

    @Named("masterTenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterTenantDTO toDtoMasterTenantId(MasterTenant masterTenant);
}
