package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextReviewViVi;

/**
 * Spring Data JPA repository for the NextReviewViVi entity.
 */
@Repository
public interface NextReviewViViRepository extends JpaRepository<NextReviewViVi, Long>, JpaSpecificationExecutor<NextReviewViVi> {
    default Optional<NextReviewViVi> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<NextReviewViVi> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<NextReviewViVi> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select nextReviewViVi from NextReviewViVi nextReviewViVi left join fetch nextReviewViVi.product",
        countQuery = "select count(nextReviewViVi) from NextReviewViVi nextReviewViVi"
    )
    Page<NextReviewViVi> findAllWithToOneRelationships(Pageable pageable);

    @Query("select nextReviewViVi from NextReviewViVi nextReviewViVi left join fetch nextReviewViVi.product")
    List<NextReviewViVi> findAllWithToOneRelationships();

    @Query("select nextReviewViVi from NextReviewViVi nextReviewViVi left join fetch nextReviewViVi.product where nextReviewViVi.id =:id")
    Optional<NextReviewViVi> findOneWithToOneRelationships(@Param("id") Long id);
}
