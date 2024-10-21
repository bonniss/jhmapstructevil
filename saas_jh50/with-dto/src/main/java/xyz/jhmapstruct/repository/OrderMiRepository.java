package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.OrderMi;

/**
 * Spring Data JPA repository for the OrderMi entity.
 */
@Repository
public interface OrderMiRepository extends JpaRepository<OrderMi, Long> {
    default Optional<OrderMi> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<OrderMi> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<OrderMi> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select orderMi from OrderMi orderMi left join fetch orderMi.customer",
        countQuery = "select count(orderMi) from OrderMi orderMi"
    )
    Page<OrderMi> findAllWithToOneRelationships(Pageable pageable);

    @Query("select orderMi from OrderMi orderMi left join fetch orderMi.customer")
    List<OrderMi> findAllWithToOneRelationships();

    @Query("select orderMi from OrderMi orderMi left join fetch orderMi.customer where orderMi.id =:id")
    Optional<OrderMi> findOneWithToOneRelationships(@Param("id") Long id);
}
