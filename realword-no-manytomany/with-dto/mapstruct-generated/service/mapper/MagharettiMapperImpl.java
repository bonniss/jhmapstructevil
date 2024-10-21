package ai.realworld.service.mapper;

import ai.realworld.domain.Magharetti;
import ai.realworld.service.dto.MagharettiDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-22T00:29:22+0700",
    comments = "version: 1.6.2, compiler: javac, environment: Java 17.0.11 (Eclipse Adoptium)"
)
@Component
public class MagharettiMapperImpl implements MagharettiMapper {

    @Override
    public Magharetti toEntity(MagharettiDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Magharetti magharetti = new Magharetti();

        magharetti.setId( dto.getId() );
        magharetti.setName( dto.getName() );
        magharetti.setLabel( dto.getLabel() );
        magharetti.setType( dto.getType() );

        return magharetti;
    }

    @Override
    public MagharettiDTO toDto(Magharetti entity) {
        if ( entity == null ) {
            return null;
        }

        MagharettiDTO magharettiDTO = new MagharettiDTO();

        magharettiDTO.setId( entity.getId() );
        magharettiDTO.setName( entity.getName() );
        magharettiDTO.setLabel( entity.getLabel() );
        magharettiDTO.setType( entity.getType() );

        return magharettiDTO;
    }

    @Override
    public List<Magharetti> toEntity(List<MagharettiDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Magharetti> list = new ArrayList<Magharetti>( dtoList.size() );
        for ( MagharettiDTO magharettiDTO : dtoList ) {
            list.add( toEntity( magharettiDTO ) );
        }

        return list;
    }

    @Override
    public List<MagharettiDTO> toDto(List<Magharetti> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<MagharettiDTO> list = new ArrayList<MagharettiDTO>( entityList.size() );
        for ( Magharetti magharetti : entityList ) {
            list.add( toDto( magharetti ) );
        }

        return list;
    }

    @Override
    public void partialUpdate(Magharetti entity, MagharettiDTO dto) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getName() != null ) {
            entity.setName( dto.getName() );
        }
        if ( dto.getLabel() != null ) {
            entity.setLabel( dto.getLabel() );
        }
        if ( dto.getType() != null ) {
            entity.setType( dto.getType() );
        }
    }
}
