package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CategoryVi;
import xyz.jhmapstruct.repository.CategoryViRepository;
import xyz.jhmapstruct.service.dto.CategoryViDTO;
import xyz.jhmapstruct.service.mapper.CategoryViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CategoryVi}.
 */
@Service
@Transactional
public class CategoryViService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryViService.class);

    private final CategoryViRepository categoryViRepository;

    private final CategoryViMapper categoryViMapper;

    public CategoryViService(CategoryViRepository categoryViRepository, CategoryViMapper categoryViMapper) {
        this.categoryViRepository = categoryViRepository;
        this.categoryViMapper = categoryViMapper;
    }

    /**
     * Save a categoryVi.
     *
     * @param categoryViDTO the entity to save.
     * @return the persisted entity.
     */
    public CategoryViDTO save(CategoryViDTO categoryViDTO) {
        LOG.debug("Request to save CategoryVi : {}", categoryViDTO);
        CategoryVi categoryVi = categoryViMapper.toEntity(categoryViDTO);
        categoryVi = categoryViRepository.save(categoryVi);
        return categoryViMapper.toDto(categoryVi);
    }

    /**
     * Update a categoryVi.
     *
     * @param categoryViDTO the entity to save.
     * @return the persisted entity.
     */
    public CategoryViDTO update(CategoryViDTO categoryViDTO) {
        LOG.debug("Request to update CategoryVi : {}", categoryViDTO);
        CategoryVi categoryVi = categoryViMapper.toEntity(categoryViDTO);
        categoryVi = categoryViRepository.save(categoryVi);
        return categoryViMapper.toDto(categoryVi);
    }

    /**
     * Partially update a categoryVi.
     *
     * @param categoryViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CategoryViDTO> partialUpdate(CategoryViDTO categoryViDTO) {
        LOG.debug("Request to partially update CategoryVi : {}", categoryViDTO);

        return categoryViRepository
            .findById(categoryViDTO.getId())
            .map(existingCategoryVi -> {
                categoryViMapper.partialUpdate(existingCategoryVi, categoryViDTO);

                return existingCategoryVi;
            })
            .map(categoryViRepository::save)
            .map(categoryViMapper::toDto);
    }

    /**
     * Get one categoryVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CategoryViDTO> findOne(Long id) {
        LOG.debug("Request to get CategoryVi : {}", id);
        return categoryViRepository.findById(id).map(categoryViMapper::toDto);
    }

    /**
     * Delete the categoryVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CategoryVi : {}", id);
        categoryViRepository.deleteById(id);
    }
}
