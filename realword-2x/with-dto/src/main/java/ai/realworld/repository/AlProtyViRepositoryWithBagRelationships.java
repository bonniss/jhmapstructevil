package ai.realworld.repository;

import ai.realworld.domain.AlProtyVi;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface AlProtyViRepositoryWithBagRelationships {
    Optional<AlProtyVi> fetchBagRelationships(Optional<AlProtyVi> alProtyVi);

    List<AlProtyVi> fetchBagRelationships(List<AlProtyVi> alProtyVis);

    Page<AlProtyVi> fetchBagRelationships(Page<AlProtyVi> alProtyVis);
}
