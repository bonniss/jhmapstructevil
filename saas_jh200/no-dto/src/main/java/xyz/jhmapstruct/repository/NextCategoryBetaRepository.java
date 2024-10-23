package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextCategoryBeta;

/**
 * Spring Data JPA repository for the NextCategoryBeta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextCategoryBetaRepository extends JpaRepository<NextCategoryBeta, Long>, JpaSpecificationExecutor<NextCategoryBeta> {}
