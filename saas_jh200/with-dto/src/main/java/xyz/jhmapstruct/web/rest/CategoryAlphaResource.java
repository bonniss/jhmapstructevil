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
import xyz.jhmapstruct.repository.CategoryAlphaRepository;
import xyz.jhmapstruct.service.CategoryAlphaQueryService;
import xyz.jhmapstruct.service.CategoryAlphaService;
import xyz.jhmapstruct.service.criteria.CategoryAlphaCriteria;
import xyz.jhmapstruct.service.dto.CategoryAlphaDTO;
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
     * @param categoryAlphaDTO the categoryAlphaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categoryAlphaDTO, or with status {@code 400 (Bad Request)} if the categoryAlpha has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CategoryAlphaDTO> createCategoryAlpha(@Valid @RequestBody CategoryAlphaDTO categoryAlphaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CategoryAlpha : {}", categoryAlphaDTO);
        if (categoryAlphaDTO.getId() != null) {
            throw new BadRequestAlertException("A new categoryAlpha cannot already have an ID", ENTITY_NAME, "idexists");
        }
        categoryAlphaDTO = categoryAlphaService.save(categoryAlphaDTO);
        return ResponseEntity.created(new URI("/api/category-alphas/" + categoryAlphaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, categoryAlphaDTO.getId().toString()))
            .body(categoryAlphaDTO);
    }

    /**
     * {@code PUT  /category-alphas/:id} : Updates an existing categoryAlpha.
     *
     * @param id the id of the categoryAlphaDTO to save.
     * @param categoryAlphaDTO the categoryAlphaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoryAlphaDTO,
     * or with status {@code 400 (Bad Request)} if the categoryAlphaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the categoryAlphaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryAlphaDTO> updateCategoryAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CategoryAlphaDTO categoryAlphaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CategoryAlpha : {}, {}", id, categoryAlphaDTO);
        if (categoryAlphaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoryAlphaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoryAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        categoryAlphaDTO = categoryAlphaService.update(categoryAlphaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoryAlphaDTO.getId().toString()))
            .body(categoryAlphaDTO);
    }

    /**
     * {@code PATCH  /category-alphas/:id} : Partial updates given fields of an existing categoryAlpha, field will ignore if it is null
     *
     * @param id the id of the categoryAlphaDTO to save.
     * @param categoryAlphaDTO the categoryAlphaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoryAlphaDTO,
     * or with status {@code 400 (Bad Request)} if the categoryAlphaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the categoryAlphaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the categoryAlphaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CategoryAlphaDTO> partialUpdateCategoryAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CategoryAlphaDTO categoryAlphaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CategoryAlpha partially : {}, {}", id, categoryAlphaDTO);
        if (categoryAlphaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoryAlphaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoryAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CategoryAlphaDTO> result = categoryAlphaService.partialUpdate(categoryAlphaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoryAlphaDTO.getId().toString())
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
    public ResponseEntity<List<CategoryAlphaDTO>> getAllCategoryAlphas(
        CategoryAlphaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CategoryAlphas by criteria: {}", criteria);

        Page<CategoryAlphaDTO> page = categoryAlphaQueryService.findByCriteria(criteria, pageable);
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
     * @param id the id of the categoryAlphaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categoryAlphaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryAlphaDTO> getCategoryAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CategoryAlpha : {}", id);
        Optional<CategoryAlphaDTO> categoryAlphaDTO = categoryAlphaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(categoryAlphaDTO);
    }

    /**
     * {@code DELETE  /category-alphas/:id} : delete the "id" categoryAlpha.
     *
     * @param id the id of the categoryAlphaDTO to delete.
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
