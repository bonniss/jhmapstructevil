package ai.realworld.service.mapper;

import ai.realworld.domain.AlPacino;
import ai.realworld.domain.AlPacinoPointHistory;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlPacinoDTO;
import ai.realworld.service.dto.AlPacinoPointHistoryDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlPacinoPointHistory} and its DTO {@link AlPacinoPointHistoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlPacinoPointHistoryMapper extends EntityMapper<AlPacinoPointHistoryDTO, AlPacinoPointHistory> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "alPacinoId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlPacinoPointHistoryDTO toDto(AlPacinoPointHistory s);

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
