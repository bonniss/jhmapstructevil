package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextProductVi;

/**
 * Spring Data JPA repository for the NextProductVi entity.
 */
@Repository
public interface NextProductViRepository extends JpaRepository<NextProductVi, Long>, JpaSpecificationExecutor<NextProductVi> {
    default Optional<NextProductVi> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<NextProductVi> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<NextProductVi> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select nextProductVi from NextProductVi nextProductVi left join fetch nextProductVi.category",
        countQuery = "select count(nextProductVi) from NextProductVi nextProductVi"
    )
    Page<NextProductVi> findAllWithToOneRelationships(Pageable pageable);

    @Query("select nextProductVi from NextProductVi nextProductVi left join fetch nextProductVi.category")
    List<NextProductVi> findAllWithToOneRelationships();

    @Query("select nextProductVi from NextProductVi nextProductVi left join fetch nextProductVi.category where nextProductVi.id =:id")
    Optional<NextProductVi> findOneWithToOneRelationships(@Param("id") Long id);
}
