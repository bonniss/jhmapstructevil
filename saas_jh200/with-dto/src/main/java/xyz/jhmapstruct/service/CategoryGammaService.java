package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CategoryGamma;
import xyz.jhmapstruct.repository.CategoryGammaRepository;
import xyz.jhmapstruct.service.dto.CategoryGammaDTO;
import xyz.jhmapstruct.service.mapper.CategoryGammaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CategoryGamma}.
 */
@Service
@Transactional
public class CategoryGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryGammaService.class);

    private final CategoryGammaRepository categoryGammaRepository;

    private final CategoryGammaMapper categoryGammaMapper;

    public CategoryGammaService(CategoryGammaRepository categoryGammaRepository, CategoryGammaMapper categoryGammaMapper) {
        this.categoryGammaRepository = categoryGammaRepository;
        this.categoryGammaMapper = categoryGammaMapper;
    }

    /**
     * Save a categoryGamma.
     *
     * @param categoryGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public CategoryGammaDTO save(CategoryGammaDTO categoryGammaDTO) {
        LOG.debug("Request to save CategoryGamma : {}", categoryGammaDTO);
        CategoryGamma categoryGamma = categoryGammaMapper.toEntity(categoryGammaDTO);
        categoryGamma = categoryGammaRepository.save(categoryGamma);
        return categoryGammaMapper.toDto(categoryGamma);
    }

    /**
     * Update a categoryGamma.
     *
     * @param categoryGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public CategoryGammaDTO update(CategoryGammaDTO categoryGammaDTO) {
        LOG.debug("Request to update CategoryGamma : {}", categoryGammaDTO);
        CategoryGamma categoryGamma = categoryGammaMapper.toEntity(categoryGammaDTO);
        categoryGamma = categoryGammaRepository.save(categoryGamma);
        return categoryGammaMapper.toDto(categoryGamma);
    }

    /**
     * Partially update a categoryGamma.
     *
     * @param categoryGammaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CategoryGammaDTO> partialUpdate(CategoryGammaDTO categoryGammaDTO) {
        LOG.debug("Request to partially update CategoryGamma : {}", categoryGammaDTO);

        return categoryGammaRepository
            .findById(categoryGammaDTO.getId())
            .map(existingCategoryGamma -> {
                categoryGammaMapper.partialUpdate(existingCategoryGamma, categoryGammaDTO);

                return existingCategoryGamma;
            })
            .map(categoryGammaRepository::save)
            .map(categoryGammaMapper::toDto);
    }

    /**
     * Get one categoryGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CategoryGammaDTO> findOne(Long id) {
        LOG.debug("Request to get CategoryGamma : {}", id);
        return categoryGammaRepository.findById(id).map(categoryGammaMapper::toDto);
    }

    /**
     * Delete the categoryGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CategoryGamma : {}", id);
        categoryGammaRepository.deleteById(id);
    }
}
