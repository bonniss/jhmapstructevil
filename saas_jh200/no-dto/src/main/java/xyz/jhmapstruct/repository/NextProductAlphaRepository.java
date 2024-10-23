package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextProductAlpha;

/**
 * Spring Data JPA repository for the NextProductAlpha entity.
 */
@Repository
public interface NextProductAlphaRepository extends JpaRepository<NextProductAlpha, Long>, JpaSpecificationExecutor<NextProductAlpha> {
    default Optional<NextProductAlpha> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<NextProductAlpha> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<NextProductAlpha> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select nextProductAlpha from NextProductAlpha nextProductAlpha left join fetch nextProductAlpha.category",
        countQuery = "select count(nextProductAlpha) from NextProductAlpha nextProductAlpha"
    )
    Page<NextProductAlpha> findAllWithToOneRelationships(Pageable pageable);

    @Query("select nextProductAlpha from NextProductAlpha nextProductAlpha left join fetch nextProductAlpha.category")
    List<NextProductAlpha> findAllWithToOneRelationships();

    @Query(
        "select nextProductAlpha from NextProductAlpha nextProductAlpha left join fetch nextProductAlpha.category where nextProductAlpha.id =:id"
    )
    Optional<NextProductAlpha> findOneWithToOneRelationships(@Param("id") Long id);
}
