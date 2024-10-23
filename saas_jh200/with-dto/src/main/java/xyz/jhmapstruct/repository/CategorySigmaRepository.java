package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.CategorySigma;

/**
 * Spring Data JPA repository for the CategorySigma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategorySigmaRepository extends JpaRepository<CategorySigma, Long>, JpaSpecificationExecutor<CategorySigma> {}
