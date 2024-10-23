package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import xyz.jhmapstruct.domain.NextSupplierSigma;

public interface NextSupplierSigmaRepositoryWithBagRelationships {
    Optional<NextSupplierSigma> fetchBagRelationships(Optional<NextSupplierSigma> nextSupplierSigma);

    List<NextSupplierSigma> fetchBagRelationships(List<NextSupplierSigma> nextSupplierSigmas);

    Page<NextSupplierSigma> fetchBagRelationships(Page<NextSupplierSigma> nextSupplierSigmas);
}
