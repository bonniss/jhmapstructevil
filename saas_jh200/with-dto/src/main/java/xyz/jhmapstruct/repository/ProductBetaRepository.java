package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.ProductBeta;

/**
 * Spring Data JPA repository for the ProductBeta entity.
 */
@Repository
public interface ProductBetaRepository extends JpaRepository<ProductBeta, Long>, JpaSpecificationExecutor<ProductBeta> {
    default Optional<ProductBeta> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProductBeta> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProductBeta> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select productBeta from ProductBeta productBeta left join fetch productBeta.category",
        countQuery = "select count(productBeta) from ProductBeta productBeta"
    )
    Page<ProductBeta> findAllWithToOneRelationships(Pageable pageable);

    @Query("select productBeta from ProductBeta productBeta left join fetch productBeta.category")
    List<ProductBeta> findAllWithToOneRelationships();

    @Query("select productBeta from ProductBeta productBeta left join fetch productBeta.category where productBeta.id =:id")
    Optional<ProductBeta> findOneWithToOneRelationships(@Param("id") Long id);
}
