package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextShipmentAlpha;

/**
 * Spring Data JPA repository for the NextShipmentAlpha entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextShipmentAlphaRepository extends JpaRepository<NextShipmentAlpha, Long>, JpaSpecificationExecutor<NextShipmentAlpha> {}
