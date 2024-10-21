package ai.realworld.service.mapper;

import ai.realworld.domain.AntonioBanderas;
import ai.realworld.service.dto.AntonioBanderasDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-22T00:23:31+0700",
    comments = "version: 1.6.2, compiler: javac, environment: Java 17.0.11 (Eclipse Adoptium)"
)
@Component
public class AntonioBanderasMapperImpl implements AntonioBanderasMapper {

    @Override
    public AntonioBanderas toEntity(AntonioBanderasDTO dto) {
        if ( dto == null ) {
            return null;
        }

        AntonioBanderas antonioBanderas = new AntonioBanderas();

        antonioBanderas.setId( dto.getId() );
        antonioBanderas.setLevel( dto.getLevel() );
        antonioBanderas.setCode( dto.getCode() );
        antonioBanderas.setName( dto.getName() );
        antonioBanderas.setFullName( dto.getFullName() );
        antonioBanderas.setNativeName( dto.getNativeName() );
        antonioBanderas.setOfficialCode( dto.getOfficialCode() );
        antonioBanderas.setDivisionTerm( dto.getDivisionTerm() );
        antonioBanderas.setIsDeleted( dto.getIsDeleted() );
        antonioBanderas.current( toEntity( dto.getCurrent() ) );
        antonioBanderas.parent( toEntity( dto.getParent() ) );

        return antonioBanderas;
    }

    @Override
    public List<AntonioBanderas> toEntity(List<AntonioBanderasDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<AntonioBanderas> list = new ArrayList<AntonioBanderas>( dtoList.size() );
        for ( AntonioBanderasDTO antonioBanderasDTO : dtoList ) {
            list.add( toEntity( antonioBanderasDTO ) );
        }

        return list;
    }

    @Override
    public List<AntonioBanderasDTO> toDto(List<AntonioBanderas> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<AntonioBanderasDTO> list = new ArrayList<AntonioBanderasDTO>( entityList.size() );
        for ( AntonioBanderas antonioBanderas : entityList ) {
            list.add( toDto( antonioBanderas ) );
        }

        return list;
    }

    @Override
    public void partialUpdate(AntonioBanderas entity, AntonioBanderasDTO dto) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getLevel() != null ) {
            entity.setLevel( dto.getLevel() );
        }
        if ( dto.getCode() != null ) {
            entity.setCode( dto.getCode() );
        }
        if ( dto.getName() != null ) {
            entity.setName( dto.getName() );
        }
        if ( dto.getFullName() != null ) {
            entity.setFullName( dto.getFullName() );
        }
        if ( dto.getNativeName() != null ) {
            entity.setNativeName( dto.getNativeName() );
        }
        if ( dto.getOfficialCode() != null ) {
            entity.setOfficialCode( dto.getOfficialCode() );
        }
        if ( dto.getDivisionTerm() != null ) {
            entity.setDivisionTerm( dto.getDivisionTerm() );
        }
        if ( dto.getIsDeleted() != null ) {
            entity.setIsDeleted( dto.getIsDeleted() );
        }
        if ( dto.getCurrent() != null ) {
            entity.current( toEntity( dto.getCurrent() ) );
        }
        if ( dto.getParent() != null ) {
            entity.parent( toEntity( dto.getParent() ) );
        }
    }

    @Override
    public AntonioBanderasDTO toDto(AntonioBanderas s) {
        if ( s == null ) {
            return null;
        }

        AntonioBanderasDTO antonioBanderasDTO = new AntonioBanderasDTO();

        antonioBanderasDTO.setCurrent( toDtoAntonioBanderasId( s.getCurrent() ) );
        antonioBanderasDTO.setParent( toDtoAntonioBanderasCode( s.getParent() ) );
        antonioBanderasDTO.setId( s.getId() );
        antonioBanderasDTO.setLevel( s.getLevel() );
        antonioBanderasDTO.setCode( s.getCode() );
        antonioBanderasDTO.setName( s.getName() );
        antonioBanderasDTO.setFullName( s.getFullName() );
        antonioBanderasDTO.setNativeName( s.getNativeName() );
        antonioBanderasDTO.setOfficialCode( s.getOfficialCode() );
        antonioBanderasDTO.setDivisionTerm( s.getDivisionTerm() );
        antonioBanderasDTO.setIsDeleted( s.getIsDeleted() );

        return antonioBanderasDTO;
    }

    @Override
    public AntonioBanderasDTO toDtoAntonioBanderasId(AntonioBanderas antonioBanderas) {
        if ( antonioBanderas == null ) {
            return null;
        }

        AntonioBanderasDTO antonioBanderasDTO = new AntonioBanderasDTO();

        antonioBanderasDTO.setId( antonioBanderas.getId() );

        return antonioBanderasDTO;
    }

    @Override
    public AntonioBanderasDTO toDtoAntonioBanderasCode(AntonioBanderas antonioBanderas) {
        if ( antonioBanderas == null ) {
            return null;
        }

        AntonioBanderasDTO antonioBanderasDTO = new AntonioBanderasDTO();

        antonioBanderasDTO.setId( antonioBanderas.getId() );
        antonioBanderasDTO.setCode( antonioBanderas.getCode() );

        return antonioBanderasDTO;
    }
}
