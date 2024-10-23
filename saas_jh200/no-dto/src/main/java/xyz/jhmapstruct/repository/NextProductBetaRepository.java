package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextProductBeta;

/**
 * Spring Data JPA repository for the NextProductBeta entity.
 */
@Repository
public interface NextProductBetaRepository extends JpaRepository<NextProductBeta, Long>, JpaSpecificationExecutor<NextProductBeta> {
    default Optional<NextProductBeta> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<NextProductBeta> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<NextProductBeta> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select nextProductBeta from NextProductBeta nextProductBeta left join fetch nextProductBeta.category",
        countQuery = "select count(nextProductBeta) from NextProductBeta nextProductBeta"
    )
    Page<NextProductBeta> findAllWithToOneRelationships(Pageable pageable);

    @Query("select nextProductBeta from NextProductBeta nextProductBeta left join fetch nextProductBeta.category")
    List<NextProductBeta> findAllWithToOneRelationships();

    @Query(
        "select nextProductBeta from NextProductBeta nextProductBeta left join fetch nextProductBeta.category where nextProductBeta.id =:id"
    )
    Optional<NextProductBeta> findOneWithToOneRelationships(@Param("id") Long id);
}
