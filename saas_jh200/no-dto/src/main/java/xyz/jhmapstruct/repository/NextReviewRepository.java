package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextReview;

/**
 * Spring Data JPA repository for the NextReview entity.
 */
@Repository
public interface NextReviewRepository extends JpaRepository<NextReview, Long>, JpaSpecificationExecutor<NextReview> {
    default Optional<NextReview> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<NextReview> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<NextReview> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select nextReview from NextReview nextReview left join fetch nextReview.product",
        countQuery = "select count(nextReview) from NextReview nextReview"
    )
    Page<NextReview> findAllWithToOneRelationships(Pageable pageable);

    @Query("select nextReview from NextReview nextReview left join fetch nextReview.product")
    List<NextReview> findAllWithToOneRelationships();

    @Query("select nextReview from NextReview nextReview left join fetch nextReview.product where nextReview.id =:id")
    Optional<NextReview> findOneWithToOneRelationships(@Param("id") Long id);
}
