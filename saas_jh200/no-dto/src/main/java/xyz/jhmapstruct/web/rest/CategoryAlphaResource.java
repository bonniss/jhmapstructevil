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
import xyz.jhmapstruct.domain.CategoryAlpha;
import xyz.jhmapstruct.repository.CategoryAlphaRepository;
import xyz.jhmapstruct.service.CategoryAlphaQueryService;
import xyz.jhmapstruct.service.CategoryAlphaService;
import xyz.jhmapstruct.service.criteria.CategoryAlphaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.CategoryAlpha}.
 */
@RestController
@RequestMapping("/api/category-alphas")
public class CategoryAlphaResource {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryAlphaResource.class);

    private static final String ENTITY_NAME = "categoryAlpha";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategoryAlphaService categoryAlphaService;

    private final CategoryAlphaRepository categoryAlphaRepository;

    private final CategoryAlphaQueryService categoryAlphaQueryService;

    public CategoryAlphaResource(
        CategoryAlphaService categoryAlphaService,
        CategoryAlphaRepository categoryAlphaRepository,
        CategoryAlphaQueryService categoryAlphaQueryService
    ) {
        this.categoryAlphaService = categoryAlphaService;
        this.categoryAlphaRepository = categoryAlphaRepository;
        this.categoryAlphaQueryService = categoryAlphaQueryService;
    }

    /**
     * {@code POST  /category-alphas} : Create a new categoryAlpha.
     *
     * @param categoryAlpha the categoryAlpha to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categoryAlpha, or with status {@code 400 (Bad Request)} if the categoryAlpha has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CategoryAlpha> createCategoryAlpha(@Valid @RequestBody CategoryAlpha categoryAlpha) throws URISyntaxException {
        LOG.debug("REST request to save CategoryAlpha : {}", categoryAlpha);
        if (categoryAlpha.getId() != null) {
            throw new BadRequestAlertException("A new categoryAlpha cannot already have an ID", ENTITY_NAME, "idexists");
        }
        categoryAlpha = categoryAlphaService.save(categoryAlpha);
        return ResponseEntity.created(new URI("/api/category-alphas/" + categoryAlpha.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, categoryAlpha.getId().toString()))
            .body(categoryAlpha);
    }

    /**
     * {@code PUT  /category-alphas/:id} : Updates an existing categoryAlpha.
     *
     * @param id the id of the categoryAlpha to save.
     * @param categoryAlpha the categoryAlpha to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoryAlpha,
     * or with status {@code 400 (Bad Request)} if the categoryAlpha is not valid,
     * or with status {@code 500 (Internal Server Error)} if the categoryAlpha couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryAlpha> updateCategoryAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CategoryAlpha categoryAlpha
    ) throws URISyntaxException {
        LOG.debug("REST request to update CategoryAlpha : {}, {}", id, categoryAlpha);
        if (categoryAlpha.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoryAlpha.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoryAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        categoryAlpha = categoryAlphaService.update(categoryAlpha);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoryAlpha.getId().toString()))
            .body(categoryAlpha);
    }

    /**
     * {@code PATCH  /category-alphas/:id} : Partial updates given fields of an existing categoryAlpha, field will ignore if it is null
     *
     * @param id the id of the categoryAlpha to save.
     * @param categoryAlpha the categoryAlpha to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoryAlpha,
     * or with status {@code 400 (Bad Request)} if the categoryAlpha is not valid,
     * or with status {@code 404 (Not Found)} if the categoryAlpha is not found,
     * or with status {@code 500 (Internal Server Error)} if the categoryAlpha couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CategoryAlpha> partialUpdateCategoryAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CategoryAlpha categoryAlpha
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CategoryAlpha partially : {}, {}", id, categoryAlpha);
        if (categoryAlpha.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoryAlpha.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoryAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CategoryAlpha> result = categoryAlphaService.partialUpdate(categoryAlpha);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoryAlpha.getId().toString())
        );
    }

    /**
     * {@code GET  /category-alphas} : get all the categoryAlphas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categoryAlphas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CategoryAlpha>> getAllCategoryAlphas(
        CategoryAlphaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CategoryAlphas by criteria: {}", criteria);

        Page<CategoryAlpha> page = categoryAlphaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /category-alphas/count} : count all the categoryAlphas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCategoryAlphas(CategoryAlphaCriteria criteria) {
        LOG.debug("REST request to count CategoryAlphas by criteria: {}", criteria);
        return ResponseEntity.ok().body(categoryAlphaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /category-alphas/:id} : get the "id" categoryAlpha.
     *
     * @param id the id of the categoryAlpha to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categoryAlpha, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryAlpha> getCategoryAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CategoryAlpha : {}", id);
        Optional<CategoryAlpha> categoryAlpha = categoryAlphaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(categoryAlpha);
    }

    /**
     * {@code DELETE  /category-alphas/:id} : delete the "id" categoryAlpha.
     *
     * @param id the id of the categoryAlpha to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoryAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CategoryAlpha : {}", id);
        categoryAlphaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
