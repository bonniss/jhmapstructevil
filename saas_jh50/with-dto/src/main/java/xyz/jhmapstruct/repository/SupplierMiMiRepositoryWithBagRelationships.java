package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import xyz.jhmapstruct.domain.SupplierMiMi;

public interface SupplierMiMiRepositoryWithBagRelationships {
    Optional<SupplierMiMi> fetchBagRelationships(Optional<SupplierMiMi> supplierMiMi);

    List<SupplierMiMi> fetchBagRelationships(List<SupplierMiMi> supplierMiMis);

    Page<SupplierMiMi> fetchBagRelationships(Page<SupplierMiMi> supplierMiMis);
}
