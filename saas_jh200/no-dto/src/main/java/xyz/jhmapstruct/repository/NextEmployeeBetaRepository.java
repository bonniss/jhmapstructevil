package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextEmployeeBeta;

/**
 * Spring Data JPA repository for the NextEmployeeBeta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextEmployeeBetaRepository extends JpaRepository<NextEmployeeBeta, Long>, JpaSpecificationExecutor<NextEmployeeBeta> {}
