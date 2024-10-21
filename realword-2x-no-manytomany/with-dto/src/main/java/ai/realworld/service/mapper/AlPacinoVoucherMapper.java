package ai.realworld.service.mapper;

import ai.realworld.domain.AlPacino;
import ai.realworld.domain.AlPacinoVoucher;
import ai.realworld.domain.AlVueVue;
import ai.realworld.domain.JohnLennon;
import ai.realworld.service.dto.AlPacinoDTO;
import ai.realworld.service.dto.AlPacinoVoucherDTO;
import ai.realworld.service.dto.AlVueVueDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlPacinoVoucher} and its DTO {@link AlPacinoVoucherDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlPacinoVoucherMapper extends EntityMapper<AlPacinoVoucherDTO, AlPacinoVoucher> {
    @Mapping(target = "user", source = "user", qualifiedByName = "alPacinoId")
    @Mapping(target = "voucher", source = "voucher", qualifiedByName = "alVueVueId")
    @Mapping(target = "application", source = "application", qualifiedByName = "johnLennonId")
    AlPacinoVoucherDTO toDto(AlPacinoVoucher s);

    @Named("alPacinoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlPacinoDTO toDtoAlPacinoId(AlPacino alPacino);

    @Named("alVueVueId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlVueVueDTO toDtoAlVueVueId(AlVueVue alVueVue);

    @Named("johnLennonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
