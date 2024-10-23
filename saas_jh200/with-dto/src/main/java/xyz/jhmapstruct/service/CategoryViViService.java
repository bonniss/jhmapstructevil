package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CategoryViVi;
import xyz.jhmapstruct.repository.CategoryViViRepository;
import xyz.jhmapstruct.service.dto.CategoryViViDTO;
import xyz.jhmapstruct.service.mapper.CategoryViViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CategoryViVi}.
 */
@Service
@Transactional
public class CategoryViViService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryViViService.class);

    private final CategoryViViRepository categoryViViRepository;

    private final CategoryViViMapper categoryViViMapper;

    public CategoryViViService(CategoryViViRepository categoryViViRepository, CategoryViViMapper categoryViViMapper) {
        this.categoryViViRepository = categoryViViRepository;
        this.categoryViViMapper = categoryViViMapper;
    }

    /**
     * Save a categoryViVi.
     *
     * @param categoryViViDTO the entity to save.
     * @return the persisted entity.
     */
    public CategoryViViDTO save(CategoryViViDTO categoryViViDTO) {
        LOG.debug("Request to save CategoryViVi : {}", categoryViViDTO);
        CategoryViVi categoryViVi = categoryViViMapper.toEntity(categoryViViDTO);
        categoryViVi = categoryViViRepository.save(categoryViVi);
        return categoryViViMapper.toDto(categoryViVi);
    }

    /**
     * Update a categoryViVi.
     *
     * @param categoryViViDTO the entity to save.
     * @return the persisted entity.
     */
    public CategoryViViDTO update(CategoryViViDTO categoryViViDTO) {
        LOG.debug("Request to update CategoryViVi : {}", categoryViViDTO);
        CategoryViVi categoryViVi = categoryViViMapper.toEntity(categoryViViDTO);
        categoryViVi = categoryViViRepository.save(categoryViVi);
        return categoryViViMapper.toDto(categoryViVi);
    }

    /**
     * Partially update a categoryViVi.
     *
     * @param categoryViViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CategoryViViDTO> partialUpdate(CategoryViViDTO categoryViViDTO) {
        LOG.debug("Request to partially update CategoryViVi : {}", categoryViViDTO);

        return categoryViViRepository
            .findById(categoryViViDTO.getId())
            .map(existingCategoryViVi -> {
                categoryViViMapper.partialUpdate(existingCategoryViVi, categoryViViDTO);

                return existingCategoryViVi;
            })
            .map(categoryViViRepository::save)
            .map(categoryViViMapper::toDto);
    }

    /**
     * Get one categoryViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CategoryViViDTO> findOne(Long id) {
        LOG.debug("Request to get CategoryViVi : {}", id);
        return categoryViViRepository.findById(id).map(categoryViViMapper::toDto);
    }

    /**
     * Delete the categoryViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CategoryViVi : {}", id);
        categoryViViRepository.deleteById(id);
    }
}
