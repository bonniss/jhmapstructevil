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
import xyz.jhmapstruct.repository.CategoryMiMiRepository;
import xyz.jhmapstruct.service.CategoryMiMiService;
import xyz.jhmapstruct.service.dto.CategoryMiMiDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.CategoryMiMi}.
 */
@RestController
@RequestMapping("/api/category-mi-mis")
public class CategoryMiMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryMiMiResource.class);

    private static final String ENTITY_NAME = "categoryMiMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategoryMiMiService categoryMiMiService;

    private final CategoryMiMiRepository categoryMiMiRepository;

    public CategoryMiMiResource(CategoryMiMiService categoryMiMiService, CategoryMiMiRepository categoryMiMiRepository) {
        this.categoryMiMiService = categoryMiMiService;
        this.categoryMiMiRepository = categoryMiMiRepository;
    }

    /**
     * {@code POST  /category-mi-mis} : Create a new categoryMiMi.
     *
     * @param categoryMiMiDTO the categoryMiMiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categoryMiMiDTO, or with status {@code 400 (Bad Request)} if the categoryMiMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CategoryMiMiDTO> createCategoryMiMi(@Valid @RequestBody CategoryMiMiDTO categoryMiMiDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CategoryMiMi : {}", categoryMiMiDTO);
        if (categoryMiMiDTO.getId() != null) {
            throw new BadRequestAlertException("A new categoryMiMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        categoryMiMiDTO = categoryMiMiService.save(categoryMiMiDTO);
        return ResponseEntity.created(new URI("/api/category-mi-mis/" + categoryMiMiDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, categoryMiMiDTO.getId().toString()))
            .body(categoryMiMiDTO);
    }

    /**
     * {@code PUT  /category-mi-mis/:id} : Updates an existing categoryMiMi.
     *
     * @param id the id of the categoryMiMiDTO to save.
     * @param categoryMiMiDTO the categoryMiMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoryMiMiDTO,
     * or with status {@code 400 (Bad Request)} if the categoryMiMiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the categoryMiMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryMiMiDTO> updateCategoryMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CategoryMiMiDTO categoryMiMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CategoryMiMi : {}, {}", id, categoryMiMiDTO);
        if (categoryMiMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoryMiMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoryMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        categoryMiMiDTO = categoryMiMiService.update(categoryMiMiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoryMiMiDTO.getId().toString()))
            .body(categoryMiMiDTO);
    }

    /**
     * {@code PATCH  /category-mi-mis/:id} : Partial updates given fields of an existing categoryMiMi, field will ignore if it is null
     *
     * @param id the id of the categoryMiMiDTO to save.
     * @param categoryMiMiDTO the categoryMiMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoryMiMiDTO,
     * or with status {@code 400 (Bad Request)} if the categoryMiMiDTO is not valid,
     * or with status {@code 404 (Not Found)} if the categoryMiMiDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the categoryMiMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CategoryMiMiDTO> partialUpdateCategoryMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CategoryMiMiDTO categoryMiMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CategoryMiMi partially : {}, {}", id, categoryMiMiDTO);
        if (categoryMiMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoryMiMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoryMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CategoryMiMiDTO> result = categoryMiMiService.partialUpdate(categoryMiMiDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoryMiMiDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /category-mi-mis} : get all the categoryMiMis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categoryMiMis in body.
     */
    @GetMapping("")
    public List<CategoryMiMiDTO> getAllCategoryMiMis() {
        LOG.debug("REST request to get all CategoryMiMis");
        return categoryMiMiService.findAll();
    }

    /**
     * {@code GET  /category-mi-mis/:id} : get the "id" categoryMiMi.
     *
     * @param id the id of the categoryMiMiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categoryMiMiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryMiMiDTO> getCategoryMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CategoryMiMi : {}", id);
        Optional<CategoryMiMiDTO> categoryMiMiDTO = categoryMiMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(categoryMiMiDTO);
    }

    /**
     * {@code DELETE  /category-mi-mis/:id} : delete the "id" categoryMiMi.
     *
     * @param id the id of the categoryMiMiDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoryMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CategoryMiMi : {}", id);
        categoryMiMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
