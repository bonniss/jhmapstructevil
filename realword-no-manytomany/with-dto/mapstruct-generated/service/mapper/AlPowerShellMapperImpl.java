package ai.realworld.service.mapper;

import ai.realworld.domain.AlLadyGaga;
import ai.realworld.domain.AlPedroTax;
import ai.realworld.domain.AlPounder;
import ai.realworld.domain.AlPowerShell;
import ai.realworld.domain.AlProPro;
import ai.realworld.domain.AndreiRightHand;
import ai.realworld.domain.AntonioBanderas;
import ai.realworld.domain.Initium;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.domain.OlAlmantinoMilo;
import ai.realworld.domain.OlMaster;
import ai.realworld.service.dto.AlLadyGagaDTO;
import ai.realworld.service.dto.AlPedroTaxDTO;
import ai.realworld.service.dto.AlPounderDTO;
import ai.realworld.service.dto.AlPowerShellDTO;
import ai.realworld.service.dto.AlProProDTO;
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
    date = "2024-10-22T00:29:22+0700",
    comments = "version: 1.6.2, compiler: javac, environment: Java 17.0.11 (Eclipse Adoptium)"
)
@Component
public class AlPowerShellMapperImpl implements AlPowerShellMapper {

    @Override
    public AlPowerShell toEntity(AlPowerShellDTO dto) {
        if ( dto == null ) {
            return null;
        }

        AlPowerShell alPowerShell = new AlPowerShell();

        alPowerShell.setId( dto.getId() );
        alPowerShell.setValue( dto.getValue() );
        alPowerShell.propertyProfile( alProProDTOToAlProPro( dto.getPropertyProfile() ) );
        alPowerShell.attributeTerm( alPounderDTOToAlPounder( dto.getAttributeTerm() ) );
        alPowerShell.application( johnLennonDTOToJohnLennon( dto.getApplication() ) );

        return alPowerShell;
    }

    @Override
    public List<AlPowerShell> toEntity(List<AlPowerShellDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<AlPowerShell> list = new ArrayList<AlPowerShell>( dtoList.size() );
        for ( AlPowerShellDTO alPowerShellDTO : dtoList ) {
            list.add( toEntity( alPowerShellDTO ) );
        }

        return list;
    }

    @Override
    public List<AlPowerShellDTO> toDto(List<AlPowerShell> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<AlPowerShellDTO> list = new ArrayList<AlPowerShellDTO>( entityList.size() );
        for ( AlPowerShell alPowerShell : entityList ) {
            list.add( toDto( alPowerShell ) );
        }

        return list;
    }

    @Override
    public void partialUpdate(AlPowerShell entity, AlPowerShellDTO dto) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getValue() != null ) {
            entity.setValue( dto.getValue() );
        }
        if ( dto.getPropertyProfile() != null ) {
            if ( entity.getPropertyProfile() == null ) {
                entity.propertyProfile( new AlProPro() );
            }
            alProProDTOToAlProPro1( dto.getPropertyProfile(), entity.getPropertyProfile() );
        }
        if ( dto.getAttributeTerm() != null ) {
            if ( entity.getAttributeTerm() == null ) {
                entity.attributeTerm( new AlPounder() );
            }
            alPounderDTOToAlPounder1( dto.getAttributeTerm(), entity.getAttributeTerm() );
        }
        if ( dto.getApplication() != null ) {
            if ( entity.getApplication() == null ) {
                entity.application( new JohnLennon() );
            }
            johnLennonDTOToJohnLennon1( dto.getApplication(), entity.getApplication() );
        }
    }

    @Override
    public AlPowerShellDTO toDto(AlPowerShell s) {
        if ( s == null ) {
            return null;
        }

        AlPowerShellDTO alPowerShellDTO = new AlPowerShellDTO();

        alPowerShellDTO.setPropertyProfile( toDtoAlProProId( s.getPropertyProfile() ) );
        alPowerShellDTO.setAttributeTerm( toDtoAlPounderId( s.getAttributeTerm() ) );
        alPowerShellDTO.setApplication( toDtoJohnLennonId( s.getApplication() ) );
        alPowerShellDTO.setId( s.getId() );
        alPowerShellDTO.setValue( s.getValue() );

        return alPowerShellDTO;
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
    public AlPounderDTO toDtoAlPounderId(AlPounder alPounder) {
        if ( alPounder == null ) {
            return null;
        }

        AlPounderDTO alPounderDTO = new AlPounderDTO();

        alPounderDTO.setId( alPounder.getId() );

        return alPounderDTO;
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

    protected AlPedroTax alPedroTaxDTOToAlPedroTax(AlPedroTaxDTO alPedroTaxDTO) {
        if ( alPedroTaxDTO == null ) {
            return null;
        }

        AlPedroTax alPedroTax = new AlPedroTax();

        alPedroTax.setId( alPedroTaxDTO.getId() );
        alPedroTax.setName( alPedroTaxDTO.getName() );
        alPedroTax.setDescription( alPedroTaxDTO.getDescription() );
        alPedroTax.setWeight( alPedroTaxDTO.getWeight() );
        alPedroTax.setPropertyType( alPedroTaxDTO.getPropertyType() );
        alPedroTax.application( johnLennonDTOToJohnLennon( alPedroTaxDTO.getApplication() ) );

        return alPedroTax;
    }

    protected AlPounder alPounderDTOToAlPounder(AlPounderDTO alPounderDTO) {
        if ( alPounderDTO == null ) {
            return null;
        }

        AlPounder alPounder = new AlPounder();

        alPounder.setId( alPounderDTO.getId() );
        alPounder.setName( alPounderDTO.getName() );
        alPounder.setWeight( alPounderDTO.getWeight() );
        alPounder.attributeTaxonomy( alPedroTaxDTOToAlPedroTax( alPounderDTO.getAttributeTaxonomy() ) );
        alPounder.application( johnLennonDTOToJohnLennon( alPounderDTO.getApplication() ) );

        return alPounder;
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

    protected void alPedroTaxDTOToAlPedroTax1(AlPedroTaxDTO alPedroTaxDTO, AlPedroTax mappingTarget) {
        if ( alPedroTaxDTO == null ) {
            return;
        }

        if ( alPedroTaxDTO.getId() != null ) {
            mappingTarget.setId( alPedroTaxDTO.getId() );
        }
        if ( alPedroTaxDTO.getName() != null ) {
            mappingTarget.setName( alPedroTaxDTO.getName() );
        }
        if ( alPedroTaxDTO.getDescription() != null ) {
            mappingTarget.setDescription( alPedroTaxDTO.getDescription() );
        }
        if ( alPedroTaxDTO.getWeight() != null ) {
            mappingTarget.setWeight( alPedroTaxDTO.getWeight() );
        }
        if ( alPedroTaxDTO.getPropertyType() != null ) {
            mappingTarget.setPropertyType( alPedroTaxDTO.getPropertyType() );
        }
        if ( alPedroTaxDTO.getApplication() != null ) {
            if ( mappingTarget.getApplication() == null ) {
                mappingTarget.application( new JohnLennon() );
            }
            johnLennonDTOToJohnLennon1( alPedroTaxDTO.getApplication(), mappingTarget.getApplication() );
        }
    }

    protected void alPounderDTOToAlPounder1(AlPounderDTO alPounderDTO, AlPounder mappingTarget) {
        if ( alPounderDTO == null ) {
            return;
        }

        if ( alPounderDTO.getId() != null ) {
            mappingTarget.setId( alPounderDTO.getId() );
        }
        if ( alPounderDTO.getName() != null ) {
            mappingTarget.setName( alPounderDTO.getName() );
        }
        if ( alPounderDTO.getWeight() != null ) {
            mappingTarget.setWeight( alPounderDTO.getWeight() );
        }
        if ( alPounderDTO.getAttributeTaxonomy() != null ) {
            if ( mappingTarget.getAttributeTaxonomy() == null ) {
                mappingTarget.attributeTaxonomy( new AlPedroTax() );
            }
            alPedroTaxDTOToAlPedroTax1( alPounderDTO.getAttributeTaxonomy(), mappingTarget.getAttributeTaxonomy() );
        }
        if ( alPounderDTO.getApplication() != null ) {
            if ( mappingTarget.getApplication() == null ) {
                mappingTarget.application( new JohnLennon() );
            }
            johnLennonDTOToJohnLennon1( alPounderDTO.getApplication(), mappingTarget.getApplication() );
        }
    }
}
