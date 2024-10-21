package ai.realworld.repository;

import ai.realworld.domain.Initium;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Initium entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InitiumRepository extends JpaRepository<Initium, Long>, JpaSpecificationExecutor<Initium> {}
