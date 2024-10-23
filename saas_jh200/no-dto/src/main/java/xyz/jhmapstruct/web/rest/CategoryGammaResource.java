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
import xyz.jhmapstruct.domain.CategoryGamma;
import xyz.jhmapstruct.repository.CategoryGammaRepository;
import xyz.jhmapstruct.service.CategoryGammaQueryService;
import xyz.jhmapstruct.service.CategoryGammaService;
import xyz.jhmapstruct.service.criteria.CategoryGammaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.CategoryGamma}.
 */
@RestController
@RequestMapping("/api/category-gammas")
public class CategoryGammaResource {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryGammaResource.class);

    private static final String ENTITY_NAME = "categoryGamma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategoryGammaService categoryGammaService;

    private final CategoryGammaRepository categoryGammaRepository;

    private final CategoryGammaQueryService categoryGammaQueryService;

    public CategoryGammaResource(
        CategoryGammaService categoryGammaService,
        CategoryGammaRepository categoryGammaRepository,
        CategoryGammaQueryService categoryGammaQueryService
    ) {
        this.categoryGammaService = categoryGammaService;
        this.categoryGammaRepository = categoryGammaRepository;
        this.categoryGammaQueryService = categoryGammaQueryService;
    }

    /**
     * {@code POST  /category-gammas} : Create a new categoryGamma.
     *
     * @param categoryGamma the categoryGamma to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categoryGamma, or with status {@code 400 (Bad Request)} if the categoryGamma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CategoryGamma> createCategoryGamma(@Valid @RequestBody CategoryGamma categoryGamma) throws URISyntaxException {
        LOG.debug("REST request to save CategoryGamma : {}", categoryGamma);
        if (categoryGamma.getId() != null) {
            throw new BadRequestAlertException("A new categoryGamma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        categoryGamma = categoryGammaService.save(categoryGamma);
        return ResponseEntity.created(new URI("/api/category-gammas/" + categoryGamma.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, categoryGamma.getId().toString()))
            .body(categoryGamma);
    }

    /**
     * {@code PUT  /category-gammas/:id} : Updates an existing categoryGamma.
     *
     * @param id the id of the categoryGamma to save.
     * @param categoryGamma the categoryGamma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoryGamma,
     * or with status {@code 400 (Bad Request)} if the categoryGamma is not valid,
     * or with status {@code 500 (Internal Server Error)} if the categoryGamma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryGamma> updateCategoryGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CategoryGamma categoryGamma
    ) throws URISyntaxException {
        LOG.debug("REST request to update CategoryGamma : {}, {}", id, categoryGamma);
        if (categoryGamma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoryGamma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoryGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        categoryGamma = categoryGammaService.update(categoryGamma);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoryGamma.getId().toString()))
            .body(categoryGamma);
    }

    /**
     * {@code PATCH  /category-gammas/:id} : Partial updates given fields of an existing categoryGamma, field will ignore if it is null
     *
     * @param id the id of the categoryGamma to save.
     * @param categoryGamma the categoryGamma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoryGamma,
     * or with status {@code 400 (Bad Request)} if the categoryGamma is not valid,
     * or with status {@code 404 (Not Found)} if the categoryGamma is not found,
     * or with status {@code 500 (Internal Server Error)} if the categoryGamma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CategoryGamma> partialUpdateCategoryGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CategoryGamma categoryGamma
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CategoryGamma partially : {}, {}", id, categoryGamma);
        if (categoryGamma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoryGamma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoryGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CategoryGamma> result = categoryGammaService.partialUpdate(categoryGamma);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoryGamma.getId().toString())
        );
    }

    /**
     * {@code GET  /category-gammas} : get all the categoryGammas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categoryGammas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CategoryGamma>> getAllCategoryGammas(
        CategoryGammaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CategoryGammas by criteria: {}", criteria);

        Page<CategoryGamma> page = categoryGammaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /category-gammas/count} : count all the categoryGammas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCategoryGammas(CategoryGammaCriteria criteria) {
        LOG.debug("REST request to count CategoryGammas by criteria: {}", criteria);
        return ResponseEntity.ok().body(categoryGammaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /category-gammas/:id} : get the "id" categoryGamma.
     *
     * @param id the id of the categoryGamma to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categoryGamma, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryGamma> getCategoryGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CategoryGamma : {}", id);
        Optional<CategoryGamma> categoryGamma = categoryGammaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(categoryGamma);
    }

    /**
     * {@code DELETE  /category-gammas/:id} : delete the "id" categoryGamma.
     *
     * @param id the id of the categoryGamma to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoryGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CategoryGamma : {}", id);
        categoryGammaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
