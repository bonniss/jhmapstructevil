package xyz.jhmapstruct.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CategoryVi;
import xyz.jhmapstruct.repository.CategoryViRepository;
import xyz.jhmapstruct.service.CategoryViService;
import xyz.jhmapstruct.service.dto.CategoryViDTO;
import xyz.jhmapstruct.service.mapper.CategoryViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CategoryVi}.
 */
@Service
@Transactional
public class CategoryViServiceImpl implements CategoryViService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryViServiceImpl.class);

    private final CategoryViRepository categoryViRepository;

    private final CategoryViMapper categoryViMapper;

    public CategoryViServiceImpl(CategoryViRepository categoryViRepository, CategoryViMapper categoryViMapper) {
        this.categoryViRepository = categoryViRepository;
        this.categoryViMapper = categoryViMapper;
    }

    @Override
    public CategoryViDTO save(CategoryViDTO categoryViDTO) {
        LOG.debug("Request to save CategoryVi : {}", categoryViDTO);
        CategoryVi categoryVi = categoryViMapper.toEntity(categoryViDTO);
        categoryVi = categoryViRepository.save(categoryVi);
        return categoryViMapper.toDto(categoryVi);
    }

    @Override
    public CategoryViDTO update(CategoryViDTO categoryViDTO) {
        LOG.debug("Request to update CategoryVi : {}", categoryViDTO);
        CategoryVi categoryVi = categoryViMapper.toEntity(categoryViDTO);
        categoryVi = categoryViRepository.save(categoryVi);
        return categoryViMapper.toDto(categoryVi);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<CategoryViDTO> findAll() {
        LOG.debug("Request to get all CategoryVis");
        return categoryViRepository.findAll().stream().map(categoryViMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoryViDTO> findOne(Long id) {
        LOG.debug("Request to get CategoryVi : {}", id);
        return categoryViRepository.findById(id).map(categoryViMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete CategoryVi : {}", id);
        categoryViRepository.deleteById(id);
    }
}
