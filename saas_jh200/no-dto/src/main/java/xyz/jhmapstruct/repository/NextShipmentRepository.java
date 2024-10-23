package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextShipment;

/**
 * Spring Data JPA repository for the NextShipment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextShipmentRepository extends JpaRepository<NextShipment, Long>, JpaSpecificationExecutor<NextShipment> {}
