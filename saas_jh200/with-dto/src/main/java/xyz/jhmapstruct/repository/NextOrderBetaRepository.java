package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextOrderBeta;

/**
 * Spring Data JPA repository for the NextOrderBeta entity.
 */
@Repository
public interface NextOrderBetaRepository extends JpaRepository<NextOrderBeta, Long>, JpaSpecificationExecutor<NextOrderBeta> {
    default Optional<NextOrderBeta> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<NextOrderBeta> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<NextOrderBeta> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select nextOrderBeta from NextOrderBeta nextOrderBeta left join fetch nextOrderBeta.customer",
        countQuery = "select count(nextOrderBeta) from NextOrderBeta nextOrderBeta"
    )
    Page<NextOrderBeta> findAllWithToOneRelationships(Pageable pageable);

    @Query("select nextOrderBeta from NextOrderBeta nextOrderBeta left join fetch nextOrderBeta.customer")
    List<NextOrderBeta> findAllWithToOneRelationships();

    @Query("select nextOrderBeta from NextOrderBeta nextOrderBeta left join fetch nextOrderBeta.customer where nextOrderBeta.id =:id")
    Optional<NextOrderBeta> findOneWithToOneRelationships(@Param("id") Long id);
}
