package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.ReviewTheta;

/**
 * Spring Data JPA repository for the ReviewTheta entity.
 */
@Repository
public interface ReviewThetaRepository extends JpaRepository<ReviewTheta, Long>, JpaSpecificationExecutor<ReviewTheta> {
    default Optional<ReviewTheta> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ReviewTheta> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ReviewTheta> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select reviewTheta from ReviewTheta reviewTheta left join fetch reviewTheta.product",
        countQuery = "select count(reviewTheta) from ReviewTheta reviewTheta"
    )
    Page<ReviewTheta> findAllWithToOneRelationships(Pageable pageable);

    @Query("select reviewTheta from ReviewTheta reviewTheta left join fetch reviewTheta.product")
    List<ReviewTheta> findAllWithToOneRelationships();

    @Query("select reviewTheta from ReviewTheta reviewTheta left join fetch reviewTheta.product where reviewTheta.id =:id")
    Optional<ReviewTheta> findOneWithToOneRelationships(@Param("id") Long id);
}
