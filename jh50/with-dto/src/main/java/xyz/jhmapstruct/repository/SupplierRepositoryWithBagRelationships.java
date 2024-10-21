package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import xyz.jhmapstruct.domain.Supplier;

public interface SupplierRepositoryWithBagRelationships {
    Optional<Supplier> fetchBagRelationships(Optional<Supplier> supplier);

    List<Supplier> fetchBagRelationships(List<Supplier> suppliers);

    Page<Supplier> fetchBagRelationships(Page<Supplier> suppliers);
}
