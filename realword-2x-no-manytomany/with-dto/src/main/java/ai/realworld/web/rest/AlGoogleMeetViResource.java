package ai.realworld.web.rest;

import ai.realworld.repository.AlGoogleMeetViRepository;
import ai.realworld.service.AlGoogleMeetViQueryService;
import ai.realworld.service.AlGoogleMeetViService;
import ai.realworld.service.criteria.AlGoogleMeetViCriteria;
import ai.realworld.service.dto.AlGoogleMeetViDTO;
import ai.realworld.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * REST controller for managing {@link ai.realworld.domain.AlGoogleMeetVi}.
 */
@RestController
@RequestMapping("/api/al-google-meet-vis")
public class AlGoogleMeetViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlGoogleMeetViResource.class);

    private static final String ENTITY_NAME = "alGoogleMeetVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlGoogleMeetViService alGoogleMeetViService;

    private final AlGoogleMeetViRepository alGoogleMeetViRepository;

    private final AlGoogleMeetViQueryService alGoogleMeetViQueryService;

    public AlGoogleMeetViResource(
        AlGoogleMeetViService alGoogleMeetViService,
        AlGoogleMeetViRepository alGoogleMeetViRepository,
        AlGoogleMeetViQueryService alGoogleMeetViQueryService
    ) {
        this.alGoogleMeetViService = alGoogleMeetViService;
        this.alGoogleMeetViRepository = alGoogleMeetViRepository;
        this.alGoogleMeetViQueryService = alGoogleMeetViQueryService;
    }

    /**
     * {@code POST  /al-google-meet-vis} : Create a new alGoogleMeetVi.
     *
     * @param alGoogleMeetViDTO the alGoogleMeetViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alGoogleMeetViDTO, or with status {@code 400 (Bad Request)} if the alGoogleMeetVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlGoogleMeetViDTO> createAlGoogleMeetVi(@Valid @RequestBody AlGoogleMeetViDTO alGoogleMeetViDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AlGoogleMeetVi : {}", alGoogleMeetViDTO);
        if (alGoogleMeetViDTO.getId() != null) {
            throw new BadRequestAlertException("A new alGoogleMeetVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alGoogleMeetViDTO = alGoogleMeetViService.save(alGoogleMeetViDTO);
        return ResponseEntity.created(new URI("/api/al-google-meet-vis/" + alGoogleMeetViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alGoogleMeetViDTO.getId().toString()))
            .body(alGoogleMeetViDTO);
    }

    /**
     * {@code PUT  /al-google-meet-vis/:id} : Updates an existing alGoogleMeetVi.
     *
     * @param id the id of the alGoogleMeetViDTO to save.
     * @param alGoogleMeetViDTO the alGoogleMeetViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alGoogleMeetViDTO,
     * or with status {@code 400 (Bad Request)} if the alGoogleMeetViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alGoogleMeetViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlGoogleMeetViDTO> updateAlGoogleMeetVi(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody AlGoogleMeetViDTO alGoogleMeetViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlGoogleMeetVi : {}, {}", id, alGoogleMeetViDTO);
        if (alGoogleMeetViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alGoogleMeetViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alGoogleMeetViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alGoogleMeetViDTO = alGoogleMeetViService.update(alGoogleMeetViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alGoogleMeetViDTO.getId().toString()))
            .body(alGoogleMeetViDTO);
    }

    /**
     * {@code PATCH  /al-google-meet-vis/:id} : Partial updates given fields of an existing alGoogleMeetVi, field will ignore if it is null
     *
     * @param id the id of the alGoogleMeetViDTO to save.
     * @param alGoogleMeetViDTO the alGoogleMeetViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alGoogleMeetViDTO,
     * or with status {@code 400 (Bad Request)} if the alGoogleMeetViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alGoogleMeetViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alGoogleMeetViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlGoogleMeetViDTO> partialUpdateAlGoogleMeetVi(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody AlGoogleMeetViDTO alGoogleMeetViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlGoogleMeetVi partially : {}, {}", id, alGoogleMeetViDTO);
        if (alGoogleMeetViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alGoogleMeetViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alGoogleMeetViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlGoogleMeetViDTO> result = alGoogleMeetViService.partialUpdate(alGoogleMeetViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alGoogleMeetViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-google-meet-vis} : get all the alGoogleMeetVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alGoogleMeetVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlGoogleMeetViDTO>> getAllAlGoogleMeetVis(
        AlGoogleMeetViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlGoogleMeetVis by criteria: {}", criteria);

        Page<AlGoogleMeetViDTO> page = alGoogleMeetViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-google-meet-vis/count} : count all the alGoogleMeetVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlGoogleMeetVis(AlGoogleMeetViCriteria criteria) {
        LOG.debug("REST request to count AlGoogleMeetVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alGoogleMeetViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-google-meet-vis/:id} : get the "id" alGoogleMeetVi.
     *
     * @param id the id of the alGoogleMeetViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alGoogleMeetViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlGoogleMeetViDTO> getAlGoogleMeetVi(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AlGoogleMeetVi : {}", id);
        Optional<AlGoogleMeetViDTO> alGoogleMeetViDTO = alGoogleMeetViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alGoogleMeetViDTO);
    }

    /**
     * {@code DELETE  /al-google-meet-vis/:id} : delete the "id" alGoogleMeetVi.
     *
     * @param id the id of the alGoogleMeetViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlGoogleMeetVi(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AlGoogleMeetVi : {}", id);
        alGoogleMeetViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
