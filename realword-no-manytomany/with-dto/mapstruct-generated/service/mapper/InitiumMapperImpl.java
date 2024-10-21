package ai.realworld.service.mapper;

import ai.realworld.domain.Initium;
import ai.realworld.service.dto.InitiumDTO;
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
public class InitiumMapperImpl implements InitiumMapper {

    @Override
    public Initium toEntity(InitiumDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Initium initium = new Initium();

        initium.setId( dto.getId() );
        initium.setName( dto.getName() );
        initium.setSlug( dto.getSlug() );
        initium.setDescription( dto.getDescription() );
        initium.setIsJelloSupported( dto.getIsJelloSupported() );

        return initium;
    }

    @Override
    public InitiumDTO toDto(Initium entity) {
        if ( entity == null ) {
            return null;
        }

        InitiumDTO initiumDTO = new InitiumDTO();

        initiumDTO.setId( entity.getId() );
        initiumDTO.setName( entity.getName() );
        initiumDTO.setSlug( entity.getSlug() );
        initiumDTO.setDescription( entity.getDescription() );
        initiumDTO.setIsJelloSupported( entity.getIsJelloSupported() );

        return initiumDTO;
    }

    @Override
    public List<Initium> toEntity(List<InitiumDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Initium> list = new ArrayList<Initium>( dtoList.size() );
        for ( InitiumDTO initiumDTO : dtoList ) {
            list.add( toEntity( initiumDTO ) );
        }

        return list;
    }

    @Override
    public List<InitiumDTO> toDto(List<Initium> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<InitiumDTO> list = new ArrayList<InitiumDTO>( entityList.size() );
        for ( Initium initium : entityList ) {
            list.add( toDto( initium ) );
        }

        return list;
    }

    @Override
    public void partialUpdate(Initium entity, InitiumDTO dto) {
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
        if ( dto.getIsJelloSupported() != null ) {
            entity.setIsJelloSupported( dto.getIsJelloSupported() );
        }
    }
}
