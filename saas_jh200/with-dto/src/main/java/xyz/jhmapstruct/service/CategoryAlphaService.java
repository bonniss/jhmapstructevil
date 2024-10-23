package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CategoryAlpha;
import xyz.jhmapstruct.repository.CategoryAlphaRepository;
import xyz.jhmapstruct.service.dto.CategoryAlphaDTO;
import xyz.jhmapstruct.service.mapper.CategoryAlphaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CategoryAlpha}.
 */
@Service
@Transactional
public class CategoryAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryAlphaService.class);

    private final CategoryAlphaRepository categoryAlphaRepository;

    private final CategoryAlphaMapper categoryAlphaMapper;

    public CategoryAlphaService(CategoryAlphaRepository categoryAlphaRepository, CategoryAlphaMapper categoryAlphaMapper) {
        this.categoryAlphaRepository = categoryAlphaRepository;
        this.categoryAlphaMapper = categoryAlphaMapper;
    }

    /**
     * Save a categoryAlpha.
     *
     * @param categoryAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public CategoryAlphaDTO save(CategoryAlphaDTO categoryAlphaDTO) {
        LOG.debug("Request to save CategoryAlpha : {}", categoryAlphaDTO);
        CategoryAlpha categoryAlpha = categoryAlphaMapper.toEntity(categoryAlphaDTO);
        categoryAlpha = categoryAlphaRepository.save(categoryAlpha);
        return categoryAlphaMapper.toDto(categoryAlpha);
    }

    /**
     * Update a categoryAlpha.
     *
     * @param categoryAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public CategoryAlphaDTO update(CategoryAlphaDTO categoryAlphaDTO) {
        LOG.debug("Request to update CategoryAlpha : {}", categoryAlphaDTO);
        CategoryAlpha categoryAlpha = categoryAlphaMapper.toEntity(categoryAlphaDTO);
        categoryAlpha = categoryAlphaRepository.save(categoryAlpha);
        return categoryAlphaMapper.toDto(categoryAlpha);
    }

    /**
     * Partially update a categoryAlpha.
     *
     * @param categoryAlphaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CategoryAlphaDTO> partialUpdate(CategoryAlphaDTO categoryAlphaDTO) {
        LOG.debug("Request to partially update CategoryAlpha : {}", categoryAlphaDTO);

        return categoryAlphaRepository
            .findById(categoryAlphaDTO.getId())
            .map(existingCategoryAlpha -> {
                categoryAlphaMapper.partialUpdate(existingCategoryAlpha, categoryAlphaDTO);

                return existingCategoryAlpha;
            })
            .map(categoryAlphaRepository::save)
            .map(categoryAlphaMapper::toDto);
    }

    /**
     * Get one categoryAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CategoryAlphaDTO> findOne(Long id) {
        LOG.debug("Request to get CategoryAlpha : {}", id);
        return categoryAlphaRepository.findById(id).map(categoryAlphaMapper::toDto);
    }

    /**
     * Delete the categoryAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CategoryAlpha : {}", id);
        categoryAlphaRepository.deleteById(id);
    }
}
