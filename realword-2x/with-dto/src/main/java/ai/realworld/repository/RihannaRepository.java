package ai.realworld.repository;

import ai.realworld.domain.Rihanna;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Rihanna entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RihannaRepository extends JpaRepository<Rihanna, Long>, JpaSpecificationExecutor<Rihanna> {}
