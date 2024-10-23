package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.ReviewViVi;

/**
 * Spring Data JPA repository for the ReviewViVi entity.
 */
@Repository
public interface ReviewViViRepository extends JpaRepository<ReviewViVi, Long>, JpaSpecificationExecutor<ReviewViVi> {
    default Optional<ReviewViVi> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ReviewViVi> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ReviewViVi> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select reviewViVi from ReviewViVi reviewViVi left join fetch reviewViVi.product",
        countQuery = "select count(reviewViVi) from ReviewViVi reviewViVi"
    )
    Page<ReviewViVi> findAllWithToOneRelationships(Pageable pageable);

    @Query("select reviewViVi from ReviewViVi reviewViVi left join fetch reviewViVi.product")
    List<ReviewViVi> findAllWithToOneRelationships();

    @Query("select reviewViVi from ReviewViVi reviewViVi left join fetch reviewViVi.product where reviewViVi.id =:id")
    Optional<ReviewViVi> findOneWithToOneRelationships(@Param("id") Long id);
}
