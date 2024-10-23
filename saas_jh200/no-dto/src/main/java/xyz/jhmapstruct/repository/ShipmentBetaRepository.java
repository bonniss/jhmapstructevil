package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.ShipmentBeta;

/**
 * Spring Data JPA repository for the ShipmentBeta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShipmentBetaRepository extends JpaRepository<ShipmentBeta, Long>, JpaSpecificationExecutor<ShipmentBeta> {}
