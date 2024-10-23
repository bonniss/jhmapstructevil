package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.OrderMiMi;

/**
 * Spring Data JPA repository for the OrderMiMi entity.
 */
@Repository
public interface OrderMiMiRepository extends JpaRepository<OrderMiMi, Long>, JpaSpecificationExecutor<OrderMiMi> {
    default Optional<OrderMiMi> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<OrderMiMi> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<OrderMiMi> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select orderMiMi from OrderMiMi orderMiMi left join fetch orderMiMi.customer",
        countQuery = "select count(orderMiMi) from OrderMiMi orderMiMi"
    )
    Page<OrderMiMi> findAllWithToOneRelationships(Pageable pageable);

    @Query("select orderMiMi from OrderMiMi orderMiMi left join fetch orderMiMi.customer")
    List<OrderMiMi> findAllWithToOneRelationships();

    @Query("select orderMiMi from OrderMiMi orderMiMi left join fetch orderMiMi.customer where orderMiMi.id =:id")
    Optional<OrderMiMi> findOneWithToOneRelationships(@Param("id") Long id);
}
