package ai.realworld.web.rest;

import ai.realworld.repository.AlMenityRepository;
import ai.realworld.service.AlMenityQueryService;
import ai.realworld.service.AlMenityService;
import ai.realworld.service.criteria.AlMenityCriteria;
import ai.realworld.service.dto.AlMenityDTO;
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
 * REST controller for managing {@link ai.realworld.domain.AlMenity}.
 */
@RestController
@RequestMapping("/api/al-menities")
public class AlMenityResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlMenityResource.class);

    private static final String ENTITY_NAME = "alMenity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlMenityService alMenityService;

    private final AlMenityRepository alMenityRepository;

    private final AlMenityQueryService alMenityQueryService;

    public AlMenityResource(
        AlMenityService alMenityService,
        AlMenityRepository alMenityRepository,
        AlMenityQueryService alMenityQueryService
    ) {
        this.alMenityService = alMenityService;
        this.alMenityRepository = alMenityRepository;
        this.alMenityQueryService = alMenityQueryService;
    }

    /**
     * {@code POST  /al-menities} : Create a new alMenity.
     *
     * @param alMenityDTO the alMenityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alMenityDTO, or with status {@code 400 (Bad Request)} if the alMenity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlMenityDTO> createAlMenity(@Valid @RequestBody AlMenityDTO alMenityDTO) throws URISyntaxException {
        LOG.debug("REST request to save AlMenity : {}", alMenityDTO);
        if (alMenityDTO.getId() != null) {
            throw new BadRequestAlertException("A new alMenity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alMenityDTO = alMenityService.save(alMenityDTO);
        return ResponseEntity.created(new URI("/api/al-menities/" + alMenityDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alMenityDTO.getId().toString()))
            .body(alMenityDTO);
    }

    /**
     * {@code PUT  /al-menities/:id} : Updates an existing alMenity.
     *
     * @param id the id of the alMenityDTO to save.
     * @param alMenityDTO the alMenityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alMenityDTO,
     * or with status {@code 400 (Bad Request)} if the alMenityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alMenityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlMenityDTO> updateAlMenity(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlMenityDTO alMenityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlMenity : {}, {}", id, alMenityDTO);
        if (alMenityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alMenityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alMenityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alMenityDTO = alMenityService.update(alMenityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alMenityDTO.getId().toString()))
            .body(alMenityDTO);
    }

    /**
     * {@code PATCH  /al-menities/:id} : Partial updates given fields of an existing alMenity, field will ignore if it is null
     *
     * @param id the id of the alMenityDTO to save.
     * @param alMenityDTO the alMenityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alMenityDTO,
     * or with status {@code 400 (Bad Request)} if the alMenityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alMenityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alMenityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlMenityDTO> partialUpdateAlMenity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlMenityDTO alMenityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlMenity partially : {}, {}", id, alMenityDTO);
        if (alMenityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alMenityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alMenityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlMenityDTO> result = alMenityService.partialUpdate(alMenityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alMenityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-menities} : get all the alMenities.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alMenities in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlMenityDTO>> getAllAlMenities(
        AlMenityCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlMenities by criteria: {}", criteria);

        Page<AlMenityDTO> page = alMenityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-menities/count} : count all the alMenities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlMenities(AlMenityCriteria criteria) {
        LOG.debug("REST request to count AlMenities by criteria: {}", criteria);
        return ResponseEntity.ok().body(alMenityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-menities/:id} : get the "id" alMenity.
     *
     * @param id the id of the alMenityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alMenityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlMenityDTO> getAlMenity(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlMenity : {}", id);
        Optional<AlMenityDTO> alMenityDTO = alMenityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alMenityDTO);
    }

    /**
     * {@code DELETE  /al-menities/:id} : delete the "id" alMenity.
     *
     * @param id the id of the alMenityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlMenity(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlMenity : {}", id);
        alMenityService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}