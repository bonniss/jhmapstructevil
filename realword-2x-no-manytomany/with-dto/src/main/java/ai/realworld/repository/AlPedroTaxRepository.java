package ai.realworld.repository;

import ai.realworld.domain.AlPedroTax;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlPedroTax entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlPedroTaxRepository extends JpaRepository<AlPedroTax, Long>, JpaSpecificationExecutor<AlPedroTax> {}
