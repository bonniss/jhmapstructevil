package ai.realworld.repository;

import ai.realworld.domain.HexChar;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HexChar entity.
 */
@Repository
public interface HexCharRepository extends JpaRepository<HexChar, Long>, JpaSpecificationExecutor<HexChar> {
    default Optional<HexChar> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<HexChar> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<HexChar> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select hexChar from HexChar hexChar left join fetch hexChar.internalUser",
        countQuery = "select count(hexChar) from HexChar hexChar"
    )
    Page<HexChar> findAllWithToOneRelationships(Pageable pageable);

    @Query("select hexChar from HexChar hexChar left join fetch hexChar.internalUser")
    List<HexChar> findAllWithToOneRelationships();

    @Query("select hexChar from HexChar hexChar left join fetch hexChar.internalUser where hexChar.id =:id")
    Optional<HexChar> findOneWithToOneRelationships(@Param("id") Long id);
}
