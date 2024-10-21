package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.Customer;

/**
 * Spring Data JPA repository for the Customer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {}
