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
import xyz.jhmapstruct.repository.CategoryViViRepository;
import xyz.jhmapstruct.service.CategoryViViService;
import xyz.jhmapstruct.service.dto.CategoryViViDTO;
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

    public CategoryViViResource(CategoryViViService categoryViViService, CategoryViViRepository categoryViViRepository) {
        this.categoryViViService = categoryViViService;
        this.categoryViViRepository = categoryViViRepository;
    }

    /**
     * {@code POST  /category-vi-vis} : Create a new categoryViVi.
     *
     * @param categoryViViDTO the categoryViViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categoryViViDTO, or with status {@code 400 (Bad Request)} if the categoryViVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CategoryViViDTO> createCategoryViVi(@Valid @RequestBody CategoryViViDTO categoryViViDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CategoryViVi : {}", categoryViViDTO);
        if (categoryViViDTO.getId() != null) {
            throw new BadRequestAlertException("A new categoryViVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        categoryViViDTO = categoryViViService.save(categoryViViDTO);
        return ResponseEntity.created(new URI("/api/category-vi-vis/" + categoryViViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, categoryViViDTO.getId().toString()))
            .body(categoryViViDTO);
    }

    /**
     * {@code PUT  /category-vi-vis/:id} : Updates an existing categoryViVi.
     *
     * @param id the id of the categoryViViDTO to save.
     * @param categoryViViDTO the categoryViViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoryViViDTO,
     * or with status {@code 400 (Bad Request)} if the categoryViViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the categoryViViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryViViDTO> updateCategoryViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CategoryViViDTO categoryViViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CategoryViVi : {}, {}", id, categoryViViDTO);
        if (categoryViViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoryViViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoryViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        categoryViViDTO = categoryViViService.update(categoryViViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoryViViDTO.getId().toString()))
            .body(categoryViViDTO);
    }

    /**
     * {@code PATCH  /category-vi-vis/:id} : Partial updates given fields of an existing categoryViVi, field will ignore if it is null
     *
     * @param id the id of the categoryViViDTO to save.
     * @param categoryViViDTO the categoryViViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoryViViDTO,
     * or with status {@code 400 (Bad Request)} if the categoryViViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the categoryViViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the categoryViViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CategoryViViDTO> partialUpdateCategoryViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CategoryViViDTO categoryViViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CategoryViVi partially : {}, {}", id, categoryViViDTO);
        if (categoryViViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoryViViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoryViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CategoryViViDTO> result = categoryViViService.partialUpdate(categoryViViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoryViViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /category-vi-vis} : get all the categoryViVis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categoryViVis in body.
     */
    @GetMapping("")
    public List<CategoryViViDTO> getAllCategoryViVis() {
        LOG.debug("REST request to get all CategoryViVis");
        return categoryViViService.findAll();
    }

    /**
     * {@code GET  /category-vi-vis/:id} : get the "id" categoryViVi.
     *
     * @param id the id of the categoryViViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categoryViViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryViViDTO> getCategoryViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CategoryViVi : {}", id);
        Optional<CategoryViViDTO> categoryViViDTO = categoryViViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(categoryViViDTO);
    }

    /**
     * {@code DELETE  /category-vi-vis/:id} : delete the "id" categoryViVi.
     *
     * @param id the id of the categoryViViDTO to delete.
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
