package ai.realworld.service.mapper;

import ai.realworld.domain.SicilyUmeto;
import ai.realworld.service.dto.SicilyUmetoDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-22T00:23:07+0700",
    comments = "version: 1.6.2, compiler: javac, environment: Java 17.0.11 (Eclipse Adoptium)"
)
@Component
public class SicilyUmetoMapperImpl implements SicilyUmetoMapper {

    @Override
    public SicilyUmeto toEntity(SicilyUmetoDTO dto) {
        if ( dto == null ) {
            return null;
        }

        SicilyUmeto sicilyUmeto = new SicilyUmeto();

        sicilyUmeto.setId( dto.getId() );
        sicilyUmeto.setType( dto.getType() );
        sicilyUmeto.setContent( dto.getContent() );

        return sicilyUmeto;
    }

    @Override
    public SicilyUmetoDTO toDto(SicilyUmeto entity) {
        if ( entity == null ) {
            return null;
        }

        SicilyUmetoDTO sicilyUmetoDTO = new SicilyUmetoDTO();

        sicilyUmetoDTO.setId( entity.getId() );
        sicilyUmetoDTO.setType( entity.getType() );
        sicilyUmetoDTO.setContent( entity.getContent() );

        return sicilyUmetoDTO;
    }

    @Override
    public List<SicilyUmeto> toEntity(List<SicilyUmetoDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<SicilyUmeto> list = new ArrayList<SicilyUmeto>( dtoList.size() );
        for ( SicilyUmetoDTO sicilyUmetoDTO : dtoList ) {
            list.add( toEntity( sicilyUmetoDTO ) );
        }

        return list;
    }

    @Override
    public List<SicilyUmetoDTO> toDto(List<SicilyUmeto> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<SicilyUmetoDTO> list = new ArrayList<SicilyUmetoDTO>( entityList.size() );
        for ( SicilyUmeto sicilyUmeto : entityList ) {
            list.add( toDto( sicilyUmeto ) );
        }

        return list;
    }

    @Override
    public void partialUpdate(SicilyUmeto entity, SicilyUmetoDTO dto) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getType() != null ) {
            entity.setType( dto.getType() );
        }
        if ( dto.getContent() != null ) {
            entity.setContent( dto.getContent() );
        }
    }
}
