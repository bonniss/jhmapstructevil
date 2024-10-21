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
import xyz.jhmapstruct.domain.CategoryMiMi;
import xyz.jhmapstruct.repository.CategoryMiMiRepository;
import xyz.jhmapstruct.service.CategoryMiMiService;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.CategoryMiMi}.
 */
@RestController
@RequestMapping("/api/category-mi-mis")
public class CategoryMiMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryMiMiResource.class);

    private static final String ENTITY_NAME = "categoryMiMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategoryMiMiService categoryMiMiService;

    private final CategoryMiMiRepository categoryMiMiRepository;

    public CategoryMiMiResource(CategoryMiMiService categoryMiMiService, CategoryMiMiRepository categoryMiMiRepository) {
        this.categoryMiMiService = categoryMiMiService;
        this.categoryMiMiRepository = categoryMiMiRepository;
    }

    /**
     * {@code POST  /category-mi-mis} : Create a new categoryMiMi.
     *
     * @param categoryMiMi the categoryMiMi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categoryMiMi, or with status {@code 400 (Bad Request)} if the categoryMiMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CategoryMiMi> createCategoryMiMi(@Valid @RequestBody CategoryMiMi categoryMiMi) throws URISyntaxException {
        LOG.debug("REST request to save CategoryMiMi : {}", categoryMiMi);
        if (categoryMiMi.getId() != null) {
            throw new BadRequestAlertException("A new categoryMiMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        categoryMiMi = categoryMiMiService.save(categoryMiMi);
        return ResponseEntity.created(new URI("/api/category-mi-mis/" + categoryMiMi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, categoryMiMi.getId().toString()))
            .body(categoryMiMi);
    }

    /**
     * {@code PUT  /category-mi-mis/:id} : Updates an existing categoryMiMi.
     *
     * @param id the id of the categoryMiMi to save.
     * @param categoryMiMi the categoryMiMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoryMiMi,
     * or with status {@code 400 (Bad Request)} if the categoryMiMi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the categoryMiMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryMiMi> updateCategoryMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CategoryMiMi categoryMiMi
    ) throws URISyntaxException {
        LOG.debug("REST request to update CategoryMiMi : {}, {}", id, categoryMiMi);
        if (categoryMiMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoryMiMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoryMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        categoryMiMi = categoryMiMiService.update(categoryMiMi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoryMiMi.getId().toString()))
            .body(categoryMiMi);
    }

    /**
     * {@code PATCH  /category-mi-mis/:id} : Partial updates given fields of an existing categoryMiMi, field will ignore if it is null
     *
     * @param id the id of the categoryMiMi to save.
     * @param categoryMiMi the categoryMiMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoryMiMi,
     * or with status {@code 400 (Bad Request)} if the categoryMiMi is not valid,
     * or with status {@code 404 (Not Found)} if the categoryMiMi is not found,
     * or with status {@code 500 (Internal Server Error)} if the categoryMiMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CategoryMiMi> partialUpdateCategoryMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CategoryMiMi categoryMiMi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CategoryMiMi partially : {}, {}", id, categoryMiMi);
        if (categoryMiMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoryMiMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoryMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CategoryMiMi> result = categoryMiMiService.partialUpdate(categoryMiMi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoryMiMi.getId().toString())
        );
    }

    /**
     * {@code GET  /category-mi-mis} : get all the categoryMiMis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categoryMiMis in body.
     */
    @GetMapping("")
    public List<CategoryMiMi> getAllCategoryMiMis() {
        LOG.debug("REST request to get all CategoryMiMis");
        return categoryMiMiService.findAll();
    }

    /**
     * {@code GET  /category-mi-mis/:id} : get the "id" categoryMiMi.
     *
     * @param id the id of the categoryMiMi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categoryMiMi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryMiMi> getCategoryMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CategoryMiMi : {}", id);
        Optional<CategoryMiMi> categoryMiMi = categoryMiMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(categoryMiMi);
    }

    /**
     * {@code DELETE  /category-mi-mis/:id} : delete the "id" categoryMiMi.
     *
     * @param id the id of the categoryMiMi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoryMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CategoryMiMi : {}", id);
        categoryMiMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
