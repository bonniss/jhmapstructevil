package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import xyz.jhmapstruct.domain.NextSupplierViVi;

public interface NextSupplierViViRepositoryWithBagRelationships {
    Optional<NextSupplierViVi> fetchBagRelationships(Optional<NextSupplierViVi> nextSupplierViVi);

    List<NextSupplierViVi> fetchBagRelationships(List<NextSupplierViVi> nextSupplierViVis);

    Page<NextSupplierViVi> fetchBagRelationships(Page<NextSupplierViVi> nextSupplierViVis);
}
