package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.ReviewAlpha;

/**
 * Spring Data JPA repository for the ReviewAlpha entity.
 */
@Repository
public interface ReviewAlphaRepository extends JpaRepository<ReviewAlpha, Long>, JpaSpecificationExecutor<ReviewAlpha> {
    default Optional<ReviewAlpha> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ReviewAlpha> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ReviewAlpha> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select reviewAlpha from ReviewAlpha reviewAlpha left join fetch reviewAlpha.product",
        countQuery = "select count(reviewAlpha) from ReviewAlpha reviewAlpha"
    )
    Page<ReviewAlpha> findAllWithToOneRelationships(Pageable pageable);

    @Query("select reviewAlpha from ReviewAlpha reviewAlpha left join fetch reviewAlpha.product")
    List<ReviewAlpha> findAllWithToOneRelationships();

    @Query("select reviewAlpha from ReviewAlpha reviewAlpha left join fetch reviewAlpha.product where reviewAlpha.id =:id")
    Optional<ReviewAlpha> findOneWithToOneRelationships(@Param("id") Long id);
}
