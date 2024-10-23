package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.ProductGamma;

/**
 * Spring Data JPA repository for the ProductGamma entity.
 */
@Repository
public interface ProductGammaRepository extends JpaRepository<ProductGamma, Long>, JpaSpecificationExecutor<ProductGamma> {
    default Optional<ProductGamma> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProductGamma> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProductGamma> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select productGamma from ProductGamma productGamma left join fetch productGamma.category",
        countQuery = "select count(productGamma) from ProductGamma productGamma"
    )
    Page<ProductGamma> findAllWithToOneRelationships(Pageable pageable);

    @Query("select productGamma from ProductGamma productGamma left join fetch productGamma.category")
    List<ProductGamma> findAllWithToOneRelationships();

    @Query("select productGamma from ProductGamma productGamma left join fetch productGamma.category where productGamma.id =:id")
    Optional<ProductGamma> findOneWithToOneRelationships(@Param("id") Long id);
}
