package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.ShipmentGamma;

/**
 * Spring Data JPA repository for the ShipmentGamma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShipmentGammaRepository extends JpaRepository<ShipmentGamma, Long>, JpaSpecificationExecutor<ShipmentGamma> {}
