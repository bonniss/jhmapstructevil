package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextProductViVi;

/**
 * Spring Data JPA repository for the NextProductViVi entity.
 */
@Repository
public interface NextProductViViRepository extends JpaRepository<NextProductViVi, Long>, JpaSpecificationExecutor<NextProductViVi> {
    default Optional<NextProductViVi> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<NextProductViVi> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<NextProductViVi> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select nextProductViVi from NextProductViVi nextProductViVi left join fetch nextProductViVi.category",
        countQuery = "select count(nextProductViVi) from NextProductViVi nextProductViVi"
    )
    Page<NextProductViVi> findAllWithToOneRelationships(Pageable pageable);

    @Query("select nextProductViVi from NextProductViVi nextProductViVi left join fetch nextProductViVi.category")
    List<NextProductViVi> findAllWithToOneRelationships();

    @Query(
        "select nextProductViVi from NextProductViVi nextProductViVi left join fetch nextProductViVi.category where nextProductViVi.id =:id"
    )
    Optional<NextProductViVi> findOneWithToOneRelationships(@Param("id") Long id);
}
