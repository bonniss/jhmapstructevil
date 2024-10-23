package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextOrderMiMi;

/**
 * Spring Data JPA repository for the NextOrderMiMi entity.
 */
@Repository
public interface NextOrderMiMiRepository extends JpaRepository<NextOrderMiMi, Long>, JpaSpecificationExecutor<NextOrderMiMi> {
    default Optional<NextOrderMiMi> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<NextOrderMiMi> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<NextOrderMiMi> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select nextOrderMiMi from NextOrderMiMi nextOrderMiMi left join fetch nextOrderMiMi.customer",
        countQuery = "select count(nextOrderMiMi) from NextOrderMiMi nextOrderMiMi"
    )
    Page<NextOrderMiMi> findAllWithToOneRelationships(Pageable pageable);

    @Query("select nextOrderMiMi from NextOrderMiMi nextOrderMiMi left join fetch nextOrderMiMi.customer")
    List<NextOrderMiMi> findAllWithToOneRelationships();

    @Query("select nextOrderMiMi from NextOrderMiMi nextOrderMiMi left join fetch nextOrderMiMi.customer where nextOrderMiMi.id =:id")
    Optional<NextOrderMiMi> findOneWithToOneRelationships(@Param("id") Long id);
}
