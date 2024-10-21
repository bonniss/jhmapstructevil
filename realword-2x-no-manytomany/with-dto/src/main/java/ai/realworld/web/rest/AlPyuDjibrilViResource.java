package ai.realworld.web.rest;

import ai.realworld.repository.AlPyuDjibrilViRepository;
import ai.realworld.service.AlPyuDjibrilViQueryService;
import ai.realworld.service.AlPyuDjibrilViService;
import ai.realworld.service.criteria.AlPyuDjibrilViCriteria;
import ai.realworld.service.dto.AlPyuDjibrilViDTO;
import ai.realworld.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link ai.realworld.domain.AlPyuDjibrilVi}.
 */
@RestController
@RequestMapping("/api/al-pyu-djibril-vis")
public class AlPyuDjibrilViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlPyuDjibrilViResource.class);

    private static final String ENTITY_NAME = "alPyuDjibrilVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlPyuDjibrilViService alPyuDjibrilViService;

    private final AlPyuDjibrilViRepository alPyuDjibrilViRepository;

    private final AlPyuDjibrilViQueryService alPyuDjibrilViQueryService;

    public AlPyuDjibrilViResource(
        AlPyuDjibrilViService alPyuDjibrilViService,
        AlPyuDjibrilViRepository alPyuDjibrilViRepository,
        AlPyuDjibrilViQueryService alPyuDjibrilViQueryService
    ) {
        this.alPyuDjibrilViService = alPyuDjibrilViService;
        this.alPyuDjibrilViRepository = alPyuDjibrilViRepository;
        this.alPyuDjibrilViQueryService = alPyuDjibrilViQueryService;
    }

    /**
     * {@code POST  /al-pyu-djibril-vis} : Create a new alPyuDjibrilVi.
     *
     * @param alPyuDjibrilViDTO the alPyuDjibrilViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alPyuDjibrilViDTO, or with status {@code 400 (Bad Request)} if the alPyuDjibrilVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlPyuDjibrilViDTO> createAlPyuDjibrilVi(@RequestBody AlPyuDjibrilViDTO alPyuDjibrilViDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AlPyuDjibrilVi : {}", alPyuDjibrilViDTO);
        if (alPyuDjibrilViDTO.getId() != null) {
            throw new BadRequestAlertException("A new alPyuDjibrilVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alPyuDjibrilViDTO = alPyuDjibrilViService.save(alPyuDjibrilViDTO);
        return ResponseEntity.created(new URI("/api/al-pyu-djibril-vis/" + alPyuDjibrilViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alPyuDjibrilViDTO.getId().toString()))
            .body(alPyuDjibrilViDTO);
    }

    /**
     * {@code PUT  /al-pyu-djibril-vis/:id} : Updates an existing alPyuDjibrilVi.
     *
     * @param id the id of the alPyuDjibrilViDTO to save.
     * @param alPyuDjibrilViDTO the alPyuDjibrilViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPyuDjibrilViDTO,
     * or with status {@code 400 (Bad Request)} if the alPyuDjibrilViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alPyuDjibrilViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlPyuDjibrilViDTO> updateAlPyuDjibrilVi(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AlPyuDjibrilViDTO alPyuDjibrilViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlPyuDjibrilVi : {}, {}", id, alPyuDjibrilViDTO);
        if (alPyuDjibrilViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPyuDjibrilViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPyuDjibrilViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alPyuDjibrilViDTO = alPyuDjibrilViService.update(alPyuDjibrilViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPyuDjibrilViDTO.getId().toString()))
            .body(alPyuDjibrilViDTO);
    }

    /**
     * {@code PATCH  /al-pyu-djibril-vis/:id} : Partial updates given fields of an existing alPyuDjibrilVi, field will ignore if it is null
     *
     * @param id the id of the alPyuDjibrilViDTO to save.
     * @param alPyuDjibrilViDTO the alPyuDjibrilViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPyuDjibrilViDTO,
     * or with status {@code 400 (Bad Request)} if the alPyuDjibrilViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alPyuDjibrilViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alPyuDjibrilViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlPyuDjibrilViDTO> partialUpdateAlPyuDjibrilVi(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AlPyuDjibrilViDTO alPyuDjibrilViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlPyuDjibrilVi partially : {}, {}", id, alPyuDjibrilViDTO);
        if (alPyuDjibrilViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPyuDjibrilViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPyuDjibrilViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlPyuDjibrilViDTO> result = alPyuDjibrilViService.partialUpdate(alPyuDjibrilViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPyuDjibrilViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-pyu-djibril-vis} : get all the alPyuDjibrilVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alPyuDjibrilVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlPyuDjibrilViDTO>> getAllAlPyuDjibrilVis(
        AlPyuDjibrilViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlPyuDjibrilVis by criteria: {}", criteria);

        Page<AlPyuDjibrilViDTO> page = alPyuDjibrilViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-pyu-djibril-vis/count} : count all the alPyuDjibrilVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlPyuDjibrilVis(AlPyuDjibrilViCriteria criteria) {
        LOG.debug("REST request to count AlPyuDjibrilVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alPyuDjibrilViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-pyu-djibril-vis/:id} : get the "id" alPyuDjibrilVi.
     *
     * @param id the id of the alPyuDjibrilViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alPyuDjibrilViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlPyuDjibrilViDTO> getAlPyuDjibrilVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlPyuDjibrilVi : {}", id);
        Optional<AlPyuDjibrilViDTO> alPyuDjibrilViDTO = alPyuDjibrilViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alPyuDjibrilViDTO);
    }

    /**
     * {@code DELETE  /al-pyu-djibril-vis/:id} : delete the "id" alPyuDjibrilVi.
     *
     * @param id the id of the alPyuDjibrilViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlPyuDjibrilVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlPyuDjibrilVi : {}", id);
        alPyuDjibrilViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
