package ai.realworld.service.mapper;

import ai.realworld.domain.AlAlexType;
import ai.realworld.domain.AlApple;
import ai.realworld.domain.AlLadyGaga;
import ai.realworld.domain.AlProPro;
import ai.realworld.domain.AlProty;
import ai.realworld.domain.AlPyuDjibril;
import ai.realworld.domain.AndreiRightHand;
import ai.realworld.domain.AntonioBanderas;
import ai.realworld.domain.Initium;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.domain.OlAlmantinoMilo;
import ai.realworld.domain.OlMaster;
import ai.realworld.service.dto.AlAlexTypeDTO;
import ai.realworld.service.dto.AlAppleDTO;
import ai.realworld.service.dto.AlLadyGagaDTO;
import ai.realworld.service.dto.AlProProDTO;
import ai.realworld.service.dto.AlProtyDTO;
import ai.realworld.service.dto.AlPyuDjibrilDTO;
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
    date = "2024-10-22T00:29:26+0700",
    comments = "version: 1.6.2, compiler: javac, environment: Java 17.0.11 (Eclipse Adoptium)"
)
@Component
public class AlPyuDjibrilMapperImpl implements AlPyuDjibrilMapper {

    @Override
    public AlPyuDjibril toEntity(AlPyuDjibrilDTO dto) {
        if ( dto == null ) {
            return null;
        }

        AlPyuDjibril alPyuDjibril = new AlPyuDjibril();

        alPyuDjibril.setId( dto.getId() );
        alPyuDjibril.setRateType( dto.getRateType() );
        alPyuDjibril.setRate( dto.getRate() );
        alPyuDjibril.setIsEnabled( dto.getIsEnabled() );
        alPyuDjibril.property( alProtyDTOToAlProty( dto.getProperty() ) );
        alPyuDjibril.application( johnLennonDTOToJohnLennon( dto.getApplication() ) );

        return alPyuDjibril;
    }

    @Override
    public List<AlPyuDjibril> toEntity(List<AlPyuDjibrilDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<AlPyuDjibril> list = new ArrayList<AlPyuDjibril>( dtoList.size() );
        for ( AlPyuDjibrilDTO alPyuDjibrilDTO : dtoList ) {
            list.add( toEntity( alPyuDjibrilDTO ) );
        }

        return list;
    }

    @Override
    public List<AlPyuDjibrilDTO> toDto(List<AlPyuDjibril> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<AlPyuDjibrilDTO> list = new ArrayList<AlPyuDjibrilDTO>( entityList.size() );
        for ( AlPyuDjibril alPyuDjibril : entityList ) {
            list.add( toDto( alPyuDjibril ) );
        }

        return list;
    }

    @Override
    public void partialUpdate(AlPyuDjibril entity, AlPyuDjibrilDTO dto) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getRateType() != null ) {
            entity.setRateType( dto.getRateType() );
        }
        if ( dto.getRate() != null ) {
            entity.setRate( dto.getRate() );
        }
        if ( dto.getIsEnabled() != null ) {
            entity.setIsEnabled( dto.getIsEnabled() );
        }
        if ( dto.getProperty() != null ) {
            if ( entity.getProperty() == null ) {
                entity.property( new AlProty() );
            }
            alProtyDTOToAlProty1( dto.getProperty(), entity.getProperty() );
        }
        if ( dto.getApplication() != null ) {
            if ( entity.getApplication() == null ) {
                entity.application( new JohnLennon() );
            }
            johnLennonDTOToJohnLennon1( dto.getApplication(), entity.getApplication() );
        }
    }

    @Override
    public AlPyuDjibrilDTO toDto(AlPyuDjibril s) {
        if ( s == null ) {
            return null;
        }

        AlPyuDjibrilDTO alPyuDjibrilDTO = new AlPyuDjibrilDTO();

        alPyuDjibrilDTO.setProperty( toDtoAlProtyId( s.getProperty() ) );
        alPyuDjibrilDTO.setApplication( toDtoJohnLennonId( s.getApplication() ) );
        alPyuDjibrilDTO.setId( s.getId() );
        alPyuDjibrilDTO.setRateType( s.getRateType() );
        alPyuDjibrilDTO.setRate( s.getRate() );
        alPyuDjibrilDTO.setIsEnabled( s.getIsEnabled() );

        return alPyuDjibrilDTO;
    }

    @Override
    public AlProtyDTO toDtoAlProtyId(AlProty alProty) {
        if ( alProty == null ) {
            return null;
        }

        AlProtyDTO alProtyDTO = new AlProtyDTO();

        alProtyDTO.setId( alProty.getId() );

        return alProtyDTO;
    }

    @Override
    public JohnLennonDTO toDtoJohnLennonId(JohnLennon johnLennon) {
        if ( johnLennon == null ) {
            return null;
        }

        JohnLennonDTO johnLennonDTO = new JohnLennonDTO();

        johnLennonDTO.setId( johnLennon.getId() );

        return johnLennonDTO;
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

    protected JohnLennon johnLennonDTOToJohnLennon(JohnLennonDTO johnLennonDTO) {
        if ( johnLennonDTO == null ) {
            return null;
        }

        JohnLennon johnLennon = new JohnLennon();

        johnLennon.setId( johnLennonDTO.getId() );
        johnLennon.setProvider( johnLennonDTO.getProvider() );
        johnLennon.setProviderAppId( johnLennonDTO.getProviderAppId() );
        johnLennon.setName( johnLennonDTO.getName() );
        johnLennon.setSlug( johnLennonDTO.getSlug() );
        johnLennon.setIsEnabled( johnLennonDTO.getIsEnabled() );
        johnLennon.logo( metaverseDTOToMetaverse( johnLennonDTO.getLogo() ) );
        johnLennon.appManager( olAlmantinoMiloDTOToOlAlmantinoMilo( johnLennonDTO.getAppManager() ) );
        johnLennon.organization( olMasterDTOToOlMaster( johnLennonDTO.getOrganization() ) );
        johnLennon.jelloInitium( initiumDTOToInitium( johnLennonDTO.getJelloInitium() ) );
        johnLennon.inhouseInitium( initiumDTOToInitium( johnLennonDTO.getInhouseInitium() ) );

        return johnLennon;
    }

    protected AlAlexType alAlexTypeDTOToAlAlexType(AlAlexTypeDTO alAlexTypeDTO) {
        if ( alAlexTypeDTO == null ) {
            return null;
        }

        AlAlexType alAlexType = new AlAlexType();

        alAlexType.setId( alAlexTypeDTO.getId() );
        alAlexType.setName( alAlexTypeDTO.getName() );
        alAlexType.setDescription( alAlexTypeDTO.getDescription() );
        alAlexType.setCanDoRetail( alAlexTypeDTO.getCanDoRetail() );
        alAlexType.setIsOrgDivision( alAlexTypeDTO.getIsOrgDivision() );
        alAlexType.setConfigJason( alAlexTypeDTO.getConfigJason() );
        alAlexType.setTreeDepth( alAlexTypeDTO.getTreeDepth() );
        alAlexType.application( johnLennonDTOToJohnLennon( alAlexTypeDTO.getApplication() ) );

        return alAlexType;
    }

    protected AlApple alAppleDTOToAlApple(AlAppleDTO alAppleDTO) {
        if ( alAppleDTO == null ) {
            return null;
        }

        AlApple alApple = new AlApple();

        alApple.setId( alAppleDTO.getId() );
        alApple.setName( alAppleDTO.getName() );
        alApple.setDescription( alAppleDTO.getDescription() );
        alApple.setHotline( alAppleDTO.getHotline() );
        alApple.setTaxCode( alAppleDTO.getTaxCode() );
        alApple.setContactsJason( alAppleDTO.getContactsJason() );
        alApple.setExtensionJason( alAppleDTO.getExtensionJason() );
        alApple.setIsEnabled( alAppleDTO.getIsEnabled() );
        alApple.address( andreiRightHandDTOToAndreiRightHand( alAppleDTO.getAddress() ) );
        alApple.agencyType( alAlexTypeDTOToAlAlexType( alAppleDTO.getAgencyType() ) );
        alApple.logo( metaverseDTOToMetaverse( alAppleDTO.getLogo() ) );
        alApple.application( johnLennonDTOToJohnLennon( alAppleDTO.getApplication() ) );

        return alApple;
    }

    protected AlLadyGaga alLadyGagaDTOToAlLadyGaga(AlLadyGagaDTO alLadyGagaDTO) {
        if ( alLadyGagaDTO == null ) {
            return null;
        }

        AlLadyGaga alLadyGaga = new AlLadyGaga();

        alLadyGaga.setId( alLadyGagaDTO.getId() );
        alLadyGaga.setName( alLadyGagaDTO.getName() );
        alLadyGaga.setDescriptionHeitiga( alLadyGagaDTO.getDescriptionHeitiga() );
        alLadyGaga.address( andreiRightHandDTOToAndreiRightHand( alLadyGagaDTO.getAddress() ) );
        alLadyGaga.avatar( metaverseDTOToMetaverse( alLadyGagaDTO.getAvatar() ) );
        alLadyGaga.application( johnLennonDTOToJohnLennon( alLadyGagaDTO.getApplication() ) );

        return alLadyGaga;
    }

    protected AlProPro alProProDTOToAlProPro(AlProProDTO alProProDTO) {
        if ( alProProDTO == null ) {
            return null;
        }

        AlProPro alProPro = new AlProPro();

        alProPro.setId( alProProDTO.getId() );
        alProPro.setName( alProProDTO.getName() );
        alProPro.setDescriptionHeitiga( alProProDTO.getDescriptionHeitiga() );
        alProPro.setPropertyType( alProProDTO.getPropertyType() );
        alProPro.setAreaInSquareMeter( alProProDTO.getAreaInSquareMeter() );
        alProPro.setNumberOfAdults( alProProDTO.getNumberOfAdults() );
        alProPro.setNumberOfPreschoolers( alProProDTO.getNumberOfPreschoolers() );
        alProPro.setNumberOfChildren( alProProDTO.getNumberOfChildren() );
        alProPro.setNumberOfRooms( alProProDTO.getNumberOfRooms() );
        alProPro.setNumberOfFloors( alProProDTO.getNumberOfFloors() );
        alProPro.setBedSize( alProProDTO.getBedSize() );
        alProPro.setIsEnabled( alProProDTO.getIsEnabled() );
        alProPro.parent( alProProDTOToAlProPro( alProProDTO.getParent() ) );
        alProPro.project( alLadyGagaDTOToAlLadyGaga( alProProDTO.getProject() ) );
        alProPro.avatar( metaverseDTOToMetaverse( alProProDTO.getAvatar() ) );
        alProPro.application( johnLennonDTOToJohnLennon( alProProDTO.getApplication() ) );

        return alProPro;
    }

    protected AlProty alProtyDTOToAlProty(AlProtyDTO alProtyDTO) {
        if ( alProtyDTO == null ) {
            return null;
        }

        AlProty alProty = new AlProty();

        alProty.setId( alProtyDTO.getId() );
        alProty.setName( alProtyDTO.getName() );
        alProty.setDescriptionHeitiga( alProtyDTO.getDescriptionHeitiga() );
        alProty.setCoordinate( alProtyDTO.getCoordinate() );
        alProty.setCode( alProtyDTO.getCode() );
        alProty.setStatus( alProtyDTO.getStatus() );
        alProty.setIsEnabled( alProtyDTO.getIsEnabled() );
        alProty.parent( alProtyDTOToAlProty( alProtyDTO.getParent() ) );
        alProty.operator( alAppleDTOToAlApple( alProtyDTO.getOperator() ) );
        alProty.propertyProfile( alProProDTOToAlProPro( alProtyDTO.getPropertyProfile() ) );
        alProty.avatar( metaverseDTOToMetaverse( alProtyDTO.getAvatar() ) );
        alProty.application( johnLennonDTOToJohnLennon( alProtyDTO.getApplication() ) );

        return alProty;
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

    protected void johnLennonDTOToJohnLennon1(JohnLennonDTO johnLennonDTO, JohnLennon mappingTarget) {
        if ( johnLennonDTO == null ) {
            return;
        }

        if ( johnLennonDTO.getId() != null ) {
            mappingTarget.setId( johnLennonDTO.getId() );
        }
        if ( johnLennonDTO.getProvider() != null ) {
            mappingTarget.setProvider( johnLennonDTO.getProvider() );
        }
        if ( johnLennonDTO.getProviderAppId() != null ) {
            mappingTarget.setProviderAppId( johnLennonDTO.getProviderAppId() );
        }
        if ( johnLennonDTO.getName() != null ) {
            mappingTarget.setName( johnLennonDTO.getName() );
        }
        if ( johnLennonDTO.getSlug() != null ) {
            mappingTarget.setSlug( johnLennonDTO.getSlug() );
        }
        if ( johnLennonDTO.getIsEnabled() != null ) {
            mappingTarget.setIsEnabled( johnLennonDTO.getIsEnabled() );
        }
        if ( johnLennonDTO.getLogo() != null ) {
            if ( mappingTarget.getLogo() == null ) {
                mappingTarget.logo( new Metaverse() );
            }
            metaverseDTOToMetaverse1( johnLennonDTO.getLogo(), mappingTarget.getLogo() );
        }
        if ( johnLennonDTO.getAppManager() != null ) {
            if ( mappingTarget.getAppManager() == null ) {
                mappingTarget.appManager( new OlAlmantinoMilo() );
            }
            olAlmantinoMiloDTOToOlAlmantinoMilo1( johnLennonDTO.getAppManager(), mappingTarget.getAppManager() );
        }
        if ( johnLennonDTO.getOrganization() != null ) {
            if ( mappingTarget.getOrganization() == null ) {
                mappingTarget.organization( new OlMaster() );
            }
            olMasterDTOToOlMaster1( johnLennonDTO.getOrganization(), mappingTarget.getOrganization() );
        }
        if ( johnLennonDTO.getJelloInitium() != null ) {
            if ( mappingTarget.getJelloInitium() == null ) {
                mappingTarget.jelloInitium( new Initium() );
            }
            initiumDTOToInitium1( johnLennonDTO.getJelloInitium(), mappingTarget.getJelloInitium() );
        }
        if ( johnLennonDTO.getInhouseInitium() != null ) {
            if ( mappingTarget.getInhouseInitium() == null ) {
                mappingTarget.inhouseInitium( new Initium() );
            }
            initiumDTOToInitium1( johnLennonDTO.getInhouseInitium(), mappingTarget.getInhouseInitium() );
        }
    }

    protected void alAlexTypeDTOToAlAlexType1(AlAlexTypeDTO alAlexTypeDTO, AlAlexType mappingTarget) {
        if ( alAlexTypeDTO == null ) {
            return;
        }

        if ( alAlexTypeDTO.getId() != null ) {
            mappingTarget.setId( alAlexTypeDTO.getId() );
        }
        if ( alAlexTypeDTO.getName() != null ) {
            mappingTarget.setName( alAlexTypeDTO.getName() );
        }
        if ( alAlexTypeDTO.getDescription() != null ) {
            mappingTarget.setDescription( alAlexTypeDTO.getDescription() );
        }
        if ( alAlexTypeDTO.getCanDoRetail() != null ) {
            mappingTarget.setCanDoRetail( alAlexTypeDTO.getCanDoRetail() );
        }
        if ( alAlexTypeDTO.getIsOrgDivision() != null ) {
            mappingTarget.setIsOrgDivision( alAlexTypeDTO.getIsOrgDivision() );
        }
        if ( alAlexTypeDTO.getConfigJason() != null ) {
            mappingTarget.setConfigJason( alAlexTypeDTO.getConfigJason() );
        }
        if ( alAlexTypeDTO.getTreeDepth() != null ) {
            mappingTarget.setTreeDepth( alAlexTypeDTO.getTreeDepth() );
        }
        if ( alAlexTypeDTO.getApplication() != null ) {
            if ( mappingTarget.getApplication() == null ) {
                mappingTarget.application( new JohnLennon() );
            }
            johnLennonDTOToJohnLennon1( alAlexTypeDTO.getApplication(), mappingTarget.getApplication() );
        }
    }

    protected void alAppleDTOToAlApple1(AlAppleDTO alAppleDTO, AlApple mappingTarget) {
        if ( alAppleDTO == null ) {
            return;
        }

        if ( alAppleDTO.getId() != null ) {
            mappingTarget.setId( alAppleDTO.getId() );
        }
        if ( alAppleDTO.getName() != null ) {
            mappingTarget.setName( alAppleDTO.getName() );
        }
        if ( alAppleDTO.getDescription() != null ) {
            mappingTarget.setDescription( alAppleDTO.getDescription() );
        }
        if ( alAppleDTO.getHotline() != null ) {
            mappingTarget.setHotline( alAppleDTO.getHotline() );
        }
        if ( alAppleDTO.getTaxCode() != null ) {
            mappingTarget.setTaxCode( alAppleDTO.getTaxCode() );
        }
        if ( alAppleDTO.getContactsJason() != null ) {
            mappingTarget.setContactsJason( alAppleDTO.getContactsJason() );
        }
        if ( alAppleDTO.getExtensionJason() != null ) {
            mappingTarget.setExtensionJason( alAppleDTO.getExtensionJason() );
        }
        if ( alAppleDTO.getIsEnabled() != null ) {
            mappingTarget.setIsEnabled( alAppleDTO.getIsEnabled() );
        }
        if ( alAppleDTO.getAddress() != null ) {
            if ( mappingTarget.getAddress() == null ) {
                mappingTarget.address( new AndreiRightHand() );
            }
            andreiRightHandDTOToAndreiRightHand1( alAppleDTO.getAddress(), mappingTarget.getAddress() );
        }
        if ( alAppleDTO.getAgencyType() != null ) {
            if ( mappingTarget.getAgencyType() == null ) {
                mappingTarget.agencyType( new AlAlexType() );
            }
            alAlexTypeDTOToAlAlexType1( alAppleDTO.getAgencyType(), mappingTarget.getAgencyType() );
        }
        if ( alAppleDTO.getLogo() != null ) {
            if ( mappingTarget.getLogo() == null ) {
                mappingTarget.logo( new Metaverse() );
            }
            metaverseDTOToMetaverse1( alAppleDTO.getLogo(), mappingTarget.getLogo() );
        }
        if ( alAppleDTO.getApplication() != null ) {
            if ( mappingTarget.getApplication() == null ) {
                mappingTarget.application( new JohnLennon() );
            }
            johnLennonDTOToJohnLennon1( alAppleDTO.getApplication(), mappingTarget.getApplication() );
        }
    }

    protected void alLadyGagaDTOToAlLadyGaga1(AlLadyGagaDTO alLadyGagaDTO, AlLadyGaga mappingTarget) {
        if ( alLadyGagaDTO == null ) {
            return;
        }

        if ( alLadyGagaDTO.getId() != null ) {
            mappingTarget.setId( alLadyGagaDTO.getId() );
        }
        if ( alLadyGagaDTO.getName() != null ) {
            mappingTarget.setName( alLadyGagaDTO.getName() );
        }
        if ( alLadyGagaDTO.getDescriptionHeitiga() != null ) {
            mappingTarget.setDescriptionHeitiga( alLadyGagaDTO.getDescriptionHeitiga() );
        }
        if ( alLadyGagaDTO.getAddress() != null ) {
            if ( mappingTarget.getAddress() == null ) {
                mappingTarget.address( new AndreiRightHand() );
            }
            andreiRightHandDTOToAndreiRightHand1( alLadyGagaDTO.getAddress(), mappingTarget.getAddress() );
        }
        if ( alLadyGagaDTO.getAvatar() != null ) {
            if ( mappingTarget.getAvatar() == null ) {
                mappingTarget.avatar( new Metaverse() );
            }
            metaverseDTOToMetaverse1( alLadyGagaDTO.getAvatar(), mappingTarget.getAvatar() );
        }
        if ( alLadyGagaDTO.getApplication() != null ) {
            if ( mappingTarget.getApplication() == null ) {
                mappingTarget.application( new JohnLennon() );
            }
            johnLennonDTOToJohnLennon1( alLadyGagaDTO.getApplication(), mappingTarget.getApplication() );
        }
    }

    protected void alProProDTOToAlProPro1(AlProProDTO alProProDTO, AlProPro mappingTarget) {
        if ( alProProDTO == null ) {
            return;
        }

        if ( alProProDTO.getId() != null ) {
            mappingTarget.setId( alProProDTO.getId() );
        }
        if ( alProProDTO.getName() != null ) {
            mappingTarget.setName( alProProDTO.getName() );
        }
        if ( alProProDTO.getDescriptionHeitiga() != null ) {
            mappingTarget.setDescriptionHeitiga( alProProDTO.getDescriptionHeitiga() );
        }
        if ( alProProDTO.getPropertyType() != null ) {
            mappingTarget.setPropertyType( alProProDTO.getPropertyType() );
        }
        if ( alProProDTO.getAreaInSquareMeter() != null ) {
            mappingTarget.setAreaInSquareMeter( alProProDTO.getAreaInSquareMeter() );
        }
        if ( alProProDTO.getNumberOfAdults() != null ) {
            mappingTarget.setNumberOfAdults( alProProDTO.getNumberOfAdults() );
        }
        if ( alProProDTO.getNumberOfPreschoolers() != null ) {
            mappingTarget.setNumberOfPreschoolers( alProProDTO.getNumberOfPreschoolers() );
        }
        if ( alProProDTO.getNumberOfChildren() != null ) {
            mappingTarget.setNumberOfChildren( alProProDTO.getNumberOfChildren() );
        }
        if ( alProProDTO.getNumberOfRooms() != null ) {
            mappingTarget.setNumberOfRooms( alProProDTO.getNumberOfRooms() );
        }
        if ( alProProDTO.getNumberOfFloors() != null ) {
            mappingTarget.setNumberOfFloors( alProProDTO.getNumberOfFloors() );
        }
        if ( alProProDTO.getBedSize() != null ) {
            mappingTarget.setBedSize( alProProDTO.getBedSize() );
        }
        if ( alProProDTO.getIsEnabled() != null ) {
            mappingTarget.setIsEnabled( alProProDTO.getIsEnabled() );
        }
        if ( alProProDTO.getParent() != null ) {
            if ( mappingTarget.getParent() == null ) {
                mappingTarget.parent( new AlProPro() );
            }
            alProProDTOToAlProPro1( alProProDTO.getParent(), mappingTarget.getParent() );
        }
        if ( alProProDTO.getProject() != null ) {
            if ( mappingTarget.getProject() == null ) {
                mappingTarget.project( new AlLadyGaga() );
            }
            alLadyGagaDTOToAlLadyGaga1( alProProDTO.getProject(), mappingTarget.getProject() );
        }
        if ( alProProDTO.getAvatar() != null ) {
            if ( mappingTarget.getAvatar() == null ) {
                mappingTarget.avatar( new Metaverse() );
            }
            metaverseDTOToMetaverse1( alProProDTO.getAvatar(), mappingTarget.getAvatar() );
        }
        if ( alProProDTO.getApplication() != null ) {
            if ( mappingTarget.getApplication() == null ) {
                mappingTarget.application( new JohnLennon() );
            }
            johnLennonDTOToJohnLennon1( alProProDTO.getApplication(), mappingTarget.getApplication() );
        }
    }

    protected void alProtyDTOToAlProty1(AlProtyDTO alProtyDTO, AlProty mappingTarget) {
        if ( alProtyDTO == null ) {
            return;
        }

        if ( alProtyDTO.getId() != null ) {
            mappingTarget.setId( alProtyDTO.getId() );
        }
        if ( alProtyDTO.getName() != null ) {
            mappingTarget.setName( alProtyDTO.getName() );
        }
        if ( alProtyDTO.getDescriptionHeitiga() != null ) {
            mappingTarget.setDescriptionHeitiga( alProtyDTO.getDescriptionHeitiga() );
        }
        if ( alProtyDTO.getCoordinate() != null ) {
            mappingTarget.setCoordinate( alProtyDTO.getCoordinate() );
        }
        if ( alProtyDTO.getCode() != null ) {
            mappingTarget.setCode( alProtyDTO.getCode() );
        }
        if ( alProtyDTO.getStatus() != null ) {
            mappingTarget.setStatus( alProtyDTO.getStatus() );
        }
        if ( alProtyDTO.getIsEnabled() != null ) {
            mappingTarget.setIsEnabled( alProtyDTO.getIsEnabled() );
        }
        if ( alProtyDTO.getParent() != null ) {
            if ( mappingTarget.getParent() == null ) {
                mappingTarget.parent( new AlProty() );
            }
            alProtyDTOToAlProty1( alProtyDTO.getParent(), mappingTarget.getParent() );
        }
        if ( alProtyDTO.getOperator() != null ) {
            if ( mappingTarget.getOperator() == null ) {
                mappingTarget.operator( new AlApple() );
            }
            alAppleDTOToAlApple1( alProtyDTO.getOperator(), mappingTarget.getOperator() );
        }
        if ( alProtyDTO.getPropertyProfile() != null ) {
            if ( mappingTarget.getPropertyProfile() == null ) {
                mappingTarget.propertyProfile( new AlProPro() );
            }
            alProProDTOToAlProPro1( alProtyDTO.getPropertyProfile(), mappingTarget.getPropertyProfile() );
        }
        if ( alProtyDTO.getAvatar() != null ) {
            if ( mappingTarget.getAvatar() == null ) {
                mappingTarget.avatar( new Metaverse() );
            }
            metaverseDTOToMetaverse1( alProtyDTO.getAvatar(), mappingTarget.getAvatar() );
        }
        if ( alProtyDTO.getApplication() != null ) {
            if ( mappingTarget.getApplication() == null ) {
                mappingTarget.application( new JohnLennon() );
            }
            johnLennonDTOToJohnLennon1( alProtyDTO.getApplication(), mappingTarget.getApplication() );
        }
    }
}
