package ai.realworld.service.mapper;

import ai.realworld.domain.AlCatalina;
import ai.realworld.domain.AlLexFerg;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.service.dto.AlCatalinaDTO;
import ai.realworld.service.dto.AlLexFergDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import ai.realworld.service.dto.MetaverseDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlLexFerg} and its DTO {@link AlLexFergDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlLexFergMapper extends EntityMapper<AlLexFergDTO, AlLexFerg> {
    @Mapping(target = "avatar", source = "avatar", qualifiedByName = "metaverseId")
    @Mapping(target = "category", source = "category", qualifiedByName = "alCatalinaId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlLexFergDTO toDto(AlLexFerg s);

    @Named("metaverseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MetaverseDTO toDtoMetaverseId(Metaverse metaverse);

    @Named("alCatalinaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlCatalinaDTO toDtoAlCatalinaId(AlCatalina alCatalina);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
