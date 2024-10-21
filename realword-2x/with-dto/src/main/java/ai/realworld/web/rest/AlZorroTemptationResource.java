package ai.realworld.web.rest;

import ai.realworld.repository.AlZorroTemptationRepository;
import ai.realworld.service.AlZorroTemptationQueryService;
import ai.realworld.service.AlZorroTemptationService;
import ai.realworld.service.criteria.AlZorroTemptationCriteria;
import ai.realworld.service.dto.AlZorroTemptationDTO;
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
 * REST controller for managing {@link ai.realworld.domain.AlZorroTemptation}.
 */
@RestController
@RequestMapping("/api/al-zorro-temptations")
public class AlZorroTemptationResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlZorroTemptationResource.class);

    private static final String ENTITY_NAME = "alZorroTemptation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlZorroTemptationService alZorroTemptationService;

    private final AlZorroTemptationRepository alZorroTemptationRepository;

    private final AlZorroTemptationQueryService alZorroTemptationQueryService;

    public AlZorroTemptationResource(
        AlZorroTemptationService alZorroTemptationService,
        AlZorroTemptationRepository alZorroTemptationRepository,
        AlZorroTemptationQueryService alZorroTemptationQueryService
    ) {
        this.alZorroTemptationService = alZorroTemptationService;
        this.alZorroTemptationRepository = alZorroTemptationRepository;
        this.alZorroTemptationQueryService = alZorroTemptationQueryService;
    }

    /**
     * {@code POST  /al-zorro-temptations} : Create a new alZorroTemptation.
     *
     * @param alZorroTemptationDTO the alZorroTemptationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alZorroTemptationDTO, or with status {@code 400 (Bad Request)} if the alZorroTemptation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlZorroTemptationDTO> createAlZorroTemptation(@Valid @RequestBody AlZorroTemptationDTO alZorroTemptationDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AlZorroTemptation : {}", alZorroTemptationDTO);
        if (alZorroTemptationDTO.getId() != null) {
            throw new BadRequestAlertException("A new alZorroTemptation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alZorroTemptationDTO = alZorroTemptationService.save(alZorroTemptationDTO);
        return ResponseEntity.created(new URI("/api/al-zorro-temptations/" + alZorroTemptationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alZorroTemptationDTO.getId().toString()))
            .body(alZorroTemptationDTO);
    }

    /**
     * {@code PUT  /al-zorro-temptations/:id} : Updates an existing alZorroTemptation.
     *
     * @param id the id of the alZorroTemptationDTO to save.
     * @param alZorroTemptationDTO the alZorroTemptationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alZorroTemptationDTO,
     * or with status {@code 400 (Bad Request)} if the alZorroTemptationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alZorroTemptationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlZorroTemptationDTO> updateAlZorroTemptation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlZorroTemptationDTO alZorroTemptationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlZorroTemptation : {}, {}", id, alZorroTemptationDTO);
        if (alZorroTemptationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alZorroTemptationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alZorroTemptationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alZorroTemptationDTO = alZorroTemptationService.update(alZorroTemptationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alZorroTemptationDTO.getId().toString()))
            .body(alZorroTemptationDTO);
    }

    /**
     * {@code PATCH  /al-zorro-temptations/:id} : Partial updates given fields of an existing alZorroTemptation, field will ignore if it is null
     *
     * @param id the id of the alZorroTemptationDTO to save.
     * @param alZorroTemptationDTO the alZorroTemptationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alZorroTemptationDTO,
     * or with status {@code 400 (Bad Request)} if the alZorroTemptationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alZorroTemptationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alZorroTemptationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlZorroTemptationDTO> partialUpdateAlZorroTemptation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlZorroTemptationDTO alZorroTemptationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlZorroTemptation partially : {}, {}", id, alZorroTemptationDTO);
        if (alZorroTemptationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alZorroTemptationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alZorroTemptationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlZorroTemptationDTO> result = alZorroTemptationService.partialUpdate(alZorroTemptationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alZorroTemptationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-zorro-temptations} : get all the alZorroTemptations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alZorroTemptations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlZorroTemptationDTO>> getAllAlZorroTemptations(
        AlZorroTemptationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlZorroTemptations by criteria: {}", criteria);

        Page<AlZorroTemptationDTO> page = alZorroTemptationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-zorro-temptations/count} : count all the alZorroTemptations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlZorroTemptations(AlZorroTemptationCriteria criteria) {
        LOG.debug("REST request to count AlZorroTemptations by criteria: {}", criteria);
        return ResponseEntity.ok().body(alZorroTemptationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-zorro-temptations/:id} : get the "id" alZorroTemptation.
     *
     * @param id the id of the alZorroTemptationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alZorroTemptationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlZorroTemptationDTO> getAlZorroTemptation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlZorroTemptation : {}", id);
        Optional<AlZorroTemptationDTO> alZorroTemptationDTO = alZorroTemptationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alZorroTemptationDTO);
    }

    /**
     * {@code DELETE  /al-zorro-temptations/:id} : delete the "id" alZorroTemptation.
     *
     * @param id the id of the alZorroTemptationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlZorroTemptation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlZorroTemptation : {}", id);
        alZorroTemptationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
