package ai.realworld.repository;

import ai.realworld.domain.AlPacinoPointHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlPacinoPointHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlPacinoPointHistoryRepository
    extends JpaRepository<AlPacinoPointHistory, Long>, JpaSpecificationExecutor<AlPacinoPointHistory> {}
