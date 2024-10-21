package ai.realworld.repository;

import ai.realworld.domain.SicilyUmetoVi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SicilyUmetoVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SicilyUmetoViRepository extends JpaRepository<SicilyUmetoVi, Long>, JpaSpecificationExecutor<SicilyUmetoVi> {}
