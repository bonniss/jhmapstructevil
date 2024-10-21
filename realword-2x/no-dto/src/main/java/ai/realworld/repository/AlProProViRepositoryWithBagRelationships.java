package ai.realworld.repository;

import ai.realworld.domain.AlProProVi;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface AlProProViRepositoryWithBagRelationships {
    Optional<AlProProVi> fetchBagRelationships(Optional<AlProProVi> alProProVi);

    List<AlProProVi> fetchBagRelationships(List<AlProProVi> alProProVis);

    Page<AlProProVi> fetchBagRelationships(Page<AlProProVi> alProProVis);
}
