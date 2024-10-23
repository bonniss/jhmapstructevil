package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CategorySigma;
import xyz.jhmapstruct.repository.CategorySigmaRepository;
import xyz.jhmapstruct.service.dto.CategorySigmaDTO;
import xyz.jhmapstruct.service.mapper.CategorySigmaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CategorySigma}.
 */
@Service
@Transactional
public class CategorySigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(CategorySigmaService.class);

    private final CategorySigmaRepository categorySigmaRepository;

    private final CategorySigmaMapper categorySigmaMapper;

    public CategorySigmaService(CategorySigmaRepository categorySigmaRepository, CategorySigmaMapper categorySigmaMapper) {
        this.categorySigmaRepository = categorySigmaRepository;
        this.categorySigmaMapper = categorySigmaMapper;
    }

    /**
     * Save a categorySigma.
     *
     * @param categorySigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public CategorySigmaDTO save(CategorySigmaDTO categorySigmaDTO) {
        LOG.debug("Request to save CategorySigma : {}", categorySigmaDTO);
        CategorySigma categorySigma = categorySigmaMapper.toEntity(categorySigmaDTO);
        categorySigma = categorySigmaRepository.save(categorySigma);
        return categorySigmaMapper.toDto(categorySigma);
    }

    /**
     * Update a categorySigma.
     *
     * @param categorySigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public CategorySigmaDTO update(CategorySigmaDTO categorySigmaDTO) {
        LOG.debug("Request to update CategorySigma : {}", categorySigmaDTO);
        CategorySigma categorySigma = categorySigmaMapper.toEntity(categorySigmaDTO);
        categorySigma = categorySigmaRepository.save(categorySigma);
        return categorySigmaMapper.toDto(categorySigma);
    }

    /**
     * Partially update a categorySigma.
     *
     * @param categorySigmaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CategorySigmaDTO> partialUpdate(CategorySigmaDTO categorySigmaDTO) {
        LOG.debug("Request to partially update CategorySigma : {}", categorySigmaDTO);

        return categorySigmaRepository
            .findById(categorySigmaDTO.getId())
            .map(existingCategorySigma -> {
                categorySigmaMapper.partialUpdate(existingCategorySigma, categorySigmaDTO);

                return existingCategorySigma;
            })
            .map(categorySigmaRepository::save)
            .map(categorySigmaMapper::toDto);
    }

    /**
     * Get one categorySigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CategorySigmaDTO> findOne(Long id) {
        LOG.debug("Request to get CategorySigma : {}", id);
        return categorySigmaRepository.findById(id).map(categorySigmaMapper::toDto);
    }

    /**
     * Delete the categorySigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CategorySigma : {}", id);
        categorySigmaRepository.deleteById(id);
    }
}
