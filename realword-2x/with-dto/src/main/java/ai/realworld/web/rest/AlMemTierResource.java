package ai.realworld.web.rest;

import ai.realworld.repository.AlMemTierRepository;
import ai.realworld.service.AlMemTierQueryService;
import ai.realworld.service.AlMemTierService;
import ai.realworld.service.criteria.AlMemTierCriteria;
import ai.realworld.service.dto.AlMemTierDTO;
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
 * REST controller for managing {@link ai.realworld.domain.AlMemTier}.
 */
@RestController
@RequestMapping("/api/al-mem-tiers")
public class AlMemTierResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlMemTierResource.class);

    private static final String ENTITY_NAME = "alMemTier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlMemTierService alMemTierService;

    private final AlMemTierRepository alMemTierRepository;

    private final AlMemTierQueryService alMemTierQueryService;

    public AlMemTierResource(
        AlMemTierService alMemTierService,
        AlMemTierRepository alMemTierRepository,
        AlMemTierQueryService alMemTierQueryService
    ) {
        this.alMemTierService = alMemTierService;
        this.alMemTierRepository = alMemTierRepository;
        this.alMemTierQueryService = alMemTierQueryService;
    }

    /**
     * {@code POST  /al-mem-tiers} : Create a new alMemTier.
     *
     * @param alMemTierDTO the alMemTierDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alMemTierDTO, or with status {@code 400 (Bad Request)} if the alMemTier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlMemTierDTO> createAlMemTier(@Valid @RequestBody AlMemTierDTO alMemTierDTO) throws URISyntaxException {
        LOG.debug("REST request to save AlMemTier : {}", alMemTierDTO);
        if (alMemTierDTO.getId() != null) {
            throw new BadRequestAlertException("A new alMemTier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alMemTierDTO = alMemTierService.save(alMemTierDTO);
        return ResponseEntity.created(new URI("/api/al-mem-tiers/" + alMemTierDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alMemTierDTO.getId().toString()))
            .body(alMemTierDTO);
    }

    /**
     * {@code PUT  /al-mem-tiers/:id} : Updates an existing alMemTier.
     *
     * @param id the id of the alMemTierDTO to save.
     * @param alMemTierDTO the alMemTierDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alMemTierDTO,
     * or with status {@code 400 (Bad Request)} if the alMemTierDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alMemTierDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlMemTierDTO> updateAlMemTier(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlMemTierDTO alMemTierDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlMemTier : {}, {}", id, alMemTierDTO);
        if (alMemTierDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alMemTierDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alMemTierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alMemTierDTO = alMemTierService.update(alMemTierDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alMemTierDTO.getId().toString()))
            .body(alMemTierDTO);
    }

    /**
     * {@code PATCH  /al-mem-tiers/:id} : Partial updates given fields of an existing alMemTier, field will ignore if it is null
     *
     * @param id the id of the alMemTierDTO to save.
     * @param alMemTierDTO the alMemTierDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alMemTierDTO,
     * or with status {@code 400 (Bad Request)} if the alMemTierDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alMemTierDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alMemTierDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlMemTierDTO> partialUpdateAlMemTier(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlMemTierDTO alMemTierDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlMemTier partially : {}, {}", id, alMemTierDTO);
        if (alMemTierDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alMemTierDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alMemTierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlMemTierDTO> result = alMemTierService.partialUpdate(alMemTierDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alMemTierDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-mem-tiers} : get all the alMemTiers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alMemTiers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlMemTierDTO>> getAllAlMemTiers(
        AlMemTierCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlMemTiers by criteria: {}", criteria);

        Page<AlMemTierDTO> page = alMemTierQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-mem-tiers/count} : count all the alMemTiers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlMemTiers(AlMemTierCriteria criteria) {
        LOG.debug("REST request to count AlMemTiers by criteria: {}", criteria);
        return ResponseEntity.ok().body(alMemTierQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-mem-tiers/:id} : get the "id" alMemTier.
     *
     * @param id the id of the alMemTierDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alMemTierDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlMemTierDTO> getAlMemTier(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlMemTier : {}", id);
        Optional<AlMemTierDTO> alMemTierDTO = alMemTierService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alMemTierDTO);
    }

    /**
     * {@code DELETE  /al-mem-tiers/:id} : delete the "id" alMemTier.
     *
     * @param id the id of the alMemTierDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlMemTier(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlMemTier : {}", id);
        alMemTierService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
