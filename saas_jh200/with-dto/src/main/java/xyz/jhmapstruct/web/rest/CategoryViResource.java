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
import xyz.jhmapstruct.repository.CategoryViRepository;
import xyz.jhmapstruct.service.CategoryViQueryService;
import xyz.jhmapstruct.service.CategoryViService;
import xyz.jhmapstruct.service.criteria.CategoryViCriteria;
import xyz.jhmapstruct.service.dto.CategoryViDTO;
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

    private final CategoryViQueryService categoryViQueryService;

    public CategoryViResource(
        CategoryViService categoryViService,
        CategoryViRepository categoryViRepository,
        CategoryViQueryService categoryViQueryService
    ) {
        this.categoryViService = categoryViService;
        this.categoryViRepository = categoryViRepository;
        this.categoryViQueryService = categoryViQueryService;
    }

    /**
     * {@code POST  /category-vis} : Create a new categoryVi.
     *
     * @param categoryViDTO the categoryViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categoryViDTO, or with status {@code 400 (Bad Request)} if the categoryVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CategoryViDTO> createCategoryVi(@Valid @RequestBody CategoryViDTO categoryViDTO) throws URISyntaxException {
        LOG.debug("REST request to save CategoryVi : {}", categoryViDTO);
        if (categoryViDTO.getId() != null) {
            throw new BadRequestAlertException("A new categoryVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        categoryViDTO = categoryViService.save(categoryViDTO);
        return ResponseEntity.created(new URI("/api/category-vis/" + categoryViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, categoryViDTO.getId().toString()))
            .body(categoryViDTO);
    }

    /**
     * {@code PUT  /category-vis/:id} : Updates an existing categoryVi.
     *
     * @param id the id of the categoryViDTO to save.
     * @param categoryViDTO the categoryViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoryViDTO,
     * or with status {@code 400 (Bad Request)} if the categoryViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the categoryViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryViDTO> updateCategoryVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CategoryViDTO categoryViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CategoryVi : {}, {}", id, categoryViDTO);
        if (categoryViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoryViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoryViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        categoryViDTO = categoryViService.update(categoryViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoryViDTO.getId().toString()))
            .body(categoryViDTO);
    }

    /**
     * {@code PATCH  /category-vis/:id} : Partial updates given fields of an existing categoryVi, field will ignore if it is null
     *
     * @param id the id of the categoryViDTO to save.
     * @param categoryViDTO the categoryViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoryViDTO,
     * or with status {@code 400 (Bad Request)} if the categoryViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the categoryViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the categoryViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CategoryViDTO> partialUpdateCategoryVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CategoryViDTO categoryViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CategoryVi partially : {}, {}", id, categoryViDTO);
        if (categoryViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoryViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoryViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CategoryViDTO> result = categoryViService.partialUpdate(categoryViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoryViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /category-vis} : get all the categoryVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categoryVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CategoryViDTO>> getAllCategoryVis(
        CategoryViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CategoryVis by criteria: {}", criteria);

        Page<CategoryViDTO> page = categoryViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /category-vis/count} : count all the categoryVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCategoryVis(CategoryViCriteria criteria) {
        LOG.debug("REST request to count CategoryVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(categoryViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /category-vis/:id} : get the "id" categoryVi.
     *
     * @param id the id of the categoryViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categoryViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryViDTO> getCategoryVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CategoryVi : {}", id);
        Optional<CategoryViDTO> categoryViDTO = categoryViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(categoryViDTO);
    }

    /**
     * {@code DELETE  /category-vis/:id} : delete the "id" categoryVi.
     *
     * @param id the id of the categoryViDTO to delete.
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
