package ai.realworld.repository;

import ai.realworld.domain.InitiumVi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InitiumVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InitiumViRepository extends JpaRepository<InitiumVi, Long>, JpaSpecificationExecutor<InitiumVi> {}
