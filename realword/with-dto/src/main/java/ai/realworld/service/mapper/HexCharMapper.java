package ai.realworld.service.mapper;

import ai.realworld.domain.HashRoss;
import ai.realworld.domain.HexChar;
import ai.realworld.domain.User;
import ai.realworld.service.dto.HashRossDTO;
import ai.realworld.service.dto.HexCharDTO;
import ai.realworld.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HexChar} and its DTO {@link HexCharDTO}.
 */
@Mapper(componentModel = "spring")
public interface HexCharMapper extends EntityMapper<HexCharDTO, HexChar> {
    @Mapping(target = "internalUser", source = "internalUser", qualifiedByName = "userLogin")
    @Mapping(target = "role", source = "role", qualifiedByName = "hashRossId")
    HexCharDTO toDto(HexChar s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("hashRossId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HashRossDTO toDtoHashRossId(HashRoss hashRoss);
}
