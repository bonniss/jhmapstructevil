package ai.realworld.repository;

import ai.realworld.domain.PamelaLouis;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PamelaLouis entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PamelaLouisRepository extends JpaRepository<PamelaLouis, Long>, JpaSpecificationExecutor<PamelaLouis> {}
