package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextOrderViVi;

/**
 * Spring Data JPA repository for the NextOrderViVi entity.
 */
@Repository
public interface NextOrderViViRepository extends JpaRepository<NextOrderViVi, Long>, JpaSpecificationExecutor<NextOrderViVi> {
    default Optional<NextOrderViVi> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<NextOrderViVi> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<NextOrderViVi> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select nextOrderViVi from NextOrderViVi nextOrderViVi left join fetch nextOrderViVi.customer",
        countQuery = "select count(nextOrderViVi) from NextOrderViVi nextOrderViVi"
    )
    Page<NextOrderViVi> findAllWithToOneRelationships(Pageable pageable);

    @Query("select nextOrderViVi from NextOrderViVi nextOrderViVi left join fetch nextOrderViVi.customer")
    List<NextOrderViVi> findAllWithToOneRelationships();

    @Query("select nextOrderViVi from NextOrderViVi nextOrderViVi left join fetch nextOrderViVi.customer where nextOrderViVi.id =:id")
    Optional<NextOrderViVi> findOneWithToOneRelationships(@Param("id") Long id);
}
