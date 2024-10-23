package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.OrderTheta;

/**
 * Spring Data JPA repository for the OrderTheta entity.
 */
@Repository
public interface OrderThetaRepository extends JpaRepository<OrderTheta, Long>, JpaSpecificationExecutor<OrderTheta> {
    default Optional<OrderTheta> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<OrderTheta> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<OrderTheta> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select orderTheta from OrderTheta orderTheta left join fetch orderTheta.customer",
        countQuery = "select count(orderTheta) from OrderTheta orderTheta"
    )
    Page<OrderTheta> findAllWithToOneRelationships(Pageable pageable);

    @Query("select orderTheta from OrderTheta orderTheta left join fetch orderTheta.customer")
    List<OrderTheta> findAllWithToOneRelationships();

    @Query("select orderTheta from OrderTheta orderTheta left join fetch orderTheta.customer where orderTheta.id =:id")
    Optional<OrderTheta> findOneWithToOneRelationships(@Param("id") Long id);
}
