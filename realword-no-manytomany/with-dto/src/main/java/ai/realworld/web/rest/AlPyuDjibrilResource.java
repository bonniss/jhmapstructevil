package ai.realworld.web.rest;

import ai.realworld.repository.AlPyuDjibrilRepository;
import ai.realworld.service.AlPyuDjibrilQueryService;
import ai.realworld.service.AlPyuDjibrilService;
import ai.realworld.service.criteria.AlPyuDjibrilCriteria;
import ai.realworld.service.dto.AlPyuDjibrilDTO;
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
 * REST controller for managing {@link ai.realworld.domain.AlPyuDjibril}.
 */
@RestController
@RequestMapping("/api/al-pyu-djibrils")
public class AlPyuDjibrilResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlPyuDjibrilResource.class);

    private static final String ENTITY_NAME = "alPyuDjibril";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlPyuDjibrilService alPyuDjibrilService;

    private final AlPyuDjibrilRepository alPyuDjibrilRepository;

    private final AlPyuDjibrilQueryService alPyuDjibrilQueryService;

    public AlPyuDjibrilResource(
        AlPyuDjibrilService alPyuDjibrilService,
        AlPyuDjibrilRepository alPyuDjibrilRepository,
        AlPyuDjibrilQueryService alPyuDjibrilQueryService
    ) {
        this.alPyuDjibrilService = alPyuDjibrilService;
        this.alPyuDjibrilRepository = alPyuDjibrilRepository;
        this.alPyuDjibrilQueryService = alPyuDjibrilQueryService;
    }

    /**
     * {@code POST  /al-pyu-djibrils} : Create a new alPyuDjibril.
     *
     * @param alPyuDjibrilDTO the alPyuDjibrilDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alPyuDjibrilDTO, or with status {@code 400 (Bad Request)} if the alPyuDjibril has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlPyuDjibrilDTO> createAlPyuDjibril(@RequestBody AlPyuDjibrilDTO alPyuDjibrilDTO) throws URISyntaxException {
        LOG.debug("REST request to save AlPyuDjibril : {}", alPyuDjibrilDTO);
        if (alPyuDjibrilDTO.getId() != null) {
            throw new BadRequestAlertException("A new alPyuDjibril cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alPyuDjibrilDTO = alPyuDjibrilService.save(alPyuDjibrilDTO);
        return ResponseEntity.created(new URI("/api/al-pyu-djibrils/" + alPyuDjibrilDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alPyuDjibrilDTO.getId().toString()))
            .body(alPyuDjibrilDTO);
    }

    /**
     * {@code PUT  /al-pyu-djibrils/:id} : Updates an existing alPyuDjibril.
     *
     * @param id the id of the alPyuDjibrilDTO to save.
     * @param alPyuDjibrilDTO the alPyuDjibrilDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPyuDjibrilDTO,
     * or with status {@code 400 (Bad Request)} if the alPyuDjibrilDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alPyuDjibrilDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlPyuDjibrilDTO> updateAlPyuDjibril(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AlPyuDjibrilDTO alPyuDjibrilDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlPyuDjibril : {}, {}", id, alPyuDjibrilDTO);
        if (alPyuDjibrilDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPyuDjibrilDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPyuDjibrilRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alPyuDjibrilDTO = alPyuDjibrilService.update(alPyuDjibrilDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPyuDjibrilDTO.getId().toString()))
            .body(alPyuDjibrilDTO);
    }

    /**
     * {@code PATCH  /al-pyu-djibrils/:id} : Partial updates given fields of an existing alPyuDjibril, field will ignore if it is null
     *
     * @param id the id of the alPyuDjibrilDTO to save.
     * @param alPyuDjibrilDTO the alPyuDjibrilDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPyuDjibrilDTO,
     * or with status {@code 400 (Bad Request)} if the alPyuDjibrilDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alPyuDjibrilDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alPyuDjibrilDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlPyuDjibrilDTO> partialUpdateAlPyuDjibril(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AlPyuDjibrilDTO alPyuDjibrilDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlPyuDjibril partially : {}, {}", id, alPyuDjibrilDTO);
        if (alPyuDjibrilDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPyuDjibrilDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPyuDjibrilRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlPyuDjibrilDTO> result = alPyuDjibrilService.partialUpdate(alPyuDjibrilDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPyuDjibrilDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-pyu-djibrils} : get all the alPyuDjibrils.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alPyuDjibrils in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlPyuDjibrilDTO>> getAllAlPyuDjibrils(
        AlPyuDjibrilCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlPyuDjibrils by criteria: {}", criteria);

        Page<AlPyuDjibrilDTO> page = alPyuDjibrilQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-pyu-djibrils/count} : count all the alPyuDjibrils.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlPyuDjibrils(AlPyuDjibrilCriteria criteria) {
        LOG.debug("REST request to count AlPyuDjibrils by criteria: {}", criteria);
        return ResponseEntity.ok().body(alPyuDjibrilQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-pyu-djibrils/:id} : get the "id" alPyuDjibril.
     *
     * @param id the id of the alPyuDjibrilDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alPyuDjibrilDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlPyuDjibrilDTO> getAlPyuDjibril(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlPyuDjibril : {}", id);
        Optional<AlPyuDjibrilDTO> alPyuDjibrilDTO = alPyuDjibrilService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alPyuDjibrilDTO);
    }

    /**
     * {@code DELETE  /al-pyu-djibrils/:id} : delete the "id" alPyuDjibril.
     *
     * @param id the id of the alPyuDjibrilDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlPyuDjibril(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlPyuDjibril : {}", id);
        alPyuDjibrilService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
