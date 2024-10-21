package ai.realworld.repository;

import ai.realworld.domain.SaisanCog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SaisanCog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SaisanCogRepository extends JpaRepository<SaisanCog, Long>, JpaSpecificationExecutor<SaisanCog> {}
