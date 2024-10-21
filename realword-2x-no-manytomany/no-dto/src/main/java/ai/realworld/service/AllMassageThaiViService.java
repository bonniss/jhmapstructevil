package ai.realworld.service;

import ai.realworld.domain.AllMassageThaiVi;
import ai.realworld.repository.AllMassageThaiViRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AllMassageThaiVi}.
 */
@Service
@Transactional
public class AllMassageThaiViService {

    private static final Logger LOG = LoggerFactory.getLogger(AllMassageThaiViService.class);

    private final AllMassageThaiViRepository allMassageThaiViRepository;

    public AllMassageThaiViService(AllMassageThaiViRepository allMassageThaiViRepository) {
        this.allMassageThaiViRepository = allMassageThaiViRepository;
    }

    /**
     * Save a allMassageThaiVi.
     *
     * @param allMassageThaiVi the entity to save.
     * @return the persisted entity.
     */
    public AllMassageThaiVi save(AllMassageThaiVi allMassageThaiVi) {
        LOG.debug("Request to save AllMassageThaiVi : {}", allMassageThaiVi);
        return allMassageThaiViRepository.save(allMassageThaiVi);
    }

    /**
     * Update a allMassageThaiVi.
     *
     * @param allMassageThaiVi the entity to save.
     * @return the persisted entity.
     */
    public AllMassageThaiVi update(AllMassageThaiVi allMassageThaiVi) {
        LOG.debug("Request to update AllMassageThaiVi : {}", allMassageThaiVi);
        return allMassageThaiViRepository.save(allMassageThaiVi);
    }

    /**
     * Partially update a allMassageThaiVi.
     *
     * @param allMassageThaiVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AllMassageThaiVi> partialUpdate(AllMassageThaiVi allMassageThaiVi) {
        LOG.debug("Request to partially update AllMassageThaiVi : {}", allMassageThaiVi);

        return allMassageThaiViRepository
            .findById(allMassageThaiVi.getId())
            .map(existingAllMassageThaiVi -> {
                if (allMassageThaiVi.getTitle() != null) {
                    existingAllMassageThaiVi.setTitle(allMassageThaiVi.getTitle());
                }
                if (allMassageThaiVi.getTopContent() != null) {
                    existingAllMassageThaiVi.setTopContent(allMassageThaiVi.getTopContent());
                }
                if (allMassageThaiVi.getContent() != null) {
                    existingAllMassageThaiVi.setContent(allMassageThaiVi.getContent());
                }
                if (allMassageThaiVi.getBottomContent() != null) {
                    existingAllMassageThaiVi.setBottomContent(allMassageThaiVi.getBottomContent());
                }
                if (allMassageThaiVi.getPropTitleMappingJason() != null) {
                    existingAllMassageThaiVi.setPropTitleMappingJason(allMassageThaiVi.getPropTitleMappingJason());
                }
                if (allMassageThaiVi.getDataSourceMappingType() != null) {
                    existingAllMassageThaiVi.setDataSourceMappingType(allMassageThaiVi.getDataSourceMappingType());
                }
                if (allMassageThaiVi.getTargetUrls() != null) {
                    existingAllMassageThaiVi.setTargetUrls(allMassageThaiVi.getTargetUrls());
                }

                return existingAllMassageThaiVi;
            })
            .map(allMassageThaiViRepository::save);
    }

    /**
     * Get one allMassageThaiVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AllMassageThaiVi> findOne(Long id) {
        LOG.debug("Request to get AllMassageThaiVi : {}", id);
        return allMassageThaiViRepository.findById(id);
    }

    /**
     * Delete the allMassageThaiVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AllMassageThaiVi : {}", id);
        allMassageThaiViRepository.deleteById(id);
    }
}
