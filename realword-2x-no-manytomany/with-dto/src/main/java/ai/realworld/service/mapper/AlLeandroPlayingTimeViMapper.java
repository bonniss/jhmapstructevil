package ai.realworld.service.mapper;

import ai.realworld.domain.AlDesire;
import ai.realworld.domain.AlLeandro;
import ai.realworld.domain.AlLeandroPlayingTimeVi;
import ai.realworld.domain.AlPacino;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlDesireDTO;
import ai.realworld.service.dto.AlLeandroDTO;
import ai.realworld.service.dto.AlLeandroPlayingTimeViDTO;
import ai.realworld.service.dto.AlPacinoDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlLeandroPlayingTimeVi} and its DTO {@link AlLeandroPlayingTimeViDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlLeandroPlayingTimeViMapper extends EntityMapper<AlLeandroPlayingTimeViDTO, AlLeandroPlayingTimeVi> {
    @Mapping(target = "maggi", source = "maggi", qualifiedByName = "alLeandroId")
    @Mapping(target = "user", source = "user", qualifiedByName = "alPacinoId")
    @Mapping(target = "award", source = "award", qualifiedByName = "alDesireId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlLeandroPlayingTimeViDTO toDto(AlLeandroPlayingTimeVi s);

    @Named("alLeandroId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlLeandroDTO toDtoAlLeandroId(AlLeandro alLeandro);

    @Named("alPacinoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlPacinoDTO toDtoAlPacinoId(AlPacino alPacino);

    @Named("alDesireId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlDesireDTO toDtoAlDesireId(AlDesire alDesire);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
