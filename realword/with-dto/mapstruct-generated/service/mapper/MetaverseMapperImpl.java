package ai.realworld.service.mapper;

import ai.realworld.domain.AlAlexType;
import ai.realworld.domain.AlApple;
import ai.realworld.domain.AlLadyGaga;
import ai.realworld.domain.AlMemTier;
import ai.realworld.domain.AlMenity;
import ai.realworld.domain.AlPacino;
import ai.realworld.domain.AlProPro;
import ai.realworld.domain.AlProty;
import ai.realworld.domain.AlPyuJoker;
import ai.realworld.domain.AlVueVueUsage;
import ai.realworld.domain.AndreiRightHand;
import ai.realworld.domain.AntonioBanderas;
import ai.realworld.domain.EdSheeran;
import ai.realworld.domain.Initium;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.domain.OlAlmantinoMilo;
import ai.realworld.domain.OlMaster;
import ai.realworld.domain.User;
import ai.realworld.service.dto.AlAlexTypeDTO;
import ai.realworld.service.dto.AlAppleDTO;
import ai.realworld.service.dto.AlLadyGagaDTO;
import ai.realworld.service.dto.AlMemTierDTO;
import ai.realworld.service.dto.AlMenityDTO;
import ai.realworld.service.dto.AlPacinoDTO;
import ai.realworld.service.dto.AlProProDTO;
import ai.realworld.service.dto.AlProtyDTO;
import ai.realworld.service.dto.AlPyuJokerDTO;
import ai.realworld.service.dto.AlVueVueUsageDTO;
import ai.realworld.service.dto.AndreiRightHandDTO;
import ai.realworld.service.dto.AntonioBanderasDTO;
import ai.realworld.service.dto.EdSheeranDTO;
import ai.realworld.service.dto.InitiumDTO;
import ai.realworld.service.dto.JohnLennonDTO;
import ai.realworld.service.dto.MetaverseDTO;
import ai.realworld.service.dto.OlAlmantinoMiloDTO;
import ai.realworld.service.dto.OlMasterDTO;
import ai.realworld.service.dto.UserDTO;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-22T00:20:11+0700",
    comments = "version: 1.6.2, compiler: javac, environment: Java 17.0.11 (Eclipse Adoptium)"
)
@Component
public class MetaverseMapperImpl implements MetaverseMapper {

    @Override
    public List<Metaverse> toEntity(List<MetaverseDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Metaverse> list = new ArrayList<Metaverse>( dtoList.size() );
        for ( MetaverseDTO metaverseDTO : dtoList ) {
            list.add( toEntity( metaverseDTO ) );
        }

        return list;
    }

    @Override
    public List<MetaverseDTO> toDto(List<Metaverse> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<MetaverseDTO> list = new ArrayList<MetaverseDTO>( entityList.size() );
        for ( Metaverse metaverse : entityList ) {
            list.add( toDto( metaverse ) );
        }

        return list;
    }

    @Override
    public void partialUpdate(Metaverse entity, MetaverseDTO dto) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getFilename() != null ) {
            entity.setFilename( dto.getFilename() );
        }
        if ( dto.getContentType() != null ) {
            entity.setContentType( dto.getContentType() );
        }
        if ( dto.getFileExt() != null ) {
            entity.setFileExt( dto.getFileExt() );
        }
        if ( dto.getFileSize() != null ) {
            entity.setFileSize( dto.getFileSize() );
        }
        if ( dto.getFileUrl() != null ) {
            entity.setFileUrl( dto.getFileUrl() );
        }
        if ( dto.getThumbnailUrl() != null ) {
            entity.setThumbnailUrl( dto.getThumbnailUrl() );
        }
        if ( dto.getBlurhash() != null ) {
            entity.setBlurhash( dto.getBlurhash() );
        }
        if ( dto.getObjectName() != null ) {
            entity.setObjectName( dto.getObjectName() );
        }
        if ( dto.getObjectMetaJason() != null ) {
            entity.setObjectMetaJason( dto.getObjectMetaJason() );
        }
        if ( dto.getUrlLifespanInSeconds() != null ) {
            entity.setUrlLifespanInSeconds( dto.getUrlLifespanInSeconds() );
        }
        if ( dto.getUrlExpiredDate() != null ) {
            entity.setUrlExpiredDate( dto.getUrlExpiredDate() );
        }
        if ( dto.getAutoRenewUrl() != null ) {
            entity.setAutoRenewUrl( dto.getAutoRenewUrl() );
        }
        if ( dto.getIsEnabled() != null ) {
            entity.setIsEnabled( dto.getIsEnabled() );
        }
        if ( entity.getAlProPros() != null ) {
            Set<AlProPro> set = alProProDTOSetToAlProProSet( dto.getAlProPros() );
            if ( set != null ) {
                entity.getAlProPros().clear();
                entity.getAlProPros().addAll( set );
            }
        }
        else {
            Set<AlProPro> set = alProProDTOSetToAlProProSet( dto.getAlProPros() );
            if ( set != null ) {
                entity.alProPros( set );
            }
        }
        if ( entity.getAlProties() != null ) {
            Set<AlProty> set1 = alProtyDTOSetToAlProtySet( dto.getAlProties() );
            if ( set1 != null ) {
                entity.getAlProties().clear();
                entity.getAlProties().addAll( set1 );
            }
        }
        else {
            Set<AlProty> set1 = alProtyDTOSetToAlProtySet( dto.getAlProties() );
            if ( set1 != null ) {
                entity.alProties( set1 );
            }
        }
    }

    @Override
    public MetaverseDTO toDto(Metaverse s) {
        if ( s == null ) {
            return null;
        }

        MetaverseDTO metaverseDTO = new MetaverseDTO();

        metaverseDTO.setAlProPros( toDtoAlProProIdSet( s.getAlProPros() ) );
        metaverseDTO.setAlProties( toDtoAlProtyIdSet( s.getAlProties() ) );
        metaverseDTO.setId( s.getId() );
        metaverseDTO.setFilename( s.getFilename() );
        metaverseDTO.setContentType( s.getContentType() );
        metaverseDTO.setFileExt( s.getFileExt() );
        metaverseDTO.setFileSize( s.getFileSize() );
        metaverseDTO.setFileUrl( s.getFileUrl() );
        metaverseDTO.setThumbnailUrl( s.getThumbnailUrl() );
        metaverseDTO.setBlurhash( s.getBlurhash() );
        metaverseDTO.setObjectName( s.getObjectName() );
        metaverseDTO.setObjectMetaJason( s.getObjectMetaJason() );
        metaverseDTO.setUrlLifespanInSeconds( s.getUrlLifespanInSeconds() );
        metaverseDTO.setUrlExpiredDate( s.getUrlExpiredDate() );
        metaverseDTO.setAutoRenewUrl( s.getAutoRenewUrl() );
        metaverseDTO.setIsEnabled( s.getIsEnabled() );

        return metaverseDTO;
    }

    @Override
    public Metaverse toEntity(MetaverseDTO metaverseDTO) {
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

    @Override
    public AlProProDTO toDtoAlProProId(AlProPro alProPro) {
        if ( alProPro == null ) {
            return null;
        }

        AlProProDTO alProProDTO = new AlProProDTO();

        alProProDTO.setId( alProPro.getId() );

        return alProProDTO;
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
        johnLennon.logo( toEntity( johnLennonDTO.getLogo() ) );
        johnLennon.appManager( olAlmantinoMiloDTOToOlAlmantinoMilo( johnLennonDTO.getAppManager() ) );
        johnLennon.organization( olMasterDTOToOlMaster( johnLennonDTO.getOrganization() ) );
        johnLennon.jelloInitium( initiumDTOToInitium( johnLennonDTO.getJelloInitium() ) );
        johnLennon.inhouseInitium( initiumDTOToInitium( johnLennonDTO.getInhouseInitium() ) );

        return johnLennon;
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
        alLadyGaga.avatar( toEntity( alLadyGagaDTO.getAvatar() ) );
        alLadyGaga.application( johnLennonDTOToJohnLennon( alLadyGagaDTO.getApplication() ) );

        return alLadyGaga;
    }

    protected AlMenity alMenityDTOToAlMenity(AlMenityDTO alMenityDTO) {
        if ( alMenityDTO == null ) {
            return null;
        }

        AlMenity alMenity = new AlMenity();

        alMenity.setId( alMenityDTO.getId() );
        alMenity.setName( alMenityDTO.getName() );
        alMenity.setIconSvg( alMenityDTO.getIconSvg() );
        alMenity.setPropertyType( alMenityDTO.getPropertyType() );
        alMenity.application( johnLennonDTOToJohnLennon( alMenityDTO.getApplication() ) );
        alMenity.propertyProfiles( alProProDTOSetToAlProProSet( alMenityDTO.getPropertyProfiles() ) );

        return alMenity;
    }

    protected Set<AlMenity> alMenityDTOSetToAlMenitySet(Set<AlMenityDTO> set) {
        if ( set == null ) {
            return null;
        }

        Set<AlMenity> set1 = new LinkedHashSet<AlMenity>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( AlMenityDTO alMenityDTO : set ) {
            set1.add( alMenityDTOToAlMenity( alMenityDTO ) );
        }

        return set1;
    }

    protected Set<Metaverse> metaverseDTOSetToMetaverseSet(Set<MetaverseDTO> set) {
        if ( set == null ) {
            return null;
        }

        Set<Metaverse> set1 = new LinkedHashSet<Metaverse>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( MetaverseDTO metaverseDTO : set ) {
            set1.add( toEntity( metaverseDTO ) );
        }

        return set1;
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
        alProPro.avatar( toEntity( alProProDTO.getAvatar() ) );
        alProPro.application( johnLennonDTOToJohnLennon( alProProDTO.getApplication() ) );
        alProPro.amenities( alMenityDTOSetToAlMenitySet( alProProDTO.getAmenities() ) );
        alProPro.images( metaverseDTOSetToMetaverseSet( alProProDTO.getImages() ) );

        return alProPro;
    }

    protected Set<AlProPro> alProProDTOSetToAlProProSet(Set<AlProProDTO> set) {
        if ( set == null ) {
            return null;
        }

        Set<AlProPro> set1 = new LinkedHashSet<AlProPro>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( AlProProDTO alProProDTO : set ) {
            set1.add( alProProDTOToAlProPro( alProProDTO ) );
        }

        return set1;
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
        alApple.logo( toEntity( alAppleDTO.getLogo() ) );
        alApple.application( johnLennonDTOToJohnLennon( alAppleDTO.getApplication() ) );

        return alApple;
    }

    protected AlMemTier alMemTierDTOToAlMemTier(AlMemTierDTO alMemTierDTO) {
        if ( alMemTierDTO == null ) {
            return null;
        }

        AlMemTier alMemTier = new AlMemTier();

        alMemTier.setId( alMemTierDTO.getId() );
        alMemTier.setName( alMemTierDTO.getName() );
        alMemTier.setDescription( alMemTierDTO.getDescription() );
        alMemTier.setMinPoint( alMemTierDTO.getMinPoint() );
        alMemTier.application( johnLennonDTOToJohnLennon( alMemTierDTO.getApplication() ) );

        return alMemTier;
    }

    protected AlVueVueUsage alVueVueUsageDTOToAlVueVueUsage(AlVueVueUsageDTO alVueVueUsageDTO) {
        if ( alVueVueUsageDTO == null ) {
            return null;
        }

        AlVueVueUsage alVueVueUsage = new AlVueVueUsage();

        alVueVueUsage.setId( alVueVueUsageDTO.getId() );
        alVueVueUsage.application( johnLennonDTOToJohnLennon( alVueVueUsageDTO.getApplication() ) );

        return alVueVueUsage;
    }

    protected AlPacino alPacinoDTOToAlPacino(AlPacinoDTO alPacinoDTO) {
        if ( alPacinoDTO == null ) {
            return null;
        }

        AlPacino alPacino = new AlPacino();

        alPacino.setId( alPacinoDTO.getId() );
        alPacino.setPlatformCode( alPacinoDTO.getPlatformCode() );
        alPacino.setPlatformUsername( alPacinoDTO.getPlatformUsername() );
        alPacino.setPlatformAvatarUrl( alPacinoDTO.getPlatformAvatarUrl() );
        alPacino.setIsSensitive( alPacinoDTO.getIsSensitive() );
        alPacino.setFamilyName( alPacinoDTO.getFamilyName() );
        alPacino.setGivenName( alPacinoDTO.getGivenName() );
        alPacino.setDob( alPacinoDTO.getDob() );
        alPacino.setGender( alPacinoDTO.getGender() );
        alPacino.setPhone( alPacinoDTO.getPhone() );
        alPacino.setEmail( alPacinoDTO.getEmail() );
        alPacino.setAcquiredFrom( alPacinoDTO.getAcquiredFrom() );
        alPacino.setCurrentPoints( alPacinoDTO.getCurrentPoints() );
        alPacino.setTotalPoints( alPacinoDTO.getTotalPoints() );
        alPacino.setIsFollowing( alPacinoDTO.getIsFollowing() );
        alPacino.setIsEnabled( alPacinoDTO.getIsEnabled() );
        alPacino.application( johnLennonDTOToJohnLennon( alPacinoDTO.getApplication() ) );
        alPacino.membershipTier( alMemTierDTOToAlMemTier( alPacinoDTO.getMembershipTier() ) );
        alPacino.alVueVueUsage( alVueVueUsageDTOToAlVueVueUsage( alPacinoDTO.getAlVueVueUsage() ) );

        return alPacino;
    }

    protected User userDTOToUser(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User user = new User();

        user.setId( userDTO.getId() );
        user.setLogin( userDTO.getLogin() );

        return user;
    }

    protected EdSheeran edSheeranDTOToEdSheeran(EdSheeranDTO edSheeranDTO) {
        if ( edSheeranDTO == null ) {
            return null;
        }

        EdSheeran edSheeran = new EdSheeran();

        edSheeran.setId( edSheeranDTO.getId() );
        edSheeran.setFamilyName( edSheeranDTO.getFamilyName() );
        edSheeran.setGivenName( edSheeranDTO.getGivenName() );
        edSheeran.setDisplay( edSheeranDTO.getDisplay() );
        edSheeran.setDob( edSheeranDTO.getDob() );
        edSheeran.setGender( edSheeranDTO.getGender() );
        edSheeran.setPhone( edSheeranDTO.getPhone() );
        edSheeran.setContactsJason( edSheeranDTO.getContactsJason() );
        edSheeran.setIsEnabled( edSheeranDTO.getIsEnabled() );
        edSheeran.agency( alAppleDTOToAlApple( edSheeranDTO.getAgency() ) );
        edSheeran.avatar( toEntity( edSheeranDTO.getAvatar() ) );
        edSheeran.internalUser( userDTOToUser( edSheeranDTO.getInternalUser() ) );
        edSheeran.appUser( alPacinoDTOToAlPacino( edSheeranDTO.getAppUser() ) );
        edSheeran.application( johnLennonDTOToJohnLennon( edSheeranDTO.getApplication() ) );

        return edSheeran;
    }

    protected AlPyuJoker alPyuJokerDTOToAlPyuJoker(AlPyuJokerDTO alPyuJokerDTO) {
        if ( alPyuJokerDTO == null ) {
            return null;
        }

        AlPyuJoker alPyuJoker = new AlPyuJoker();

        alPyuJoker.setId( alPyuJokerDTO.getId() );
        alPyuJoker.setBookingNo( alPyuJokerDTO.getBookingNo() );
        alPyuJoker.setNoteHeitiga( alPyuJokerDTO.getNoteHeitiga() );
        alPyuJoker.setPeriodType( alPyuJokerDTO.getPeriodType() );
        alPyuJoker.setFromDate( alPyuJokerDTO.getFromDate() );
        alPyuJoker.setToDate( alPyuJokerDTO.getToDate() );
        alPyuJoker.setCheckInDate( alPyuJokerDTO.getCheckInDate() );
        alPyuJoker.setCheckOutDate( alPyuJokerDTO.getCheckOutDate() );
        alPyuJoker.setNumberOfAdults( alPyuJokerDTO.getNumberOfAdults() );
        alPyuJoker.setNumberOfPreschoolers( alPyuJokerDTO.getNumberOfPreschoolers() );
        alPyuJoker.setNumberOfChildren( alPyuJokerDTO.getNumberOfChildren() );
        alPyuJoker.setBookingPrice( alPyuJokerDTO.getBookingPrice() );
        alPyuJoker.setExtraFee( alPyuJokerDTO.getExtraFee() );
        alPyuJoker.setTotalPrice( alPyuJokerDTO.getTotalPrice() );
        alPyuJoker.setBookingStatus( alPyuJokerDTO.getBookingStatus() );
        alPyuJoker.setHistoryRefJason( alPyuJokerDTO.getHistoryRefJason() );
        alPyuJoker.customer( alPacinoDTOToAlPacino( alPyuJokerDTO.getCustomer() ) );
        alPyuJoker.personInCharge( edSheeranDTOToEdSheeran( alPyuJokerDTO.getPersonInCharge() ) );
        alPyuJoker.application( johnLennonDTOToJohnLennon( alPyuJokerDTO.getApplication() ) );
        alPyuJoker.properties( alProtyDTOSetToAlProtySet( alPyuJokerDTO.getProperties() ) );

        return alPyuJoker;
    }

    protected Set<AlPyuJoker> alPyuJokerDTOSetToAlPyuJokerSet(Set<AlPyuJokerDTO> set) {
        if ( set == null ) {
            return null;
        }

        Set<AlPyuJoker> set1 = new LinkedHashSet<AlPyuJoker>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( AlPyuJokerDTO alPyuJokerDTO : set ) {
            set1.add( alPyuJokerDTOToAlPyuJoker( alPyuJokerDTO ) );
        }

        return set1;
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
        alProty.avatar( toEntity( alProtyDTO.getAvatar() ) );
        alProty.application( johnLennonDTOToJohnLennon( alProtyDTO.getApplication() ) );
        alProty.images( metaverseDTOSetToMetaverseSet( alProtyDTO.getImages() ) );
        alProty.bookings( alPyuJokerDTOSetToAlPyuJokerSet( alProtyDTO.getBookings() ) );

        return alProty;
    }

    protected Set<AlProty> alProtyDTOSetToAlProtySet(Set<AlProtyDTO> set) {
        if ( set == null ) {
            return null;
        }

        Set<AlProty> set1 = new LinkedHashSet<AlProty>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( AlProtyDTO alProtyDTO : set ) {
            set1.add( alProtyDTOToAlProty( alProtyDTO ) );
        }

        return set1;
    }
}
