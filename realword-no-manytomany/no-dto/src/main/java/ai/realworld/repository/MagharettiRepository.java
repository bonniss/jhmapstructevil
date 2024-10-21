package ai.realworld.repository;

import ai.realworld.domain.Magharetti;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Magharetti entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MagharettiRepository extends JpaRepository<Magharetti, Long>, JpaSpecificationExecutor<Magharetti> {}
