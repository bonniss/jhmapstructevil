package ai.realworld.repository;

import ai.realworld.domain.AlMenity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlMenity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlMenityRepository extends JpaRepository<AlMenity, Long>, JpaSpecificationExecutor<AlMenity> {}
