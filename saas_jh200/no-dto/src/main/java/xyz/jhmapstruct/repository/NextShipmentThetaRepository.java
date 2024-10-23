package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextShipmentTheta;

/**
 * Spring Data JPA repository for the NextShipmentTheta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextShipmentThetaRepository extends JpaRepository<NextShipmentTheta, Long>, JpaSpecificationExecutor<NextShipmentTheta> {}
