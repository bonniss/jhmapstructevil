package ai.realworld.service.mapper;

import ai.realworld.domain.HashRoss;
import ai.realworld.service.dto.HashRossDTO;
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
public class HashRossMapperImpl implements HashRossMapper {

    @Override
    public HashRoss toEntity(HashRossDTO dto) {
        if ( dto == null ) {
            return null;
        }

        HashRoss hashRoss = new HashRoss();

        hashRoss.setId( dto.getId() );
        hashRoss.setName( dto.getName() );
        hashRoss.setSlug( dto.getSlug() );
        hashRoss.setDescription( dto.getDescription() );
        hashRoss.setPermissionGridJason( dto.getPermissionGridJason() );

        return hashRoss;
    }

    @Override
    public HashRossDTO toDto(HashRoss entity) {
        if ( entity == null ) {
            return null;
        }

        HashRossDTO hashRossDTO = new HashRossDTO();

        hashRossDTO.setId( entity.getId() );
        hashRossDTO.setName( entity.getName() );
        hashRossDTO.setSlug( entity.getSlug() );
        hashRossDTO.setDescription( entity.getDescription() );
        hashRossDTO.setPermissionGridJason( entity.getPermissionGridJason() );

        return hashRossDTO;
    }

    @Override
    public List<HashRoss> toEntity(List<HashRossDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<HashRoss> list = new ArrayList<HashRoss>( dtoList.size() );
        for ( HashRossDTO hashRossDTO : dtoList ) {
            list.add( toEntity( hashRossDTO ) );
        }

        return list;
    }

    @Override
    public List<HashRossDTO> toDto(List<HashRoss> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<HashRossDTO> list = new ArrayList<HashRossDTO>( entityList.size() );
        for ( HashRoss hashRoss : entityList ) {
            list.add( toDto( hashRoss ) );
        }

        return list;
    }

    @Override
    public void partialUpdate(HashRoss entity, HashRossDTO dto) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getName() != null ) {
            entity.setName( dto.getName() );
        }
        if ( dto.getSlug() != null ) {
            entity.setSlug( dto.getSlug() );
        }
        if ( dto.getDescription() != null ) {
            entity.setDescription( dto.getDescription() );
        }
        if ( dto.getPermissionGridJason() != null ) {
            entity.setPermissionGridJason( dto.getPermissionGridJason() );
        }
    }
}
