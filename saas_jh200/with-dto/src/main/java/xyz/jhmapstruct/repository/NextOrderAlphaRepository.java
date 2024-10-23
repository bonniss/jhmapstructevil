package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextOrderAlpha;

/**
 * Spring Data JPA repository for the NextOrderAlpha entity.
 */
@Repository
public interface NextOrderAlphaRepository extends JpaRepository<NextOrderAlpha, Long>, JpaSpecificationExecutor<NextOrderAlpha> {
    default Optional<NextOrderAlpha> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<NextOrderAlpha> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<NextOrderAlpha> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select nextOrderAlpha from NextOrderAlpha nextOrderAlpha left join fetch nextOrderAlpha.customer",
        countQuery = "select count(nextOrderAlpha) from NextOrderAlpha nextOrderAlpha"
    )
    Page<NextOrderAlpha> findAllWithToOneRelationships(Pageable pageable);

    @Query("select nextOrderAlpha from NextOrderAlpha nextOrderAlpha left join fetch nextOrderAlpha.customer")
    List<NextOrderAlpha> findAllWithToOneRelationships();

    @Query("select nextOrderAlpha from NextOrderAlpha nextOrderAlpha left join fetch nextOrderAlpha.customer where nextOrderAlpha.id =:id")
    Optional<NextOrderAlpha> findOneWithToOneRelationships(@Param("id") Long id);
}
