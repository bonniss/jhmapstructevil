package ai.realworld.service.mapper;

import ai.realworld.domain.AndreiRightHand;
import ai.realworld.domain.AntonioBanderas;
import ai.realworld.domain.OlMaster;
import ai.realworld.service.dto.AndreiRightHandDTO;
import ai.realworld.service.dto.AntonioBanderasDTO;
import ai.realworld.service.dto.OlMasterDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-22T00:29:27+0700",
    comments = "version: 1.6.2, compiler: javac, environment: Java 17.0.11 (Eclipse Adoptium)"
)
@Component
public class OlMasterMapperImpl implements OlMasterMapper {

    @Override
    public OlMaster toEntity(OlMasterDTO dto) {
        if ( dto == null ) {
            return null;
        }

        OlMaster olMaster = new OlMaster();

        olMaster.setId( dto.getId() );
        olMaster.setName( dto.getName() );
        olMaster.setSlug( dto.getSlug() );
        olMaster.setDescriptionHeitiga( dto.getDescriptionHeitiga() );
        olMaster.setBusinessType( dto.getBusinessType() );
        olMaster.setEmail( dto.getEmail() );
        olMaster.setHotline( dto.getHotline() );
        olMaster.setTaxCode( dto.getTaxCode() );
        olMaster.setContactsJason( dto.getContactsJason() );
        olMaster.setExtensionJason( dto.getExtensionJason() );
        olMaster.setIsEnabled( dto.getIsEnabled() );
        olMaster.address( andreiRightHandDTOToAndreiRightHand( dto.getAddress() ) );

        return olMaster;
    }

    @Override
    public List<OlMaster> toEntity(List<OlMasterDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<OlMaster> list = new ArrayList<OlMaster>( dtoList.size() );
        for ( OlMasterDTO olMasterDTO : dtoList ) {
            list.add( toEntity( olMasterDTO ) );
        }

        return list;
    }

    @Override
    public List<OlMasterDTO> toDto(List<OlMaster> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<OlMasterDTO> list = new ArrayList<OlMasterDTO>( entityList.size() );
        for ( OlMaster olMaster : entityList ) {
            list.add( toDto( olMaster ) );
        }

        return list;
    }

    @Override
    public void partialUpdate(OlMaster entity, OlMasterDTO dto) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getName() != null ) {
            entity.setName( dto.getName() );
        }
        if ( dto.getSlug() != null ) {
            entity.setSlug( dto.getSlug() );
        }
        if ( dto.getDescriptionHeitiga() != null ) {
            entity.setDescriptionHeitiga( dto.getDescriptionHeitiga() );
        }
        if ( dto.getBusinessType() != null ) {
            entity.setBusinessType( dto.getBusinessType() );
        }
        if ( dto.getEmail() != null ) {
            entity.setEmail( dto.getEmail() );
        }
        if ( dto.getHotline() != null ) {
            entity.setHotline( dto.getHotline() );
        }
        if ( dto.getTaxCode() != null ) {
            entity.setTaxCode( dto.getTaxCode() );
        }
        if ( dto.getContactsJason() != null ) {
            entity.setContactsJason( dto.getContactsJason() );
        }
        if ( dto.getExtensionJason() != null ) {
            entity.setExtensionJason( dto.getExtensionJason() );
        }
        if ( dto.getIsEnabled() != null ) {
            entity.setIsEnabled( dto.getIsEnabled() );
        }
        if ( dto.getAddress() != null ) {
            if ( entity.getAddress() == null ) {
                entity.address( new AndreiRightHand() );
            }
            andreiRightHandDTOToAndreiRightHand1( dto.getAddress(), entity.getAddress() );
        }
    }

    @Override
    public OlMasterDTO toDto(OlMaster s) {
        if ( s == null ) {
            return null;
        }

        OlMasterDTO olMasterDTO = new OlMasterDTO();

        olMasterDTO.setAddress( toDtoAndreiRightHandId( s.getAddress() ) );
        olMasterDTO.setId( s.getId() );
        olMasterDTO.setName( s.getName() );
        olMasterDTO.setSlug( s.getSlug() );
        olMasterDTO.setDescriptionHeitiga( s.getDescriptionHeitiga() );
        olMasterDTO.setBusinessType( s.getBusinessType() );
        olMasterDTO.setEmail( s.getEmail() );
        olMasterDTO.setHotline( s.getHotline() );
        olMasterDTO.setTaxCode( s.getTaxCode() );
        olMasterDTO.setContactsJason( s.getContactsJason() );
        olMasterDTO.setExtensionJason( s.getExtensionJason() );
        olMasterDTO.setIsEnabled( s.getIsEnabled() );

        return olMasterDTO;
    }

    @Override
    public AndreiRightHandDTO toDtoAndreiRightHandId(AndreiRightHand andreiRightHand) {
        if ( andreiRightHand == null ) {
            return null;
        }

        AndreiRightHandDTO andreiRightHandDTO = new AndreiRightHandDTO();

        andreiRightHandDTO.setId( andreiRightHand.getId() );

        return andreiRightHandDTO;
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

    protected AndreiRightHand andreiRightHandDTOToAndreiRightHand(AndreiRightHandDTO andreiRightHandDTO) {
        if ( andreiRightHandDTO == null ) {
            return null;
        }

        AndreiRightHand andreiRightHand = new AndreiRightHand();

        andreiRightHand.setId( andreiRightHandDTO.getId() );
        andreiRightHand.setDetails( andreiRightHandDTO.getDetails() );
        andreiRightHand.setLat( andreiRightHandDTO.getLat() );
        andreiRightHand.setLon( andreiRightHandDTO.getLon() );
        andreiRightHand.country( antonioBanderasDTOToAntonioBanderas( andreiRightHandDTO.getCountry() ) );
        andreiRightHand.province( antonioBanderasDTOToAntonioBanderas( andreiRightHandDTO.getProvince() ) );
        andreiRightHand.district( antonioBanderasDTOToAntonioBanderas( andreiRightHandDTO.getDistrict() ) );
        andreiRightHand.ward( antonioBanderasDTOToAntonioBanderas( andreiRightHandDTO.getWard() ) );

        return andreiRightHand;
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

    protected void andreiRightHandDTOToAndreiRightHand1(AndreiRightHandDTO andreiRightHandDTO, AndreiRightHand mappingTarget) {
        if ( andreiRightHandDTO == null ) {
            return;
        }

        if ( andreiRightHandDTO.getId() != null ) {
            mappingTarget.setId( andreiRightHandDTO.getId() );
        }
        if ( andreiRightHandDTO.getDetails() != null ) {
            mappingTarget.setDetails( andreiRightHandDTO.getDetails() );
        }
        if ( andreiRightHandDTO.getLat() != null ) {
            mappingTarget.setLat( andreiRightHandDTO.getLat() );
        }
        if ( andreiRightHandDTO.getLon() != null ) {
            mappingTarget.setLon( andreiRightHandDTO.getLon() );
        }
        if ( andreiRightHandDTO.getCountry() != null ) {
            if ( mappingTarget.getCountry() == null ) {
                mappingTarget.country( new AntonioBanderas() );
            }
            antonioBanderasDTOToAntonioBanderas1( andreiRightHandDTO.getCountry(), mappingTarget.getCountry() );
        }
        if ( andreiRightHandDTO.getProvince() != null ) {
            if ( mappingTarget.getProvince() == null ) {
                mappingTarget.province( new AntonioBanderas() );
            }
            antonioBanderasDTOToAntonioBanderas1( andreiRightHandDTO.getProvince(), mappingTarget.getProvince() );
        }
        if ( andreiRightHandDTO.getDistrict() != null ) {
            if ( mappingTarget.getDistrict() == null ) {
                mappingTarget.district( new AntonioBanderas() );
            }
            antonioBanderasDTOToAntonioBanderas1( andreiRightHandDTO.getDistrict(), mappingTarget.getDistrict() );
        }
        if ( andreiRightHandDTO.getWard() != null ) {
            if ( mappingTarget.getWard() == null ) {
                mappingTarget.ward( new AntonioBanderas() );
            }
            antonioBanderasDTOToAntonioBanderas1( andreiRightHandDTO.getWard(), mappingTarget.getWard() );
        }
    }
}
