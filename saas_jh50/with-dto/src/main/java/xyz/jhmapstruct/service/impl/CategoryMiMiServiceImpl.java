package xyz.jhmapstruct.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CategoryMiMi;
import xyz.jhmapstruct.repository.CategoryMiMiRepository;
import xyz.jhmapstruct.service.CategoryMiMiService;
import xyz.jhmapstruct.service.dto.CategoryMiMiDTO;
import xyz.jhmapstruct.service.mapper.CategoryMiMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CategoryMiMi}.
 */
@Service
@Transactional
public class CategoryMiMiServiceImpl implements CategoryMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryMiMiServiceImpl.class);

    private final CategoryMiMiRepository categoryMiMiRepository;

    private final CategoryMiMiMapper categoryMiMiMapper;

    public CategoryMiMiServiceImpl(CategoryMiMiRepository categoryMiMiRepository, CategoryMiMiMapper categoryMiMiMapper) {
        this.categoryMiMiRepository = categoryMiMiRepository;
        this.categoryMiMiMapper = categoryMiMiMapper;
    }

    @Override
    public CategoryMiMiDTO save(CategoryMiMiDTO categoryMiMiDTO) {
        LOG.debug("Request to save CategoryMiMi : {}", categoryMiMiDTO);
        CategoryMiMi categoryMiMi = categoryMiMiMapper.toEntity(categoryMiMiDTO);
        categoryMiMi = categoryMiMiRepository.save(categoryMiMi);
        return categoryMiMiMapper.toDto(categoryMiMi);
    }

    @Override
    public CategoryMiMiDTO update(CategoryMiMiDTO categoryMiMiDTO) {
        LOG.debug("Request to update CategoryMiMi : {}", categoryMiMiDTO);
        CategoryMiMi categoryMiMi = categoryMiMiMapper.toEntity(categoryMiMiDTO);
        categoryMiMi = categoryMiMiRepository.save(categoryMiMi);
        return categoryMiMiMapper.toDto(categoryMiMi);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<CategoryMiMiDTO> findAll() {
        LOG.debug("Request to get all CategoryMiMis");
        return categoryMiMiRepository.findAll().stream().map(categoryMiMiMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoryMiMiDTO> findOne(Long id) {
        LOG.debug("Request to get CategoryMiMi : {}", id);
        return categoryMiMiRepository.findById(id).map(categoryMiMiMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete CategoryMiMi : {}", id);
        categoryMiMiRepository.deleteById(id);
    }
}
