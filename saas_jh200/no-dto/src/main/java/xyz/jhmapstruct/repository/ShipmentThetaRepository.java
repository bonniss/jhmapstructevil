package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.ShipmentTheta;

/**
 * Spring Data JPA repository for the ShipmentTheta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShipmentThetaRepository extends JpaRepository<ShipmentTheta, Long>, JpaSpecificationExecutor<ShipmentTheta> {}
