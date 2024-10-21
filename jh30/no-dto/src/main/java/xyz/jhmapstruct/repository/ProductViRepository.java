package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.ProductVi;

/**
 * Spring Data JPA repository for the ProductVi entity.
 */
@Repository
public interface ProductViRepository extends JpaRepository<ProductVi, Long> {
    default Optional<ProductVi> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProductVi> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProductVi> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select productVi from ProductVi productVi left join fetch productVi.category",
        countQuery = "select count(productVi) from ProductVi productVi"
    )
    Page<ProductVi> findAllWithToOneRelationships(Pageable pageable);

    @Query("select productVi from ProductVi productVi left join fetch productVi.category")
    List<ProductVi> findAllWithToOneRelationships();

    @Query("select productVi from ProductVi productVi left join fetch productVi.category where productVi.id =:id")
    Optional<ProductVi> findOneWithToOneRelationships(@Param("id") Long id);
}
