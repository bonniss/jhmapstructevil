package ai.realworld.web.rest;

import ai.realworld.repository.AlProtyViRepository;
import ai.realworld.service.AlProtyViQueryService;
import ai.realworld.service.AlProtyViService;
import ai.realworld.service.criteria.AlProtyViCriteria;
import ai.realworld.service.dto.AlProtyViDTO;
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
 * REST controller for managing {@link ai.realworld.domain.AlProtyVi}.
 */
@RestController
@RequestMapping("/api/al-proty-vis")
public class AlProtyViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlProtyViResource.class);

    private static final String ENTITY_NAME = "alProtyVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlProtyViService alProtyViService;

    private final AlProtyViRepository alProtyViRepository;

    private final AlProtyViQueryService alProtyViQueryService;

    public AlProtyViResource(
        AlProtyViService alProtyViService,
        AlProtyViRepository alProtyViRepository,
        AlProtyViQueryService alProtyViQueryService
    ) {
        this.alProtyViService = alProtyViService;
        this.alProtyViRepository = alProtyViRepository;
        this.alProtyViQueryService = alProtyViQueryService;
    }

    /**
     * {@code POST  /al-proty-vis} : Create a new alProtyVi.
     *
     * @param alProtyViDTO the alProtyViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alProtyViDTO, or with status {@code 400 (Bad Request)} if the alProtyVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlProtyViDTO> createAlProtyVi(@Valid @RequestBody AlProtyViDTO alProtyViDTO) throws URISyntaxException {
        LOG.debug("REST request to save AlProtyVi : {}", alProtyViDTO);
        if (alProtyViDTO.getId() != null) {
            throw new BadRequestAlertException("A new alProtyVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alProtyViDTO = alProtyViService.save(alProtyViDTO);
        return ResponseEntity.created(new URI("/api/al-proty-vis/" + alProtyViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alProtyViDTO.getId().toString()))
            .body(alProtyViDTO);
    }

    /**
     * {@code PUT  /al-proty-vis/:id} : Updates an existing alProtyVi.
     *
     * @param id the id of the alProtyViDTO to save.
     * @param alProtyViDTO the alProtyViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alProtyViDTO,
     * or with status {@code 400 (Bad Request)} if the alProtyViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alProtyViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlProtyViDTO> updateAlProtyVi(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody AlProtyViDTO alProtyViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlProtyVi : {}, {}", id, alProtyViDTO);
        if (alProtyViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alProtyViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alProtyViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alProtyViDTO = alProtyViService.update(alProtyViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alProtyViDTO.getId().toString()))
            .body(alProtyViDTO);
    }

    /**
     * {@code PATCH  /al-proty-vis/:id} : Partial updates given fields of an existing alProtyVi, field will ignore if it is null
     *
     * @param id the id of the alProtyViDTO to save.
     * @param alProtyViDTO the alProtyViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alProtyViDTO,
     * or with status {@code 400 (Bad Request)} if the alProtyViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alProtyViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alProtyViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlProtyViDTO> partialUpdateAlProtyVi(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody AlProtyViDTO alProtyViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlProtyVi partially : {}, {}", id, alProtyViDTO);
        if (alProtyViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alProtyViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alProtyViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlProtyViDTO> result = alProtyViService.partialUpdate(alProtyViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alProtyViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-proty-vis} : get all the alProtyVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alProtyVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlProtyViDTO>> getAllAlProtyVis(
        AlProtyViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlProtyVis by criteria: {}", criteria);

        Page<AlProtyViDTO> page = alProtyViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-proty-vis/count} : count all the alProtyVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlProtyVis(AlProtyViCriteria criteria) {
        LOG.debug("REST request to count AlProtyVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alProtyViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-proty-vis/:id} : get the "id" alProtyVi.
     *
     * @param id the id of the alProtyViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alProtyViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlProtyViDTO> getAlProtyVi(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AlProtyVi : {}", id);
        Optional<AlProtyViDTO> alProtyViDTO = alProtyViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alProtyViDTO);
    }

    /**
     * {@code DELETE  /al-proty-vis/:id} : delete the "id" alProtyVi.
     *
     * @param id the id of the alProtyViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlProtyVi(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AlProtyVi : {}", id);
        alProtyViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
