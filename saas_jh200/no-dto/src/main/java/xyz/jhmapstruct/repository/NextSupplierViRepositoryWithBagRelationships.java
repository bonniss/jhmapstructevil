package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import xyz.jhmapstruct.domain.NextSupplierVi;

public interface NextSupplierViRepositoryWithBagRelationships {
    Optional<NextSupplierVi> fetchBagRelationships(Optional<NextSupplierVi> nextSupplierVi);

    List<NextSupplierVi> fetchBagRelationships(List<NextSupplierVi> nextSupplierVis);

    Page<NextSupplierVi> fetchBagRelationships(Page<NextSupplierVi> nextSupplierVis);
}
