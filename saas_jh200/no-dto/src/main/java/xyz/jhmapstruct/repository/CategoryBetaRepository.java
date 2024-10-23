package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.CategoryBeta;

/**
 * Spring Data JPA repository for the CategoryBeta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoryBetaRepository extends JpaRepository<CategoryBeta, Long>, JpaSpecificationExecutor<CategoryBeta> {}
