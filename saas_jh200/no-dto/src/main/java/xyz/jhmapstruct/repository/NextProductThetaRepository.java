package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextProductTheta;

/**
 * Spring Data JPA repository for the NextProductTheta entity.
 */
@Repository
public interface NextProductThetaRepository extends JpaRepository<NextProductTheta, Long>, JpaSpecificationExecutor<NextProductTheta> {
    default Optional<NextProductTheta> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<NextProductTheta> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<NextProductTheta> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select nextProductTheta from NextProductTheta nextProductTheta left join fetch nextProductTheta.category",
        countQuery = "select count(nextProductTheta) from NextProductTheta nextProductTheta"
    )
    Page<NextProductTheta> findAllWithToOneRelationships(Pageable pageable);

    @Query("select nextProductTheta from NextProductTheta nextProductTheta left join fetch nextProductTheta.category")
    List<NextProductTheta> findAllWithToOneRelationships();

    @Query(
        "select nextProductTheta from NextProductTheta nextProductTheta left join fetch nextProductTheta.category where nextProductTheta.id =:id"
    )
    Optional<NextProductTheta> findOneWithToOneRelationships(@Param("id") Long id);
}
