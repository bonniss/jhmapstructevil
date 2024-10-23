package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.ProductSigma;

/**
 * Spring Data JPA repository for the ProductSigma entity.
 */
@Repository
public interface ProductSigmaRepository extends JpaRepository<ProductSigma, Long>, JpaSpecificationExecutor<ProductSigma> {
    default Optional<ProductSigma> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProductSigma> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProductSigma> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select productSigma from ProductSigma productSigma left join fetch productSigma.category",
        countQuery = "select count(productSigma) from ProductSigma productSigma"
    )
    Page<ProductSigma> findAllWithToOneRelationships(Pageable pageable);

    @Query("select productSigma from ProductSigma productSigma left join fetch productSigma.category")
    List<ProductSigma> findAllWithToOneRelationships();

    @Query("select productSigma from ProductSigma productSigma left join fetch productSigma.category where productSigma.id =:id")
    Optional<ProductSigma> findOneWithToOneRelationships(@Param("id") Long id);
}
