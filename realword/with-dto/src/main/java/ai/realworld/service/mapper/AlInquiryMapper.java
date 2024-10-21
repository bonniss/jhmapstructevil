package ai.realworld.service.mapper;

import ai.realworld.domain.AlApple;
import ai.realworld.domain.AlInquiry;
import ai.realworld.domain.AlPacino;
import ai.realworld.domain.EdSheeran;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlAppleDTO;
import ai.realworld.service.dto.AlInquiryDTO;
import ai.realworld.service.dto.AlPacinoDTO;
import ai.realworld.service.dto.EdSheeranDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlInquiry} and its DTO {@link AlInquiryDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlInquiryMapper extends EntityMapper<AlInquiryDTO, AlInquiry> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "alPacinoId")
    @Mapping(target = "agency", source = "agency", qualifiedByName = "alAppleId")
    @Mapping(target = "personInCharge", source = "personInCharge", qualifiedByName = "edSheeranId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlInquiryDTO toDto(AlInquiry s);

    @Named("alPacinoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlPacinoDTO toDtoAlPacinoId(AlPacino alPacino);

    @Named("alAppleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlAppleDTO toDtoAlAppleId(AlApple alApple);

    @Named("edSheeranId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EdSheeranDTO toDtoEdSheeranId(EdSheeran edSheeran);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
