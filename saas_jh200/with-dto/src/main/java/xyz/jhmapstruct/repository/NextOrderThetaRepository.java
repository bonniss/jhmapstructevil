package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextOrderTheta;

/**
 * Spring Data JPA repository for the NextOrderTheta entity.
 */
@Repository
public interface NextOrderThetaRepository extends JpaRepository<NextOrderTheta, Long>, JpaSpecificationExecutor<NextOrderTheta> {
    default Optional<NextOrderTheta> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<NextOrderTheta> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<NextOrderTheta> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select nextOrderTheta from NextOrderTheta nextOrderTheta left join fetch nextOrderTheta.customer",
        countQuery = "select count(nextOrderTheta) from NextOrderTheta nextOrderTheta"
    )
    Page<NextOrderTheta> findAllWithToOneRelationships(Pageable pageable);

    @Query("select nextOrderTheta from NextOrderTheta nextOrderTheta left join fetch nextOrderTheta.customer")
    List<NextOrderTheta> findAllWithToOneRelationships();

    @Query("select nextOrderTheta from NextOrderTheta nextOrderTheta left join fetch nextOrderTheta.customer where nextOrderTheta.id =:id")
    Optional<NextOrderTheta> findOneWithToOneRelationships(@Param("id") Long id);
}
