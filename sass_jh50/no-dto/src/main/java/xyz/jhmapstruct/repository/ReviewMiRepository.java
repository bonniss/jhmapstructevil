package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.ReviewMi;

/**
 * Spring Data JPA repository for the ReviewMi entity.
 */
@Repository
public interface ReviewMiRepository extends JpaRepository<ReviewMi, Long> {
    default Optional<ReviewMi> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ReviewMi> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ReviewMi> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select reviewMi from ReviewMi reviewMi left join fetch reviewMi.product",
        countQuery = "select count(reviewMi) from ReviewMi reviewMi"
    )
    Page<ReviewMi> findAllWithToOneRelationships(Pageable pageable);

    @Query("select reviewMi from ReviewMi reviewMi left join fetch reviewMi.product")
    List<ReviewMi> findAllWithToOneRelationships();

    @Query("select reviewMi from ReviewMi reviewMi left join fetch reviewMi.product where reviewMi.id =:id")
    Optional<ReviewMi> findOneWithToOneRelationships(@Param("id") Long id);
}
