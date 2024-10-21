package ai.realworld.repository;

import ai.realworld.domain.SaisanCogVi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SaisanCogVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SaisanCogViRepository extends JpaRepository<SaisanCogVi, Long>, JpaSpecificationExecutor<SaisanCogVi> {}
