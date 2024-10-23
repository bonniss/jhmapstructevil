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
import xyz.jhmapstruct.domain.CategoryBeta;
import xyz.jhmapstruct.repository.CategoryBetaRepository;
import xyz.jhmapstruct.service.CategoryBetaQueryService;
import xyz.jhmapstruct.service.CategoryBetaService;
import xyz.jhmapstruct.service.criteria.CategoryBetaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.CategoryBeta}.
 */
@RestController
@RequestMapping("/api/category-betas")
public class CategoryBetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryBetaResource.class);

    private static final String ENTITY_NAME = "categoryBeta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategoryBetaService categoryBetaService;

    private final CategoryBetaRepository categoryBetaRepository;

    private final CategoryBetaQueryService categoryBetaQueryService;

    public CategoryBetaResource(
        CategoryBetaService categoryBetaService,
        CategoryBetaRepository categoryBetaRepository,
        CategoryBetaQueryService categoryBetaQueryService
    ) {
        this.categoryBetaService = categoryBetaService;
        this.categoryBetaRepository = categoryBetaRepository;
        this.categoryBetaQueryService = categoryBetaQueryService;
    }

    /**
     * {@code POST  /category-betas} : Create a new categoryBeta.
     *
     * @param categoryBeta the categoryBeta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categoryBeta, or with status {@code 400 (Bad Request)} if the categoryBeta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CategoryBeta> createCategoryBeta(@Valid @RequestBody CategoryBeta categoryBeta) throws URISyntaxException {
        LOG.debug("REST request to save CategoryBeta : {}", categoryBeta);
        if (categoryBeta.getId() != null) {
            throw new BadRequestAlertException("A new categoryBeta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        categoryBeta = categoryBetaService.save(categoryBeta);
        return ResponseEntity.created(new URI("/api/category-betas/" + categoryBeta.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, categoryBeta.getId().toString()))
            .body(categoryBeta);
    }

    /**
     * {@code PUT  /category-betas/:id} : Updates an existing categoryBeta.
     *
     * @param id the id of the categoryBeta to save.
     * @param categoryBeta the categoryBeta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoryBeta,
     * or with status {@code 400 (Bad Request)} if the categoryBeta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the categoryBeta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryBeta> updateCategoryBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CategoryBeta categoryBeta
    ) throws URISyntaxException {
        LOG.debug("REST request to update CategoryBeta : {}, {}", id, categoryBeta);
        if (categoryBeta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoryBeta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoryBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        categoryBeta = categoryBetaService.update(categoryBeta);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoryBeta.getId().toString()))
            .body(categoryBeta);
    }

    /**
     * {@code PATCH  /category-betas/:id} : Partial updates given fields of an existing categoryBeta, field will ignore if it is null
     *
     * @param id the id of the categoryBeta to save.
     * @param categoryBeta the categoryBeta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoryBeta,
     * or with status {@code 400 (Bad Request)} if the categoryBeta is not valid,
     * or with status {@code 404 (Not Found)} if the categoryBeta is not found,
     * or with status {@code 500 (Internal Server Error)} if the categoryBeta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CategoryBeta> partialUpdateCategoryBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CategoryBeta categoryBeta
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CategoryBeta partially : {}, {}", id, categoryBeta);
        if (categoryBeta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoryBeta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoryBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CategoryBeta> result = categoryBetaService.partialUpdate(categoryBeta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoryBeta.getId().toString())
        );
    }

    /**
     * {@code GET  /category-betas} : get all the categoryBetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categoryBetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CategoryBeta>> getAllCategoryBetas(
        CategoryBetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CategoryBetas by criteria: {}", criteria);

        Page<CategoryBeta> page = categoryBetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /category-betas/count} : count all the categoryBetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCategoryBetas(CategoryBetaCriteria criteria) {
        LOG.debug("REST request to count CategoryBetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(categoryBetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /category-betas/:id} : get the "id" categoryBeta.
     *
     * @param id the id of the categoryBeta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categoryBeta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryBeta> getCategoryBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CategoryBeta : {}", id);
        Optional<CategoryBeta> categoryBeta = categoryBetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(categoryBeta);
    }

    /**
     * {@code DELETE  /category-betas/:id} : delete the "id" categoryBeta.
     *
     * @param id the id of the categoryBeta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoryBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CategoryBeta : {}", id);
        categoryBetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
