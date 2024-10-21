package ai.realworld.repository;

import ai.realworld.domain.AlLexFergVi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlLexFergVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlLexFergViRepository extends JpaRepository<AlLexFergVi, Long>, JpaSpecificationExecutor<AlLexFergVi> {}
