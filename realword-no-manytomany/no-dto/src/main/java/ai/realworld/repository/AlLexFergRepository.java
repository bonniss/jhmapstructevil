package ai.realworld.repository;

import ai.realworld.domain.AlLexFerg;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlLexFerg entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlLexFergRepository extends JpaRepository<AlLexFerg, Long>, JpaSpecificationExecutor<AlLexFerg> {}
