package ai.realworld.service.mapper;

import ai.realworld.domain.AlApple;
import ai.realworld.domain.AlPacino;
import ai.realworld.domain.EdSheeran;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.domain.User;
import ai.realworld.service.dto.AlAppleDTO;
import ai.realworld.service.dto.AlPacinoDTO;
import ai.realworld.service.dto.EdSheeranDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import ai.realworld.service.dto.MetaverseDTO;
import ai.realworld.service.dto.UserDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EdSheeran} and its DTO {@link EdSheeranDTO}.
 */
@Mapper(componentModel = "spring")
public interface EdSheeranMapper extends EntityMapper<EdSheeranDTO, EdSheeran> {
    @Mapping(target = "agency", source = "agency", qualifiedByName = "alAppleId")
    @Mapping(target = "avatar", source = "avatar", qualifiedByName = "metaverseId")
    @Mapping(target = "internalUser", source = "internalUser", qualifiedByName = "userLogin")
    @Mapping(target = "appUser", source = "appUser", qualifiedByName = "alPacinoId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    EdSheeranDTO toDto(EdSheeran s);

    @Named("alAppleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlAppleDTO toDtoAlAppleId(AlApple alApple);

    @Named("metaverseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MetaverseDTO toDtoMetaverseId(Metaverse metaverse);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("alPacinoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlPacinoDTO toDtoAlPacinoId(AlPacino alPacino);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
