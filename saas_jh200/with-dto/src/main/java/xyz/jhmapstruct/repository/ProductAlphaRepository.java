package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.ProductAlpha;

/**
 * Spring Data JPA repository for the ProductAlpha entity.
 */
@Repository
public interface ProductAlphaRepository extends JpaRepository<ProductAlpha, Long>, JpaSpecificationExecutor<ProductAlpha> {
    default Optional<ProductAlpha> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProductAlpha> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProductAlpha> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select productAlpha from ProductAlpha productAlpha left join fetch productAlpha.category",
        countQuery = "select count(productAlpha) from ProductAlpha productAlpha"
    )
    Page<ProductAlpha> findAllWithToOneRelationships(Pageable pageable);

    @Query("select productAlpha from ProductAlpha productAlpha left join fetch productAlpha.category")
    List<ProductAlpha> findAllWithToOneRelationships();

    @Query("select productAlpha from ProductAlpha productAlpha left join fetch productAlpha.category where productAlpha.id =:id")
    Optional<ProductAlpha> findOneWithToOneRelationships(@Param("id") Long id);
}
