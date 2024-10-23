package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextReviewBeta;

/**
 * Spring Data JPA repository for the NextReviewBeta entity.
 */
@Repository
public interface NextReviewBetaRepository extends JpaRepository<NextReviewBeta, Long>, JpaSpecificationExecutor<NextReviewBeta> {
    default Optional<NextReviewBeta> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<NextReviewBeta> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<NextReviewBeta> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select nextReviewBeta from NextReviewBeta nextReviewBeta left join fetch nextReviewBeta.product",
        countQuery = "select count(nextReviewBeta) from NextReviewBeta nextReviewBeta"
    )
    Page<NextReviewBeta> findAllWithToOneRelationships(Pageable pageable);

    @Query("select nextReviewBeta from NextReviewBeta nextReviewBeta left join fetch nextReviewBeta.product")
    List<NextReviewBeta> findAllWithToOneRelationships();

    @Query("select nextReviewBeta from NextReviewBeta nextReviewBeta left join fetch nextReviewBeta.product where nextReviewBeta.id =:id")
    Optional<NextReviewBeta> findOneWithToOneRelationships(@Param("id") Long id);
}
