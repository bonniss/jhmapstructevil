package ai.realworld.service.mapper;

import ai.realworld.domain.AlAppleVi;
import ai.realworld.domain.AlPacino;
import ai.realworld.domain.EdSheeranVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.domain.User;
import ai.realworld.service.dto.AlAppleViDTO;
import ai.realworld.service.dto.AlPacinoDTO;
import ai.realworld.service.dto.EdSheeranViDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import ai.realworld.service.dto.MetaverseDTO;
import ai.realworld.service.dto.UserDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EdSheeranVi} and its DTO {@link EdSheeranViDTO}.
 */
@Mapper(componentModel = "spring")
public interface EdSheeranViMapper extends EntityMapper<EdSheeranViDTO, EdSheeranVi> {
    @Mapping(target = "agency", source = "agency", qualifiedByName = "alAppleViId")
    @Mapping(target = "avatar", source = "avatar", qualifiedByName = "metaverseId")
    @Mapping(target = "internalUser", source = "internalUser", qualifiedByName = "userLogin")
    @Mapping(target = "appUser", source = "appUser", qualifiedByName = "alPacinoId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    EdSheeranViDTO toDto(EdSheeranVi s);

    @Named("alAppleViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlAppleViDTO toDtoAlAppleViId(AlAppleVi alAppleVi);

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
