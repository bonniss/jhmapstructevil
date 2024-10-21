package ai.realworld.repository;

import ai.realworld.domain.AlProty;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface AlProtyRepositoryWithBagRelationships {
    Optional<AlProty> fetchBagRelationships(Optional<AlProty> alProty);

    List<AlProty> fetchBagRelationships(List<AlProty> alProties);

    Page<AlProty> fetchBagRelationships(Page<AlProty> alProties);
}
