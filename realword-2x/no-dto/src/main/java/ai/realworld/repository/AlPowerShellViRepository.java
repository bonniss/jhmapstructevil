package ai.realworld.repository;

import ai.realworld.domain.AlPowerShellVi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlPowerShellVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlPowerShellViRepository extends JpaRepository<AlPowerShellVi, Long>, JpaSpecificationExecutor<AlPowerShellVi> {}
