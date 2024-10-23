package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextReviewMiMi;

/**
 * Spring Data JPA repository for the NextReviewMiMi entity.
 */
@Repository
public interface NextReviewMiMiRepository extends JpaRepository<NextReviewMiMi, Long>, JpaSpecificationExecutor<NextReviewMiMi> {
    default Optional<NextReviewMiMi> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<NextReviewMiMi> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<NextReviewMiMi> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select nextReviewMiMi from NextReviewMiMi nextReviewMiMi left join fetch nextReviewMiMi.product",
        countQuery = "select count(nextReviewMiMi) from NextReviewMiMi nextReviewMiMi"
    )
    Page<NextReviewMiMi> findAllWithToOneRelationships(Pageable pageable);

    @Query("select nextReviewMiMi from NextReviewMiMi nextReviewMiMi left join fetch nextReviewMiMi.product")
    List<NextReviewMiMi> findAllWithToOneRelationships();

    @Query("select nextReviewMiMi from NextReviewMiMi nextReviewMiMi left join fetch nextReviewMiMi.product where nextReviewMiMi.id =:id")
    Optional<NextReviewMiMi> findOneWithToOneRelationships(@Param("id") Long id);
}
