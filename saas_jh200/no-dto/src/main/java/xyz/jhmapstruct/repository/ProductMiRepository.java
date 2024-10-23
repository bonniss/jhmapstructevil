package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.ProductMi;

/**
 * Spring Data JPA repository for the ProductMi entity.
 */
@Repository
public interface ProductMiRepository extends JpaRepository<ProductMi, Long>, JpaSpecificationExecutor<ProductMi> {
    default Optional<ProductMi> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProductMi> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProductMi> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select productMi from ProductMi productMi left join fetch productMi.category",
        countQuery = "select count(productMi) from ProductMi productMi"
    )
    Page<ProductMi> findAllWithToOneRelationships(Pageable pageable);

    @Query("select productMi from ProductMi productMi left join fetch productMi.category")
    List<ProductMi> findAllWithToOneRelationships();

    @Query("select productMi from ProductMi productMi left join fetch productMi.category where productMi.id =:id")
    Optional<ProductMi> findOneWithToOneRelationships(@Param("id") Long id);
}
