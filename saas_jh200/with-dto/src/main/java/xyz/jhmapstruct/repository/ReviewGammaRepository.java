package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.ReviewGamma;

/**
 * Spring Data JPA repository for the ReviewGamma entity.
 */
@Repository
public interface ReviewGammaRepository extends JpaRepository<ReviewGamma, Long>, JpaSpecificationExecutor<ReviewGamma> {
    default Optional<ReviewGamma> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ReviewGamma> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ReviewGamma> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select reviewGamma from ReviewGamma reviewGamma left join fetch reviewGamma.product",
        countQuery = "select count(reviewGamma) from ReviewGamma reviewGamma"
    )
    Page<ReviewGamma> findAllWithToOneRelationships(Pageable pageable);

    @Query("select reviewGamma from ReviewGamma reviewGamma left join fetch reviewGamma.product")
    List<ReviewGamma> findAllWithToOneRelationships();

    @Query("select reviewGamma from ReviewGamma reviewGamma left join fetch reviewGamma.product where reviewGamma.id =:id")
    Optional<ReviewGamma> findOneWithToOneRelationships(@Param("id") Long id);
}
