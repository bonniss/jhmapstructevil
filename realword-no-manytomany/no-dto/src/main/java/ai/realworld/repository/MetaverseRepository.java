package ai.realworld.repository;

import ai.realworld.domain.Metaverse;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Metaverse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MetaverseRepository extends JpaRepository<Metaverse, Long>, JpaSpecificationExecutor<Metaverse> {}
