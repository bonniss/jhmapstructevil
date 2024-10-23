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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import xyz.jhmapstruct.domain.CategoryMi;
import xyz.jhmapstruct.repository.CategoryMiRepository;
import xyz.jhmapstruct.service.CategoryMiQueryService;
import xyz.jhmapstruct.service.CategoryMiService;
import xyz.jhmapstruct.service.criteria.CategoryMiCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.CategoryMi}.
 */
@RestController
@RequestMapping("/api/category-mis")
public class CategoryMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryMiResource.class);

    private static final String ENTITY_NAME = "categoryMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategoryMiService categoryMiService;

    private final CategoryMiRepository categoryMiRepository;

    private final CategoryMiQueryService categoryMiQueryService;

    public CategoryMiResource(
        CategoryMiService categoryMiService,
        CategoryMiRepository categoryMiRepository,
        CategoryMiQueryService categoryMiQueryService
    ) {
        this.categoryMiService = categoryMiService;
        this.categoryMiRepository = categoryMiRepository;
        this.categoryMiQueryService = categoryMiQueryService;
    }

    /**
     * {@code POST  /category-mis} : Create a new categoryMi.
     *
     * @param categoryMi the categoryMi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categoryMi, or with status {@code 400 (Bad Request)} if the categoryMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CategoryMi> createCategoryMi(@Valid @RequestBody CategoryMi categoryMi) throws URISyntaxException {
        LOG.debug("REST request to save CategoryMi : {}", categoryMi);
        if (categoryMi.getId() != null) {
            throw new BadRequestAlertException("A new categoryMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        categoryMi = categoryMiService.save(categoryMi);
        return ResponseEntity.created(new URI("/api/category-mis/" + categoryMi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, categoryMi.getId().toString()))
            .body(categoryMi);
    }

    /**
     * {@code PUT  /category-mis/:id} : Updates an existing categoryMi.
     *
     * @param id the id of the categoryMi to save.
     * @param categoryMi the categoryMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoryMi,
     * or with status {@code 400 (Bad Request)} if the categoryMi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the categoryMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryMi> updateCategoryMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CategoryMi categoryMi
    ) throws URISyntaxException {
        LOG.debug("REST request to update CategoryMi : {}, {}", id, categoryMi);
        if (categoryMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoryMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoryMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        categoryMi = categoryMiService.update(categoryMi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoryMi.getId().toString()))
            .body(categoryMi);
    }

    /**
     * {@code PATCH  /category-mis/:id} : Partial updates given fields of an existing categoryMi, field will ignore if it is null
     *
     * @param id the id of the categoryMi to save.
     * @param categoryMi the categoryMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoryMi,
     * or with status {@code 400 (Bad Request)} if the categoryMi is not valid,
     * or with status {@code 404 (Not Found)} if the categoryMi is not found,
     * or with status {@code 500 (Internal Server Error)} if the categoryMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CategoryMi> partialUpdateCategoryMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CategoryMi categoryMi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CategoryMi partially : {}, {}", id, categoryMi);
        if (categoryMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoryMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoryMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CategoryMi> result = categoryMiService.partialUpdate(categoryMi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoryMi.getId().toString())
        );
    }

    /**
     * {@code GET  /category-mis} : get all the categoryMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categoryMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CategoryMi>> getAllCategoryMis(
        CategoryMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CategoryMis by criteria: {}", criteria);

        Page<CategoryMi> page = categoryMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /category-mis/count} : count all the categoryMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCategoryMis(CategoryMiCriteria criteria) {
        LOG.debug("REST request to count CategoryMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(categoryMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /category-mis/:id} : get the "id" categoryMi.
     *
     * @param id the id of the categoryMi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categoryMi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryMi> getCategoryMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CategoryMi : {}", id);
        Optional<CategoryMi> categoryMi = categoryMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(categoryMi);
    }

    /**
     * {@code DELETE  /category-mis/:id} : delete the "id" categoryMi.
     *
     * @param id the id of the categoryMi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoryMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CategoryMi : {}", id);
        categoryMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
