package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.ReviewVi;

/**
 * Spring Data JPA repository for the ReviewVi entity.
 */
@Repository
public interface ReviewViRepository extends JpaRepository<ReviewVi, Long> {
    default Optional<ReviewVi> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ReviewVi> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ReviewVi> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select reviewVi from ReviewVi reviewVi left join fetch reviewVi.product",
        countQuery = "select count(reviewVi) from ReviewVi reviewVi"
    )
    Page<ReviewVi> findAllWithToOneRelationships(Pageable pageable);

    @Query("select reviewVi from ReviewVi reviewVi left join fetch reviewVi.product")
    List<ReviewVi> findAllWithToOneRelationships();

    @Query("select reviewVi from ReviewVi reviewVi left join fetch reviewVi.product where reviewVi.id =:id")
    Optional<ReviewVi> findOneWithToOneRelationships(@Param("id") Long id);
}
