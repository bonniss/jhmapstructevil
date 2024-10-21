package ai.realworld.repository;

import ai.realworld.domain.SicilyUmeto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SicilyUmeto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SicilyUmetoRepository extends JpaRepository<SicilyUmeto, Long>, JpaSpecificationExecutor<SicilyUmeto> {}
