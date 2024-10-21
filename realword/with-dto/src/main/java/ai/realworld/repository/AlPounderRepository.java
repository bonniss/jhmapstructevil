package ai.realworld.repository;

import ai.realworld.domain.AlPounder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlPounder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlPounderRepository extends JpaRepository<AlPounder, Long>, JpaSpecificationExecutor<AlPounder> {}
