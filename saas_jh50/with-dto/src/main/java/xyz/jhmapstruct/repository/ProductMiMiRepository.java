package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.ProductMiMi;

/**
 * Spring Data JPA repository for the ProductMiMi entity.
 */
@Repository
public interface ProductMiMiRepository extends JpaRepository<ProductMiMi, Long> {
    default Optional<ProductMiMi> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProductMiMi> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProductMiMi> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select productMiMi from ProductMiMi productMiMi left join fetch productMiMi.category",
        countQuery = "select count(productMiMi) from ProductMiMi productMiMi"
    )
    Page<ProductMiMi> findAllWithToOneRelationships(Pageable pageable);

    @Query("select productMiMi from ProductMiMi productMiMi left join fetch productMiMi.category")
    List<ProductMiMi> findAllWithToOneRelationships();

    @Query("select productMiMi from ProductMiMi productMiMi left join fetch productMiMi.category where productMiMi.id =:id")
    Optional<ProductMiMi> findOneWithToOneRelationships(@Param("id") Long id);
}
