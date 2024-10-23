package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CategoryMi;
import xyz.jhmapstruct.repository.CategoryMiRepository;
import xyz.jhmapstruct.service.dto.CategoryMiDTO;
import xyz.jhmapstruct.service.mapper.CategoryMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CategoryMi}.
 */
@Service
@Transactional
public class CategoryMiService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryMiService.class);

    private final CategoryMiRepository categoryMiRepository;

    private final CategoryMiMapper categoryMiMapper;

    public CategoryMiService(CategoryMiRepository categoryMiRepository, CategoryMiMapper categoryMiMapper) {
        this.categoryMiRepository = categoryMiRepository;
        this.categoryMiMapper = categoryMiMapper;
    }

    /**
     * Save a categoryMi.
     *
     * @param categoryMiDTO the entity to save.
     * @return the persisted entity.
     */
    public CategoryMiDTO save(CategoryMiDTO categoryMiDTO) {
        LOG.debug("Request to save CategoryMi : {}", categoryMiDTO);
        CategoryMi categoryMi = categoryMiMapper.toEntity(categoryMiDTO);
        categoryMi = categoryMiRepository.save(categoryMi);
        return categoryMiMapper.toDto(categoryMi);
    }

    /**
     * Update a categoryMi.
     *
     * @param categoryMiDTO the entity to save.
     * @return the persisted entity.
     */
    public CategoryMiDTO update(CategoryMiDTO categoryMiDTO) {
        LOG.debug("Request to update CategoryMi : {}", categoryMiDTO);
        CategoryMi categoryMi = categoryMiMapper.toEntity(categoryMiDTO);
        categoryMi = categoryMiRepository.save(categoryMi);
        return categoryMiMapper.toDto(categoryMi);
    }

    /**
     * Partially update a categoryMi.
     *
     * @param categoryMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CategoryMiDTO> partialUpdate(CategoryMiDTO categoryMiDTO) {
        LOG.debug("Request to partially update CategoryMi : {}", categoryMiDTO);

        return categoryMiRepository
            .findById(categoryMiDTO.getId())
            .map(existingCategoryMi -> {
                categoryMiMapper.partialUpdate(existingCategoryMi, categoryMiDTO);

                return existingCategoryMi;
            })
            .map(categoryMiRepository::save)
            .map(categoryMiMapper::toDto);
    }

    /**
     * Get one categoryMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CategoryMiDTO> findOne(Long id) {
        LOG.debug("Request to get CategoryMi : {}", id);
        return categoryMiRepository.findById(id).map(categoryMiMapper::toDto);
    }

    /**
     * Delete the categoryMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CategoryMi : {}", id);
        categoryMiRepository.deleteById(id);
    }
}
