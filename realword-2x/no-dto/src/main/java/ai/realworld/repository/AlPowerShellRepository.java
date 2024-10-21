package ai.realworld.repository;

import ai.realworld.domain.AlPowerShell;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlPowerShell entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlPowerShellRepository extends JpaRepository<AlPowerShell, Long>, JpaSpecificationExecutor<AlPowerShell> {}
