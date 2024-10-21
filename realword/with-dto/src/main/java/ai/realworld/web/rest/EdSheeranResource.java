package ai.realworld.web.rest;

import ai.realworld.repository.EdSheeranRepository;
import ai.realworld.service.EdSheeranQueryService;
import ai.realworld.service.EdSheeranService;
import ai.realworld.service.criteria.EdSheeranCriteria;
import ai.realworld.service.dto.EdSheeranDTO;
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
 * REST controller for managing {@link ai.realworld.domain.EdSheeran}.
 */
@RestController
@RequestMapping("/api/ed-sheerans")
public class EdSheeranResource {

    private static final Logger LOG = LoggerFactory.getLogger(EdSheeranResource.class);

    private static final String ENTITY_NAME = "edSheeran";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EdSheeranService edSheeranService;

    private final EdSheeranRepository edSheeranRepository;

    private final EdSheeranQueryService edSheeranQueryService;

    public EdSheeranResource(
        EdSheeranService edSheeranService,
        EdSheeranRepository edSheeranRepository,
        EdSheeranQueryService edSheeranQueryService
    ) {
        this.edSheeranService = edSheeranService;
        this.edSheeranRepository = edSheeranRepository;
        this.edSheeranQueryService = edSheeranQueryService;
    }

    /**
     * {@code POST  /ed-sheerans} : Create a new edSheeran.
     *
     * @param edSheeranDTO the edSheeranDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new edSheeranDTO, or with status {@code 400 (Bad Request)} if the edSheeran has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EdSheeranDTO> createEdSheeran(@Valid @RequestBody EdSheeranDTO edSheeranDTO) throws URISyntaxException {
        LOG.debug("REST request to save EdSheeran : {}", edSheeranDTO);
        if (edSheeranDTO.getId() != null) {
            throw new BadRequestAlertException("A new edSheeran cannot already have an ID", ENTITY_NAME, "idexists");
        }
        edSheeranDTO = edSheeranService.save(edSheeranDTO);
        return ResponseEntity.created(new URI("/api/ed-sheerans/" + edSheeranDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, edSheeranDTO.getId().toString()))
            .body(edSheeranDTO);
    }

    /**
     * {@code PUT  /ed-sheerans/:id} : Updates an existing edSheeran.
     *
     * @param id the id of the edSheeranDTO to save.
     * @param edSheeranDTO the edSheeranDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated edSheeranDTO,
     * or with status {@code 400 (Bad Request)} if the edSheeranDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the edSheeranDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EdSheeranDTO> updateEdSheeran(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EdSheeranDTO edSheeranDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EdSheeran : {}, {}", id, edSheeranDTO);
        if (edSheeranDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, edSheeranDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!edSheeranRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        edSheeranDTO = edSheeranService.update(edSheeranDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, edSheeranDTO.getId().toString()))
            .body(edSheeranDTO);
    }

    /**
     * {@code PATCH  /ed-sheerans/:id} : Partial updates given fields of an existing edSheeran, field will ignore if it is null
     *
     * @param id the id of the edSheeranDTO to save.
     * @param edSheeranDTO the edSheeranDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated edSheeranDTO,
     * or with status {@code 400 (Bad Request)} if the edSheeranDTO is not valid,
     * or with status {@code 404 (Not Found)} if the edSheeranDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the edSheeranDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EdSheeranDTO> partialUpdateEdSheeran(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EdSheeranDTO edSheeranDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EdSheeran partially : {}, {}", id, edSheeranDTO);
        if (edSheeranDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, edSheeranDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!edSheeranRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EdSheeranDTO> result = edSheeranService.partialUpdate(edSheeranDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, edSheeranDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ed-sheerans} : get all the edSheerans.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of edSheerans in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EdSheeranDTO>> getAllEdSheerans(
        EdSheeranCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get EdSheerans by criteria: {}", criteria);

        Page<EdSheeranDTO> page = edSheeranQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ed-sheerans/count} : count all the edSheerans.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEdSheerans(EdSheeranCriteria criteria) {
        LOG.debug("REST request to count EdSheerans by criteria: {}", criteria);
        return ResponseEntity.ok().body(edSheeranQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ed-sheerans/:id} : get the "id" edSheeran.
     *
     * @param id the id of the edSheeranDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the edSheeranDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EdSheeranDTO> getEdSheeran(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EdSheeran : {}", id);
        Optional<EdSheeranDTO> edSheeranDTO = edSheeranService.findOne(id);
        return ResponseUtil.wrapOrNotFound(edSheeranDTO);
    }

    /**
     * {@code DELETE  /ed-sheerans/:id} : delete the "id" edSheeran.
     *
     * @param id the id of the edSheeranDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEdSheeran(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EdSheeran : {}", id);
        edSheeranService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
