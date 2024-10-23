package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextReviewVi;

/**
 * Spring Data JPA repository for the NextReviewVi entity.
 */
@Repository
public interface NextReviewViRepository extends JpaRepository<NextReviewVi, Long>, JpaSpecificationExecutor<NextReviewVi> {
    default Optional<NextReviewVi> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<NextReviewVi> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<NextReviewVi> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select nextReviewVi from NextReviewVi nextReviewVi left join fetch nextReviewVi.product",
        countQuery = "select count(nextReviewVi) from NextReviewVi nextReviewVi"
    )
    Page<NextReviewVi> findAllWithToOneRelationships(Pageable pageable);

    @Query("select nextReviewVi from NextReviewVi nextReviewVi left join fetch nextReviewVi.product")
    List<NextReviewVi> findAllWithToOneRelationships();

    @Query("select nextReviewVi from NextReviewVi nextReviewVi left join fetch nextReviewVi.product where nextReviewVi.id =:id")
    Optional<NextReviewVi> findOneWithToOneRelationships(@Param("id") Long id);
}
