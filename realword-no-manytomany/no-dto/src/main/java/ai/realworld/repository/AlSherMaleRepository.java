package ai.realworld.repository;

import ai.realworld.domain.AlSherMale;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlSherMale entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlSherMaleRepository extends JpaRepository<AlSherMale, Long>, JpaSpecificationExecutor<AlSherMale> {}
