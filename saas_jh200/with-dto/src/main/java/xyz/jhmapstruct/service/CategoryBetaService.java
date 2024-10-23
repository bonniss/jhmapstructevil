package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CategoryBeta;
import xyz.jhmapstruct.repository.CategoryBetaRepository;
import xyz.jhmapstruct.service.dto.CategoryBetaDTO;
import xyz.jhmapstruct.service.mapper.CategoryBetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CategoryBeta}.
 */
@Service
@Transactional
public class CategoryBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryBetaService.class);

    private final CategoryBetaRepository categoryBetaRepository;

    private final CategoryBetaMapper categoryBetaMapper;

    public CategoryBetaService(CategoryBetaRepository categoryBetaRepository, CategoryBetaMapper categoryBetaMapper) {
        this.categoryBetaRepository = categoryBetaRepository;
        this.categoryBetaMapper = categoryBetaMapper;
    }

    /**
     * Save a categoryBeta.
     *
     * @param categoryBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public CategoryBetaDTO save(CategoryBetaDTO categoryBetaDTO) {
        LOG.debug("Request to save CategoryBeta : {}", categoryBetaDTO);
        CategoryBeta categoryBeta = categoryBetaMapper.toEntity(categoryBetaDTO);
        categoryBeta = categoryBetaRepository.save(categoryBeta);
        return categoryBetaMapper.toDto(categoryBeta);
    }

    /**
     * Update a categoryBeta.
     *
     * @param categoryBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public CategoryBetaDTO update(CategoryBetaDTO categoryBetaDTO) {
        LOG.debug("Request to update CategoryBeta : {}", categoryBetaDTO);
        CategoryBeta categoryBeta = categoryBetaMapper.toEntity(categoryBetaDTO);
        categoryBeta = categoryBetaRepository.save(categoryBeta);
        return categoryBetaMapper.toDto(categoryBeta);
    }

    /**
     * Partially update a categoryBeta.
     *
     * @param categoryBetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CategoryBetaDTO> partialUpdate(CategoryBetaDTO categoryBetaDTO) {
        LOG.debug("Request to partially update CategoryBeta : {}", categoryBetaDTO);

        return categoryBetaRepository
            .findById(categoryBetaDTO.getId())
            .map(existingCategoryBeta -> {
                categoryBetaMapper.partialUpdate(existingCategoryBeta, categoryBetaDTO);

                return existingCategoryBeta;
            })
            .map(categoryBetaRepository::save)
            .map(categoryBetaMapper::toDto);
    }

    /**
     * Get one categoryBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CategoryBetaDTO> findOne(Long id) {
        LOG.debug("Request to get CategoryBeta : {}", id);
        return categoryBetaRepository.findById(id).map(categoryBetaMapper::toDto);
    }

    /**
     * Delete the categoryBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CategoryBeta : {}", id);
        categoryBetaRepository.deleteById(id);
    }
}
