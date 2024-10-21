package ai.realworld.service.mapper;

import ai.realworld.domain.AlAlexType;
import ai.realworld.domain.AlBetonamuRelation;
import ai.realworld.domain.AndreiRightHand;
import ai.realworld.domain.AntonioBanderas;
import ai.realworld.domain.Initium;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.domain.OlAlmantinoMilo;
import ai.realworld.domain.OlMaster;
import ai.realworld.service.dto.AlAlexTypeDTO;
import ai.realworld.service.dto.AlBetonamuRelationDTO;
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
    date = "2024-10-22T00:29:23+0700",
    comments = "version: 1.6.2, compiler: javac, environment: Java 17.0.11 (Eclipse Adoptium)"
)
@Component
public class AlBetonamuRelationMapperImpl implements AlBetonamuRelationMapper {

    @Override
    public AlBetonamuRelation toEntity(AlBetonamuRelationDTO dto) {
        if ( dto == null ) {
            return null;
        }

        AlBetonamuRelation alBetonamuRelation = new AlBetonamuRelation();

        alBetonamuRelation.setId( dto.getId() );
        alBetonamuRelation.setType( dto.getType() );
        alBetonamuRelation.supplier( alAlexTypeDTOToAlAlexType( dto.getSupplier() ) );
        alBetonamuRelation.customer( alAlexTypeDTOToAlAlexType( dto.getCustomer() ) );
        alBetonamuRelation.application( johnLennonDTOToJohnLennon( dto.getApplication() ) );

        return alBetonamuRelation;
    }

    @Override
    public List<AlBetonamuRelation> toEntity(List<AlBetonamuRelationDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<AlBetonamuRelation> list = new ArrayList<AlBetonamuRelation>( dtoList.size() );
        for ( AlBetonamuRelationDTO alBetonamuRelationDTO : dtoList ) {
            list.add( toEntity( alBetonamuRelationDTO ) );
        }

        return list;
    }

    @Override
    public List<AlBetonamuRelationDTO> toDto(List<AlBetonamuRelation> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<AlBetonamuRelationDTO> list = new ArrayList<AlBetonamuRelationDTO>( entityList.size() );
        for ( AlBetonamuRelation alBetonamuRelation : entityList ) {
            list.add( toDto( alBetonamuRelation ) );
        }

        return list;
    }

    @Override
    public void partialUpdate(AlBetonamuRelation entity, AlBetonamuRelationDTO dto) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getType() != null ) {
            entity.setType( dto.getType() );
        }
        if ( dto.getSupplier() != null ) {
            if ( entity.getSupplier() == null ) {
                entity.supplier( new AlAlexType() );
            }
            alAlexTypeDTOToAlAlexType1( dto.getSupplier(), entity.getSupplier() );
        }
        if ( dto.getCustomer() != null ) {
            if ( entity.getCustomer() == null ) {
                entity.customer( new AlAlexType() );
            }
            alAlexTypeDTOToAlAlexType1( dto.getCustomer(), entity.getCustomer() );
        }
        if ( dto.getApplication() != null ) {
            if ( entity.getApplication() == null ) {
                entity.application( new JohnLennon() );
            }
            johnLennonDTOToJohnLennon1( dto.getApplication(), entity.getApplication() );
        }
    }

    @Override
    public AlBetonamuRelationDTO toDto(AlBetonamuRelation s) {
        if ( s == null ) {
            return null;
        }

        AlBetonamuRelationDTO alBetonamuRelationDTO = new AlBetonamuRelationDTO();

        alBetonamuRelationDTO.setSupplier( toDtoAlAlexTypeId( s.getSupplier() ) );
        alBetonamuRelationDTO.setCustomer( toDtoAlAlexTypeId( s.getCustomer() ) );
        alBetonamuRelationDTO.setApplication( toDtoJohnLennonId( s.getApplication() ) );
        alBetonamuRelationDTO.setId( s.getId() );
        alBetonamuRelationDTO.setType( s.getType() );

        return alBetonamuRelationDTO;
    }

    @Override
    public AlAlexTypeDTO toDtoAlAlexTypeId(AlAlexType alAlexType) {
        if ( alAlexType == null ) {
            return null;
        }

        AlAlexTypeDTO alAlexTypeDTO = new AlAlexTypeDTO();

        alAlexTypeDTO.setId( alAlexType.getId() );

        return alAlexTypeDTO;
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
}
