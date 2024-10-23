package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextProductMi;

/**
 * Spring Data JPA repository for the NextProductMi entity.
 */
@Repository
public interface NextProductMiRepository extends JpaRepository<NextProductMi, Long>, JpaSpecificationExecutor<NextProductMi> {
    default Optional<NextProductMi> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<NextProductMi> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<NextProductMi> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select nextProductMi from NextProductMi nextProductMi left join fetch nextProductMi.category",
        countQuery = "select count(nextProductMi) from NextProductMi nextProductMi"
    )
    Page<NextProductMi> findAllWithToOneRelationships(Pageable pageable);

    @Query("select nextProductMi from NextProductMi nextProductMi left join fetch nextProductMi.category")
    List<NextProductMi> findAllWithToOneRelationships();

    @Query("select nextProductMi from NextProductMi nextProductMi left join fetch nextProductMi.category where nextProductMi.id =:id")
    Optional<NextProductMi> findOneWithToOneRelationships(@Param("id") Long id);
}
