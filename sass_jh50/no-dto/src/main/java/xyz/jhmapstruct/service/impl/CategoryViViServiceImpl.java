package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CategoryViVi;
import xyz.jhmapstruct.repository.CategoryViViRepository;
import xyz.jhmapstruct.service.CategoryViViService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CategoryViVi}.
 */
@Service
@Transactional
public class CategoryViViServiceImpl implements CategoryViViService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryViViServiceImpl.class);

    private final CategoryViViRepository categoryViViRepository;

    public CategoryViViServiceImpl(CategoryViViRepository categoryViViRepository) {
        this.categoryViViRepository = categoryViViRepository;
    }

    @Override
    public CategoryViVi save(CategoryViVi categoryViVi) {
        LOG.debug("Request to save CategoryViVi : {}", categoryViVi);
        return categoryViViRepository.save(categoryViVi);
    }

    @Override
    public CategoryViVi update(CategoryViVi categoryViVi) {
        LOG.debug("Request to update CategoryViVi : {}", categoryViVi);
        return categoryViViRepository.save(categoryViVi);
    }

    @Override
    public Optional<CategoryViVi> partialUpdate(CategoryViVi categoryViVi) {
        LOG.debug("Request to partially update CategoryViVi : {}", categoryViVi);

        return categoryViViRepository
            .findById(categoryViVi.getId())
            .map(existingCategoryViVi -> {
                if (categoryViVi.getName() != null) {
                    existingCategoryViVi.setName(categoryViVi.getName());
                }
                if (categoryViVi.getDescription() != null) {
                    existingCategoryViVi.setDescription(categoryViVi.getDescription());
                }

                return existingCategoryViVi;
            })
            .map(categoryViViRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryViVi> findAll() {
        LOG.debug("Request to get all CategoryViVis");
        return categoryViViRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoryViVi> findOne(Long id) {
        LOG.debug("Request to get CategoryViVi : {}", id);
        return categoryViViRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete CategoryViVi : {}", id);
        categoryViViRepository.deleteById(id);
    }
}
