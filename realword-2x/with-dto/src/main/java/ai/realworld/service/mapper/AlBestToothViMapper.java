package ai.realworld.service.mapper;

import ai.realworld.domain.AlBestToothVi;
import ai.realworld.service.dto.AlBestToothViDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlBestToothVi} and its DTO {@link AlBestToothViDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlBestToothViMapper extends EntityMapper<AlBestToothViDTO, AlBestToothVi> {}
