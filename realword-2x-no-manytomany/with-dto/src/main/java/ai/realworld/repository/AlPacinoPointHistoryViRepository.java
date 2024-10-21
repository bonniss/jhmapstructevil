package ai.realworld.repository;

import ai.realworld.domain.AlPacinoPointHistoryVi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlPacinoPointHistoryVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlPacinoPointHistoryViRepository
    extends JpaRepository<AlPacinoPointHistoryVi, Long>, JpaSpecificationExecutor<AlPacinoPointHistoryVi> {}
