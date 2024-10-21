package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CategoryVi;
import xyz.jhmapstruct.repository.CategoryViRepository;
import xyz.jhmapstruct.service.CategoryViService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CategoryVi}.
 */
@Service
@Transactional
public class CategoryViServiceImpl implements CategoryViService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryViServiceImpl.class);

    private final CategoryViRepository categoryViRepository;

    public CategoryViServiceImpl(CategoryViRepository categoryViRepository) {
        this.categoryViRepository = categoryViRepository;
    }

    @Override
    public CategoryVi save(CategoryVi categoryVi) {
        LOG.debug("Request to save CategoryVi : {}", categoryVi);
        return categoryViRepository.save(categoryVi);
    }

    @Override
    public CategoryVi update(CategoryVi categoryVi) {
        LOG.debug("Request to update CategoryVi : {}", categoryVi);
        return categoryViRepository.save(categoryVi);
    }

    @Override
    public Optional<CategoryVi> partialUpdate(CategoryVi categoryVi) {
        LOG.debug("Request to partially update CategoryVi : {}", categoryVi);

        return categoryViRepository
            .findById(categoryVi.getId())
            .map(existingCategoryVi -> {
                if (categoryVi.getName() != null) {
                    existingCategoryVi.setName(categoryVi.getName());
                }
                if (categoryVi.getDescription() != null) {
                    existingCategoryVi.setDescription(categoryVi.getDescription());
                }

                return existingCategoryVi;
            })
            .map(categoryViRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryVi> findAll() {
        LOG.debug("Request to get all CategoryVis");
        return categoryViRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoryVi> findOne(Long id) {
        LOG.debug("Request to get CategoryVi : {}", id);
        return categoryViRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete CategoryVi : {}", id);
        categoryViRepository.deleteById(id);
    }
}
