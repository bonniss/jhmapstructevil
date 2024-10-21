package ai.realworld.service.mapper;

import ai.realworld.domain.AndreiRightHand;
import ai.realworld.domain.AndreiRightHandVi;
import ai.realworld.domain.OlMaster;
import ai.realworld.service.dto.AndreiRightHandDTO;
import ai.realworld.service.dto.AndreiRightHandViDTO;
import ai.realworld.service.dto.OlMasterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OlMaster} and its DTO {@link OlMasterDTO}.
 */
@Mapper(componentModel = "spring")
public interface OlMasterMapper extends EntityMapper<OlMasterDTO, OlMaster> {
    @Mapping(target = "address", source = "address", qualifiedByName = "andreiRightHandId")
    @Mapping(target = "addressVi", source = "addressVi", qualifiedByName = "andreiRightHandViId")
    OlMasterDTO toDto(OlMaster s);

    @Named("andreiRightHandId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AndreiRightHandDTO toDtoAndreiRightHandId(AndreiRightHand andreiRightHand);

    @Named("andreiRightHandViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AndreiRightHandViDTO toDtoAndreiRightHandViId(AndreiRightHandVi andreiRightHandVi);
}
