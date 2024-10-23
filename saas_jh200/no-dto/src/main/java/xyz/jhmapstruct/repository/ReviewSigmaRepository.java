package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.ReviewSigma;

/**
 * Spring Data JPA repository for the ReviewSigma entity.
 */
@Repository
public interface ReviewSigmaRepository extends JpaRepository<ReviewSigma, Long>, JpaSpecificationExecutor<ReviewSigma> {
    default Optional<ReviewSigma> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ReviewSigma> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ReviewSigma> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select reviewSigma from ReviewSigma reviewSigma left join fetch reviewSigma.product",
        countQuery = "select count(reviewSigma) from ReviewSigma reviewSigma"
    )
    Page<ReviewSigma> findAllWithToOneRelationships(Pageable pageable);

    @Query("select reviewSigma from ReviewSigma reviewSigma left join fetch reviewSigma.product")
    List<ReviewSigma> findAllWithToOneRelationships();

    @Query("select reviewSigma from ReviewSigma reviewSigma left join fetch reviewSigma.product where reviewSigma.id =:id")
    Optional<ReviewSigma> findOneWithToOneRelationships(@Param("id") Long id);
}
