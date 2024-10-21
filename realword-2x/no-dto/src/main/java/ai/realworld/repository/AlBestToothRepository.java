package ai.realworld.repository;

import ai.realworld.domain.AlBestTooth;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlBestTooth entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlBestToothRepository extends JpaRepository<AlBestTooth, Long>, JpaSpecificationExecutor<AlBestTooth> {}
