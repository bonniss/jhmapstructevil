package ai.realworld.service.mapper;

import ai.realworld.domain.Metaverse;
import ai.realworld.service.dto.MetaverseDTO;
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
public class MetaverseMapperImpl implements MetaverseMapper {

    @Override
    public Metaverse toEntity(MetaverseDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Metaverse metaverse = new Metaverse();

        metaverse.setId( dto.getId() );
        metaverse.setFilename( dto.getFilename() );
        metaverse.setContentType( dto.getContentType() );
        metaverse.setFileExt( dto.getFileExt() );
        metaverse.setFileSize( dto.getFileSize() );
        metaverse.setFileUrl( dto.getFileUrl() );
        metaverse.setThumbnailUrl( dto.getThumbnailUrl() );
        metaverse.setBlurhash( dto.getBlurhash() );
        metaverse.setObjectName( dto.getObjectName() );
        metaverse.setObjectMetaJason( dto.getObjectMetaJason() );
        metaverse.setUrlLifespanInSeconds( dto.getUrlLifespanInSeconds() );
        metaverse.setUrlExpiredDate( dto.getUrlExpiredDate() );
        metaverse.setAutoRenewUrl( dto.getAutoRenewUrl() );
        metaverse.setIsEnabled( dto.getIsEnabled() );

        return metaverse;
    }

    @Override
    public MetaverseDTO toDto(Metaverse entity) {
        if ( entity == null ) {
            return null;
        }

        MetaverseDTO metaverseDTO = new MetaverseDTO();

        metaverseDTO.setId( entity.getId() );
        metaverseDTO.setFilename( entity.getFilename() );
        metaverseDTO.setContentType( entity.getContentType() );
        metaverseDTO.setFileExt( entity.getFileExt() );
        metaverseDTO.setFileSize( entity.getFileSize() );
        metaverseDTO.setFileUrl( entity.getFileUrl() );
        metaverseDTO.setThumbnailUrl( entity.getThumbnailUrl() );
        metaverseDTO.setBlurhash( entity.getBlurhash() );
        metaverseDTO.setObjectName( entity.getObjectName() );
        metaverseDTO.setObjectMetaJason( entity.getObjectMetaJason() );
        metaverseDTO.setUrlLifespanInSeconds( entity.getUrlLifespanInSeconds() );
        metaverseDTO.setUrlExpiredDate( entity.getUrlExpiredDate() );
        metaverseDTO.setAutoRenewUrl( entity.getAutoRenewUrl() );
        metaverseDTO.setIsEnabled( entity.getIsEnabled() );

        return metaverseDTO;
    }

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
    }
}
