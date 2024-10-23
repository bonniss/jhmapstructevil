package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextCategorySigma;

/**
 * Spring Data JPA repository for the NextCategorySigma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextCategorySigmaRepository extends JpaRepository<NextCategorySigma, Long>, JpaSpecificationExecutor<NextCategorySigma> {}
