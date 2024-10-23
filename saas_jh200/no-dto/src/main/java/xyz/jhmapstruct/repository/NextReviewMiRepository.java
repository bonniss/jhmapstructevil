package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextReviewMi;

/**
 * Spring Data JPA repository for the NextReviewMi entity.
 */
@Repository
public interface NextReviewMiRepository extends JpaRepository<NextReviewMi, Long>, JpaSpecificationExecutor<NextReviewMi> {
    default Optional<NextReviewMi> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<NextReviewMi> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<NextReviewMi> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select nextReviewMi from NextReviewMi nextReviewMi left join fetch nextReviewMi.product",
        countQuery = "select count(nextReviewMi) from NextReviewMi nextReviewMi"
    )
    Page<NextReviewMi> findAllWithToOneRelationships(Pageable pageable);

    @Query("select nextReviewMi from NextReviewMi nextReviewMi left join fetch nextReviewMi.product")
    List<NextReviewMi> findAllWithToOneRelationships();

    @Query("select nextReviewMi from NextReviewMi nextReviewMi left join fetch nextReviewMi.product where nextReviewMi.id =:id")
    Optional<NextReviewMi> findOneWithToOneRelationships(@Param("id") Long id);
}
