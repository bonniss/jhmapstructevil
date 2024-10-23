package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextCategoryTheta;

/**
 * Spring Data JPA repository for the NextCategoryTheta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextCategoryThetaRepository extends JpaRepository<NextCategoryTheta, Long>, JpaSpecificationExecutor<NextCategoryTheta> {}
