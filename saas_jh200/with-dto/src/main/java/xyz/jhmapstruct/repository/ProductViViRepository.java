package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.ProductViVi;

/**
 * Spring Data JPA repository for the ProductViVi entity.
 */
@Repository
public interface ProductViViRepository extends JpaRepository<ProductViVi, Long>, JpaSpecificationExecutor<ProductViVi> {
    default Optional<ProductViVi> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProductViVi> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProductViVi> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select productViVi from ProductViVi productViVi left join fetch productViVi.category",
        countQuery = "select count(productViVi) from ProductViVi productViVi"
    )
    Page<ProductViVi> findAllWithToOneRelationships(Pageable pageable);

    @Query("select productViVi from ProductViVi productViVi left join fetch productViVi.category")
    List<ProductViVi> findAllWithToOneRelationships();

    @Query("select productViVi from ProductViVi productViVi left join fetch productViVi.category where productViVi.id =:id")
    Optional<ProductViVi> findOneWithToOneRelationships(@Param("id") Long id);
}
