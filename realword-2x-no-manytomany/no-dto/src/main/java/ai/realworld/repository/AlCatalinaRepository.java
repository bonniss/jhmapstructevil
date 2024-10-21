package ai.realworld.repository;

import ai.realworld.domain.AlCatalina;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlCatalina entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlCatalinaRepository extends JpaRepository<AlCatalina, Long>, JpaSpecificationExecutor<AlCatalina> {}
