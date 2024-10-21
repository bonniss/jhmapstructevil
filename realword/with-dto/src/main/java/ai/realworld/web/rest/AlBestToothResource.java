package ai.realworld.web.rest;

import ai.realworld.repository.AlBestToothRepository;
import ai.realworld.service.AlBestToothQueryService;
import ai.realworld.service.AlBestToothService;
import ai.realworld.service.criteria.AlBestToothCriteria;
import ai.realworld.service.dto.AlBestToothDTO;
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
 * REST controller for managing {@link ai.realworld.domain.AlBestTooth}.
 */
@RestController
@RequestMapping("/api/al-best-tooths")
public class AlBestToothResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlBestToothResource.class);

    private static final String ENTITY_NAME = "alBestTooth";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlBestToothService alBestToothService;

    private final AlBestToothRepository alBestToothRepository;

    private final AlBestToothQueryService alBestToothQueryService;

    public AlBestToothResource(
        AlBestToothService alBestToothService,
        AlBestToothRepository alBestToothRepository,
        AlBestToothQueryService alBestToothQueryService
    ) {
        this.alBestToothService = alBestToothService;
        this.alBestToothRepository = alBestToothRepository;
        this.alBestToothQueryService = alBestToothQueryService;
    }

    /**
     * {@code POST  /al-best-tooths} : Create a new alBestTooth.
     *
     * @param alBestToothDTO the alBestToothDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alBestToothDTO, or with status {@code 400 (Bad Request)} if the alBestTooth has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlBestToothDTO> createAlBestTooth(@Valid @RequestBody AlBestToothDTO alBestToothDTO) throws URISyntaxException {
        LOG.debug("REST request to save AlBestTooth : {}", alBestToothDTO);
        if (alBestToothDTO.getId() != null) {
            throw new BadRequestAlertException("A new alBestTooth cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alBestToothDTO = alBestToothService.save(alBestToothDTO);
        return ResponseEntity.created(new URI("/api/al-best-tooths/" + alBestToothDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alBestToothDTO.getId().toString()))
            .body(alBestToothDTO);
    }

    /**
     * {@code PUT  /al-best-tooths/:id} : Updates an existing alBestTooth.
     *
     * @param id the id of the alBestToothDTO to save.
     * @param alBestToothDTO the alBestToothDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alBestToothDTO,
     * or with status {@code 400 (Bad Request)} if the alBestToothDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alBestToothDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlBestToothDTO> updateAlBestTooth(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlBestToothDTO alBestToothDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlBestTooth : {}, {}", id, alBestToothDTO);
        if (alBestToothDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alBestToothDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alBestToothRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alBestToothDTO = alBestToothService.update(alBestToothDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alBestToothDTO.getId().toString()))
            .body(alBestToothDTO);
    }

    /**
     * {@code PATCH  /al-best-tooths/:id} : Partial updates given fields of an existing alBestTooth, field will ignore if it is null
     *
     * @param id the id of the alBestToothDTO to save.
     * @param alBestToothDTO the alBestToothDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alBestToothDTO,
     * or with status {@code 400 (Bad Request)} if the alBestToothDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alBestToothDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alBestToothDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlBestToothDTO> partialUpdateAlBestTooth(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlBestToothDTO alBestToothDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlBestTooth partially : {}, {}", id, alBestToothDTO);
        if (alBestToothDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alBestToothDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alBestToothRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlBestToothDTO> result = alBestToothService.partialUpdate(alBestToothDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alBestToothDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-best-tooths} : get all the alBestTooths.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alBestTooths in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlBestToothDTO>> getAllAlBestTooths(
        AlBestToothCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlBestTooths by criteria: {}", criteria);

        Page<AlBestToothDTO> page = alBestToothQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-best-tooths/count} : count all the alBestTooths.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlBestTooths(AlBestToothCriteria criteria) {
        LOG.debug("REST request to count AlBestTooths by criteria: {}", criteria);
        return ResponseEntity.ok().body(alBestToothQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-best-tooths/:id} : get the "id" alBestTooth.
     *
     * @param id the id of the alBestToothDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alBestToothDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlBestToothDTO> getAlBestTooth(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlBestTooth : {}", id);
        Optional<AlBestToothDTO> alBestToothDTO = alBestToothService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alBestToothDTO);
    }

    /**
     * {@code DELETE  /al-best-tooths/:id} : delete the "id" alBestTooth.
     *
     * @param id the id of the alBestToothDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlBestTooth(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlBestTooth : {}", id);
        alBestToothService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
