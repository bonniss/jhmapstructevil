package ai.realworld.web.rest;

import ai.realworld.repository.AlBestToothViRepository;
import ai.realworld.service.AlBestToothViQueryService;
import ai.realworld.service.AlBestToothViService;
import ai.realworld.service.criteria.AlBestToothViCriteria;
import ai.realworld.service.dto.AlBestToothViDTO;
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
 * REST controller for managing {@link ai.realworld.domain.AlBestToothVi}.
 */
@RestController
@RequestMapping("/api/al-best-tooth-vis")
public class AlBestToothViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlBestToothViResource.class);

    private static final String ENTITY_NAME = "alBestToothVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlBestToothViService alBestToothViService;

    private final AlBestToothViRepository alBestToothViRepository;

    private final AlBestToothViQueryService alBestToothViQueryService;

    public AlBestToothViResource(
        AlBestToothViService alBestToothViService,
        AlBestToothViRepository alBestToothViRepository,
        AlBestToothViQueryService alBestToothViQueryService
    ) {
        this.alBestToothViService = alBestToothViService;
        this.alBestToothViRepository = alBestToothViRepository;
        this.alBestToothViQueryService = alBestToothViQueryService;
    }

    /**
     * {@code POST  /al-best-tooth-vis} : Create a new alBestToothVi.
     *
     * @param alBestToothViDTO the alBestToothViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alBestToothViDTO, or with status {@code 400 (Bad Request)} if the alBestToothVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlBestToothViDTO> createAlBestToothVi(@Valid @RequestBody AlBestToothViDTO alBestToothViDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AlBestToothVi : {}", alBestToothViDTO);
        if (alBestToothViDTO.getId() != null) {
            throw new BadRequestAlertException("A new alBestToothVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alBestToothViDTO = alBestToothViService.save(alBestToothViDTO);
        return ResponseEntity.created(new URI("/api/al-best-tooth-vis/" + alBestToothViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alBestToothViDTO.getId().toString()))
            .body(alBestToothViDTO);
    }

    /**
     * {@code PUT  /al-best-tooth-vis/:id} : Updates an existing alBestToothVi.
     *
     * @param id the id of the alBestToothViDTO to save.
     * @param alBestToothViDTO the alBestToothViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alBestToothViDTO,
     * or with status {@code 400 (Bad Request)} if the alBestToothViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alBestToothViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlBestToothViDTO> updateAlBestToothVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlBestToothViDTO alBestToothViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlBestToothVi : {}, {}", id, alBestToothViDTO);
        if (alBestToothViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alBestToothViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alBestToothViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alBestToothViDTO = alBestToothViService.update(alBestToothViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alBestToothViDTO.getId().toString()))
            .body(alBestToothViDTO);
    }

    /**
     * {@code PATCH  /al-best-tooth-vis/:id} : Partial updates given fields of an existing alBestToothVi, field will ignore if it is null
     *
     * @param id the id of the alBestToothViDTO to save.
     * @param alBestToothViDTO the alBestToothViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alBestToothViDTO,
     * or with status {@code 400 (Bad Request)} if the alBestToothViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alBestToothViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alBestToothViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlBestToothViDTO> partialUpdateAlBestToothVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlBestToothViDTO alBestToothViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlBestToothVi partially : {}, {}", id, alBestToothViDTO);
        if (alBestToothViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alBestToothViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alBestToothViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlBestToothViDTO> result = alBestToothViService.partialUpdate(alBestToothViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alBestToothViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-best-tooth-vis} : get all the alBestToothVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alBestToothVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlBestToothViDTO>> getAllAlBestToothVis(
        AlBestToothViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlBestToothVis by criteria: {}", criteria);

        Page<AlBestToothViDTO> page = alBestToothViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-best-tooth-vis/count} : count all the alBestToothVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlBestToothVis(AlBestToothViCriteria criteria) {
        LOG.debug("REST request to count AlBestToothVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alBestToothViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-best-tooth-vis/:id} : get the "id" alBestToothVi.
     *
     * @param id the id of the alBestToothViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alBestToothViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlBestToothViDTO> getAlBestToothVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlBestToothVi : {}", id);
        Optional<AlBestToothViDTO> alBestToothViDTO = alBestToothViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alBestToothViDTO);
    }

    /**
     * {@code DELETE  /al-best-tooth-vis/:id} : delete the "id" alBestToothVi.
     *
     * @param id the id of the alBestToothViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlBestToothVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlBestToothVi : {}", id);
        alBestToothViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
