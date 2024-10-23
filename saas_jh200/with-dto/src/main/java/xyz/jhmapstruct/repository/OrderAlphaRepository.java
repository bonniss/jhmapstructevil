package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.OrderAlpha;

/**
 * Spring Data JPA repository for the OrderAlpha entity.
 */
@Repository
public interface OrderAlphaRepository extends JpaRepository<OrderAlpha, Long>, JpaSpecificationExecutor<OrderAlpha> {
    default Optional<OrderAlpha> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<OrderAlpha> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<OrderAlpha> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select orderAlpha from OrderAlpha orderAlpha left join fetch orderAlpha.customer",
        countQuery = "select count(orderAlpha) from OrderAlpha orderAlpha"
    )
    Page<OrderAlpha> findAllWithToOneRelationships(Pageable pageable);

    @Query("select orderAlpha from OrderAlpha orderAlpha left join fetch orderAlpha.customer")
    List<OrderAlpha> findAllWithToOneRelationships();

    @Query("select orderAlpha from OrderAlpha orderAlpha left join fetch orderAlpha.customer where orderAlpha.id =:id")
    Optional<OrderAlpha> findOneWithToOneRelationships(@Param("id") Long id);
}
