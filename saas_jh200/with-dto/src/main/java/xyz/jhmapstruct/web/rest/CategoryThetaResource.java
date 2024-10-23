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
import xyz.jhmapstruct.repository.CategoryThetaRepository;
import xyz.jhmapstruct.service.CategoryThetaQueryService;
import xyz.jhmapstruct.service.CategoryThetaService;
import xyz.jhmapstruct.service.criteria.CategoryThetaCriteria;
import xyz.jhmapstruct.service.dto.CategoryThetaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.CategoryTheta}.
 */
@RestController
@RequestMapping("/api/category-thetas")
public class CategoryThetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryThetaResource.class);

    private static final String ENTITY_NAME = "categoryTheta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategoryThetaService categoryThetaService;

    private final CategoryThetaRepository categoryThetaRepository;

    private final CategoryThetaQueryService categoryThetaQueryService;

    public CategoryThetaResource(
        CategoryThetaService categoryThetaService,
        CategoryThetaRepository categoryThetaRepository,
        CategoryThetaQueryService categoryThetaQueryService
    ) {
        this.categoryThetaService = categoryThetaService;
        this.categoryThetaRepository = categoryThetaRepository;
        this.categoryThetaQueryService = categoryThetaQueryService;
    }

    /**
     * {@code POST  /category-thetas} : Create a new categoryTheta.
     *
     * @param categoryThetaDTO the categoryThetaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categoryThetaDTO, or with status {@code 400 (Bad Request)} if the categoryTheta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CategoryThetaDTO> createCategoryTheta(@Valid @RequestBody CategoryThetaDTO categoryThetaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CategoryTheta : {}", categoryThetaDTO);
        if (categoryThetaDTO.getId() != null) {
            throw new BadRequestAlertException("A new categoryTheta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        categoryThetaDTO = categoryThetaService.save(categoryThetaDTO);
        return ResponseEntity.created(new URI("/api/category-thetas/" + categoryThetaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, categoryThetaDTO.getId().toString()))
            .body(categoryThetaDTO);
    }

    /**
     * {@code PUT  /category-thetas/:id} : Updates an existing categoryTheta.
     *
     * @param id the id of the categoryThetaDTO to save.
     * @param categoryThetaDTO the categoryThetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoryThetaDTO,
     * or with status {@code 400 (Bad Request)} if the categoryThetaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the categoryThetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryThetaDTO> updateCategoryTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CategoryThetaDTO categoryThetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CategoryTheta : {}, {}", id, categoryThetaDTO);
        if (categoryThetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoryThetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoryThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        categoryThetaDTO = categoryThetaService.update(categoryThetaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoryThetaDTO.getId().toString()))
            .body(categoryThetaDTO);
    }

    /**
     * {@code PATCH  /category-thetas/:id} : Partial updates given fields of an existing categoryTheta, field will ignore if it is null
     *
     * @param id the id of the categoryThetaDTO to save.
     * @param categoryThetaDTO the categoryThetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoryThetaDTO,
     * or with status {@code 400 (Bad Request)} if the categoryThetaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the categoryThetaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the categoryThetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CategoryThetaDTO> partialUpdateCategoryTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CategoryThetaDTO categoryThetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CategoryTheta partially : {}, {}", id, categoryThetaDTO);
        if (categoryThetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoryThetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoryThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CategoryThetaDTO> result = categoryThetaService.partialUpdate(categoryThetaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoryThetaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /category-thetas} : get all the categoryThetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categoryThetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CategoryThetaDTO>> getAllCategoryThetas(
        CategoryThetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CategoryThetas by criteria: {}", criteria);

        Page<CategoryThetaDTO> page = categoryThetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /category-thetas/count} : count all the categoryThetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCategoryThetas(CategoryThetaCriteria criteria) {
        LOG.debug("REST request to count CategoryThetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(categoryThetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /category-thetas/:id} : get the "id" categoryTheta.
     *
     * @param id the id of the categoryThetaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categoryThetaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryThetaDTO> getCategoryTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CategoryTheta : {}", id);
        Optional<CategoryThetaDTO> categoryThetaDTO = categoryThetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(categoryThetaDTO);
    }

    /**
     * {@code DELETE  /category-thetas/:id} : delete the "id" categoryTheta.
     *
     * @param id the id of the categoryThetaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoryTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CategoryTheta : {}", id);
        categoryThetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
