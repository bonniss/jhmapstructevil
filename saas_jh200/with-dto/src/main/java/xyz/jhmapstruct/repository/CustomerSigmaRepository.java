package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.CustomerSigma;

/**
 * Spring Data JPA repository for the CustomerSigma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerSigmaRepository extends JpaRepository<CustomerSigma, Long>, JpaSpecificationExecutor<CustomerSigma> {}
