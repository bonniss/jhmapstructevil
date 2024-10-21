package ai.realworld.service.mapper;

import ai.realworld.domain.AndreiRightHand;
import ai.realworld.domain.AntonioBanderas;
import ai.realworld.domain.Initium;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.domain.OlAlmantinoMilo;
import ai.realworld.domain.OlMaster;
import ai.realworld.service.dto.AndreiRightHandDTO;
import ai.realworld.service.dto.AntonioBanderasDTO;
import ai.realworld.service.dto.InitiumDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import ai.realworld.service.dto.MetaverseDTO;
import ai.realworld.service.dto.OlAlmantinoMiloDTO;
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
public class JohnLennonMapperImpl implements JohnLennonMapper {

    @Override
    public JohnLennon toEntity(JohnLennonDTO dto) {
        if ( dto == null ) {
            return null;
        }

        JohnLennon johnLennon = new JohnLennon();

        johnLennon.setId( dto.getId() );
        johnLennon.setProvider( dto.getProvider() );
        johnLennon.setProviderAppId( dto.getProviderAppId() );
        johnLennon.setName( dto.getName() );
        johnLennon.setSlug( dto.getSlug() );
        johnLennon.setIsEnabled( dto.getIsEnabled() );
        johnLennon.logo( metaverseDTOToMetaverse( dto.getLogo() ) );
        johnLennon.appManager( olAlmantinoMiloDTOToOlAlmantinoMilo( dto.getAppManager() ) );
        johnLennon.organization( olMasterDTOToOlMaster( dto.getOrganization() ) );
        johnLennon.jelloInitium( initiumDTOToInitium( dto.getJelloInitium() ) );
        johnLennon.inhouseInitium( initiumDTOToInitium( dto.getInhouseInitium() ) );

        return johnLennon;
    }

    @Override
    public List<JohnLennon> toEntity(List<JohnLennonDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<JohnLennon> list = new ArrayList<JohnLennon>( dtoList.size() );
        for ( JohnLennonDTO johnLennonDTO : dtoList ) {
            list.add( toEntity( johnLennonDTO ) );
        }

        return list;
    }

    @Override
    public List<JohnLennonDTO> toDto(List<JohnLennon> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<JohnLennonDTO> list = new ArrayList<JohnLennonDTO>( entityList.size() );
        for ( JohnLennon johnLennon : entityList ) {
            list.add( toDto( johnLennon ) );
        }

        return list;
    }

    @Override
    public void partialUpdate(JohnLennon entity, JohnLennonDTO dto) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getProvider() != null ) {
            entity.setProvider( dto.getProvider() );
        }
        if ( dto.getProviderAppId() != null ) {
            entity.setProviderAppId( dto.getProviderAppId() );
        }
        if ( dto.getName() != null ) {
            entity.setName( dto.getName() );
        }
        if ( dto.getSlug() != null ) {
            entity.setSlug( dto.getSlug() );
        }
        if ( dto.getIsEnabled() != null ) {
            entity.setIsEnabled( dto.getIsEnabled() );
        }
        if ( dto.getLogo() != null ) {
            if ( entity.getLogo() == null ) {
                entity.logo( new Metaverse() );
            }
            metaverseDTOToMetaverse1( dto.getLogo(), entity.getLogo() );
        }
        if ( dto.getAppManager() != null ) {
            if ( entity.getAppManager() == null ) {
                entity.appManager( new OlAlmantinoMilo() );
            }
            olAlmantinoMiloDTOToOlAlmantinoMilo1( dto.getAppManager(), entity.getAppManager() );
        }
        if ( dto.getOrganization() != null ) {
            if ( entity.getOrganization() == null ) {
                entity.organization( new OlMaster() );
            }
            olMasterDTOToOlMaster1( dto.getOrganization(), entity.getOrganization() );
        }
        if ( dto.getJelloInitium() != null ) {
            if ( entity.getJelloInitium() == null ) {
                entity.jelloInitium( new Initium() );
            }
            initiumDTOToInitium1( dto.getJelloInitium(), entity.getJelloInitium() );
        }
        if ( dto.getInhouseInitium() != null ) {
            if ( entity.getInhouseInitium() == null ) {
                entity.inhouseInitium( new Initium() );
            }
            initiumDTOToInitium1( dto.getInhouseInitium(), entity.getInhouseInitium() );
        }
    }

    @Override
    public JohnLennonDTO toDto(JohnLennon s) {
        if ( s == null ) {
            return null;
        }

        JohnLennonDTO johnLennonDTO = new JohnLennonDTO();

        johnLennonDTO.setLogo( toDtoMetaverseId( s.getLogo() ) );
        johnLennonDTO.setAppManager( toDtoOlAlmantinoMiloId( s.getAppManager() ) );
        johnLennonDTO.setOrganization( toDtoOlMasterId( s.getOrganization() ) );
        johnLennonDTO.setJelloInitium( toDtoInitiumId( s.getJelloInitium() ) );
        johnLennonDTO.setInhouseInitium( toDtoInitiumId( s.getInhouseInitium() ) );
        johnLennonDTO.setId( s.getId() );
        johnLennonDTO.setProvider( s.getProvider() );
        johnLennonDTO.setProviderAppId( s.getProviderAppId() );
        johnLennonDTO.setName( s.getName() );
        johnLennonDTO.setSlug( s.getSlug() );
        johnLennonDTO.setIsEnabled( s.getIsEnabled() );

        return johnLennonDTO;
    }

    @Override
    public MetaverseDTO toDtoMetaverseId(Metaverse metaverse) {
        if ( metaverse == null ) {
            return null;
        }

        MetaverseDTO metaverseDTO = new MetaverseDTO();

        metaverseDTO.setId( metaverse.getId() );

        return metaverseDTO;
    }

    @Override
    public OlAlmantinoMiloDTO toDtoOlAlmantinoMiloId(OlAlmantinoMilo olAlmantinoMilo) {
        if ( olAlmantinoMilo == null ) {
            return null;
        }

        OlAlmantinoMiloDTO olAlmantinoMiloDTO = new OlAlmantinoMiloDTO();

        olAlmantinoMiloDTO.setId( olAlmantinoMilo.getId() );

        return olAlmantinoMiloDTO;
    }

    @Override
    public OlMasterDTO toDtoOlMasterId(OlMaster olMaster) {
        if ( olMaster == null ) {
            return null;
        }

        OlMasterDTO olMasterDTO = new OlMasterDTO();

        olMasterDTO.setId( olMaster.getId() );

        return olMasterDTO;
    }

    @Override
    public InitiumDTO toDtoInitiumId(Initium initium) {
        if ( initium == null ) {
            return null;
        }

        InitiumDTO initiumDTO = new InitiumDTO();

        initiumDTO.setId( initium.getId() );

        return initiumDTO;
    }

    protected Metaverse metaverseDTOToMetaverse(MetaverseDTO metaverseDTO) {
        if ( metaverseDTO == null ) {
            return null;
        }

        Metaverse metaverse = new Metaverse();

        metaverse.setId( metaverseDTO.getId() );
        metaverse.setFilename( metaverseDTO.getFilename() );
        metaverse.setContentType( metaverseDTO.getContentType() );
        metaverse.setFileExt( metaverseDTO.getFileExt() );
        metaverse.setFileSize( metaverseDTO.getFileSize() );
        metaverse.setFileUrl( metaverseDTO.getFileUrl() );
        metaverse.setThumbnailUrl( metaverseDTO.getThumbnailUrl() );
        metaverse.setBlurhash( metaverseDTO.getBlurhash() );
        metaverse.setObjectName( metaverseDTO.getObjectName() );
        metaverse.setObjectMetaJason( metaverseDTO.getObjectMetaJason() );
        metaverse.setUrlLifespanInSeconds( metaverseDTO.getUrlLifespanInSeconds() );
        metaverse.setUrlExpiredDate( metaverseDTO.getUrlExpiredDate() );
        metaverse.setAutoRenewUrl( metaverseDTO.getAutoRenewUrl() );
        metaverse.setIsEnabled( metaverseDTO.getIsEnabled() );

        return metaverse;
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

    protected OlMaster olMasterDTOToOlMaster(OlMasterDTO olMasterDTO) {
        if ( olMasterDTO == null ) {
            return null;
        }

        OlMaster olMaster = new OlMaster();

        olMaster.setId( olMasterDTO.getId() );
        olMaster.setName( olMasterDTO.getName() );
        olMaster.setSlug( olMasterDTO.getSlug() );
        olMaster.setDescriptionHeitiga( olMasterDTO.getDescriptionHeitiga() );
        olMaster.setBusinessType( olMasterDTO.getBusinessType() );
        olMaster.setEmail( olMasterDTO.getEmail() );
        olMaster.setHotline( olMasterDTO.getHotline() );
        olMaster.setTaxCode( olMasterDTO.getTaxCode() );
        olMaster.setContactsJason( olMasterDTO.getContactsJason() );
        olMaster.setExtensionJason( olMasterDTO.getExtensionJason() );
        olMaster.setIsEnabled( olMasterDTO.getIsEnabled() );
        olMaster.address( andreiRightHandDTOToAndreiRightHand( olMasterDTO.getAddress() ) );

        return olMaster;
    }

    protected OlAlmantinoMilo olAlmantinoMiloDTOToOlAlmantinoMilo(OlAlmantinoMiloDTO olAlmantinoMiloDTO) {
        if ( olAlmantinoMiloDTO == null ) {
            return null;
        }

        OlAlmantinoMilo olAlmantinoMilo = new OlAlmantinoMilo();

        olAlmantinoMilo.setId( olAlmantinoMiloDTO.getId() );
        olAlmantinoMilo.setProvider( olAlmantinoMiloDTO.getProvider() );
        olAlmantinoMilo.setProviderAppManagerId( olAlmantinoMiloDTO.getProviderAppManagerId() );
        olAlmantinoMilo.setName( olAlmantinoMiloDTO.getName() );
        olAlmantinoMilo.setProviderSecretKey( olAlmantinoMiloDTO.getProviderSecretKey() );
        olAlmantinoMilo.setProviderToken( olAlmantinoMiloDTO.getProviderToken() );
        olAlmantinoMilo.setProviderRefreshToken( olAlmantinoMiloDTO.getProviderRefreshToken() );
        olAlmantinoMilo.organization( olMasterDTOToOlMaster( olAlmantinoMiloDTO.getOrganization() ) );

        return olAlmantinoMilo;
    }

    protected Initium initiumDTOToInitium(InitiumDTO initiumDTO) {
        if ( initiumDTO == null ) {
            return null;
        }

        Initium initium = new Initium();

        initium.setId( initiumDTO.getId() );
        initium.setName( initiumDTO.getName() );
        initium.setSlug( initiumDTO.getSlug() );
        initium.setDescription( initiumDTO.getDescription() );
        initium.setIsJelloSupported( initiumDTO.getIsJelloSupported() );

        return initium;
    }

    protected void metaverseDTOToMetaverse1(MetaverseDTO metaverseDTO, Metaverse mappingTarget) {
        if ( metaverseDTO == null ) {
            return;
        }

        if ( metaverseDTO.getId() != null ) {
            mappingTarget.setId( metaverseDTO.getId() );
        }
        if ( metaverseDTO.getFilename() != null ) {
            mappingTarget.setFilename( metaverseDTO.getFilename() );
        }
        if ( metaverseDTO.getContentType() != null ) {
            mappingTarget.setContentType( metaverseDTO.getContentType() );
        }
        if ( metaverseDTO.getFileExt() != null ) {
            mappingTarget.setFileExt( metaverseDTO.getFileExt() );
        }
        if ( metaverseDTO.getFileSize() != null ) {
            mappingTarget.setFileSize( metaverseDTO.getFileSize() );
        }
        if ( metaverseDTO.getFileUrl() != null ) {
            mappingTarget.setFileUrl( metaverseDTO.getFileUrl() );
        }
        if ( metaverseDTO.getThumbnailUrl() != null ) {
            mappingTarget.setThumbnailUrl( metaverseDTO.getThumbnailUrl() );
        }
        if ( metaverseDTO.getBlurhash() != null ) {
            mappingTarget.setBlurhash( metaverseDTO.getBlurhash() );
        }
        if ( metaverseDTO.getObjectName() != null ) {
            mappingTarget.setObjectName( metaverseDTO.getObjectName() );
        }
        if ( metaverseDTO.getObjectMetaJason() != null ) {
            mappingTarget.setObjectMetaJason( metaverseDTO.getObjectMetaJason() );
        }
        if ( metaverseDTO.getUrlLifespanInSeconds() != null ) {
            mappingTarget.setUrlLifespanInSeconds( metaverseDTO.getUrlLifespanInSeconds() );
        }
        if ( metaverseDTO.getUrlExpiredDate() != null ) {
            mappingTarget.setUrlExpiredDate( metaverseDTO.getUrlExpiredDate() );
        }
        if ( metaverseDTO.getAutoRenewUrl() != null ) {
            mappingTarget.setAutoRenewUrl( metaverseDTO.getAutoRenewUrl() );
        }
        if ( metaverseDTO.getIsEnabled() != null ) {
            mappingTarget.setIsEnabled( metaverseDTO.getIsEnabled() );
        }
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

    protected void olMasterDTOToOlMaster1(OlMasterDTO olMasterDTO, OlMaster mappingTarget) {
        if ( olMasterDTO == null ) {
            return;
        }

        if ( olMasterDTO.getId() != null ) {
            mappingTarget.setId( olMasterDTO.getId() );
        }
        if ( olMasterDTO.getName() != null ) {
            mappingTarget.setName( olMasterDTO.getName() );
        }
        if ( olMasterDTO.getSlug() != null ) {
            mappingTarget.setSlug( olMasterDTO.getSlug() );
        }
        if ( olMasterDTO.getDescriptionHeitiga() != null ) {
            mappingTarget.setDescriptionHeitiga( olMasterDTO.getDescriptionHeitiga() );
        }
        if ( olMasterDTO.getBusinessType() != null ) {
            mappingTarget.setBusinessType( olMasterDTO.getBusinessType() );
        }
        if ( olMasterDTO.getEmail() != null ) {
            mappingTarget.setEmail( olMasterDTO.getEmail() );
        }
        if ( olMasterDTO.getHotline() != null ) {
            mappingTarget.setHotline( olMasterDTO.getHotline() );
        }
        if ( olMasterDTO.getTaxCode() != null ) {
            mappingTarget.setTaxCode( olMasterDTO.getTaxCode() );
        }
        if ( olMasterDTO.getContactsJason() != null ) {
            mappingTarget.setContactsJason( olMasterDTO.getContactsJason() );
        }
        if ( olMasterDTO.getExtensionJason() != null ) {
            mappingTarget.setExtensionJason( olMasterDTO.getExtensionJason() );
        }
        if ( olMasterDTO.getIsEnabled() != null ) {
            mappingTarget.setIsEnabled( olMasterDTO.getIsEnabled() );
        }
        if ( olMasterDTO.getAddress() != null ) {
            if ( mappingTarget.getAddress() == null ) {
                mappingTarget.address( new AndreiRightHand() );
            }
            andreiRightHandDTOToAndreiRightHand1( olMasterDTO.getAddress(), mappingTarget.getAddress() );
        }
    }

    protected void olAlmantinoMiloDTOToOlAlmantinoMilo1(OlAlmantinoMiloDTO olAlmantinoMiloDTO, OlAlmantinoMilo mappingTarget) {
        if ( olAlmantinoMiloDTO == null ) {
            return;
        }

        if ( olAlmantinoMiloDTO.getId() != null ) {
            mappingTarget.setId( olAlmantinoMiloDTO.getId() );
        }
        if ( olAlmantinoMiloDTO.getProvider() != null ) {
            mappingTarget.setProvider( olAlmantinoMiloDTO.getProvider() );
        }
        if ( olAlmantinoMiloDTO.getProviderAppManagerId() != null ) {
            mappingTarget.setProviderAppManagerId( olAlmantinoMiloDTO.getProviderAppManagerId() );
        }
        if ( olAlmantinoMiloDTO.getName() != null ) {
            mappingTarget.setName( olAlmantinoMiloDTO.getName() );
        }
        if ( olAlmantinoMiloDTO.getProviderSecretKey() != null ) {
            mappingTarget.setProviderSecretKey( olAlmantinoMiloDTO.getProviderSecretKey() );
        }
        if ( olAlmantinoMiloDTO.getProviderToken() != null ) {
            mappingTarget.setProviderToken( olAlmantinoMiloDTO.getProviderToken() );
        }
        if ( olAlmantinoMiloDTO.getProviderRefreshToken() != null ) {
            mappingTarget.setProviderRefreshToken( olAlmantinoMiloDTO.getProviderRefreshToken() );
        }
        if ( olAlmantinoMiloDTO.getOrganization() != null ) {
            if ( mappingTarget.getOrganization() == null ) {
                mappingTarget.organization( new OlMaster() );
            }
            olMasterDTOToOlMaster1( olAlmantinoMiloDTO.getOrganization(), mappingTarget.getOrganization() );
        }
    }

    protected void initiumDTOToInitium1(InitiumDTO initiumDTO, Initium mappingTarget) {
        if ( initiumDTO == null ) {
            return;
        }

        if ( initiumDTO.getId() != null ) {
            mappingTarget.setId( initiumDTO.getId() );
        }
        if ( initiumDTO.getName() != null ) {
            mappingTarget.setName( initiumDTO.getName() );
        }
        if ( initiumDTO.getSlug() != null ) {
            mappingTarget.setSlug( initiumDTO.getSlug() );
        }
        if ( initiumDTO.getDescription() != null ) {
            mappingTarget.setDescription( initiumDTO.getDescription() );
        }
        if ( initiumDTO.getIsJelloSupported() != null ) {
            mappingTarget.setIsJelloSupported( initiumDTO.getIsJelloSupported() );
        }
    }
}
