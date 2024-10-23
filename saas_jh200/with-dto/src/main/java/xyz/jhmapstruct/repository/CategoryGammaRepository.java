package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.CategoryGamma;

/**
 * Spring Data JPA repository for the CategoryGamma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoryGammaRepository extends JpaRepository<CategoryGamma, Long>, JpaSpecificationExecutor<CategoryGamma> {}
