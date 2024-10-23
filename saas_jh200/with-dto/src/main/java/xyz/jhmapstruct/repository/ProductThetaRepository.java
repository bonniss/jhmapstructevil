package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.ProductTheta;

/**
 * Spring Data JPA repository for the ProductTheta entity.
 */
@Repository
public interface ProductThetaRepository extends JpaRepository<ProductTheta, Long>, JpaSpecificationExecutor<ProductTheta> {
    default Optional<ProductTheta> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProductTheta> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProductTheta> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select productTheta from ProductTheta productTheta left join fetch productTheta.category",
        countQuery = "select count(productTheta) from ProductTheta productTheta"
    )
    Page<ProductTheta> findAllWithToOneRelationships(Pageable pageable);

    @Query("select productTheta from ProductTheta productTheta left join fetch productTheta.category")
    List<ProductTheta> findAllWithToOneRelationships();

    @Query("select productTheta from ProductTheta productTheta left join fetch productTheta.category where productTheta.id =:id")
    Optional<ProductTheta> findOneWithToOneRelationships(@Param("id") Long id);
}
