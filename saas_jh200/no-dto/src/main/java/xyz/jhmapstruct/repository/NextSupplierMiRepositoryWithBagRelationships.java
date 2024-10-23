package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import xyz.jhmapstruct.domain.NextSupplierMi;

public interface NextSupplierMiRepositoryWithBagRelationships {
    Optional<NextSupplierMi> fetchBagRelationships(Optional<NextSupplierMi> nextSupplierMi);

    List<NextSupplierMi> fetchBagRelationships(List<NextSupplierMi> nextSupplierMis);

    Page<NextSupplierMi> fetchBagRelationships(Page<NextSupplierMi> nextSupplierMis);
}
