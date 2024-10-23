package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextOrderGamma;

/**
 * Spring Data JPA repository for the NextOrderGamma entity.
 */
@Repository
public interface NextOrderGammaRepository extends JpaRepository<NextOrderGamma, Long>, JpaSpecificationExecutor<NextOrderGamma> {
    default Optional<NextOrderGamma> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<NextOrderGamma> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<NextOrderGamma> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select nextOrderGamma from NextOrderGamma nextOrderGamma left join fetch nextOrderGamma.customer",
        countQuery = "select count(nextOrderGamma) from NextOrderGamma nextOrderGamma"
    )
    Page<NextOrderGamma> findAllWithToOneRelationships(Pageable pageable);

    @Query("select nextOrderGamma from NextOrderGamma nextOrderGamma left join fetch nextOrderGamma.customer")
    List<NextOrderGamma> findAllWithToOneRelationships();

    @Query("select nextOrderGamma from NextOrderGamma nextOrderGamma left join fetch nextOrderGamma.customer where nextOrderGamma.id =:id")
    Optional<NextOrderGamma> findOneWithToOneRelationships(@Param("id") Long id);
}
