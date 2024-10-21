package ai.realworld.service.mapper;

import ai.realworld.domain.AlPacino;
import ai.realworld.domain.AlPacinoVoucherVi;
import ai.realworld.domain.AlVueVueVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlPacinoDTO;
import ai.realworld.service.dto.AlPacinoVoucherViDTO;
import ai.realworld.service.dto.AlVueVueViDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlPacinoVoucherVi} and its DTO {@link AlPacinoVoucherViDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlPacinoVoucherViMapper extends EntityMapper<AlPacinoVoucherViDTO, AlPacinoVoucherVi> {
    @Mapping(target = "user", source = "user", qualifiedByName = "alPacinoId")
    @Mapping(target = "voucher", source = "voucher", qualifiedByName = "alVueVueViId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlPacinoVoucherViDTO toDto(AlPacinoVoucherVi s);

    @Named("alPacinoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlPacinoDTO toDtoAlPacinoId(AlPacino alPacino);

    @Named("alVueVueViId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlVueVueViDTO toDtoAlVueVueViId(AlVueVueVi alVueVueVi);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
