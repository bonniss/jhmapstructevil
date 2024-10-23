package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextProductGamma;

/**
 * Spring Data JPA repository for the NextProductGamma entity.
 */
@Repository
public interface NextProductGammaRepository extends JpaRepository<NextProductGamma, Long>, JpaSpecificationExecutor<NextProductGamma> {
    default Optional<NextProductGamma> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<NextProductGamma> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<NextProductGamma> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select nextProductGamma from NextProductGamma nextProductGamma left join fetch nextProductGamma.category",
        countQuery = "select count(nextProductGamma) from NextProductGamma nextProductGamma"
    )
    Page<NextProductGamma> findAllWithToOneRelationships(Pageable pageable);

    @Query("select nextProductGamma from NextProductGamma nextProductGamma left join fetch nextProductGamma.category")
    List<NextProductGamma> findAllWithToOneRelationships();

    @Query(
        "select nextProductGamma from NextProductGamma nextProductGamma left join fetch nextProductGamma.category where nextProductGamma.id =:id"
    )
    Optional<NextProductGamma> findOneWithToOneRelationships(@Param("id") Long id);
}
