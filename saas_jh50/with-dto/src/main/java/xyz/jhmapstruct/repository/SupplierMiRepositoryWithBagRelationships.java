package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import xyz.jhmapstruct.domain.SupplierMi;

public interface SupplierMiRepositoryWithBagRelationships {
    Optional<SupplierMi> fetchBagRelationships(Optional<SupplierMi> supplierMi);

    List<SupplierMi> fetchBagRelationships(List<SupplierMi> supplierMis);

    Page<SupplierMi> fetchBagRelationships(Page<SupplierMi> supplierMis);
}
