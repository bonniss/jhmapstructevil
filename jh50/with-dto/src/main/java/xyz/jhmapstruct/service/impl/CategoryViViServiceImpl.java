package xyz.jhmapstruct.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CategoryViVi;
import xyz.jhmapstruct.repository.CategoryViViRepository;
import xyz.jhmapstruct.service.CategoryViViService;
import xyz.jhmapstruct.service.dto.CategoryViViDTO;
import xyz.jhmapstruct.service.mapper.CategoryViViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CategoryViVi}.
 */
@Service
@Transactional
public class CategoryViViServiceImpl implements CategoryViViService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryViViServiceImpl.class);

    private final CategoryViViRepository categoryViViRepository;

    private final CategoryViViMapper categoryViViMapper;

    public CategoryViViServiceImpl(CategoryViViRepository categoryViViRepository, CategoryViViMapper categoryViViMapper) {
        this.categoryViViRepository = categoryViViRepository;
        this.categoryViViMapper = categoryViViMapper;
    }

    @Override
    public CategoryViViDTO save(CategoryViViDTO categoryViViDTO) {
        LOG.debug("Request to save CategoryViVi : {}", categoryViViDTO);
        CategoryViVi categoryViVi = categoryViViMapper.toEntity(categoryViViDTO);
        categoryViVi = categoryViViRepository.save(categoryViVi);
        return categoryViViMapper.toDto(categoryViVi);
    }

    @Override
    public CategoryViViDTO update(CategoryViViDTO categoryViViDTO) {
        LOG.debug("Request to update CategoryViVi : {}", categoryViViDTO);
        CategoryViVi categoryViVi = categoryViViMapper.toEntity(categoryViViDTO);
        categoryViVi = categoryViViRepository.save(categoryViVi);
        return categoryViViMapper.toDto(categoryViVi);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<CategoryViViDTO> findAll() {
        LOG.debug("Request to get all CategoryViVis");
        return categoryViViRepository.findAll().stream().map(categoryViViMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoryViViDTO> findOne(Long id) {
        LOG.debug("Request to get CategoryViVi : {}", id);
        return categoryViViRepository.findById(id).map(categoryViViMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete CategoryViVi : {}", id);
        categoryViViRepository.deleteById(id);
    }
}
