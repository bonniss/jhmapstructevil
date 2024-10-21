package ai.realworld.service.mapper;

import ai.realworld.domain.SaisanCog;
import ai.realworld.service.dto.SaisanCogDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-22T00:23:40+0700",
    comments = "version: 1.6.2, compiler: javac, environment: Java 17.0.11 (Eclipse Adoptium)"
)
@Component
public class SaisanCogMapperImpl implements SaisanCogMapper {

    @Override
    public SaisanCog toEntity(SaisanCogDTO dto) {
        if ( dto == null ) {
            return null;
        }

        SaisanCog saisanCog = new SaisanCog();

        saisanCog.setId( dto.getId() );
        saisanCog.setKey( dto.getKey() );
        saisanCog.setValueJason( dto.getValueJason() );

        return saisanCog;
    }

    @Override
    public SaisanCogDTO toDto(SaisanCog entity) {
        if ( entity == null ) {
            return null;
        }

        SaisanCogDTO saisanCogDTO = new SaisanCogDTO();

        saisanCogDTO.setId( entity.getId() );
        saisanCogDTO.setKey( entity.getKey() );
        saisanCogDTO.setValueJason( entity.getValueJason() );

        return saisanCogDTO;
    }

    @Override
    public List<SaisanCog> toEntity(List<SaisanCogDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<SaisanCog> list = new ArrayList<SaisanCog>( dtoList.size() );
        for ( SaisanCogDTO saisanCogDTO : dtoList ) {
            list.add( toEntity( saisanCogDTO ) );
        }

        return list;
    }

    @Override
    public List<SaisanCogDTO> toDto(List<SaisanCog> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<SaisanCogDTO> list = new ArrayList<SaisanCogDTO>( entityList.size() );
        for ( SaisanCog saisanCog : entityList ) {
            list.add( toDto( saisanCog ) );
        }

        return list;
    }

    @Override
    public void partialUpdate(SaisanCog entity, SaisanCogDTO dto) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getKey() != null ) {
            entity.setKey( dto.getKey() );
        }
        if ( dto.getValueJason() != null ) {
            entity.setValueJason( dto.getValueJason() );
        }
    }
}
