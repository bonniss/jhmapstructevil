package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.OrderGamma;

/**
 * Spring Data JPA repository for the OrderGamma entity.
 */
@Repository
public interface OrderGammaRepository extends JpaRepository<OrderGamma, Long>, JpaSpecificationExecutor<OrderGamma> {
    default Optional<OrderGamma> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<OrderGamma> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<OrderGamma> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select orderGamma from OrderGamma orderGamma left join fetch orderGamma.customer",
        countQuery = "select count(orderGamma) from OrderGamma orderGamma"
    )
    Page<OrderGamma> findAllWithToOneRelationships(Pageable pageable);

    @Query("select orderGamma from OrderGamma orderGamma left join fetch orderGamma.customer")
    List<OrderGamma> findAllWithToOneRelationships();

    @Query("select orderGamma from OrderGamma orderGamma left join fetch orderGamma.customer where orderGamma.id =:id")
    Optional<OrderGamma> findOneWithToOneRelationships(@Param("id") Long id);
}
