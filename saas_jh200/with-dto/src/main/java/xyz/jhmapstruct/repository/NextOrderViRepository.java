package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextOrderVi;

/**
 * Spring Data JPA repository for the NextOrderVi entity.
 */
@Repository
public interface NextOrderViRepository extends JpaRepository<NextOrderVi, Long>, JpaSpecificationExecutor<NextOrderVi> {
    default Optional<NextOrderVi> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<NextOrderVi> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<NextOrderVi> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select nextOrderVi from NextOrderVi nextOrderVi left join fetch nextOrderVi.customer",
        countQuery = "select count(nextOrderVi) from NextOrderVi nextOrderVi"
    )
    Page<NextOrderVi> findAllWithToOneRelationships(Pageable pageable);

    @Query("select nextOrderVi from NextOrderVi nextOrderVi left join fetch nextOrderVi.customer")
    List<NextOrderVi> findAllWithToOneRelationships();

    @Query("select nextOrderVi from NextOrderVi nextOrderVi left join fetch nextOrderVi.customer where nextOrderVi.id =:id")
    Optional<NextOrderVi> findOneWithToOneRelationships(@Param("id") Long id);
}
