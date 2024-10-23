package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CategoryTheta;
import xyz.jhmapstruct.repository.CategoryThetaRepository;
import xyz.jhmapstruct.service.dto.CategoryThetaDTO;
import xyz.jhmapstruct.service.mapper.CategoryThetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CategoryTheta}.
 */
@Service
@Transactional
public class CategoryThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryThetaService.class);

    private final CategoryThetaRepository categoryThetaRepository;

    private final CategoryThetaMapper categoryThetaMapper;

    public CategoryThetaService(CategoryThetaRepository categoryThetaRepository, CategoryThetaMapper categoryThetaMapper) {
        this.categoryThetaRepository = categoryThetaRepository;
        this.categoryThetaMapper = categoryThetaMapper;
    }

    /**
     * Save a categoryTheta.
     *
     * @param categoryThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public CategoryThetaDTO save(CategoryThetaDTO categoryThetaDTO) {
        LOG.debug("Request to save CategoryTheta : {}", categoryThetaDTO);
        CategoryTheta categoryTheta = categoryThetaMapper.toEntity(categoryThetaDTO);
        categoryTheta = categoryThetaRepository.save(categoryTheta);
        return categoryThetaMapper.toDto(categoryTheta);
    }

    /**
     * Update a categoryTheta.
     *
     * @param categoryThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public CategoryThetaDTO update(CategoryThetaDTO categoryThetaDTO) {
        LOG.debug("Request to update CategoryTheta : {}", categoryThetaDTO);
        CategoryTheta categoryTheta = categoryThetaMapper.toEntity(categoryThetaDTO);
        categoryTheta = categoryThetaRepository.save(categoryTheta);
        return categoryThetaMapper.toDto(categoryTheta);
    }

    /**
     * Partially update a categoryTheta.
     *
     * @param categoryThetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CategoryThetaDTO> partialUpdate(CategoryThetaDTO categoryThetaDTO) {
        LOG.debug("Request to partially update CategoryTheta : {}", categoryThetaDTO);

        return categoryThetaRepository
            .findById(categoryThetaDTO.getId())
            .map(existingCategoryTheta -> {
                categoryThetaMapper.partialUpdate(existingCategoryTheta, categoryThetaDTO);

                return existingCategoryTheta;
            })
            .map(categoryThetaRepository::save)
            .map(categoryThetaMapper::toDto);
    }

    /**
     * Get one categoryTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CategoryThetaDTO> findOne(Long id) {
        LOG.debug("Request to get CategoryTheta : {}", id);
        return categoryThetaRepository.findById(id).map(categoryThetaMapper::toDto);
    }

    /**
     * Delete the categoryTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CategoryTheta : {}", id);
        categoryThetaRepository.deleteById(id);
    }
}
