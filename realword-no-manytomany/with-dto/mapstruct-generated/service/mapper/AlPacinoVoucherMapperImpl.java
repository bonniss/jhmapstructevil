package ai.realworld.service.mapper;

import ai.realworld.domain.AlMemTier;
import ai.realworld.domain.AlPacino;
import ai.realworld.domain.AlPacinoVoucher;
import ai.realworld.domain.AlVueVue;
import ai.realworld.domain.AlVueVueUsage;
import ai.realworld.domain.AndreiRightHand;
import ai.realworld.domain.AntonioBanderas;
import ai.realworld.domain.Initium;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.domain.OlAlmantinoMilo;
import ai.realworld.domain.OlMaster;
import ai.realworld.service.dto.AlMemTierDTO;
import ai.realworld.service.dto.AlPacinoDTO;
import ai.realworld.service.dto.AlPacinoVoucherDTO;
import ai.realworld.service.dto.AlVueVueDTO;
import ai.realworld.service.dto.AlVueVueUsageDTO;
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
public class AlPacinoVoucherMapperImpl implements AlPacinoVoucherMapper {

    @Override
    public AlPacinoVoucher toEntity(AlPacinoVoucherDTO dto) {
        if ( dto == null ) {
            return null;
        }

        AlPacinoVoucher alPacinoVoucher = new AlPacinoVoucher();

        alPacinoVoucher.setId( dto.getId() );
        alPacinoVoucher.setSourceTitle( dto.getSourceTitle() );
        alPacinoVoucher.setSourceUrl( dto.getSourceUrl() );
        alPacinoVoucher.setCollectedDate( dto.getCollectedDate() );
        alPacinoVoucher.user( alPacinoDTOToAlPacino( dto.getUser() ) );
        alPacinoVoucher.voucher( alVueVueDTOToAlVueVue( dto.getVoucher() ) );
        alPacinoVoucher.application( johnLennonDTOToJohnLennon( dto.getApplication() ) );

        return alPacinoVoucher;
    }

    @Override
    public List<AlPacinoVoucher> toEntity(List<AlPacinoVoucherDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<AlPacinoVoucher> list = new ArrayList<AlPacinoVoucher>( dtoList.size() );
        for ( AlPacinoVoucherDTO alPacinoVoucherDTO : dtoList ) {
            list.add( toEntity( alPacinoVoucherDTO ) );
        }

        return list;
    }

    @Override
    public List<AlPacinoVoucherDTO> toDto(List<AlPacinoVoucher> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<AlPacinoVoucherDTO> list = new ArrayList<AlPacinoVoucherDTO>( entityList.size() );
        for ( AlPacinoVoucher alPacinoVoucher : entityList ) {
            list.add( toDto( alPacinoVoucher ) );
        }

        return list;
    }

    @Override
    public void partialUpdate(AlPacinoVoucher entity, AlPacinoVoucherDTO dto) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getSourceTitle() != null ) {
            entity.setSourceTitle( dto.getSourceTitle() );
        }
        if ( dto.getSourceUrl() != null ) {
            entity.setSourceUrl( dto.getSourceUrl() );
        }
        if ( dto.getCollectedDate() != null ) {
            entity.setCollectedDate( dto.getCollectedDate() );
        }
        if ( dto.getUser() != null ) {
            if ( entity.getUser() == null ) {
                entity.user( new AlPacino() );
            }
            alPacinoDTOToAlPacino1( dto.getUser(), entity.getUser() );
        }
        if ( dto.getVoucher() != null ) {
            if ( entity.getVoucher() == null ) {
                entity.voucher( new AlVueVue() );
            }
            alVueVueDTOToAlVueVue1( dto.getVoucher(), entity.getVoucher() );
        }
        if ( dto.getApplication() != null ) {
            if ( entity.getApplication() == null ) {
                entity.application( new JohnLennon() );
            }
            johnLennonDTOToJohnLennon1( dto.getApplication(), entity.getApplication() );
        }
    }

    @Override
    public AlPacinoVoucherDTO toDto(AlPacinoVoucher s) {
        if ( s == null ) {
            return null;
        }

        AlPacinoVoucherDTO alPacinoVoucherDTO = new AlPacinoVoucherDTO();

        alPacinoVoucherDTO.setUser( toDtoAlPacinoId( s.getUser() ) );
        alPacinoVoucherDTO.setVoucher( toDtoAlVueVueId( s.getVoucher() ) );
        alPacinoVoucherDTO.setApplication( toDtoJohnLennonId( s.getApplication() ) );
        alPacinoVoucherDTO.setId( s.getId() );
        alPacinoVoucherDTO.setSourceTitle( s.getSourceTitle() );
        alPacinoVoucherDTO.setSourceUrl( s.getSourceUrl() );
        alPacinoVoucherDTO.setCollectedDate( s.getCollectedDate() );

        return alPacinoVoucherDTO;
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
    public AlVueVueDTO toDtoAlVueVueId(AlVueVue alVueVue) {
        if ( alVueVue == null ) {
            return null;
        }

        AlVueVueDTO alVueVueDTO = new AlVueVueDTO();

        alVueVueDTO.setId( alVueVue.getId() );

        return alVueVueDTO;
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

    protected AlVueVue alVueVueDTOToAlVueVue(AlVueVueDTO alVueVueDTO) {
        if ( alVueVueDTO == null ) {
            return null;
        }

        AlVueVue alVueVue = new AlVueVue();

        alVueVue.setId( alVueVueDTO.getId() );
        alVueVue.setCode( alVueVueDTO.getCode() );
        alVueVue.setName( alVueVueDTO.getName() );
        alVueVue.setContentHeitiga( alVueVueDTO.getContentHeitiga() );
        alVueVue.setDiscountType( alVueVueDTO.getDiscountType() );
        alVueVue.setDiscountRate( alVueVueDTO.getDiscountRate() );
        alVueVue.setScope( alVueVueDTO.getScope() );
        alVueVue.setIsIndividuallyUsedOnly( alVueVueDTO.getIsIndividuallyUsedOnly() );
        alVueVue.setUsageLifeTimeLimit( alVueVueDTO.getUsageLifeTimeLimit() );
        alVueVue.setUsageLimitPerUser( alVueVueDTO.getUsageLimitPerUser() );
        alVueVue.setUsageQuantity( alVueVueDTO.getUsageQuantity() );
        alVueVue.setMinimumSpend( alVueVueDTO.getMinimumSpend() );
        alVueVue.setMaximumSpend( alVueVueDTO.getMaximumSpend() );
        alVueVue.setCanBeCollectedByUser( alVueVueDTO.getCanBeCollectedByUser() );
        alVueVue.setSalePriceFromDate( alVueVueDTO.getSalePriceFromDate() );
        alVueVue.setSalePriceToDate( alVueVueDTO.getSalePriceToDate() );
        alVueVue.setPublicationStatus( alVueVueDTO.getPublicationStatus() );
        alVueVue.setPublishedDate( alVueVueDTO.getPublishedDate() );
        alVueVue.image( metaverseDTOToMetaverse( alVueVueDTO.getImage() ) );
        alVueVue.alVueVueUsage( alVueVueUsageDTOToAlVueVueUsage( alVueVueDTO.getAlVueVueUsage() ) );
        alVueVue.application( johnLennonDTOToJohnLennon( alVueVueDTO.getApplication() ) );

        return alVueVue;
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

    protected void alVueVueDTOToAlVueVue1(AlVueVueDTO alVueVueDTO, AlVueVue mappingTarget) {
        if ( alVueVueDTO == null ) {
            return;
        }

        if ( alVueVueDTO.getId() != null ) {
            mappingTarget.setId( alVueVueDTO.getId() );
        }
        if ( alVueVueDTO.getCode() != null ) {
            mappingTarget.setCode( alVueVueDTO.getCode() );
        }
        if ( alVueVueDTO.getName() != null ) {
            mappingTarget.setName( alVueVueDTO.getName() );
        }
        if ( alVueVueDTO.getContentHeitiga() != null ) {
            mappingTarget.setContentHeitiga( alVueVueDTO.getContentHeitiga() );
        }
        if ( alVueVueDTO.getDiscountType() != null ) {
            mappingTarget.setDiscountType( alVueVueDTO.getDiscountType() );
        }
        if ( alVueVueDTO.getDiscountRate() != null ) {
            mappingTarget.setDiscountRate( alVueVueDTO.getDiscountRate() );
        }
        if ( alVueVueDTO.getScope() != null ) {
            mappingTarget.setScope( alVueVueDTO.getScope() );
        }
        if ( alVueVueDTO.getIsIndividuallyUsedOnly() != null ) {
            mappingTarget.setIsIndividuallyUsedOnly( alVueVueDTO.getIsIndividuallyUsedOnly() );
        }
        if ( alVueVueDTO.getUsageLifeTimeLimit() != null ) {
            mappingTarget.setUsageLifeTimeLimit( alVueVueDTO.getUsageLifeTimeLimit() );
        }
        if ( alVueVueDTO.getUsageLimitPerUser() != null ) {
            mappingTarget.setUsageLimitPerUser( alVueVueDTO.getUsageLimitPerUser() );
        }
        if ( alVueVueDTO.getUsageQuantity() != null ) {
            mappingTarget.setUsageQuantity( alVueVueDTO.getUsageQuantity() );
        }
        if ( alVueVueDTO.getMinimumSpend() != null ) {
            mappingTarget.setMinimumSpend( alVueVueDTO.getMinimumSpend() );
        }
        if ( alVueVueDTO.getMaximumSpend() != null ) {
            mappingTarget.setMaximumSpend( alVueVueDTO.getMaximumSpend() );
        }
        if ( alVueVueDTO.getCanBeCollectedByUser() != null ) {
            mappingTarget.setCanBeCollectedByUser( alVueVueDTO.getCanBeCollectedByUser() );
        }
        if ( alVueVueDTO.getSalePriceFromDate() != null ) {
            mappingTarget.setSalePriceFromDate( alVueVueDTO.getSalePriceFromDate() );
        }
        if ( alVueVueDTO.getSalePriceToDate() != null ) {
            mappingTarget.setSalePriceToDate( alVueVueDTO.getSalePriceToDate() );
        }
        if ( alVueVueDTO.getPublicationStatus() != null ) {
            mappingTarget.setPublicationStatus( alVueVueDTO.getPublicationStatus() );
        }
        if ( alVueVueDTO.getPublishedDate() != null ) {
            mappingTarget.setPublishedDate( alVueVueDTO.getPublishedDate() );
        }
        if ( alVueVueDTO.getImage() != null ) {
            if ( mappingTarget.getImage() == null ) {
                mappingTarget.image( new Metaverse() );
            }
            metaverseDTOToMetaverse1( alVueVueDTO.getImage(), mappingTarget.getImage() );
        }
        if ( alVueVueDTO.getAlVueVueUsage() != null ) {
            if ( mappingTarget.getAlVueVueUsage() == null ) {
                mappingTarget.alVueVueUsage( new AlVueVueUsage() );
            }
            alVueVueUsageDTOToAlVueVueUsage1( alVueVueDTO.getAlVueVueUsage(), mappingTarget.getAlVueVueUsage() );
        }
        if ( alVueVueDTO.getApplication() != null ) {
            if ( mappingTarget.getApplication() == null ) {
                mappingTarget.application( new JohnLennon() );
            }
            johnLennonDTOToJohnLennon1( alVueVueDTO.getApplication(), mappingTarget.getApplication() );
        }
    }
}