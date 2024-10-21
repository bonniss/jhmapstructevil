package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CategoryMi;
import xyz.jhmapstruct.repository.CategoryMiRepository;
import xyz.jhmapstruct.service.CategoryMiService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CategoryMi}.
 */
@Service
@Transactional
public class CategoryMiServiceImpl implements CategoryMiService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryMiServiceImpl.class);

    private final CategoryMiRepository categoryMiRepository;

    public CategoryMiServiceImpl(CategoryMiRepository categoryMiRepository) {
        this.categoryMiRepository = categoryMiRepository;
    }

    @Override
    public CategoryMi save(CategoryMi categoryMi) {
        LOG.debug("Request to save CategoryMi : {}", categoryMi);
        return categoryMiRepository.save(categoryMi);
    }

    @Override
    public CategoryMi update(CategoryMi categoryMi) {
        LOG.debug("Request to update CategoryMi : {}", categoryMi);
        return categoryMiRepository.save(categoryMi);
    }

    @Override
    public Optional<CategoryMi> partialUpdate(CategoryMi categoryMi) {
        LOG.debug("Request to partially update CategoryMi : {}", categoryMi);

        return categoryMiRepository
            .findById(categoryMi.getId())
            .map(existingCategoryMi -> {
                if (categoryMi.getName() != null) {
                    existingCategoryMi.setName(categoryMi.getName());
                }
                if (categoryMi.getDescription() != null) {
                    existingCategoryMi.setDescription(categoryMi.getDescription());
                }

                return existingCategoryMi;
            })
            .map(categoryMiRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryMi> findAll() {
        LOG.debug("Request to get all CategoryMis");
        return categoryMiRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoryMi> findOne(Long id) {
        LOG.debug("Request to get CategoryMi : {}", id);
        return categoryMiRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete CategoryMi : {}", id);
        categoryMiRepository.deleteById(id);
    }
}
