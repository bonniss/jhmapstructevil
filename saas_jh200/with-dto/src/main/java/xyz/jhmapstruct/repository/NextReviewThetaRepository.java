package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextReviewTheta;

/**
 * Spring Data JPA repository for the NextReviewTheta entity.
 */
@Repository
public interface NextReviewThetaRepository extends JpaRepository<NextReviewTheta, Long>, JpaSpecificationExecutor<NextReviewTheta> {
    default Optional<NextReviewTheta> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<NextReviewTheta> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<NextReviewTheta> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select nextReviewTheta from NextReviewTheta nextReviewTheta left join fetch nextReviewTheta.product",
        countQuery = "select count(nextReviewTheta) from NextReviewTheta nextReviewTheta"
    )
    Page<NextReviewTheta> findAllWithToOneRelationships(Pageable pageable);

    @Query("select nextReviewTheta from NextReviewTheta nextReviewTheta left join fetch nextReviewTheta.product")
    List<NextReviewTheta> findAllWithToOneRelationships();

    @Query(
        "select nextReviewTheta from NextReviewTheta nextReviewTheta left join fetch nextReviewTheta.product where nextReviewTheta.id =:id"
    )
    Optional<NextReviewTheta> findOneWithToOneRelationships(@Param("id") Long id);
}
