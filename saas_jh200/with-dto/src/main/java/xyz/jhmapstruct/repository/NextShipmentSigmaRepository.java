package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextShipmentSigma;

/**
 * Spring Data JPA repository for the NextShipmentSigma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextShipmentSigmaRepository extends JpaRepository<NextShipmentSigma, Long>, JpaSpecificationExecutor<NextShipmentSigma> {}
