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
import xyz.jhmapstruct.domain.CategoryViVi;
import xyz.jhmapstruct.repository.CategoryViViRepository;
import xyz.jhmapstruct.service.CategoryViViQueryService;
import xyz.jhmapstruct.service.CategoryViViService;
import xyz.jhmapstruct.service.criteria.CategoryViViCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.CategoryViVi}.
 */
@RestController
@RequestMapping("/api/category-vi-vis")
public class CategoryViViResource {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryViViResource.class);

    private static final String ENTITY_NAME = "categoryViVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategoryViViService categoryViViService;

    private final CategoryViViRepository categoryViViRepository;

    private final CategoryViViQueryService categoryViViQueryService;

    public CategoryViViResource(
        CategoryViViService categoryViViService,
        CategoryViViRepository categoryViViRepository,
        CategoryViViQueryService categoryViViQueryService
    ) {
        this.categoryViViService = categoryViViService;
        this.categoryViViRepository = categoryViViRepository;
        this.categoryViViQueryService = categoryViViQueryService;
    }

    /**
     * {@code POST  /category-vi-vis} : Create a new categoryViVi.
     *
     * @param categoryViVi the categoryViVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categoryViVi, or with status {@code 400 (Bad Request)} if the categoryViVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CategoryViVi> createCategoryViVi(@Valid @RequestBody CategoryViVi categoryViVi) throws URISyntaxException {
        LOG.debug("REST request to save CategoryViVi : {}", categoryViVi);
        if (categoryViVi.getId() != null) {
            throw new BadRequestAlertException("A new categoryViVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        categoryViVi = categoryViViService.save(categoryViVi);
        return ResponseEntity.created(new URI("/api/category-vi-vis/" + categoryViVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, categoryViVi.getId().toString()))
            .body(categoryViVi);
    }

    /**
     * {@code PUT  /category-vi-vis/:id} : Updates an existing categoryViVi.
     *
     * @param id the id of the categoryViVi to save.
     * @param categoryViVi the categoryViVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoryViVi,
     * or with status {@code 400 (Bad Request)} if the categoryViVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the categoryViVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryViVi> updateCategoryViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CategoryViVi categoryViVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update CategoryViVi : {}, {}", id, categoryViVi);
        if (categoryViVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoryViVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoryViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        categoryViVi = categoryViViService.update(categoryViVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoryViVi.getId().toString()))
            .body(categoryViVi);
    }

    /**
     * {@code PATCH  /category-vi-vis/:id} : Partial updates given fields of an existing categoryViVi, field will ignore if it is null
     *
     * @param id the id of the categoryViVi to save.
     * @param categoryViVi the categoryViVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoryViVi,
     * or with status {@code 400 (Bad Request)} if the categoryViVi is not valid,
     * or with status {@code 404 (Not Found)} if the categoryViVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the categoryViVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CategoryViVi> partialUpdateCategoryViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CategoryViVi categoryViVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CategoryViVi partially : {}, {}", id, categoryViVi);
        if (categoryViVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoryViVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoryViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CategoryViVi> result = categoryViViService.partialUpdate(categoryViVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoryViVi.getId().toString())
        );
    }

    /**
     * {@code GET  /category-vi-vis} : get all the categoryViVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categoryViVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CategoryViVi>> getAllCategoryViVis(
        CategoryViViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CategoryViVis by criteria: {}", criteria);

        Page<CategoryViVi> page = categoryViViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /category-vi-vis/count} : count all the categoryViVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCategoryViVis(CategoryViViCriteria criteria) {
        LOG.debug("REST request to count CategoryViVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(categoryViViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /category-vi-vis/:id} : get the "id" categoryViVi.
     *
     * @param id the id of the categoryViVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categoryViVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryViVi> getCategoryViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CategoryViVi : {}", id);
        Optional<CategoryViVi> categoryViVi = categoryViViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(categoryViVi);
    }

    /**
     * {@code DELETE  /category-vi-vis/:id} : delete the "id" categoryViVi.
     *
     * @param id the id of the categoryViVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoryViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CategoryViVi : {}", id);
        categoryViViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
