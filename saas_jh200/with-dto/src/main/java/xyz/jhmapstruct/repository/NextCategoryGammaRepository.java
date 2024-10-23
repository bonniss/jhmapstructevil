package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextCategoryGamma;

/**
 * Spring Data JPA repository for the NextCategoryGamma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextCategoryGammaRepository extends JpaRepository<NextCategoryGamma, Long>, JpaSpecificationExecutor<NextCategoryGamma> {}
