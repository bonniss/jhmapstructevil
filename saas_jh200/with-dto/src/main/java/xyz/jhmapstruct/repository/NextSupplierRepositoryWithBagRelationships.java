package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import xyz.jhmapstruct.domain.NextSupplier;

public interface NextSupplierRepositoryWithBagRelationships {
    Optional<NextSupplier> fetchBagRelationships(Optional<NextSupplier> nextSupplier);

    List<NextSupplier> fetchBagRelationships(List<NextSupplier> nextSuppliers);

    Page<NextSupplier> fetchBagRelationships(Page<NextSupplier> nextSuppliers);
}
