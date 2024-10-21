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
    date = "2024-10-22T00:19:22+0700",
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
        alLadyGaga.application( toEntity( alLadyGagaDTO.getApplication() ) );

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
        alMenity.application( toEntity( alMenityDTO.getApplication() ) );
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
        alProPro.application( toEntity( alProProDTO.getApplication() ) );
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
        alAlexType.application( toEntity( alAlexTypeDTO.getApplication() ) );

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
        alApple.application( toEntity( alAppleDTO.getApplication() ) );

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
        alMemTier.application( toEntity( alMemTierDTO.getApplication() ) );

        return alMemTier;
    }

    protected AlVueVueUsage alVueVueUsageDTOToAlVueVueUsage(AlVueVueUsageDTO alVueVueUsageDTO) {
        if ( alVueVueUsageDTO == null ) {
            return null;
        }

        AlVueVueUsage alVueVueUsage = new AlVueVueUsage();

        alVueVueUsage.setId( alVueVueUsageDTO.getId() );
        alVueVueUsage.application( toEntity( alVueVueUsageDTO.getApplication() ) );

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
        alPacino.application( toEntity( alPacinoDTO.getApplication() ) );
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
        edSheeran.application( toEntity( edSheeranDTO.getApplication() ) );

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
        alPyuJoker.application( toEntity( alPyuJokerDTO.getApplication() ) );
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
        alProty.avatar( metaverseDTOToMetaverse( alProtyDTO.getAvatar() ) );
        alProty.application( toEntity( alProtyDTO.getApplication() ) );
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
}
