package ai.realworld.web.rest;

import ai.realworld.repository.AlProProViRepository;
import ai.realworld.service.AlProProViQueryService;
import ai.realworld.service.AlProProViService;
import ai.realworld.service.criteria.AlProProViCriteria;
import ai.realworld.service.dto.AlProProViDTO;
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
 * REST controller for managing {@link ai.realworld.domain.AlProProVi}.
 */
@RestController
@RequestMapping("/api/al-pro-pro-vis")
public class AlProProViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlProProViResource.class);

    private static final String ENTITY_NAME = "alProProVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlProProViService alProProViService;

    private final AlProProViRepository alProProViRepository;

    private final AlProProViQueryService alProProViQueryService;

    public AlProProViResource(
        AlProProViService alProProViService,
        AlProProViRepository alProProViRepository,
        AlProProViQueryService alProProViQueryService
    ) {
        this.alProProViService = alProProViService;
        this.alProProViRepository = alProProViRepository;
        this.alProProViQueryService = alProProViQueryService;
    }

    /**
     * {@code POST  /al-pro-pro-vis} : Create a new alProProVi.
     *
     * @param alProProViDTO the alProProViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alProProViDTO, or with status {@code 400 (Bad Request)} if the alProProVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlProProViDTO> createAlProProVi(@Valid @RequestBody AlProProViDTO alProProViDTO) throws URISyntaxException {
        LOG.debug("REST request to save AlProProVi : {}", alProProViDTO);
        if (alProProViDTO.getId() != null) {
            throw new BadRequestAlertException("A new alProProVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alProProViDTO = alProProViService.save(alProProViDTO);
        return ResponseEntity.created(new URI("/api/al-pro-pro-vis/" + alProProViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alProProViDTO.getId().toString()))
            .body(alProProViDTO);
    }

    /**
     * {@code PUT  /al-pro-pro-vis/:id} : Updates an existing alProProVi.
     *
     * @param id the id of the alProProViDTO to save.
     * @param alProProViDTO the alProProViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alProProViDTO,
     * or with status {@code 400 (Bad Request)} if the alProProViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alProProViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlProProViDTO> updateAlProProVi(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody AlProProViDTO alProProViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlProProVi : {}, {}", id, alProProViDTO);
        if (alProProViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alProProViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alProProViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alProProViDTO = alProProViService.update(alProProViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alProProViDTO.getId().toString()))
            .body(alProProViDTO);
    }

    /**
     * {@code PATCH  /al-pro-pro-vis/:id} : Partial updates given fields of an existing alProProVi, field will ignore if it is null
     *
     * @param id the id of the alProProViDTO to save.
     * @param alProProViDTO the alProProViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alProProViDTO,
     * or with status {@code 400 (Bad Request)} if the alProProViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alProProViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alProProViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlProProViDTO> partialUpdateAlProProVi(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody AlProProViDTO alProProViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlProProVi partially : {}, {}", id, alProProViDTO);
        if (alProProViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alProProViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alProProViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlProProViDTO> result = alProProViService.partialUpdate(alProProViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alProProViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-pro-pro-vis} : get all the alProProVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alProProVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlProProViDTO>> getAllAlProProVis(
        AlProProViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlProProVis by criteria: {}", criteria);

        Page<AlProProViDTO> page = alProProViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-pro-pro-vis/count} : count all the alProProVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlProProVis(AlProProViCriteria criteria) {
        LOG.debug("REST request to count AlProProVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alProProViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-pro-pro-vis/:id} : get the "id" alProProVi.
     *
     * @param id the id of the alProProViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alProProViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlProProViDTO> getAlProProVi(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AlProProVi : {}", id);
        Optional<AlProProViDTO> alProProViDTO = alProProViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alProProViDTO);
    }

    /**
     * {@code DELETE  /al-pro-pro-vis/:id} : delete the "id" alProProVi.
     *
     * @param id the id of the alProProViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlProProVi(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AlProProVi : {}", id);
        alProProViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
