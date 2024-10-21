package ai.realworld.web.rest;

import ai.realworld.repository.AlPyuJokerViRepository;
import ai.realworld.service.AlPyuJokerViQueryService;
import ai.realworld.service.AlPyuJokerViService;
import ai.realworld.service.criteria.AlPyuJokerViCriteria;
import ai.realworld.service.dto.AlPyuJokerViDTO;
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
 * REST controller for managing {@link ai.realworld.domain.AlPyuJokerVi}.
 */
@RestController
@RequestMapping("/api/al-pyu-joker-vis")
public class AlPyuJokerViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlPyuJokerViResource.class);

    private static final String ENTITY_NAME = "alPyuJokerVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlPyuJokerViService alPyuJokerViService;

    private final AlPyuJokerViRepository alPyuJokerViRepository;

    private final AlPyuJokerViQueryService alPyuJokerViQueryService;

    public AlPyuJokerViResource(
        AlPyuJokerViService alPyuJokerViService,
        AlPyuJokerViRepository alPyuJokerViRepository,
        AlPyuJokerViQueryService alPyuJokerViQueryService
    ) {
        this.alPyuJokerViService = alPyuJokerViService;
        this.alPyuJokerViRepository = alPyuJokerViRepository;
        this.alPyuJokerViQueryService = alPyuJokerViQueryService;
    }

    /**
     * {@code POST  /al-pyu-joker-vis} : Create a new alPyuJokerVi.
     *
     * @param alPyuJokerViDTO the alPyuJokerViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alPyuJokerViDTO, or with status {@code 400 (Bad Request)} if the alPyuJokerVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlPyuJokerViDTO> createAlPyuJokerVi(@Valid @RequestBody AlPyuJokerViDTO alPyuJokerViDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AlPyuJokerVi : {}", alPyuJokerViDTO);
        if (alPyuJokerViDTO.getId() != null) {
            throw new BadRequestAlertException("A new alPyuJokerVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alPyuJokerViDTO = alPyuJokerViService.save(alPyuJokerViDTO);
        return ResponseEntity.created(new URI("/api/al-pyu-joker-vis/" + alPyuJokerViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alPyuJokerViDTO.getId().toString()))
            .body(alPyuJokerViDTO);
    }

    /**
     * {@code PUT  /al-pyu-joker-vis/:id} : Updates an existing alPyuJokerVi.
     *
     * @param id the id of the alPyuJokerViDTO to save.
     * @param alPyuJokerViDTO the alPyuJokerViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPyuJokerViDTO,
     * or with status {@code 400 (Bad Request)} if the alPyuJokerViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alPyuJokerViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlPyuJokerViDTO> updateAlPyuJokerVi(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody AlPyuJokerViDTO alPyuJokerViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlPyuJokerVi : {}, {}", id, alPyuJokerViDTO);
        if (alPyuJokerViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPyuJokerViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPyuJokerViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alPyuJokerViDTO = alPyuJokerViService.update(alPyuJokerViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPyuJokerViDTO.getId().toString()))
            .body(alPyuJokerViDTO);
    }

    /**
     * {@code PATCH  /al-pyu-joker-vis/:id} : Partial updates given fields of an existing alPyuJokerVi, field will ignore if it is null
     *
     * @param id the id of the alPyuJokerViDTO to save.
     * @param alPyuJokerViDTO the alPyuJokerViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPyuJokerViDTO,
     * or with status {@code 400 (Bad Request)} if the alPyuJokerViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alPyuJokerViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alPyuJokerViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlPyuJokerViDTO> partialUpdateAlPyuJokerVi(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody AlPyuJokerViDTO alPyuJokerViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlPyuJokerVi partially : {}, {}", id, alPyuJokerViDTO);
        if (alPyuJokerViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPyuJokerViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPyuJokerViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlPyuJokerViDTO> result = alPyuJokerViService.partialUpdate(alPyuJokerViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPyuJokerViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-pyu-joker-vis} : get all the alPyuJokerVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alPyuJokerVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlPyuJokerViDTO>> getAllAlPyuJokerVis(
        AlPyuJokerViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlPyuJokerVis by criteria: {}", criteria);

        Page<AlPyuJokerViDTO> page = alPyuJokerViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-pyu-joker-vis/count} : count all the alPyuJokerVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlPyuJokerVis(AlPyuJokerViCriteria criteria) {
        LOG.debug("REST request to count AlPyuJokerVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alPyuJokerViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-pyu-joker-vis/:id} : get the "id" alPyuJokerVi.
     *
     * @param id the id of the alPyuJokerViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alPyuJokerViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlPyuJokerViDTO> getAlPyuJokerVi(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AlPyuJokerVi : {}", id);
        Optional<AlPyuJokerViDTO> alPyuJokerViDTO = alPyuJokerViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alPyuJokerViDTO);
    }

    /**
     * {@code DELETE  /al-pyu-joker-vis/:id} : delete the "id" alPyuJokerVi.
     *
     * @param id the id of the alPyuJokerViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlPyuJokerVi(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AlPyuJokerVi : {}", id);
        alPyuJokerViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
