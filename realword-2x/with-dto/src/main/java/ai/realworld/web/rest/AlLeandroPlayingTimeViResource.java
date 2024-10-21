package ai.realworld.web.rest;

import ai.realworld.repository.AlLeandroPlayingTimeViRepository;
import ai.realworld.service.AlLeandroPlayingTimeViQueryService;
import ai.realworld.service.AlLeandroPlayingTimeViService;
import ai.realworld.service.criteria.AlLeandroPlayingTimeViCriteria;
import ai.realworld.service.dto.AlLeandroPlayingTimeViDTO;
import ai.realworld.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
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
 * REST controller for managing {@link ai.realworld.domain.AlLeandroPlayingTimeVi}.
 */
@RestController
@RequestMapping("/api/al-leandro-playing-time-vis")
public class AlLeandroPlayingTimeViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlLeandroPlayingTimeViResource.class);

    private static final String ENTITY_NAME = "alLeandroPlayingTimeVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlLeandroPlayingTimeViService alLeandroPlayingTimeViService;

    private final AlLeandroPlayingTimeViRepository alLeandroPlayingTimeViRepository;

    private final AlLeandroPlayingTimeViQueryService alLeandroPlayingTimeViQueryService;

    public AlLeandroPlayingTimeViResource(
        AlLeandroPlayingTimeViService alLeandroPlayingTimeViService,
        AlLeandroPlayingTimeViRepository alLeandroPlayingTimeViRepository,
        AlLeandroPlayingTimeViQueryService alLeandroPlayingTimeViQueryService
    ) {
        this.alLeandroPlayingTimeViService = alLeandroPlayingTimeViService;
        this.alLeandroPlayingTimeViRepository = alLeandroPlayingTimeViRepository;
        this.alLeandroPlayingTimeViQueryService = alLeandroPlayingTimeViQueryService;
    }

    /**
     * {@code POST  /al-leandro-playing-time-vis} : Create a new alLeandroPlayingTimeVi.
     *
     * @param alLeandroPlayingTimeViDTO the alLeandroPlayingTimeViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alLeandroPlayingTimeViDTO, or with status {@code 400 (Bad Request)} if the alLeandroPlayingTimeVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlLeandroPlayingTimeViDTO> createAlLeandroPlayingTimeVi(
        @RequestBody AlLeandroPlayingTimeViDTO alLeandroPlayingTimeViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save AlLeandroPlayingTimeVi : {}", alLeandroPlayingTimeViDTO);
        if (alLeandroPlayingTimeViDTO.getId() != null) {
            throw new BadRequestAlertException("A new alLeandroPlayingTimeVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alLeandroPlayingTimeViDTO = alLeandroPlayingTimeViService.save(alLeandroPlayingTimeViDTO);
        return ResponseEntity.created(new URI("/api/al-leandro-playing-time-vis/" + alLeandroPlayingTimeViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alLeandroPlayingTimeViDTO.getId().toString()))
            .body(alLeandroPlayingTimeViDTO);
    }

    /**
     * {@code PUT  /al-leandro-playing-time-vis/:id} : Updates an existing alLeandroPlayingTimeVi.
     *
     * @param id the id of the alLeandroPlayingTimeViDTO to save.
     * @param alLeandroPlayingTimeViDTO the alLeandroPlayingTimeViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alLeandroPlayingTimeViDTO,
     * or with status {@code 400 (Bad Request)} if the alLeandroPlayingTimeViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alLeandroPlayingTimeViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlLeandroPlayingTimeViDTO> updateAlLeandroPlayingTimeVi(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody AlLeandroPlayingTimeViDTO alLeandroPlayingTimeViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlLeandroPlayingTimeVi : {}, {}", id, alLeandroPlayingTimeViDTO);
        if (alLeandroPlayingTimeViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alLeandroPlayingTimeViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alLeandroPlayingTimeViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alLeandroPlayingTimeViDTO = alLeandroPlayingTimeViService.update(alLeandroPlayingTimeViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alLeandroPlayingTimeViDTO.getId().toString()))
            .body(alLeandroPlayingTimeViDTO);
    }

    /**
     * {@code PATCH  /al-leandro-playing-time-vis/:id} : Partial updates given fields of an existing alLeandroPlayingTimeVi, field will ignore if it is null
     *
     * @param id the id of the alLeandroPlayingTimeViDTO to save.
     * @param alLeandroPlayingTimeViDTO the alLeandroPlayingTimeViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alLeandroPlayingTimeViDTO,
     * or with status {@code 400 (Bad Request)} if the alLeandroPlayingTimeViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alLeandroPlayingTimeViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alLeandroPlayingTimeViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlLeandroPlayingTimeViDTO> partialUpdateAlLeandroPlayingTimeVi(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody AlLeandroPlayingTimeViDTO alLeandroPlayingTimeViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlLeandroPlayingTimeVi partially : {}, {}", id, alLeandroPlayingTimeViDTO);
        if (alLeandroPlayingTimeViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alLeandroPlayingTimeViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alLeandroPlayingTimeViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlLeandroPlayingTimeViDTO> result = alLeandroPlayingTimeViService.partialUpdate(alLeandroPlayingTimeViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alLeandroPlayingTimeViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-leandro-playing-time-vis} : get all the alLeandroPlayingTimeVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alLeandroPlayingTimeVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlLeandroPlayingTimeViDTO>> getAllAlLeandroPlayingTimeVis(
        AlLeandroPlayingTimeViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlLeandroPlayingTimeVis by criteria: {}", criteria);

        Page<AlLeandroPlayingTimeViDTO> page = alLeandroPlayingTimeViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-leandro-playing-time-vis/count} : count all the alLeandroPlayingTimeVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlLeandroPlayingTimeVis(AlLeandroPlayingTimeViCriteria criteria) {
        LOG.debug("REST request to count AlLeandroPlayingTimeVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alLeandroPlayingTimeViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-leandro-playing-time-vis/:id} : get the "id" alLeandroPlayingTimeVi.
     *
     * @param id the id of the alLeandroPlayingTimeViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alLeandroPlayingTimeViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlLeandroPlayingTimeViDTO> getAlLeandroPlayingTimeVi(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AlLeandroPlayingTimeVi : {}", id);
        Optional<AlLeandroPlayingTimeViDTO> alLeandroPlayingTimeViDTO = alLeandroPlayingTimeViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alLeandroPlayingTimeViDTO);
    }

    /**
     * {@code DELETE  /al-leandro-playing-time-vis/:id} : delete the "id" alLeandroPlayingTimeVi.
     *
     * @param id the id of the alLeandroPlayingTimeViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlLeandroPlayingTimeVi(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AlLeandroPlayingTimeVi : {}", id);
        alLeandroPlayingTimeViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
