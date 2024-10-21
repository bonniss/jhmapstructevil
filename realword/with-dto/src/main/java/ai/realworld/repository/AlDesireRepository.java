package ai.realworld.repository;

import ai.realworld.domain.AlDesire;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlDesire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlDesireRepository extends JpaRepository<AlDesire, UUID>, JpaSpecificationExecutor<AlDesire> {}
