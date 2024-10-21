package ai.realworld.service.mapper;

import ai.realworld.domain.AlAppleVi;
import ai.realworld.domain.AlInquiryVi;
import ai.realworld.domain.AlPacino;
import ai.realworld.domain.EdSheeranVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlAppleViDTO;
import ai.realworld.service.dto.AlInquiryViDTO;
import ai.realworld.service.dto.AlPacinoDTO;
import ai.realworld.service.dto.EdSheeranViDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlInquiryVi} and its DTO {@link AlInquiryViDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlInquiryViMapper extends EntityMapper<AlInquiryViDTO, AlInquiryVi> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "alPacinoId")
    @Mapping(target = "agency", source = "agency", qualifiedByName = "alAppleViId")
    @Mapping(target = "personInCharge", source = "personInCharge", qualifiedByName = "edSheeranViId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlInquiryViDTO toDto(AlInquiryVi s);

    @Named("alPacinoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlPacinoDTO toDtoAlPacinoId(AlPacino alPacino);

    @Named("alAppleViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlAppleViDTO toDtoAlAppleViId(AlAppleVi alAppleVi);

    @Named("edSheeranViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EdSheeranViDTO toDtoEdSheeranViId(EdSheeranVi edSheeranVi);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
