package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import xyz.jhmapstruct.domain.NextSupplierMiMi;

public interface NextSupplierMiMiRepositoryWithBagRelationships {
    Optional<NextSupplierMiMi> fetchBagRelationships(Optional<NextSupplierMiMi> nextSupplierMiMi);

    List<NextSupplierMiMi> fetchBagRelationships(List<NextSupplierMiMi> nextSupplierMiMis);

    Page<NextSupplierMiMi> fetchBagRelationships(Page<NextSupplierMiMi> nextSupplierMiMis);
}
