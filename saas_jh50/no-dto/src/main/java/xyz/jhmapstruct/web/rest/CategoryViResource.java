package xyz.jhmapstruct.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import xyz.jhmapstruct.domain.CategoryVi;
import xyz.jhmapstruct.repository.CategoryViRepository;
import xyz.jhmapstruct.service.CategoryViService;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.CategoryVi}.
 */
@RestController
@RequestMapping("/api/category-vis")
public class CategoryViResource {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryViResource.class);

    private static final String ENTITY_NAME = "categoryVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategoryViService categoryViService;

    private final CategoryViRepository categoryViRepository;

    public CategoryViResource(CategoryViService categoryViService, CategoryViRepository categoryViRepository) {
        this.categoryViService = categoryViService;
        this.categoryViRepository = categoryViRepository;
    }

    /**
     * {@code POST  /category-vis} : Create a new categoryVi.
     *
     * @param categoryVi the categoryVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categoryVi, or with status {@code 400 (Bad Request)} if the categoryVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CategoryVi> createCategoryVi(@Valid @RequestBody CategoryVi categoryVi) throws URISyntaxException {
        LOG.debug("REST request to save CategoryVi : {}", categoryVi);
        if (categoryVi.getId() != null) {
            throw new BadRequestAlertException("A new categoryVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        categoryVi = categoryViService.save(categoryVi);
        return ResponseEntity.created(new URI("/api/category-vis/" + categoryVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, categoryVi.getId().toString()))
            .body(categoryVi);
    }

    /**
     * {@code PUT  /category-vis/:id} : Updates an existing categoryVi.
     *
     * @param id the id of the categoryVi to save.
     * @param categoryVi the categoryVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoryVi,
     * or with status {@code 400 (Bad Request)} if the categoryVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the categoryVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryVi> updateCategoryVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CategoryVi categoryVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update CategoryVi : {}, {}", id, categoryVi);
        if (categoryVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoryVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoryViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        categoryVi = categoryViService.update(categoryVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoryVi.getId().toString()))
            .body(categoryVi);
    }

    /**
     * {@code PATCH  /category-vis/:id} : Partial updates given fields of an existing categoryVi, field will ignore if it is null
     *
     * @param id the id of the categoryVi to save.
     * @param categoryVi the categoryVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoryVi,
     * or with status {@code 400 (Bad Request)} if the categoryVi is not valid,
     * or with status {@code 404 (Not Found)} if the categoryVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the categoryVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CategoryVi> partialUpdateCategoryVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CategoryVi categoryVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CategoryVi partially : {}, {}", id, categoryVi);
        if (categoryVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoryVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoryViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CategoryVi> result = categoryViService.partialUpdate(categoryVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoryVi.getId().toString())
        );
    }

    /**
     * {@code GET  /category-vis} : get all the categoryVis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categoryVis in body.
     */
    @GetMapping("")
    public List<CategoryVi> getAllCategoryVis() {
        LOG.debug("REST request to get all CategoryVis");
        return categoryViService.findAll();
    }

    /**
     * {@code GET  /category-vis/:id} : get the "id" categoryVi.
     *
     * @param id the id of the categoryVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categoryVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryVi> getCategoryVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CategoryVi : {}", id);
        Optional<CategoryVi> categoryVi = categoryViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(categoryVi);
    }

    /**
     * {@code DELETE  /category-vis/:id} : delete the "id" categoryVi.
     *
     * @param id the id of the categoryVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoryVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CategoryVi : {}", id);
        categoryViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
