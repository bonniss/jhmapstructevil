package ai.realworld.service.mapper;

import ai.realworld.domain.AndreiRightHand;
import ai.realworld.domain.AntonioBanderas;
import ai.realworld.service.dto.AndreiRightHandDTO;
import ai.realworld.service.dto.AntonioBanderasDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-22T00:22:45+0700",
    comments = "version: 1.6.2, compiler: javac, environment: Java 17.0.11 (Eclipse Adoptium)"
)
@Component
public class AndreiRightHandMapperImpl implements AndreiRightHandMapper {

    @Override
    public AndreiRightHand toEntity(AndreiRightHandDTO dto) {
        if ( dto == null ) {
            return null;
        }

        AndreiRightHand andreiRightHand = new AndreiRightHand();

        andreiRightHand.setId( dto.getId() );
        andreiRightHand.setDetails( dto.getDetails() );
        andreiRightHand.setLat( dto.getLat() );
        andreiRightHand.setLon( dto.getLon() );
        andreiRightHand.country( antonioBanderasDTOToAntonioBanderas( dto.getCountry() ) );
        andreiRightHand.province( antonioBanderasDTOToAntonioBanderas( dto.getProvince() ) );
        andreiRightHand.district( antonioBanderasDTOToAntonioBanderas( dto.getDistrict() ) );
        andreiRightHand.ward( antonioBanderasDTOToAntonioBanderas( dto.getWard() ) );

        return andreiRightHand;
    }

    @Override
    public List<AndreiRightHand> toEntity(List<AndreiRightHandDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<AndreiRightHand> list = new ArrayList<AndreiRightHand>( dtoList.size() );
        for ( AndreiRightHandDTO andreiRightHandDTO : dtoList ) {
            list.add( toEntity( andreiRightHandDTO ) );
        }

        return list;
    }

    @Override
    public List<AndreiRightHandDTO> toDto(List<AndreiRightHand> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<AndreiRightHandDTO> list = new ArrayList<AndreiRightHandDTO>( entityList.size() );
        for ( AndreiRightHand andreiRightHand : entityList ) {
            list.add( toDto( andreiRightHand ) );
        }

        return list;
    }

    @Override
    public void partialUpdate(AndreiRightHand entity, AndreiRightHandDTO dto) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getDetails() != null ) {
            entity.setDetails( dto.getDetails() );
        }
        if ( dto.getLat() != null ) {
            entity.setLat( dto.getLat() );
        }
        if ( dto.getLon() != null ) {
            entity.setLon( dto.getLon() );
        }
        if ( dto.getCountry() != null ) {
            if ( entity.getCountry() == null ) {
                entity.country( new AntonioBanderas() );
            }
            antonioBanderasDTOToAntonioBanderas1( dto.getCountry(), entity.getCountry() );
        }
        if ( dto.getProvince() != null ) {
            if ( entity.getProvince() == null ) {
                entity.province( new AntonioBanderas() );
            }
            antonioBanderasDTOToAntonioBanderas1( dto.getProvince(), entity.getProvince() );
        }
        if ( dto.getDistrict() != null ) {
            if ( entity.getDistrict() == null ) {
                entity.district( new AntonioBanderas() );
            }
            antonioBanderasDTOToAntonioBanderas1( dto.getDistrict(), entity.getDistrict() );
        }
        if ( dto.getWard() != null ) {
            if ( entity.getWard() == null ) {
                entity.ward( new AntonioBanderas() );
            }
            antonioBanderasDTOToAntonioBanderas1( dto.getWard(), entity.getWard() );
        }
    }

    @Override
    public AndreiRightHandDTO toDto(AndreiRightHand s) {
        if ( s == null ) {
            return null;
        }

        AndreiRightHandDTO andreiRightHandDTO = new AndreiRightHandDTO();

        andreiRightHandDTO.setCountry( toDtoAntonioBanderasId( s.getCountry() ) );
        andreiRightHandDTO.setProvince( toDtoAntonioBanderasId( s.getProvince() ) );
        andreiRightHandDTO.setDistrict( toDtoAntonioBanderasId( s.getDistrict() ) );
        andreiRightHandDTO.setWard( toDtoAntonioBanderasId( s.getWard() ) );
        andreiRightHandDTO.setId( s.getId() );
        andreiRightHandDTO.setDetails( s.getDetails() );
        andreiRightHandDTO.setLat( s.getLat() );
        andreiRightHandDTO.setLon( s.getLon() );

        return andreiRightHandDTO;
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

    protected AntonioBanderas antonioBanderasDTOToAntonioBanderas(AntonioBanderasDTO antonioBanderasDTO) {
        if ( antonioBanderasDTO == null ) {
            return null;
        }

        AntonioBanderas antonioBanderas = new AntonioBanderas();

        antonioBanderas.setId( antonioBanderasDTO.getId() );
        antonioBanderas.setLevel( antonioBanderasDTO.getLevel() );
        antonioBanderas.setCode( antonioBanderasDTO.getCode() );
        antonioBanderas.setName( antonioBanderasDTO.getName() );
        antonioBanderas.setFullName( antonioBanderasDTO.getFullName() );
        antonioBanderas.setNativeName( antonioBanderasDTO.getNativeName() );
        antonioBanderas.setOfficialCode( antonioBanderasDTO.getOfficialCode() );
        antonioBanderas.setDivisionTerm( antonioBanderasDTO.getDivisionTerm() );
        antonioBanderas.setIsDeleted( antonioBanderasDTO.getIsDeleted() );
        antonioBanderas.current( antonioBanderasDTOToAntonioBanderas( antonioBanderasDTO.getCurrent() ) );
        antonioBanderas.parent( antonioBanderasDTOToAntonioBanderas( antonioBanderasDTO.getParent() ) );

        return antonioBanderas;
    }

    protected void antonioBanderasDTOToAntonioBanderas1(AntonioBanderasDTO antonioBanderasDTO, AntonioBanderas mappingTarget) {
        if ( antonioBanderasDTO == null ) {
            return;
        }

        if ( antonioBanderasDTO.getId() != null ) {
            mappingTarget.setId( antonioBanderasDTO.getId() );
        }
        if ( antonioBanderasDTO.getLevel() != null ) {
            mappingTarget.setLevel( antonioBanderasDTO.getLevel() );
        }
        if ( antonioBanderasDTO.getCode() != null ) {
            mappingTarget.setCode( antonioBanderasDTO.getCode() );
        }
        if ( antonioBanderasDTO.getName() != null ) {
            mappingTarget.setName( antonioBanderasDTO.getName() );
        }
        if ( antonioBanderasDTO.getFullName() != null ) {
            mappingTarget.setFullName( antonioBanderasDTO.getFullName() );
        }
        if ( antonioBanderasDTO.getNativeName() != null ) {
            mappingTarget.setNativeName( antonioBanderasDTO.getNativeName() );
        }
        if ( antonioBanderasDTO.getOfficialCode() != null ) {
            mappingTarget.setOfficialCode( antonioBanderasDTO.getOfficialCode() );
        }
        if ( antonioBanderasDTO.getDivisionTerm() != null ) {
            mappingTarget.setDivisionTerm( antonioBanderasDTO.getDivisionTerm() );
        }
        if ( antonioBanderasDTO.getIsDeleted() != null ) {
            mappingTarget.setIsDeleted( antonioBanderasDTO.getIsDeleted() );
        }
        if ( antonioBanderasDTO.getCurrent() != null ) {
            if ( mappingTarget.getCurrent() == null ) {
                mappingTarget.current( new AntonioBanderas() );
            }
            antonioBanderasDTOToAntonioBanderas1( antonioBanderasDTO.getCurrent(), mappingTarget.getCurrent() );
        }
        if ( antonioBanderasDTO.getParent() != null ) {
            if ( mappingTarget.getParent() == null ) {
                mappingTarget.parent( new AntonioBanderas() );
            }
            antonioBanderasDTOToAntonioBanderas1( antonioBanderasDTO.getParent(), mappingTarget.getParent() );
        }
    }
}
