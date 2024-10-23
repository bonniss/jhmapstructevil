package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextProductMiMi;

/**
 * Spring Data JPA repository for the NextProductMiMi entity.
 */
@Repository
public interface NextProductMiMiRepository extends JpaRepository<NextProductMiMi, Long>, JpaSpecificationExecutor<NextProductMiMi> {
    default Optional<NextProductMiMi> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<NextProductMiMi> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<NextProductMiMi> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select nextProductMiMi from NextProductMiMi nextProductMiMi left join fetch nextProductMiMi.category",
        countQuery = "select count(nextProductMiMi) from NextProductMiMi nextProductMiMi"
    )
    Page<NextProductMiMi> findAllWithToOneRelationships(Pageable pageable);

    @Query("select nextProductMiMi from NextProductMiMi nextProductMiMi left join fetch nextProductMiMi.category")
    List<NextProductMiMi> findAllWithToOneRelationships();

    @Query(
        "select nextProductMiMi from NextProductMiMi nextProductMiMi left join fetch nextProductMiMi.category where nextProductMiMi.id =:id"
    )
    Optional<NextProductMiMi> findOneWithToOneRelationships(@Param("id") Long id);
}
