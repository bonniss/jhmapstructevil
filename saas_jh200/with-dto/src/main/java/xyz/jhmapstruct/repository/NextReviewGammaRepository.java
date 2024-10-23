package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextReviewGamma;

/**
 * Spring Data JPA repository for the NextReviewGamma entity.
 */
@Repository
public interface NextReviewGammaRepository extends JpaRepository<NextReviewGamma, Long>, JpaSpecificationExecutor<NextReviewGamma> {
    default Optional<NextReviewGamma> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<NextReviewGamma> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<NextReviewGamma> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select nextReviewGamma from NextReviewGamma nextReviewGamma left join fetch nextReviewGamma.product",
        countQuery = "select count(nextReviewGamma) from NextReviewGamma nextReviewGamma"
    )
    Page<NextReviewGamma> findAllWithToOneRelationships(Pageable pageable);

    @Query("select nextReviewGamma from NextReviewGamma nextReviewGamma left join fetch nextReviewGamma.product")
    List<NextReviewGamma> findAllWithToOneRelationships();

    @Query(
        "select nextReviewGamma from NextReviewGamma nextReviewGamma left join fetch nextReviewGamma.product where nextReviewGamma.id =:id"
    )
    Optional<NextReviewGamma> findOneWithToOneRelationships(@Param("id") Long id);
}
