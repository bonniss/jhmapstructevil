package ai.realworld.repository;

import ai.realworld.domain.AlProPro;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface AlProProRepositoryWithBagRelationships {
    Optional<AlProPro> fetchBagRelationships(Optional<AlProPro> alProPro);

    List<AlProPro> fetchBagRelationships(List<AlProPro> alProPros);

    Page<AlProPro> fetchBagRelationships(Page<AlProPro> alProPros);
}
