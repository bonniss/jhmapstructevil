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
    date = "2024-10-22T00:19:33+0700",
    comments = "version: 1.6.2, compiler: javac, environment: Java 17.0.11 (Eclipse Adoptium)"
)
@Component
public class AlPyuJokerMapperImpl implements AlPyuJokerMapper {

    @Override
    public List<AlPyuJoker> toEntity(List<AlPyuJokerDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<AlPyuJoker> list = new ArrayList<AlPyuJoker>( dtoList.size() );
        for ( AlPyuJokerDTO alPyuJokerDTO : dtoList ) {
            list.add( toEntity( alPyuJokerDTO ) );
        }

        return list;
    }

    @Override
    public List<AlPyuJokerDTO> toDto(List<AlPyuJoker> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<AlPyuJokerDTO> list = new ArrayList<AlPyuJokerDTO>( entityList.size() );
        for ( AlPyuJoker alPyuJoker : entityList ) {
            list.add( toDto( alPyuJoker ) );
        }

        return list;
    }

    @Override
    public void partialUpdate(AlPyuJoker entity, AlPyuJokerDTO dto) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getBookingNo() != null ) {
            entity.setBookingNo( dto.getBookingNo() );
        }
        if ( dto.getNoteHeitiga() != null ) {
            entity.setNoteHeitiga( dto.getNoteHeitiga() );
        }
        if ( dto.getPeriodType() != null ) {
            entity.setPeriodType( dto.getPeriodType() );
        }
        if ( dto.getFromDate() != null ) {
            entity.setFromDate( dto.getFromDate() );
        }
        if ( dto.getToDate() != null ) {
            entity.setToDate( dto.getToDate() );
        }
        if ( dto.getCheckInDate() != null ) {
            entity.setCheckInDate( dto.getCheckInDate() );
        }
        if ( dto.getCheckOutDate() != null ) {
            entity.setCheckOutDate( dto.getCheckOutDate() );
        }
        if ( dto.getNumberOfAdults() != null ) {
            entity.setNumberOfAdults( dto.getNumberOfAdults() );
        }
        if ( dto.getNumberOfPreschoolers() != null ) {
            entity.setNumberOfPreschoolers( dto.getNumberOfPreschoolers() );
        }
        if ( dto.getNumberOfChildren() != null ) {
            entity.setNumberOfChildren( dto.getNumberOfChildren() );
        }
        if ( dto.getBookingPrice() != null ) {
            entity.setBookingPrice( dto.getBookingPrice() );
        }
        if ( dto.getExtraFee() != null ) {
            entity.setExtraFee( dto.getExtraFee() );
        }
        if ( dto.getTotalPrice() != null ) {
            entity.setTotalPrice( dto.getTotalPrice() );
        }
        if ( dto.getBookingStatus() != null ) {
            entity.setBookingStatus( dto.getBookingStatus() );
        }
        if ( dto.getHistoryRefJason() != null ) {
            entity.setHistoryRefJason( dto.getHistoryRefJason() );
        }
        if ( dto.getCustomer() != null ) {
            if ( entity.getCustomer() == null ) {
                entity.customer( new AlPacino() );
            }
            alPacinoDTOToAlPacino( dto.getCustomer(), entity.getCustomer() );
        }
        if ( dto.getPersonInCharge() != null ) {
            if ( entity.getPersonInCharge() == null ) {
                entity.personInCharge( new EdSheeran() );
            }
            edSheeranDTOToEdSheeran( dto.getPersonInCharge(), entity.getPersonInCharge() );
        }
        if ( dto.getApplication() != null ) {
            if ( entity.getApplication() == null ) {
                entity.application( new JohnLennon() );
            }
            johnLennonDTOToJohnLennon( dto.getApplication(), entity.getApplication() );
        }
        if ( entity.getProperties() != null ) {
            Set<AlProty> set = alProtyDTOSetToAlProtySet( dto.getProperties() );
            if ( set != null ) {
                entity.getProperties().clear();
                entity.getProperties().addAll( set );
            }
        }
        else {
            Set<AlProty> set = alProtyDTOSetToAlProtySet( dto.getProperties() );
            if ( set != null ) {
                entity.properties( set );
            }
        }
    }

    @Override
    public AlPyuJokerDTO toDto(AlPyuJoker s) {
        if ( s == null ) {
            return null;
        }

        AlPyuJokerDTO alPyuJokerDTO = new AlPyuJokerDTO();

        alPyuJokerDTO.setCustomer( toDtoAlPacinoId( s.getCustomer() ) );
        alPyuJokerDTO.setPersonInCharge( toDtoEdSheeranId( s.getPersonInCharge() ) );
        alPyuJokerDTO.setApplication( toDtoJohnLennonId( s.getApplication() ) );
        alPyuJokerDTO.setProperties( toDtoAlProtyIdSet( s.getProperties() ) );
        alPyuJokerDTO.setId( s.getId() );
        alPyuJokerDTO.setBookingNo( s.getBookingNo() );
        alPyuJokerDTO.setNoteHeitiga( s.getNoteHeitiga() );
        alPyuJokerDTO.setPeriodType( s.getPeriodType() );
        alPyuJokerDTO.setFromDate( s.getFromDate() );
        alPyuJokerDTO.setToDate( s.getToDate() );
        alPyuJokerDTO.setCheckInDate( s.getCheckInDate() );
        alPyuJokerDTO.setCheckOutDate( s.getCheckOutDate() );
        alPyuJokerDTO.setNumberOfAdults( s.getNumberOfAdults() );
        alPyuJokerDTO.setNumberOfPreschoolers( s.getNumberOfPreschoolers() );
        alPyuJokerDTO.setNumberOfChildren( s.getNumberOfChildren() );
        alPyuJokerDTO.setBookingPrice( s.getBookingPrice() );
        alPyuJokerDTO.setExtraFee( s.getExtraFee() );
        alPyuJokerDTO.setTotalPrice( s.getTotalPrice() );
        alPyuJokerDTO.setBookingStatus( s.getBookingStatus() );
        alPyuJokerDTO.setHistoryRefJason( s.getHistoryRefJason() );

        return alPyuJokerDTO;
    }

    @Override
    public AlPyuJoker toEntity(AlPyuJokerDTO alPyuJokerDTO) {
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
        alPyuJoker.customer( alPacinoDTOToAlPacino1( alPyuJokerDTO.getCustomer() ) );
        alPyuJoker.personInCharge( edSheeranDTOToEdSheeran1( alPyuJokerDTO.getPersonInCharge() ) );
        alPyuJoker.application( johnLennonDTOToJohnLennon( alPyuJokerDTO.getApplication() ) );
        alPyuJoker.properties( alProtyDTOSetToAlProtySet( alPyuJokerDTO.getProperties() ) );

        return alPyuJoker;
    }

    @Override
    public AlPacinoDTO toDtoAlPacinoId(AlPacino alPacino) {
        if ( alPacino == null ) {
            return null;
        }

        AlPacinoDTO alPacinoDTO = new AlPacinoDTO();

        alPacinoDTO.setId( alPacino.getId() );

        return alPacinoDTO;
    }

    @Override
    public EdSheeranDTO toDtoEdSheeranId(EdSheeran edSheeran) {
        if ( edSheeran == null ) {
            return null;
        }

        EdSheeranDTO edSheeranDTO = new EdSheeranDTO();

        edSheeranDTO.setId( edSheeran.getId() );

        return edSheeranDTO;
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

    protected Set<AlPyuJoker> alPyuJokerDTOSetToAlPyuJokerSet(Set<AlPyuJokerDTO> set) {
        if ( set == null ) {
            return null;
        }

        Set<AlPyuJoker> set1 = new LinkedHashSet<AlPyuJoker>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( AlPyuJokerDTO alPyuJokerDTO : set ) {
            set1.add( toEntity( alPyuJokerDTO ) );
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

    protected void metaverseDTOToMetaverse(MetaverseDTO metaverseDTO, Metaverse mappingTarget) {
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

    protected void johnLennonDTOToJohnLennon(JohnLennonDTO johnLennonDTO, JohnLennon mappingTarget) {
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
            metaverseDTOToMetaverse( johnLennonDTO.getLogo(), mappingTarget.getLogo() );
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

    protected void alMemTierDTOToAlMemTier(AlMemTierDTO alMemTierDTO, AlMemTier mappingTarget) {
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
            johnLennonDTOToJohnLennon( alMemTierDTO.getApplication(), mappingTarget.getApplication() );
        }
    }

    protected void alVueVueUsageDTOToAlVueVueUsage(AlVueVueUsageDTO alVueVueUsageDTO, AlVueVueUsage mappingTarget) {
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
            johnLennonDTOToJohnLennon( alVueVueUsageDTO.getApplication(), mappingTarget.getApplication() );
        }
    }

    protected void alPacinoDTOToAlPacino(AlPacinoDTO alPacinoDTO, AlPacino mappingTarget) {
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
            johnLennonDTOToJohnLennon( alPacinoDTO.getApplication(), mappingTarget.getApplication() );
        }
        if ( alPacinoDTO.getMembershipTier() != null ) {
            if ( mappingTarget.getMembershipTier() == null ) {
                mappingTarget.membershipTier( new AlMemTier() );
            }
            alMemTierDTOToAlMemTier( alPacinoDTO.getMembershipTier(), mappingTarget.getMembershipTier() );
        }
        if ( alPacinoDTO.getAlVueVueUsage() != null ) {
            if ( mappingTarget.getAlVueVueUsage() == null ) {
                mappingTarget.alVueVueUsage( new AlVueVueUsage() );
            }
            alVueVueUsageDTOToAlVueVueUsage( alPacinoDTO.getAlVueVueUsage(), mappingTarget.getAlVueVueUsage() );
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
            johnLennonDTOToJohnLennon( alAlexTypeDTO.getApplication(), mappingTarget.getApplication() );
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
            metaverseDTOToMetaverse( alAppleDTO.getLogo(), mappingTarget.getLogo() );
        }
        if ( alAppleDTO.getApplication() != null ) {
            if ( mappingTarget.getApplication() == null ) {
                mappingTarget.application( new JohnLennon() );
            }
            johnLennonDTOToJohnLennon( alAppleDTO.getApplication(), mappingTarget.getApplication() );
        }
    }

    protected void userDTOToUser(UserDTO userDTO, User mappingTarget) {
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

    protected void edSheeranDTOToEdSheeran(EdSheeranDTO edSheeranDTO, EdSheeran mappingTarget) {
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
            metaverseDTOToMetaverse( edSheeranDTO.getAvatar(), mappingTarget.getAvatar() );
        }
        if ( edSheeranDTO.getInternalUser() != null ) {
            if ( mappingTarget.getInternalUser() == null ) {
                mappingTarget.internalUser( new User() );
            }
            userDTOToUser( edSheeranDTO.getInternalUser(), mappingTarget.getInternalUser() );
        }
        if ( edSheeranDTO.getAppUser() != null ) {
            if ( mappingTarget.getAppUser() == null ) {
                mappingTarget.appUser( new AlPacino() );
            }
            alPacinoDTOToAlPacino( edSheeranDTO.getAppUser(), mappingTarget.getAppUser() );
        }
        if ( edSheeranDTO.getApplication() != null ) {
            if ( mappingTarget.getApplication() == null ) {
                mappingTarget.application( new JohnLennon() );
            }
            johnLennonDTOToJohnLennon( edSheeranDTO.getApplication(), mappingTarget.getApplication() );
        }
    }

    protected AlMemTier alMemTierDTOToAlMemTier1(AlMemTierDTO alMemTierDTO) {
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

    protected AlVueVueUsage alVueVueUsageDTOToAlVueVueUsage1(AlVueVueUsageDTO alVueVueUsageDTO) {
        if ( alVueVueUsageDTO == null ) {
            return null;
        }

        AlVueVueUsage alVueVueUsage = new AlVueVueUsage();

        alVueVueUsage.setId( alVueVueUsageDTO.getId() );
        alVueVueUsage.application( johnLennonDTOToJohnLennon( alVueVueUsageDTO.getApplication() ) );

        return alVueVueUsage;
    }

    protected AlPacino alPacinoDTOToAlPacino1(AlPacinoDTO alPacinoDTO) {
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
        alPacino.membershipTier( alMemTierDTOToAlMemTier1( alPacinoDTO.getMembershipTier() ) );
        alPacino.alVueVueUsage( alVueVueUsageDTOToAlVueVueUsage1( alPacinoDTO.getAlVueVueUsage() ) );

        return alPacino;
    }

    protected User userDTOToUser1(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User user = new User();

        user.setId( userDTO.getId() );
        user.setLogin( userDTO.getLogin() );

        return user;
    }

    protected EdSheeran edSheeranDTOToEdSheeran1(EdSheeranDTO edSheeranDTO) {
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
        edSheeran.internalUser( userDTOToUser1( edSheeranDTO.getInternalUser() ) );
        edSheeran.appUser( alPacinoDTOToAlPacino1( edSheeranDTO.getAppUser() ) );
        edSheeran.application( johnLennonDTOToJohnLennon( edSheeranDTO.getApplication() ) );

        return edSheeran;
    }
}
