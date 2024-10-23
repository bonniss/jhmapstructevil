package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextEmployeeAlpha;

/**
 * Spring Data JPA repository for the NextEmployeeAlpha entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextEmployeeAlphaRepository extends JpaRepository<NextEmployeeAlpha, Long>, JpaSpecificationExecutor<NextEmployeeAlpha> {}
