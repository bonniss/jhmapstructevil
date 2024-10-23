package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextReviewSigma;

/**
 * Spring Data JPA repository for the NextReviewSigma entity.
 */
@Repository
public interface NextReviewSigmaRepository extends JpaRepository<NextReviewSigma, Long>, JpaSpecificationExecutor<NextReviewSigma> {
    default Optional<NextReviewSigma> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<NextReviewSigma> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<NextReviewSigma> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select nextReviewSigma from NextReviewSigma nextReviewSigma left join fetch nextReviewSigma.product",
        countQuery = "select count(nextReviewSigma) from NextReviewSigma nextReviewSigma"
    )
    Page<NextReviewSigma> findAllWithToOneRelationships(Pageable pageable);

    @Query("select nextReviewSigma from NextReviewSigma nextReviewSigma left join fetch nextReviewSigma.product")
    List<NextReviewSigma> findAllWithToOneRelationships();

    @Query(
        "select nextReviewSigma from NextReviewSigma nextReviewSigma left join fetch nextReviewSigma.product where nextReviewSigma.id =:id"
    )
    Optional<NextReviewSigma> findOneWithToOneRelationships(@Param("id") Long id);
}
