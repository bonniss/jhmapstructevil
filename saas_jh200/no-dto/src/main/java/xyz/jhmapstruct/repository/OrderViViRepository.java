package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.OrderViVi;

/**
 * Spring Data JPA repository for the OrderViVi entity.
 */
@Repository
public interface OrderViViRepository extends JpaRepository<OrderViVi, Long>, JpaSpecificationExecutor<OrderViVi> {
    default Optional<OrderViVi> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<OrderViVi> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<OrderViVi> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select orderViVi from OrderViVi orderViVi left join fetch orderViVi.customer",
        countQuery = "select count(orderViVi) from OrderViVi orderViVi"
    )
    Page<OrderViVi> findAllWithToOneRelationships(Pageable pageable);

    @Query("select orderViVi from OrderViVi orderViVi left join fetch orderViVi.customer")
    List<OrderViVi> findAllWithToOneRelationships();

    @Query("select orderViVi from OrderViVi orderViVi left join fetch orderViVi.customer where orderViVi.id =:id")
    Optional<OrderViVi> findOneWithToOneRelationships(@Param("id") Long id);
}
