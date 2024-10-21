package ai.realworld.web.rest;

import ai.realworld.repository.AlBetonamuRelationViRepository;
import ai.realworld.service.AlBetonamuRelationViQueryService;
import ai.realworld.service.AlBetonamuRelationViService;
import ai.realworld.service.criteria.AlBetonamuRelationViCriteria;
import ai.realworld.service.dto.AlBetonamuRelationViDTO;
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
 * REST controller for managing {@link ai.realworld.domain.AlBetonamuRelationVi}.
 */
@RestController
@RequestMapping("/api/al-betonamu-relation-vis")
public class AlBetonamuRelationViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlBetonamuRelationViResource.class);

    private static final String ENTITY_NAME = "alBetonamuRelationVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlBetonamuRelationViService alBetonamuRelationViService;

    private final AlBetonamuRelationViRepository alBetonamuRelationViRepository;

    private final AlBetonamuRelationViQueryService alBetonamuRelationViQueryService;

    public AlBetonamuRelationViResource(
        AlBetonamuRelationViService alBetonamuRelationViService,
        AlBetonamuRelationViRepository alBetonamuRelationViRepository,
        AlBetonamuRelationViQueryService alBetonamuRelationViQueryService
    ) {
        this.alBetonamuRelationViService = alBetonamuRelationViService;
        this.alBetonamuRelationViRepository = alBetonamuRelationViRepository;
        this.alBetonamuRelationViQueryService = alBetonamuRelationViQueryService;
    }

    /**
     * {@code POST  /al-betonamu-relation-vis} : Create a new alBetonamuRelationVi.
     *
     * @param alBetonamuRelationViDTO the alBetonamuRelationViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alBetonamuRelationViDTO, or with status {@code 400 (Bad Request)} if the alBetonamuRelationVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlBetonamuRelationViDTO> createAlBetonamuRelationVi(
        @Valid @RequestBody AlBetonamuRelationViDTO alBetonamuRelationViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save AlBetonamuRelationVi : {}", alBetonamuRelationViDTO);
        if (alBetonamuRelationViDTO.getId() != null) {
            throw new BadRequestAlertException("A new alBetonamuRelationVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alBetonamuRelationViDTO = alBetonamuRelationViService.save(alBetonamuRelationViDTO);
        return ResponseEntity.created(new URI("/api/al-betonamu-relation-vis/" + alBetonamuRelationViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alBetonamuRelationViDTO.getId().toString()))
            .body(alBetonamuRelationViDTO);
    }

    /**
     * {@code PUT  /al-betonamu-relation-vis/:id} : Updates an existing alBetonamuRelationVi.
     *
     * @param id the id of the alBetonamuRelationViDTO to save.
     * @param alBetonamuRelationViDTO the alBetonamuRelationViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alBetonamuRelationViDTO,
     * or with status {@code 400 (Bad Request)} if the alBetonamuRelationViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alBetonamuRelationViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlBetonamuRelationViDTO> updateAlBetonamuRelationVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlBetonamuRelationViDTO alBetonamuRelationViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlBetonamuRelationVi : {}, {}", id, alBetonamuRelationViDTO);
        if (alBetonamuRelationViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alBetonamuRelationViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alBetonamuRelationViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alBetonamuRelationViDTO = alBetonamuRelationViService.update(alBetonamuRelationViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alBetonamuRelationViDTO.getId().toString()))
            .body(alBetonamuRelationViDTO);
    }

    /**
     * {@code PATCH  /al-betonamu-relation-vis/:id} : Partial updates given fields of an existing alBetonamuRelationVi, field will ignore if it is null
     *
     * @param id the id of the alBetonamuRelationViDTO to save.
     * @param alBetonamuRelationViDTO the alBetonamuRelationViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alBetonamuRelationViDTO,
     * or with status {@code 400 (Bad Request)} if the alBetonamuRelationViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alBetonamuRelationViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alBetonamuRelationViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlBetonamuRelationViDTO> partialUpdateAlBetonamuRelationVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlBetonamuRelationViDTO alBetonamuRelationViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlBetonamuRelationVi partially : {}, {}", id, alBetonamuRelationViDTO);
        if (alBetonamuRelationViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alBetonamuRelationViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alBetonamuRelationViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlBetonamuRelationViDTO> result = alBetonamuRelationViService.partialUpdate(alBetonamuRelationViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alBetonamuRelationViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-betonamu-relation-vis} : get all the alBetonamuRelationVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alBetonamuRelationVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlBetonamuRelationViDTO>> getAllAlBetonamuRelationVis(
        AlBetonamuRelationViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlBetonamuRelationVis by criteria: {}", criteria);

        Page<AlBetonamuRelationViDTO> page = alBetonamuRelationViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-betonamu-relation-vis/count} : count all the alBetonamuRelationVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlBetonamuRelationVis(AlBetonamuRelationViCriteria criteria) {
        LOG.debug("REST request to count AlBetonamuRelationVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alBetonamuRelationViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-betonamu-relation-vis/:id} : get the "id" alBetonamuRelationVi.
     *
     * @param id the id of the alBetonamuRelationViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alBetonamuRelationViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlBetonamuRelationViDTO> getAlBetonamuRelationVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlBetonamuRelationVi : {}", id);
        Optional<AlBetonamuRelationViDTO> alBetonamuRelationViDTO = alBetonamuRelationViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alBetonamuRelationViDTO);
    }

    /**
     * {@code DELETE  /al-betonamu-relation-vis/:id} : delete the "id" alBetonamuRelationVi.
     *
     * @param id the id of the alBetonamuRelationViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlBetonamuRelationVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlBetonamuRelationVi : {}", id);
        alBetonamuRelationViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
