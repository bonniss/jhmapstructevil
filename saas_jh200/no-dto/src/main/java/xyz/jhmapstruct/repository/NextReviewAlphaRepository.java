package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextReviewAlpha;

/**
 * Spring Data JPA repository for the NextReviewAlpha entity.
 */
@Repository
public interface NextReviewAlphaRepository extends JpaRepository<NextReviewAlpha, Long>, JpaSpecificationExecutor<NextReviewAlpha> {
    default Optional<NextReviewAlpha> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<NextReviewAlpha> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<NextReviewAlpha> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select nextReviewAlpha from NextReviewAlpha nextReviewAlpha left join fetch nextReviewAlpha.product",
        countQuery = "select count(nextReviewAlpha) from NextReviewAlpha nextReviewAlpha"
    )
    Page<NextReviewAlpha> findAllWithToOneRelationships(Pageable pageable);

    @Query("select nextReviewAlpha from NextReviewAlpha nextReviewAlpha left join fetch nextReviewAlpha.product")
    List<NextReviewAlpha> findAllWithToOneRelationships();

    @Query(
        "select nextReviewAlpha from NextReviewAlpha nextReviewAlpha left join fetch nextReviewAlpha.product where nextReviewAlpha.id =:id"
    )
    Optional<NextReviewAlpha> findOneWithToOneRelationships(@Param("id") Long id);
}
