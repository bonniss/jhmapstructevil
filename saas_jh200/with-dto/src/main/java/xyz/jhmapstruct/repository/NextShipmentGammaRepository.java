package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextShipmentGamma;

/**
 * Spring Data JPA repository for the NextShipmentGamma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextShipmentGammaRepository extends JpaRepository<NextShipmentGamma, Long>, JpaSpecificationExecutor<NextShipmentGamma> {}
