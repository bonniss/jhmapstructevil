package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.ReviewBeta;

/**
 * Spring Data JPA repository for the ReviewBeta entity.
 */
@Repository
public interface ReviewBetaRepository extends JpaRepository<ReviewBeta, Long>, JpaSpecificationExecutor<ReviewBeta> {
    default Optional<ReviewBeta> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ReviewBeta> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ReviewBeta> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select reviewBeta from ReviewBeta reviewBeta left join fetch reviewBeta.product",
        countQuery = "select count(reviewBeta) from ReviewBeta reviewBeta"
    )
    Page<ReviewBeta> findAllWithToOneRelationships(Pageable pageable);

    @Query("select reviewBeta from ReviewBeta reviewBeta left join fetch reviewBeta.product")
    List<ReviewBeta> findAllWithToOneRelationships();

    @Query("select reviewBeta from ReviewBeta reviewBeta left join fetch reviewBeta.product where reviewBeta.id =:id")
    Optional<ReviewBeta> findOneWithToOneRelationships(@Param("id") Long id);
}
