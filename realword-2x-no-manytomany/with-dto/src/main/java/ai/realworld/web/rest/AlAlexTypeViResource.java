package ai.realworld.web.rest;

import ai.realworld.repository.AlAlexTypeViRepository;
import ai.realworld.service.AlAlexTypeViQueryService;
import ai.realworld.service.AlAlexTypeViService;
import ai.realworld.service.criteria.AlAlexTypeViCriteria;
import ai.realworld.service.dto.AlAlexTypeViDTO;
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
 * REST controller for managing {@link ai.realworld.domain.AlAlexTypeVi}.
 */
@RestController
@RequestMapping("/api/al-alex-type-vis")
public class AlAlexTypeViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlAlexTypeViResource.class);

    private static final String ENTITY_NAME = "alAlexTypeVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlAlexTypeViService alAlexTypeViService;

    private final AlAlexTypeViRepository alAlexTypeViRepository;

    private final AlAlexTypeViQueryService alAlexTypeViQueryService;

    public AlAlexTypeViResource(
        AlAlexTypeViService alAlexTypeViService,
        AlAlexTypeViRepository alAlexTypeViRepository,
        AlAlexTypeViQueryService alAlexTypeViQueryService
    ) {
        this.alAlexTypeViService = alAlexTypeViService;
        this.alAlexTypeViRepository = alAlexTypeViRepository;
        this.alAlexTypeViQueryService = alAlexTypeViQueryService;
    }

    /**
     * {@code POST  /al-alex-type-vis} : Create a new alAlexTypeVi.
     *
     * @param alAlexTypeViDTO the alAlexTypeViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alAlexTypeViDTO, or with status {@code 400 (Bad Request)} if the alAlexTypeVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlAlexTypeViDTO> createAlAlexTypeVi(@Valid @RequestBody AlAlexTypeViDTO alAlexTypeViDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AlAlexTypeVi : {}", alAlexTypeViDTO);
        if (alAlexTypeViDTO.getId() != null) {
            throw new BadRequestAlertException("A new alAlexTypeVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alAlexTypeViDTO = alAlexTypeViService.save(alAlexTypeViDTO);
        return ResponseEntity.created(new URI("/api/al-alex-type-vis/" + alAlexTypeViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alAlexTypeViDTO.getId().toString()))
            .body(alAlexTypeViDTO);
    }

    /**
     * {@code PUT  /al-alex-type-vis/:id} : Updates an existing alAlexTypeVi.
     *
     * @param id the id of the alAlexTypeViDTO to save.
     * @param alAlexTypeViDTO the alAlexTypeViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alAlexTypeViDTO,
     * or with status {@code 400 (Bad Request)} if the alAlexTypeViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alAlexTypeViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlAlexTypeViDTO> updateAlAlexTypeVi(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody AlAlexTypeViDTO alAlexTypeViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlAlexTypeVi : {}, {}", id, alAlexTypeViDTO);
        if (alAlexTypeViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alAlexTypeViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alAlexTypeViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alAlexTypeViDTO = alAlexTypeViService.update(alAlexTypeViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alAlexTypeViDTO.getId().toString()))
            .body(alAlexTypeViDTO);
    }

    /**
     * {@code PATCH  /al-alex-type-vis/:id} : Partial updates given fields of an existing alAlexTypeVi, field will ignore if it is null
     *
     * @param id the id of the alAlexTypeViDTO to save.
     * @param alAlexTypeViDTO the alAlexTypeViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alAlexTypeViDTO,
     * or with status {@code 400 (Bad Request)} if the alAlexTypeViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alAlexTypeViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alAlexTypeViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlAlexTypeViDTO> partialUpdateAlAlexTypeVi(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody AlAlexTypeViDTO alAlexTypeViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlAlexTypeVi partially : {}, {}", id, alAlexTypeViDTO);
        if (alAlexTypeViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alAlexTypeViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alAlexTypeViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlAlexTypeViDTO> result = alAlexTypeViService.partialUpdate(alAlexTypeViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alAlexTypeViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-alex-type-vis} : get all the alAlexTypeVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alAlexTypeVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlAlexTypeViDTO>> getAllAlAlexTypeVis(
        AlAlexTypeViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlAlexTypeVis by criteria: {}", criteria);

        Page<AlAlexTypeViDTO> page = alAlexTypeViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-alex-type-vis/count} : count all the alAlexTypeVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlAlexTypeVis(AlAlexTypeViCriteria criteria) {
        LOG.debug("REST request to count AlAlexTypeVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alAlexTypeViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-alex-type-vis/:id} : get the "id" alAlexTypeVi.
     *
     * @param id the id of the alAlexTypeViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alAlexTypeViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlAlexTypeViDTO> getAlAlexTypeVi(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AlAlexTypeVi : {}", id);
        Optional<AlAlexTypeViDTO> alAlexTypeViDTO = alAlexTypeViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alAlexTypeViDTO);
    }

    /**
     * {@code DELETE  /al-alex-type-vis/:id} : delete the "id" alAlexTypeVi.
     *
     * @param id the id of the alAlexTypeViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlAlexTypeVi(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AlAlexTypeVi : {}", id);
        alAlexTypeViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
