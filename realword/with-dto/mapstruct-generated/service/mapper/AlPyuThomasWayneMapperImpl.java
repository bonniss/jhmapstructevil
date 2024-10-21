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
import ai.realworld.domain.AlPyuThomasWayne;
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
import ai.realworld.service.dto.AlPyuThomasWayneDTO;
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
    date = "2024-10-22T00:21:04+0700",
    comments = "version: 1.6.2, compiler: javac, environment: Java 17.0.11 (Eclipse Adoptium)"
)
@Component
public class AlPyuThomasWayneMapperImpl implements AlPyuThomasWayneMapper {

    @Override
    public AlPyuThomasWayne toEntity(AlPyuThomasWayneDTO dto) {
        if ( dto == null ) {
            return null;
        }

        AlPyuThomasWayne alPyuThomasWayne = new AlPyuThomasWayne();

        alPyuThomasWayne.setId( dto.getId() );
        alPyuThomasWayne.setRating( dto.getRating() );
        alPyuThomasWayne.setComment( dto.getComment() );
        alPyuThomasWayne.booking( alPyuJokerDTOToAlPyuJoker( dto.getBooking() ) );

        return alPyuThomasWayne;
    }

    @Override
    public List<AlPyuThomasWayne> toEntity(List<AlPyuThomasWayneDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<AlPyuThomasWayne> list = new ArrayList<AlPyuThomasWayne>( dtoList.size() );
        for ( AlPyuThomasWayneDTO alPyuThomasWayneDTO : dtoList ) {
            list.add( toEntity( alPyuThomasWayneDTO ) );
        }

        return list;
    }

    @Override
    public List<AlPyuThomasWayneDTO> toDto(List<AlPyuThomasWayne> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<AlPyuThomasWayneDTO> list = new ArrayList<AlPyuThomasWayneDTO>( entityList.size() );
        for ( AlPyuThomasWayne alPyuThomasWayne : entityList ) {
            list.add( toDto( alPyuThomasWayne ) );
        }

        return list;
    }

    @Override
    public void partialUpdate(AlPyuThomasWayne entity, AlPyuThomasWayneDTO dto) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getRating() != null ) {
            entity.setRating( dto.getRating() );
        }
        if ( dto.getComment() != null ) {
            entity.setComment( dto.getComment() );
        }
        if ( dto.getBooking() != null ) {
            if ( entity.getBooking() == null ) {
                entity.booking( new AlPyuJoker() );
            }
            alPyuJokerDTOToAlPyuJoker1( dto.getBooking(), entity.getBooking() );
        }
    }

    @Override
    public AlPyuThomasWayneDTO toDto(AlPyuThomasWayne s) {
        if ( s == null ) {
            return null;
        }

        AlPyuThomasWayneDTO alPyuThomasWayneDTO = new AlPyuThomasWayneDTO();

        alPyuThomasWayneDTO.setBooking( toDtoAlPyuJokerId( s.getBooking() ) );
        alPyuThomasWayneDTO.setId( s.getId() );
        alPyuThomasWayneDTO.setRating( s.getRating() );
        alPyuThomasWayneDTO.setComment( s.getComment() );

        return alPyuThomasWayneDTO;
    }

    @Override
    public AlPyuJokerDTO toDtoAlPyuJokerId(AlPyuJoker alPyuJoker) {
        if ( alPyuJoker == null ) {
            return null;
        }

        AlPyuJokerDTO alPyuJokerDTO = new AlPyuJokerDTO();

        alPyuJokerDTO.setId( alPyuJoker.getId() );

        return alPyuJokerDTO;
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
            set1.add( metaverseDTOToMetaverse( metaverseDTO ) );
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
        alProPro.avatar( metaverseDTOToMetaverse( alProProDTO.getAvatar() ) );
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
        alApple.logo( metaverseDTOToMetaverse( alAppleDTO.getLogo() ) );
        alApple.application( johnLennonDTOToJohnLennon( alAppleDTO.getApplication() ) );

        return alApple;
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
        alProty.avatar( metaverseDTOToMetaverse( alProtyDTO.getAvatar() ) );
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
        metaverse.alProPros( alProProDTOSetToAlProProSet( metaverseDTO.getAlProPros() ) );
        metaverse.alProties( alProtyDTOSetToAlProtySet( metaverseDTO.getAlProties() ) );

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
        edSheeran.avatar( metaverseDTOToMetaverse( edSheeranDTO.getAvatar() ) );
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
        if ( mappingTarget.getAlProPros() != null ) {
            Set<AlProPro> set = alProProDTOSetToAlProProSet( metaverseDTO.getAlProPros() );
            if ( set != null ) {
                mappingTarget.getAlProPros().clear();
                mappingTarget.getAlProPros().addAll( set );
            }
        }
        else {
            Set<AlProPro> set = alProProDTOSetToAlProProSet( metaverseDTO.getAlProPros() );
            if ( set != null ) {
                mappingTarget.alProPros( set );
            }
        }
        if ( mappingTarget.getAlProties() != null ) {
            Set<AlProty> set1 = alProtyDTOSetToAlProtySet( metaverseDTO.getAlProties() );
            if ( set1 != null ) {
                mappingTarget.getAlProties().clear();
                mappingTarget.getAlProties().addAll( set1 );
            }
        }
        else {
            Set<AlProty> set1 = alProtyDTOSetToAlProtySet( metaverseDTO.getAlProties() );
            if ( set1 != null ) {
                mappingTarget.alProties( set1 );
            }
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

    protected void alMemTierDTOToAlMemTier1(AlMemTierDTO alMemTierDTO, AlMemTier mappingTarget) {
        if ( alMemTierDTO == null ) {
            return;
        }

        if ( alMemTierDTO.getId() != null ) {
            mappingTarget.setId( alMemTierDTO.getId() );
        }
        if ( alMemTierDTO.getName() != null ) {
            mappingTarget.setName( alMemTierDTO.getName() );
        }
        if ( alMemTierDTO.getDescription() != null ) {
            mappingTarget.setDescription( alMemTierDTO.getDescription() );
        }
        if ( alMemTierDTO.getMinPoint() != null ) {
            mappingTarget.setMinPoint( alMemTierDTO.getMinPoint() );
        }
        if ( alMemTierDTO.getApplication() != null ) {
            if ( mappingTarget.getApplication() == null ) {
                mappingTarget.application( new JohnLennon() );
            }
            johnLennonDTOToJohnLennon1( alMemTierDTO.getApplication(), mappingTarget.getApplication() );
        }
    }

    protected void alVueVueUsageDTOToAlVueVueUsage1(AlVueVueUsageDTO alVueVueUsageDTO, AlVueVueUsage mappingTarget) {
        if ( alVueVueUsageDTO == null ) {
            return;
        }

        if ( alVueVueUsageDTO.getId() != null ) {
            mappingTarget.setId( alVueVueUsageDTO.getId() );
        }
        if ( alVueVueUsageDTO.getApplication() != null ) {
            if ( mappingTarget.getApplication() == null ) {
                mappingTarget.application( new JohnLennon() );
            }
            johnLennonDTOToJohnLennon1( alVueVueUsageDTO.getApplication(), mappingTarget.getApplication() );
        }
    }

    protected void alPacinoDTOToAlPacino1(AlPacinoDTO alPacinoDTO, AlPacino mappingTarget) {
        if ( alPacinoDTO == null ) {
            return;
        }

        if ( alPacinoDTO.getId() != null ) {
            mappingTarget.setId( alPacinoDTO.getId() );
        }
        if ( alPacinoDTO.getPlatformCode() != null ) {
            mappingTarget.setPlatformCode( alPacinoDTO.getPlatformCode() );
        }
        if ( alPacinoDTO.getPlatformUsername() != null ) {
            mappingTarget.setPlatformUsername( alPacinoDTO.getPlatformUsername() );
        }
        if ( alPacinoDTO.getPlatformAvatarUrl() != null ) {
            mappingTarget.setPlatformAvatarUrl( alPacinoDTO.getPlatformAvatarUrl() );
        }
        if ( alPacinoDTO.getIsSensitive() != null ) {
            mappingTarget.setIsSensitive( alPacinoDTO.getIsSensitive() );
        }
        if ( alPacinoDTO.getFamilyName() != null ) {
            mappingTarget.setFamilyName( alPacinoDTO.getFamilyName() );
        }
        if ( alPacinoDTO.getGivenName() != null ) {
            mappingTarget.setGivenName( alPacinoDTO.getGivenName() );
        }
        if ( alPacinoDTO.getDob() != null ) {
            mappingTarget.setDob( alPacinoDTO.getDob() );
        }
        if ( alPacinoDTO.getGender() != null ) {
            mappingTarget.setGender( alPacinoDTO.getGender() );
        }
        if ( alPacinoDTO.getPhone() != null ) {
            mappingTarget.setPhone( alPacinoDTO.getPhone() );
        }
        if ( alPacinoDTO.getEmail() != null ) {
            mappingTarget.setEmail( alPacinoDTO.getEmail() );
        }
        if ( alPacinoDTO.getAcquiredFrom() != null ) {
            mappingTarget.setAcquiredFrom( alPacinoDTO.getAcquiredFrom() );
        }
        if ( alPacinoDTO.getCurrentPoints() != null ) {
            mappingTarget.setCurrentPoints( alPacinoDTO.getCurrentPoints() );
        }
        if ( alPacinoDTO.getTotalPoints() != null ) {
            mappingTarget.setTotalPoints( alPacinoDTO.getTotalPoints() );
        }
        if ( alPacinoDTO.getIsFollowing() != null ) {
            mappingTarget.setIsFollowing( alPacinoDTO.getIsFollowing() );
        }
        if ( alPacinoDTO.getIsEnabled() != null ) {
            mappingTarget.setIsEnabled( alPacinoDTO.getIsEnabled() );
        }
        if ( alPacinoDTO.getApplication() != null ) {
            if ( mappingTarget.getApplication() == null ) {
                mappingTarget.application( new JohnLennon() );
            }
            johnLennonDTOToJohnLennon1( alPacinoDTO.getApplication(), mappingTarget.getApplication() );
        }
        if ( alPacinoDTO.getMembershipTier() != null ) {
            if ( mappingTarget.getMembershipTier() == null ) {
                mappingTarget.membershipTier( new AlMemTier() );
            }
            alMemTierDTOToAlMemTier1( alPacinoDTO.getMembershipTier(), mappingTarget.getMembershipTier() );
        }
        if ( alPacinoDTO.getAlVueVueUsage() != null ) {
            if ( mappingTarget.getAlVueVueUsage() == null ) {
                mappingTarget.alVueVueUsage( new AlVueVueUsage() );
            }
            alVueVueUsageDTOToAlVueVueUsage1( alPacinoDTO.getAlVueVueUsage(), mappingTarget.getAlVueVueUsage() );
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

    protected void userDTOToUser1(UserDTO userDTO, User mappingTarget) {
        if ( userDTO == null ) {
            return;
        }

        if ( userDTO.getId() != null ) {
            mappingTarget.setId( userDTO.getId() );
        }
        if ( userDTO.getLogin() != null ) {
            mappingTarget.setLogin( userDTO.getLogin() );
        }
    }

    protected void edSheeranDTOToEdSheeran1(EdSheeranDTO edSheeranDTO, EdSheeran mappingTarget) {
        if ( edSheeranDTO == null ) {
            return;
        }

        if ( edSheeranDTO.getId() != null ) {
            mappingTarget.setId( edSheeranDTO.getId() );
        }
        if ( edSheeranDTO.getFamilyName() != null ) {
            mappingTarget.setFamilyName( edSheeranDTO.getFamilyName() );
        }
        if ( edSheeranDTO.getGivenName() != null ) {
            mappingTarget.setGivenName( edSheeranDTO.getGivenName() );
        }
        if ( edSheeranDTO.getDisplay() != null ) {
            mappingTarget.setDisplay( edSheeranDTO.getDisplay() );
        }
        if ( edSheeranDTO.getDob() != null ) {
            mappingTarget.setDob( edSheeranDTO.getDob() );
        }
        if ( edSheeranDTO.getGender() != null ) {
            mappingTarget.setGender( edSheeranDTO.getGender() );
        }
        if ( edSheeranDTO.getPhone() != null ) {
            mappingTarget.setPhone( edSheeranDTO.getPhone() );
        }
        if ( edSheeranDTO.getContactsJason() != null ) {
            mappingTarget.setContactsJason( edSheeranDTO.getContactsJason() );
        }
        if ( edSheeranDTO.getIsEnabled() != null ) {
            mappingTarget.setIsEnabled( edSheeranDTO.getIsEnabled() );
        }
        if ( edSheeranDTO.getAgency() != null ) {
            if ( mappingTarget.getAgency() == null ) {
                mappingTarget.agency( new AlApple() );
            }
            alAppleDTOToAlApple1( edSheeranDTO.getAgency(), mappingTarget.getAgency() );
        }
        if ( edSheeranDTO.getAvatar() != null ) {
            if ( mappingTarget.getAvatar() == null ) {
                mappingTarget.avatar( new Metaverse() );
            }
            metaverseDTOToMetaverse1( edSheeranDTO.getAvatar(), mappingTarget.getAvatar() );
        }
        if ( edSheeranDTO.getInternalUser() != null ) {
            if ( mappingTarget.getInternalUser() == null ) {
                mappingTarget.internalUser( new User() );
            }
            userDTOToUser1( edSheeranDTO.getInternalUser(), mappingTarget.getInternalUser() );
        }
        if ( edSheeranDTO.getAppUser() != null ) {
            if ( mappingTarget.getAppUser() == null ) {
                mappingTarget.appUser( new AlPacino() );
            }
            alPacinoDTOToAlPacino1( edSheeranDTO.getAppUser(), mappingTarget.getAppUser() );
        }
        if ( edSheeranDTO.getApplication() != null ) {
            if ( mappingTarget.getApplication() == null ) {
                mappingTarget.application( new JohnLennon() );
            }
            johnLennonDTOToJohnLennon1( edSheeranDTO.getApplication(), mappingTarget.getApplication() );
        }
    }

    protected void alPyuJokerDTOToAlPyuJoker1(AlPyuJokerDTO alPyuJokerDTO, AlPyuJoker mappingTarget) {
        if ( alPyuJokerDTO == null ) {
            return;
        }

        if ( alPyuJokerDTO.getId() != null ) {
            mappingTarget.setId( alPyuJokerDTO.getId() );
        }
        if ( alPyuJokerDTO.getBookingNo() != null ) {
            mappingTarget.setBookingNo( alPyuJokerDTO.getBookingNo() );
        }
        if ( alPyuJokerDTO.getNoteHeitiga() != null ) {
            mappingTarget.setNoteHeitiga( alPyuJokerDTO.getNoteHeitiga() );
        }
        if ( alPyuJokerDTO.getPeriodType() != null ) {
            mappingTarget.setPeriodType( alPyuJokerDTO.getPeriodType() );
        }
        if ( alPyuJokerDTO.getFromDate() != null ) {
            mappingTarget.setFromDate( alPyuJokerDTO.getFromDate() );
        }
        if ( alPyuJokerDTO.getToDate() != null ) {
            mappingTarget.setToDate( alPyuJokerDTO.getToDate() );
        }
        if ( alPyuJokerDTO.getCheckInDate() != null ) {
            mappingTarget.setCheckInDate( alPyuJokerDTO.getCheckInDate() );
        }
        if ( alPyuJokerDTO.getCheckOutDate() != null ) {
            mappingTarget.setCheckOutDate( alPyuJokerDTO.getCheckOutDate() );
        }
        if ( alPyuJokerDTO.getNumberOfAdults() != null ) {
            mappingTarget.setNumberOfAdults( alPyuJokerDTO.getNumberOfAdults() );
        }
        if ( alPyuJokerDTO.getNumberOfPreschoolers() != null ) {
            mappingTarget.setNumberOfPreschoolers( alPyuJokerDTO.getNumberOfPreschoolers() );
        }
        if ( alPyuJokerDTO.getNumberOfChildren() != null ) {
            mappingTarget.setNumberOfChildren( alPyuJokerDTO.getNumberOfChildren() );
        }
        if ( alPyuJokerDTO.getBookingPrice() != null ) {
            mappingTarget.setBookingPrice( alPyuJokerDTO.getBookingPrice() );
        }
        if ( alPyuJokerDTO.getExtraFee() != null ) {
            mappingTarget.setExtraFee( alPyuJokerDTO.getExtraFee() );
        }
        if ( alPyuJokerDTO.getTotalPrice() != null ) {
            mappingTarget.setTotalPrice( alPyuJokerDTO.getTotalPrice() );
        }
        if ( alPyuJokerDTO.getBookingStatus() != null ) {
            mappingTarget.setBookingStatus( alPyuJokerDTO.getBookingStatus() );
        }
        if ( alPyuJokerDTO.getHistoryRefJason() != null ) {
            mappingTarget.setHistoryRefJason( alPyuJokerDTO.getHistoryRefJason() );
        }
        if ( alPyuJokerDTO.getCustomer() != null ) {
            if ( mappingTarget.getCustomer() == null ) {
                mappingTarget.customer( new AlPacino() );
            }
            alPacinoDTOToAlPacino1( alPyuJokerDTO.getCustomer(), mappingTarget.getCustomer() );
        }
        if ( alPyuJokerDTO.getPersonInCharge() != null ) {
            if ( mappingTarget.getPersonInCharge() == null ) {
                mappingTarget.personInCharge( new EdSheeran() );
            }
            edSheeranDTOToEdSheeran1( alPyuJokerDTO.getPersonInCharge(), mappingTarget.getPersonInCharge() );
        }
        if ( alPyuJokerDTO.getApplication() != null ) {
            if ( mappingTarget.getApplication() == null ) {
                mappingTarget.application( new JohnLennon() );
            }
            johnLennonDTOToJohnLennon1( alPyuJokerDTO.getApplication(), mappingTarget.getApplication() );
        }
        if ( mappingTarget.getProperties() != null ) {
            Set<AlProty> set = alProtyDTOSetToAlProtySet( alPyuJokerDTO.getProperties() );
            if ( set != null ) {
                mappingTarget.getProperties().clear();
                mappingTarget.getProperties().addAll( set );
            }
        }
        else {
            Set<AlProty> set = alProtyDTOSetToAlProtySet( alPyuJokerDTO.getProperties() );
            if ( set != null ) {
                mappingTarget.properties( set );
            }
        }
    }
}
