package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.OrderVi;

/**
 * Spring Data JPA repository for the OrderVi entity.
 */
@Repository
public interface OrderViRepository extends JpaRepository<OrderVi, Long> {
    default Optional<OrderVi> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<OrderVi> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<OrderVi> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select orderVi from OrderVi orderVi left join fetch orderVi.customer",
        countQuery = "select count(orderVi) from OrderVi orderVi"
    )
    Page<OrderVi> findAllWithToOneRelationships(Pageable pageable);

    @Query("select orderVi from OrderVi orderVi left join fetch orderVi.customer")
    List<OrderVi> findAllWithToOneRelationships();

    @Query("select orderVi from OrderVi orderVi left join fetch orderVi.customer where orderVi.id =:id")
    Optional<OrderVi> findOneWithToOneRelationships(@Param("id") Long id);
}
