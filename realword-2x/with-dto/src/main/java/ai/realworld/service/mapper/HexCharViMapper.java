package ai.realworld.service.mapper;

import ai.realworld.domain.HashRossVi;
import ai.realworld.domain.HexCharVi;
import ai.realworld.domain.User;
import ai.realworld.service.dto.HashRossViDTO;
import ai.realworld.service.dto.HexCharViDTO;
import ai.realworld.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HexCharVi} and its DTO {@link HexCharViDTO}.
 */
@Mapper(componentModel = "spring")
public interface HexCharViMapper extends EntityMapper<HexCharViDTO, HexCharVi> {
    @Mapping(target = "internalUser", source = "internalUser", qualifiedByName = "userLogin")
    @Mapping(target = "role", source = "role", qualifiedByName = "hashRossViId")
    HexCharViDTO toDto(HexCharVi s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("hashRossViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HashRossViDTO toDtoHashRossViId(HashRossVi hashRossVi);
}
