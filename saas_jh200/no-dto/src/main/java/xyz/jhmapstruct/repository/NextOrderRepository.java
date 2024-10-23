package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextOrder;

/**
 * Spring Data JPA repository for the NextOrder entity.
 */
@Repository
public interface NextOrderRepository extends JpaRepository<NextOrder, Long>, JpaSpecificationExecutor<NextOrder> {
    default Optional<NextOrder> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<NextOrder> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<NextOrder> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select nextOrder from NextOrder nextOrder left join fetch nextOrder.customer",
        countQuery = "select count(nextOrder) from NextOrder nextOrder"
    )
    Page<NextOrder> findAllWithToOneRelationships(Pageable pageable);

    @Query("select nextOrder from NextOrder nextOrder left join fetch nextOrder.customer")
    List<NextOrder> findAllWithToOneRelationships();

    @Query("select nextOrder from NextOrder nextOrder left join fetch nextOrder.customer where nextOrder.id =:id")
    Optional<NextOrder> findOneWithToOneRelationships(@Param("id") Long id);
}
