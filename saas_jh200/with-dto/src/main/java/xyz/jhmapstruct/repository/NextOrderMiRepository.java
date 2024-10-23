package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextOrderMi;

/**
 * Spring Data JPA repository for the NextOrderMi entity.
 */
@Repository
public interface NextOrderMiRepository extends JpaRepository<NextOrderMi, Long>, JpaSpecificationExecutor<NextOrderMi> {
    default Optional<NextOrderMi> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<NextOrderMi> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<NextOrderMi> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select nextOrderMi from NextOrderMi nextOrderMi left join fetch nextOrderMi.customer",
        countQuery = "select count(nextOrderMi) from NextOrderMi nextOrderMi"
    )
    Page<NextOrderMi> findAllWithToOneRelationships(Pageable pageable);

    @Query("select nextOrderMi from NextOrderMi nextOrderMi left join fetch nextOrderMi.customer")
    List<NextOrderMi> findAllWithToOneRelationships();

    @Query("select nextOrderMi from NextOrderMi nextOrderMi left join fetch nextOrderMi.customer where nextOrderMi.id =:id")
    Optional<NextOrderMi> findOneWithToOneRelationships(@Param("id") Long id);
}
