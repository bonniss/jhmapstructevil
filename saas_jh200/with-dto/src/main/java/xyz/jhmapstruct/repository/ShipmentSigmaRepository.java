package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.ShipmentSigma;

/**
 * Spring Data JPA repository for the ShipmentSigma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShipmentSigmaRepository extends JpaRepository<ShipmentSigma, Long>, JpaSpecificationExecutor<ShipmentSigma> {}
