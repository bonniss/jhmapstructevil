package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextOrderSigma;

/**
 * Spring Data JPA repository for the NextOrderSigma entity.
 */
@Repository
public interface NextOrderSigmaRepository extends JpaRepository<NextOrderSigma, Long>, JpaSpecificationExecutor<NextOrderSigma> {
    default Optional<NextOrderSigma> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<NextOrderSigma> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<NextOrderSigma> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select nextOrderSigma from NextOrderSigma nextOrderSigma left join fetch nextOrderSigma.customer",
        countQuery = "select count(nextOrderSigma) from NextOrderSigma nextOrderSigma"
    )
    Page<NextOrderSigma> findAllWithToOneRelationships(Pageable pageable);

    @Query("select nextOrderSigma from NextOrderSigma nextOrderSigma left join fetch nextOrderSigma.customer")
    List<NextOrderSigma> findAllWithToOneRelationships();

    @Query("select nextOrderSigma from NextOrderSigma nextOrderSigma left join fetch nextOrderSigma.customer where nextOrderSigma.id =:id")
    Optional<NextOrderSigma> findOneWithToOneRelationships(@Param("id") Long id);
}
