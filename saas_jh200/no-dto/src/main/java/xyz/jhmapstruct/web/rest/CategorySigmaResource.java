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
import xyz.jhmapstruct.domain.CategorySigma;
import xyz.jhmapstruct.repository.CategorySigmaRepository;
import xyz.jhmapstruct.service.CategorySigmaQueryService;
import xyz.jhmapstruct.service.CategorySigmaService;
import xyz.jhmapstruct.service.criteria.CategorySigmaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.CategorySigma}.
 */
@RestController
@RequestMapping("/api/category-sigmas")
public class CategorySigmaResource {

    private static final Logger LOG = LoggerFactory.getLogger(CategorySigmaResource.class);

    private static final String ENTITY_NAME = "categorySigma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategorySigmaService categorySigmaService;

    private final CategorySigmaRepository categorySigmaRepository;

    private final CategorySigmaQueryService categorySigmaQueryService;

    public CategorySigmaResource(
        CategorySigmaService categorySigmaService,
        CategorySigmaRepository categorySigmaRepository,
        CategorySigmaQueryService categorySigmaQueryService
    ) {
        this.categorySigmaService = categorySigmaService;
        this.categorySigmaRepository = categorySigmaRepository;
        this.categorySigmaQueryService = categorySigmaQueryService;
    }

    /**
     * {@code POST  /category-sigmas} : Create a new categorySigma.
     *
     * @param categorySigma the categorySigma to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categorySigma, or with status {@code 400 (Bad Request)} if the categorySigma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CategorySigma> createCategorySigma(@Valid @RequestBody CategorySigma categorySigma) throws URISyntaxException {
        LOG.debug("REST request to save CategorySigma : {}", categorySigma);
        if (categorySigma.getId() != null) {
            throw new BadRequestAlertException("A new categorySigma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        categorySigma = categorySigmaService.save(categorySigma);
        return ResponseEntity.created(new URI("/api/category-sigmas/" + categorySigma.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, categorySigma.getId().toString()))
            .body(categorySigma);
    }

    /**
     * {@code PUT  /category-sigmas/:id} : Updates an existing categorySigma.
     *
     * @param id the id of the categorySigma to save.
     * @param categorySigma the categorySigma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categorySigma,
     * or with status {@code 400 (Bad Request)} if the categorySigma is not valid,
     * or with status {@code 500 (Internal Server Error)} if the categorySigma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategorySigma> updateCategorySigma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CategorySigma categorySigma
    ) throws URISyntaxException {
        LOG.debug("REST request to update CategorySigma : {}, {}", id, categorySigma);
        if (categorySigma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categorySigma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categorySigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        categorySigma = categorySigmaService.update(categorySigma);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categorySigma.getId().toString()))
            .body(categorySigma);
    }

    /**
     * {@code PATCH  /category-sigmas/:id} : Partial updates given fields of an existing categorySigma, field will ignore if it is null
     *
     * @param id the id of the categorySigma to save.
     * @param categorySigma the categorySigma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categorySigma,
     * or with status {@code 400 (Bad Request)} if the categorySigma is not valid,
     * or with status {@code 404 (Not Found)} if the categorySigma is not found,
     * or with status {@code 500 (Internal Server Error)} if the categorySigma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CategorySigma> partialUpdateCategorySigma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CategorySigma categorySigma
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CategorySigma partially : {}, {}", id, categorySigma);
        if (categorySigma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categorySigma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categorySigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CategorySigma> result = categorySigmaService.partialUpdate(categorySigma);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categorySigma.getId().toString())
        );
    }

    /**
     * {@code GET  /category-sigmas} : get all the categorySigmas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categorySigmas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CategorySigma>> getAllCategorySigmas(
        CategorySigmaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CategorySigmas by criteria: {}", criteria);

        Page<CategorySigma> page = categorySigmaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /category-sigmas/count} : count all the categorySigmas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCategorySigmas(CategorySigmaCriteria criteria) {
        LOG.debug("REST request to count CategorySigmas by criteria: {}", criteria);
        return ResponseEntity.ok().body(categorySigmaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /category-sigmas/:id} : get the "id" categorySigma.
     *
     * @param id the id of the categorySigma to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categorySigma, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategorySigma> getCategorySigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CategorySigma : {}", id);
        Optional<CategorySigma> categorySigma = categorySigmaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(categorySigma);
    }

    /**
     * {@code DELETE  /category-sigmas/:id} : delete the "id" categorySigma.
     *
     * @param id the id of the categorySigma to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategorySigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CategorySigma : {}", id);
        categorySigmaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
