package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextShipmentVi;

/**
 * Spring Data JPA repository for the NextShipmentVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextShipmentViRepository extends JpaRepository<NextShipmentVi, Long>, JpaSpecificationExecutor<NextShipmentVi> {}
