package ai.realworld.repository;

import ai.realworld.domain.HexCharVi;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HexCharVi entity.
 */
@Repository
public interface HexCharViRepository extends JpaRepository<HexCharVi, Long>, JpaSpecificationExecutor<HexCharVi> {
    default Optional<HexCharVi> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<HexCharVi> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<HexCharVi> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select hexCharVi from HexCharVi hexCharVi left join fetch hexCharVi.internalUser",
        countQuery = "select count(hexCharVi) from HexCharVi hexCharVi"
    )
    Page<HexCharVi> findAllWithToOneRelationships(Pageable pageable);

    @Query("select hexCharVi from HexCharVi hexCharVi left join fetch hexCharVi.internalUser")
    List<HexCharVi> findAllWithToOneRelationships();

    @Query("select hexCharVi from HexCharVi hexCharVi left join fetch hexCharVi.internalUser where hexCharVi.id =:id")
    Optional<HexCharVi> findOneWithToOneRelationships(@Param("id") Long id);
}
