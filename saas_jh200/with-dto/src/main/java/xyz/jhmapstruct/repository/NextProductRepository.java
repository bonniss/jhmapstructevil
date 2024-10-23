package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextProduct;

/**
 * Spring Data JPA repository for the NextProduct entity.
 */
@Repository
public interface NextProductRepository extends JpaRepository<NextProduct, Long>, JpaSpecificationExecutor<NextProduct> {
    default Optional<NextProduct> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<NextProduct> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<NextProduct> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select nextProduct from NextProduct nextProduct left join fetch nextProduct.category",
        countQuery = "select count(nextProduct) from NextProduct nextProduct"
    )
    Page<NextProduct> findAllWithToOneRelationships(Pageable pageable);

    @Query("select nextProduct from NextProduct nextProduct left join fetch nextProduct.category")
    List<NextProduct> findAllWithToOneRelationships();

    @Query("select nextProduct from NextProduct nextProduct left join fetch nextProduct.category where nextProduct.id =:id")
    Optional<NextProduct> findOneWithToOneRelationships(@Param("id") Long id);
}
