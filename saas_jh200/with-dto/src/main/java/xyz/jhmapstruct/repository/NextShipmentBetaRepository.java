package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextShipmentBeta;

/**
 * Spring Data JPA repository for the NextShipmentBeta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextShipmentBetaRepository extends JpaRepository<NextShipmentBeta, Long>, JpaSpecificationExecutor<NextShipmentBeta> {}
