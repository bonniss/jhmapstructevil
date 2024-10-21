package ai.realworld.repository;

import ai.realworld.domain.AlPedroTaxVi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlPedroTaxVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlPedroTaxViRepository extends JpaRepository<AlPedroTaxVi, Long>, JpaSpecificationExecutor<AlPedroTaxVi> {}
