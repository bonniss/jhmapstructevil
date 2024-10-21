package ai.realworld.web.rest;

import ai.realworld.repository.EdSheeranViRepository;
import ai.realworld.service.EdSheeranViQueryService;
import ai.realworld.service.EdSheeranViService;
import ai.realworld.service.criteria.EdSheeranViCriteria;
import ai.realworld.service.dto.EdSheeranViDTO;
import ai.realworld.web.rest.errors.BadRequestAlertException;
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

/**
 * REST controller for managing {@link ai.realworld.domain.EdSheeranVi}.
 */
@RestController
@RequestMapping("/api/ed-sheeran-vis")
public class EdSheeranViResource {

    private static final Logger LOG = LoggerFactory.getLogger(EdSheeranViResource.class);

    private static final String ENTITY_NAME = "edSheeranVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EdSheeranViService edSheeranViService;

    private final EdSheeranViRepository edSheeranViRepository;

    private final EdSheeranViQueryService edSheeranViQueryService;

    public EdSheeranViResource(
        EdSheeranViService edSheeranViService,
        EdSheeranViRepository edSheeranViRepository,
        EdSheeranViQueryService edSheeranViQueryService
    ) {
        this.edSheeranViService = edSheeranViService;
        this.edSheeranViRepository = edSheeranViRepository;
        this.edSheeranViQueryService = edSheeranViQueryService;
    }

    /**
     * {@code POST  /ed-sheeran-vis} : Create a new edSheeranVi.
     *
     * @param edSheeranViDTO the edSheeranViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new edSheeranViDTO, or with status {@code 400 (Bad Request)} if the edSheeranVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EdSheeranViDTO> createEdSheeranVi(@Valid @RequestBody EdSheeranViDTO edSheeranViDTO) throws URISyntaxException {
        LOG.debug("REST request to save EdSheeranVi : {}", edSheeranViDTO);
        if (edSheeranViDTO.getId() != null) {
            throw new BadRequestAlertException("A new edSheeranVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        edSheeranViDTO = edSheeranViService.save(edSheeranViDTO);
        return ResponseEntity.created(new URI("/api/ed-sheeran-vis/" + edSheeranViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, edSheeranViDTO.getId().toString()))
            .body(edSheeranViDTO);
    }

    /**
     * {@code PUT  /ed-sheeran-vis/:id} : Updates an existing edSheeranVi.
     *
     * @param id the id of the edSheeranViDTO to save.
     * @param edSheeranViDTO the edSheeranViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated edSheeranViDTO,
     * or with status {@code 400 (Bad Request)} if the edSheeranViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the edSheeranViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EdSheeranViDTO> updateEdSheeranVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EdSheeranViDTO edSheeranViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EdSheeranVi : {}, {}", id, edSheeranViDTO);
        if (edSheeranViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, edSheeranViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!edSheeranViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        edSheeranViDTO = edSheeranViService.update(edSheeranViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, edSheeranViDTO.getId().toString()))
            .body(edSheeranViDTO);
    }

    /**
     * {@code PATCH  /ed-sheeran-vis/:id} : Partial updates given fields of an existing edSheeranVi, field will ignore if it is null
     *
     * @param id the id of the edSheeranViDTO to save.
     * @param edSheeranViDTO the edSheeranViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated edSheeranViDTO,
     * or with status {@code 400 (Bad Request)} if the edSheeranViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the edSheeranViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the edSheeranViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EdSheeranViDTO> partialUpdateEdSheeranVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EdSheeranViDTO edSheeranViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EdSheeranVi partially : {}, {}", id, edSheeranViDTO);
        if (edSheeranViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, edSheeranViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!edSheeranViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EdSheeranViDTO> result = edSheeranViService.partialUpdate(edSheeranViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, edSheeranViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ed-sheeran-vis} : get all the edSheeranVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of edSheeranVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EdSheeranViDTO>> getAllEdSheeranVis(
        EdSheeranViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get EdSheeranVis by criteria: {}", criteria);

        Page<EdSheeranViDTO> page = edSheeranViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ed-sheeran-vis/count} : count all the edSheeranVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEdSheeranVis(EdSheeranViCriteria criteria) {
        LOG.debug("REST request to count EdSheeranVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(edSheeranViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ed-sheeran-vis/:id} : get the "id" edSheeranVi.
     *
     * @param id the id of the edSheeranViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the edSheeranViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EdSheeranViDTO> getEdSheeranVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EdSheeranVi : {}", id);
        Optional<EdSheeranViDTO> edSheeranViDTO = edSheeranViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(edSheeranViDTO);
    }

    /**
     * {@code DELETE  /ed-sheeran-vis/:id} : delete the "id" edSheeranVi.
     *
     * @param id the id of the edSheeranViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEdSheeranVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EdSheeranVi : {}", id);
        edSheeranViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
