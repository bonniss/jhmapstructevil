package ai.realworld.service.mapper;

import ai.realworld.domain.HashRoss;
import ai.realworld.domain.HexChar;
import ai.realworld.domain.User;
import ai.realworld.service.dto.HashRossDTO;
import ai.realworld.service.dto.HexCharDTO;
import ai.realworld.service.dto.UserDTO;
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
public class HexCharMapperImpl implements HexCharMapper {

    @Override
    public HexChar toEntity(HexCharDTO dto) {
        if ( dto == null ) {
            return null;
        }

        HexChar hexChar = new HexChar();

        hexChar.setId( dto.getId() );
        hexChar.setDob( dto.getDob() );
        hexChar.setGender( dto.getGender() );
        hexChar.setPhone( dto.getPhone() );
        hexChar.setBioHeitiga( dto.getBioHeitiga() );
        hexChar.setIsEnabled( dto.getIsEnabled() );
        hexChar.internalUser( userDTOToUser( dto.getInternalUser() ) );
        hexChar.role( hashRossDTOToHashRoss( dto.getRole() ) );

        return hexChar;
    }

    @Override
    public List<HexChar> toEntity(List<HexCharDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<HexChar> list = new ArrayList<HexChar>( dtoList.size() );
        for ( HexCharDTO hexCharDTO : dtoList ) {
            list.add( toEntity( hexCharDTO ) );
        }

        return list;
    }

    @Override
    public List<HexCharDTO> toDto(List<HexChar> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<HexCharDTO> list = new ArrayList<HexCharDTO>( entityList.size() );
        for ( HexChar hexChar : entityList ) {
            list.add( toDto( hexChar ) );
        }

        return list;
    }

    @Override
    public void partialUpdate(HexChar entity, HexCharDTO dto) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getDob() != null ) {
            entity.setDob( dto.getDob() );
        }
        if ( dto.getGender() != null ) {
            entity.setGender( dto.getGender() );
        }
        if ( dto.getPhone() != null ) {
            entity.setPhone( dto.getPhone() );
        }
        if ( dto.getBioHeitiga() != null ) {
            entity.setBioHeitiga( dto.getBioHeitiga() );
        }
        if ( dto.getIsEnabled() != null ) {
            entity.setIsEnabled( dto.getIsEnabled() );
        }
        if ( dto.getInternalUser() != null ) {
            if ( entity.getInternalUser() == null ) {
                entity.internalUser( new User() );
            }
            userDTOToUser1( dto.getInternalUser(), entity.getInternalUser() );
        }
        if ( dto.getRole() != null ) {
            if ( entity.getRole() == null ) {
                entity.role( new HashRoss() );
            }
            hashRossDTOToHashRoss1( dto.getRole(), entity.getRole() );
        }
    }

    @Override
    public HexCharDTO toDto(HexChar s) {
        if ( s == null ) {
            return null;
        }

        HexCharDTO hexCharDTO = new HexCharDTO();

        hexCharDTO.setInternalUser( toDtoUserLogin( s.getInternalUser() ) );
        hexCharDTO.setRole( toDtoHashRossId( s.getRole() ) );
        hexCharDTO.setId( s.getId() );
        hexCharDTO.setDob( s.getDob() );
        hexCharDTO.setGender( s.getGender() );
        hexCharDTO.setPhone( s.getPhone() );
        hexCharDTO.setBioHeitiga( s.getBioHeitiga() );
        hexCharDTO.setIsEnabled( s.getIsEnabled() );

        return hexCharDTO;
    }

    @Override
    public UserDTO toDtoUserLogin(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId( user.getId() );
        userDTO.setLogin( user.getLogin() );

        return userDTO;
    }

    @Override
    public HashRossDTO toDtoHashRossId(HashRoss hashRoss) {
        if ( hashRoss == null ) {
            return null;
        }

        HashRossDTO hashRossDTO = new HashRossDTO();

        hashRossDTO.setId( hashRoss.getId() );

        return hashRossDTO;
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

    protected HashRoss hashRossDTOToHashRoss(HashRossDTO hashRossDTO) {
        if ( hashRossDTO == null ) {
            return null;
        }

        HashRoss hashRoss = new HashRoss();

        hashRoss.setId( hashRossDTO.getId() );
        hashRoss.setName( hashRossDTO.getName() );
        hashRoss.setSlug( hashRossDTO.getSlug() );
        hashRoss.setDescription( hashRossDTO.getDescription() );
        hashRoss.setPermissionGridJason( hashRossDTO.getPermissionGridJason() );

        return hashRoss;
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

    protected void hashRossDTOToHashRoss1(HashRossDTO hashRossDTO, HashRoss mappingTarget) {
        if ( hashRossDTO == null ) {
            return;
        }

        if ( hashRossDTO.getId() != null ) {
            mappingTarget.setId( hashRossDTO.getId() );
        }
        if ( hashRossDTO.getName() != null ) {
            mappingTarget.setName( hashRossDTO.getName() );
        }
        if ( hashRossDTO.getSlug() != null ) {
            mappingTarget.setSlug( hashRossDTO.getSlug() );
        }
        if ( hashRossDTO.getDescription() != null ) {
            mappingTarget.setDescription( hashRossDTO.getDescription() );
        }
        if ( hashRossDTO.getPermissionGridJason() != null ) {
            mappingTarget.setPermissionGridJason( hashRossDTO.getPermissionGridJason() );
        }
    }
}
