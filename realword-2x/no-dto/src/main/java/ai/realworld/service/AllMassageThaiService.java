package ai.realworld.service;

import ai.realworld.domain.AllMassageThai;
import ai.realworld.repository.AllMassageThaiRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AllMassageThai}.
 */
@Service
@Transactional
public class AllMassageThaiService {

    private static final Logger LOG = LoggerFactory.getLogger(AllMassageThaiService.class);

    private final AllMassageThaiRepository allMassageThaiRepository;

    public AllMassageThaiService(AllMassageThaiRepository allMassageThaiRepository) {
        this.allMassageThaiRepository = allMassageThaiRepository;
    }

    /**
     * Save a allMassageThai.
     *
     * @param allMassageThai the entity to save.
     * @return the persisted entity.
     */
    public AllMassageThai save(AllMassageThai allMassageThai) {
        LOG.debug("Request to save AllMassageThai : {}", allMassageThai);
        return allMassageThaiRepository.save(allMassageThai);
    }

    /**
     * Update a allMassageThai.
     *
     * @param allMassageThai the entity to save.
     * @return the persisted entity.
     */
    public AllMassageThai update(AllMassageThai allMassageThai) {
        LOG.debug("Request to update AllMassageThai : {}", allMassageThai);
        return allMassageThaiRepository.save(allMassageThai);
    }

    /**
     * Partially update a allMassageThai.
     *
     * @param allMassageThai the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AllMassageThai> partialUpdate(AllMassageThai allMassageThai) {
        LOG.debug("Request to partially update AllMassageThai : {}", allMassageThai);

        return allMassageThaiRepository
            .findById(allMassageThai.getId())
            .map(existingAllMassageThai -> {
                if (allMassageThai.getTitle() != null) {
                    existingAllMassageThai.setTitle(allMassageThai.getTitle());
                }
                if (allMassageThai.getTopContent() != null) {
                    existingAllMassageThai.setTopContent(allMassageThai.getTopContent());
                }
                if (allMassageThai.getContent() != null) {
                    existingAllMassageThai.setContent(allMassageThai.getContent());
                }
                if (allMassageThai.getBottomContent() != null) {
                    existingAllMassageThai.setBottomContent(allMassageThai.getBottomContent());
                }
                if (allMassageThai.getPropTitleMappingJason() != null) {
                    existingAllMassageThai.setPropTitleMappingJason(allMassageThai.getPropTitleMappingJason());
                }
                if (allMassageThai.getDataSourceMappingType() != null) {
                    existingAllMassageThai.setDataSourceMappingType(allMassageThai.getDataSourceMappingType());
                }
                if (allMassageThai.getTargetUrls() != null) {
                    existingAllMassageThai.setTargetUrls(allMassageThai.getTargetUrls());
                }

                return existingAllMassageThai;
            })
            .map(allMassageThaiRepository::save);
    }

    /**
     * Get one allMassageThai by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AllMassageThai> findOne(Long id) {
        LOG.debug("Request to get AllMassageThai : {}", id);
        return allMassageThaiRepository.findById(id);
    }

    /**
     * Delete the allMassageThai by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AllMassageThai : {}", id);
        allMassageThaiRepository.deleteById(id);
    }
}
