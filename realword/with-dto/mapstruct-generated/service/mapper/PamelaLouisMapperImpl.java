package ai.realworld.service.mapper;

import ai.realworld.domain.PamelaLouis;
import ai.realworld.service.dto.PamelaLouisDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-22T00:22:53+0700",
    comments = "version: 1.6.2, compiler: javac, environment: Java 17.0.11 (Eclipse Adoptium)"
)
@Component
public class PamelaLouisMapperImpl implements PamelaLouisMapper {

    @Override
    public PamelaLouis toEntity(PamelaLouisDTO dto) {
        if ( dto == null ) {
            return null;
        }

        PamelaLouis pamelaLouis = new PamelaLouis();

        pamelaLouis.setId( dto.getId() );
        pamelaLouis.setName( dto.getName() );
        pamelaLouis.setConfigJason( dto.getConfigJason() );

        return pamelaLouis;
    }

    @Override
    public PamelaLouisDTO toDto(PamelaLouis entity) {
        if ( entity == null ) {
            return null;
        }

        PamelaLouisDTO pamelaLouisDTO = new PamelaLouisDTO();

        pamelaLouisDTO.setId( entity.getId() );
        pamelaLouisDTO.setName( entity.getName() );
        pamelaLouisDTO.setConfigJason( entity.getConfigJason() );

        return pamelaLouisDTO;
    }

    @Override
    public List<PamelaLouis> toEntity(List<PamelaLouisDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<PamelaLouis> list = new ArrayList<PamelaLouis>( dtoList.size() );
        for ( PamelaLouisDTO pamelaLouisDTO : dtoList ) {
            list.add( toEntity( pamelaLouisDTO ) );
        }

        return list;
    }

    @Override
    public List<PamelaLouisDTO> toDto(List<PamelaLouis> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<PamelaLouisDTO> list = new ArrayList<PamelaLouisDTO>( entityList.size() );
        for ( PamelaLouis pamelaLouis : entityList ) {
            list.add( toDto( pamelaLouis ) );
        }

        return list;
    }

    @Override
    public void partialUpdate(PamelaLouis entity, PamelaLouisDTO dto) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getName() != null ) {
            entity.setName( dto.getName() );
        }
        if ( dto.getConfigJason() != null ) {
            entity.setConfigJason( dto.getConfigJason() );
        }
    }
}
