package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.OrderSigma;

/**
 * Spring Data JPA repository for the OrderSigma entity.
 */
@Repository
public interface OrderSigmaRepository extends JpaRepository<OrderSigma, Long>, JpaSpecificationExecutor<OrderSigma> {
    default Optional<OrderSigma> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<OrderSigma> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<OrderSigma> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select orderSigma from OrderSigma orderSigma left join fetch orderSigma.customer",
        countQuery = "select count(orderSigma) from OrderSigma orderSigma"
    )
    Page<OrderSigma> findAllWithToOneRelationships(Pageable pageable);

    @Query("select orderSigma from OrderSigma orderSigma left join fetch orderSigma.customer")
    List<OrderSigma> findAllWithToOneRelationships();

    @Query("select orderSigma from OrderSigma orderSigma left join fetch orderSigma.customer where orderSigma.id =:id")
    Optional<OrderSigma> findOneWithToOneRelationships(@Param("id") Long id);
}
