package xyz.jhmapstruct.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CategoryMi;
import xyz.jhmapstruct.repository.CategoryMiRepository;
import xyz.jhmapstruct.service.CategoryMiService;
import xyz.jhmapstruct.service.dto.CategoryMiDTO;
import xyz.jhmapstruct.service.mapper.CategoryMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CategoryMi}.
 */
@Service
@Transactional
public class CategoryMiServiceImpl implements CategoryMiService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryMiServiceImpl.class);

    private final CategoryMiRepository categoryMiRepository;

    private final CategoryMiMapper categoryMiMapper;

    public CategoryMiServiceImpl(CategoryMiRepository categoryMiRepository, CategoryMiMapper categoryMiMapper) {
        this.categoryMiRepository = categoryMiRepository;
        this.categoryMiMapper = categoryMiMapper;
    }

    @Override
    public CategoryMiDTO save(CategoryMiDTO categoryMiDTO) {
        LOG.debug("Request to save CategoryMi : {}", categoryMiDTO);
        CategoryMi categoryMi = categoryMiMapper.toEntity(categoryMiDTO);
        categoryMi = categoryMiRepository.save(categoryMi);
        return categoryMiMapper.toDto(categoryMi);
    }

    @Override
    public CategoryMiDTO update(CategoryMiDTO categoryMiDTO) {
        LOG.debug("Request to update CategoryMi : {}", categoryMiDTO);
        CategoryMi categoryMi = categoryMiMapper.toEntity(categoryMiDTO);
        categoryMi = categoryMiRepository.save(categoryMi);
        return categoryMiMapper.toDto(categoryMi);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<CategoryMiDTO> findAll() {
        LOG.debug("Request to get all CategoryMis");
        return categoryMiRepository.findAll().stream().map(categoryMiMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoryMiDTO> findOne(Long id) {
        LOG.debug("Request to get CategoryMi : {}", id);
        return categoryMiRepository.findById(id).map(categoryMiMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete CategoryMi : {}", id);
        categoryMiRepository.deleteById(id);
    }
}
