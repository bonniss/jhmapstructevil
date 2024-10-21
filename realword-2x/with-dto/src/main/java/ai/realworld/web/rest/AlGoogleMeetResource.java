package ai.realworld.web.rest;

import ai.realworld.repository.AlGoogleMeetRepository;
import ai.realworld.service.AlGoogleMeetQueryService;
import ai.realworld.service.AlGoogleMeetService;
import ai.realworld.service.criteria.AlGoogleMeetCriteria;
import ai.realworld.service.dto.AlGoogleMeetDTO;
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
 * REST controller for managing {@link ai.realworld.domain.AlGoogleMeet}.
 */
@RestController
@RequestMapping("/api/al-google-meets")
public class AlGoogleMeetResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlGoogleMeetResource.class);

    private static final String ENTITY_NAME = "alGoogleMeet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlGoogleMeetService alGoogleMeetService;

    private final AlGoogleMeetRepository alGoogleMeetRepository;

    private final AlGoogleMeetQueryService alGoogleMeetQueryService;

    public AlGoogleMeetResource(
        AlGoogleMeetService alGoogleMeetService,
        AlGoogleMeetRepository alGoogleMeetRepository,
        AlGoogleMeetQueryService alGoogleMeetQueryService
    ) {
        this.alGoogleMeetService = alGoogleMeetService;
        this.alGoogleMeetRepository = alGoogleMeetRepository;
        this.alGoogleMeetQueryService = alGoogleMeetQueryService;
    }

    /**
     * {@code POST  /al-google-meets} : Create a new alGoogleMeet.
     *
     * @param alGoogleMeetDTO the alGoogleMeetDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alGoogleMeetDTO, or with status {@code 400 (Bad Request)} if the alGoogleMeet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlGoogleMeetDTO> createAlGoogleMeet(@Valid @RequestBody AlGoogleMeetDTO alGoogleMeetDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AlGoogleMeet : {}", alGoogleMeetDTO);
        if (alGoogleMeetDTO.getId() != null) {
            throw new BadRequestAlertException("A new alGoogleMeet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alGoogleMeetDTO = alGoogleMeetService.save(alGoogleMeetDTO);
        return ResponseEntity.created(new URI("/api/al-google-meets/" + alGoogleMeetDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alGoogleMeetDTO.getId().toString()))
            .body(alGoogleMeetDTO);
    }

    /**
     * {@code PUT  /al-google-meets/:id} : Updates an existing alGoogleMeet.
     *
     * @param id the id of the alGoogleMeetDTO to save.
     * @param alGoogleMeetDTO the alGoogleMeetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alGoogleMeetDTO,
     * or with status {@code 400 (Bad Request)} if the alGoogleMeetDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alGoogleMeetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlGoogleMeetDTO> updateAlGoogleMeet(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody AlGoogleMeetDTO alGoogleMeetDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlGoogleMeet : {}, {}", id, alGoogleMeetDTO);
        if (alGoogleMeetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alGoogleMeetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alGoogleMeetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alGoogleMeetDTO = alGoogleMeetService.update(alGoogleMeetDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alGoogleMeetDTO.getId().toString()))
            .body(alGoogleMeetDTO);
    }

    /**
     * {@code PATCH  /al-google-meets/:id} : Partial updates given fields of an existing alGoogleMeet, field will ignore if it is null
     *
     * @param id the id of the alGoogleMeetDTO to save.
     * @param alGoogleMeetDTO the alGoogleMeetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alGoogleMeetDTO,
     * or with status {@code 400 (Bad Request)} if the alGoogleMeetDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alGoogleMeetDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alGoogleMeetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlGoogleMeetDTO> partialUpdateAlGoogleMeet(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody AlGoogleMeetDTO alGoogleMeetDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlGoogleMeet partially : {}, {}", id, alGoogleMeetDTO);
        if (alGoogleMeetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alGoogleMeetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alGoogleMeetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlGoogleMeetDTO> result = alGoogleMeetService.partialUpdate(alGoogleMeetDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alGoogleMeetDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-google-meets} : get all the alGoogleMeets.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alGoogleMeets in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlGoogleMeetDTO>> getAllAlGoogleMeets(
        AlGoogleMeetCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlGoogleMeets by criteria: {}", criteria);

        Page<AlGoogleMeetDTO> page = alGoogleMeetQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-google-meets/count} : count all the alGoogleMeets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlGoogleMeets(AlGoogleMeetCriteria criteria) {
        LOG.debug("REST request to count AlGoogleMeets by criteria: {}", criteria);
        return ResponseEntity.ok().body(alGoogleMeetQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-google-meets/:id} : get the "id" alGoogleMeet.
     *
     * @param id the id of the alGoogleMeetDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alGoogleMeetDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlGoogleMeetDTO> getAlGoogleMeet(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AlGoogleMeet : {}", id);
        Optional<AlGoogleMeetDTO> alGoogleMeetDTO = alGoogleMeetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alGoogleMeetDTO);
    }

    /**
     * {@code DELETE  /al-google-meets/:id} : delete the "id" alGoogleMeet.
     *
     * @param id the id of the alGoogleMeetDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlGoogleMeet(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AlGoogleMeet : {}", id);
        alGoogleMeetService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
