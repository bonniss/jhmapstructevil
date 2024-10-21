package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.ReviewMiMi;

/**
 * Spring Data JPA repository for the ReviewMiMi entity.
 */
@Repository
public interface ReviewMiMiRepository extends JpaRepository<ReviewMiMi, Long> {
    default Optional<ReviewMiMi> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ReviewMiMi> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ReviewMiMi> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select reviewMiMi from ReviewMiMi reviewMiMi left join fetch reviewMiMi.product",
        countQuery = "select count(reviewMiMi) from ReviewMiMi reviewMiMi"
    )
    Page<ReviewMiMi> findAllWithToOneRelationships(Pageable pageable);

    @Query("select reviewMiMi from ReviewMiMi reviewMiMi left join fetch reviewMiMi.product")
    List<ReviewMiMi> findAllWithToOneRelationships();

    @Query("select reviewMiMi from ReviewMiMi reviewMiMi left join fetch reviewMiMi.product where reviewMiMi.id =:id")
    Optional<ReviewMiMi> findOneWithToOneRelationships(@Param("id") Long id);
}
