package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextCategory;

/**
 * Spring Data JPA repository for the NextCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextCategoryRepository extends JpaRepository<NextCategory, Long>, JpaSpecificationExecutor<NextCategory> {}
