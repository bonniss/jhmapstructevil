package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CategoryMiMi;
import xyz.jhmapstruct.repository.CategoryMiMiRepository;
import xyz.jhmapstruct.service.CategoryMiMiService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CategoryMiMi}.
 */
@Service
@Transactional
public class CategoryMiMiServiceImpl implements CategoryMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryMiMiServiceImpl.class);

    private final CategoryMiMiRepository categoryMiMiRepository;

    public CategoryMiMiServiceImpl(CategoryMiMiRepository categoryMiMiRepository) {
        this.categoryMiMiRepository = categoryMiMiRepository;
    }

    @Override
    public CategoryMiMi save(CategoryMiMi categoryMiMi) {
        LOG.debug("Request to save CategoryMiMi : {}", categoryMiMi);
        return categoryMiMiRepository.save(categoryMiMi);
    }

    @Override
    public CategoryMiMi update(CategoryMiMi categoryMiMi) {
        LOG.debug("Request to update CategoryMiMi : {}", categoryMiMi);
        return categoryMiMiRepository.save(categoryMiMi);
    }

    @Override
    public Optional<CategoryMiMi> partialUpdate(CategoryMiMi categoryMiMi) {
        LOG.debug("Request to partially update CategoryMiMi : {}", categoryMiMi);

        return categoryMiMiRepository
            .findById(categoryMiMi.getId())
            .map(existingCategoryMiMi -> {
                if (categoryMiMi.getName() != null) {
                    existingCategoryMiMi.setName(categoryMiMi.getName());
                }
                if (categoryMiMi.getDescription() != null) {
                    existingCategoryMiMi.setDescription(categoryMiMi.getDescription());
                }

                return existingCategoryMiMi;
            })
            .map(categoryMiMiRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryMiMi> findAll() {
        LOG.debug("Request to get all CategoryMiMis");
        return categoryMiMiRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoryMiMi> findOne(Long id) {
        LOG.debug("Request to get CategoryMiMi : {}", id);
        return categoryMiMiRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete CategoryMiMi : {}", id);
        categoryMiMiRepository.deleteById(id);
    }
}
