package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextProductSigma;

/**
 * Spring Data JPA repository for the NextProductSigma entity.
 */
@Repository
public interface NextProductSigmaRepository extends JpaRepository<NextProductSigma, Long>, JpaSpecificationExecutor<NextProductSigma> {
    default Optional<NextProductSigma> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<NextProductSigma> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<NextProductSigma> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select nextProductSigma from NextProductSigma nextProductSigma left join fetch nextProductSigma.category",
        countQuery = "select count(nextProductSigma) from NextProductSigma nextProductSigma"
    )
    Page<NextProductSigma> findAllWithToOneRelationships(Pageable pageable);

    @Query("select nextProductSigma from NextProductSigma nextProductSigma left join fetch nextProductSigma.category")
    List<NextProductSigma> findAllWithToOneRelationships();

    @Query(
        "select nextProductSigma from NextProductSigma nextProductSigma left join fetch nextProductSigma.category where nextProductSigma.id =:id"
    )
    Optional<NextProductSigma> findOneWithToOneRelationships(@Param("id") Long id);
}
