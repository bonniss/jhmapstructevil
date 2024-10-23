package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CategoryMiMi;
import xyz.jhmapstruct.repository.CategoryMiMiRepository;
import xyz.jhmapstruct.service.dto.CategoryMiMiDTO;
import xyz.jhmapstruct.service.mapper.CategoryMiMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CategoryMiMi}.
 */
@Service
@Transactional
public class CategoryMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryMiMiService.class);

    private final CategoryMiMiRepository categoryMiMiRepository;

    private final CategoryMiMiMapper categoryMiMiMapper;

    public CategoryMiMiService(CategoryMiMiRepository categoryMiMiRepository, CategoryMiMiMapper categoryMiMiMapper) {
        this.categoryMiMiRepository = categoryMiMiRepository;
        this.categoryMiMiMapper = categoryMiMiMapper;
    }

    /**
     * Save a categoryMiMi.
     *
     * @param categoryMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public CategoryMiMiDTO save(CategoryMiMiDTO categoryMiMiDTO) {
        LOG.debug("Request to save CategoryMiMi : {}", categoryMiMiDTO);
        CategoryMiMi categoryMiMi = categoryMiMiMapper.toEntity(categoryMiMiDTO);
        categoryMiMi = categoryMiMiRepository.save(categoryMiMi);
        return categoryMiMiMapper.toDto(categoryMiMi);
    }

    /**
     * Update a categoryMiMi.
     *
     * @param categoryMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public CategoryMiMiDTO update(CategoryMiMiDTO categoryMiMiDTO) {
        LOG.debug("Request to update CategoryMiMi : {}", categoryMiMiDTO);
        CategoryMiMi categoryMiMi = categoryMiMiMapper.toEntity(categoryMiMiDTO);
        categoryMiMi = categoryMiMiRepository.save(categoryMiMi);
        return categoryMiMiMapper.toDto(categoryMiMi);
    }

    /**
     * Partially update a categoryMiMi.
     *
     * @param categoryMiMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CategoryMiMiDTO> partialUpdate(CategoryMiMiDTO categoryMiMiDTO) {
        LOG.debug("Request to partially update CategoryMiMi : {}", categoryMiMiDTO);

        return categoryMiMiRepository
            .findById(categoryMiMiDTO.getId())
            .map(existingCategoryMiMi -> {
                categoryMiMiMapper.partialUpdate(existingCategoryMiMi, categoryMiMiDTO);

                return existingCategoryMiMi;
            })
            .map(categoryMiMiRepository::save)
            .map(categoryMiMiMapper::toDto);
    }

    /**
     * Get one categoryMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CategoryMiMiDTO> findOne(Long id) {
        LOG.debug("Request to get CategoryMiMi : {}", id);
        return categoryMiMiRepository.findById(id).map(categoryMiMiMapper::toDto);
    }

    /**
     * Delete the categoryMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CategoryMiMi : {}", id);
        categoryMiMiRepository.deleteById(id);
    }
}
