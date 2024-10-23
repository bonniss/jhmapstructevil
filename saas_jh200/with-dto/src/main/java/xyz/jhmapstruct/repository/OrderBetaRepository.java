package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.OrderBeta;

/**
 * Spring Data JPA repository for the OrderBeta entity.
 */
@Repository
public interface OrderBetaRepository extends JpaRepository<OrderBeta, Long>, JpaSpecificationExecutor<OrderBeta> {
    default Optional<OrderBeta> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<OrderBeta> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<OrderBeta> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select orderBeta from OrderBeta orderBeta left join fetch orderBeta.customer",
        countQuery = "select count(orderBeta) from OrderBeta orderBeta"
    )
    Page<OrderBeta> findAllWithToOneRelationships(Pageable pageable);

    @Query("select orderBeta from OrderBeta orderBeta left join fetch orderBeta.customer")
    List<OrderBeta> findAllWithToOneRelationships();

    @Query("select orderBeta from OrderBeta orderBeta left join fetch orderBeta.customer where orderBeta.id =:id")
    Optional<OrderBeta> findOneWithToOneRelationships(@Param("id") Long id);
}
