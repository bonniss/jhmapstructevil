package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.CategoryTheta;

/**
 * Spring Data JPA repository for the CategoryTheta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoryThetaRepository extends JpaRepository<CategoryTheta, Long>, JpaSpecificationExecutor<CategoryTheta> {}
