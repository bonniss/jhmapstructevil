package ai.realworld.web.rest;

import ai.realworld.repository.AlPacinoPointHistoryViRepository;
import ai.realworld.service.AlPacinoPointHistoryViQueryService;
import ai.realworld.service.AlPacinoPointHistoryViService;
import ai.realworld.service.criteria.AlPacinoPointHistoryViCriteria;
import ai.realworld.service.dto.AlPacinoPointHistoryViDTO;
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
 * REST controller for managing {@link ai.realworld.domain.AlPacinoPointHistoryVi}.
 */
@RestController
@RequestMapping("/api/al-pacino-point-history-vis")
public class AlPacinoPointHistoryViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlPacinoPointHistoryViResource.class);

    private static final String ENTITY_NAME = "alPacinoPointHistoryVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlPacinoPointHistoryViService alPacinoPointHistoryViService;

    private final AlPacinoPointHistoryViRepository alPacinoPointHistoryViRepository;

    private final AlPacinoPointHistoryViQueryService alPacinoPointHistoryViQueryService;

    public AlPacinoPointHistoryViResource(
        AlPacinoPointHistoryViService alPacinoPointHistoryViService,
        AlPacinoPointHistoryViRepository alPacinoPointHistoryViRepository,
        AlPacinoPointHistoryViQueryService alPacinoPointHistoryViQueryService
    ) {
        this.alPacinoPointHistoryViService = alPacinoPointHistoryViService;
        this.alPacinoPointHistoryViRepository = alPacinoPointHistoryViRepository;
        this.alPacinoPointHistoryViQueryService = alPacinoPointHistoryViQueryService;
    }

    /**
     * {@code POST  /al-pacino-point-history-vis} : Create a new alPacinoPointHistoryVi.
     *
     * @param alPacinoPointHistoryViDTO the alPacinoPointHistoryViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alPacinoPointHistoryViDTO, or with status {@code 400 (Bad Request)} if the alPacinoPointHistoryVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlPacinoPointHistoryViDTO> createAlPacinoPointHistoryVi(
        @RequestBody AlPacinoPointHistoryViDTO alPacinoPointHistoryViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save AlPacinoPointHistoryVi : {}", alPacinoPointHistoryViDTO);
        if (alPacinoPointHistoryViDTO.getId() != null) {
            throw new BadRequestAlertException("A new alPacinoPointHistoryVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alPacinoPointHistoryViDTO = alPacinoPointHistoryViService.save(alPacinoPointHistoryViDTO);
        return ResponseEntity.created(new URI("/api/al-pacino-point-history-vis/" + alPacinoPointHistoryViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alPacinoPointHistoryViDTO.getId().toString()))
            .body(alPacinoPointHistoryViDTO);
    }

    /**
     * {@code PUT  /al-pacino-point-history-vis/:id} : Updates an existing alPacinoPointHistoryVi.
     *
     * @param id the id of the alPacinoPointHistoryViDTO to save.
     * @param alPacinoPointHistoryViDTO the alPacinoPointHistoryViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPacinoPointHistoryViDTO,
     * or with status {@code 400 (Bad Request)} if the alPacinoPointHistoryViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alPacinoPointHistoryViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlPacinoPointHistoryViDTO> updateAlPacinoPointHistoryVi(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AlPacinoPointHistoryViDTO alPacinoPointHistoryViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlPacinoPointHistoryVi : {}, {}", id, alPacinoPointHistoryViDTO);
        if (alPacinoPointHistoryViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPacinoPointHistoryViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPacinoPointHistoryViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alPacinoPointHistoryViDTO = alPacinoPointHistoryViService.update(alPacinoPointHistoryViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPacinoPointHistoryViDTO.getId().toString()))
            .body(alPacinoPointHistoryViDTO);
    }

    /**
     * {@code PATCH  /al-pacino-point-history-vis/:id} : Partial updates given fields of an existing alPacinoPointHistoryVi, field will ignore if it is null
     *
     * @param id the id of the alPacinoPointHistoryViDTO to save.
     * @param alPacinoPointHistoryViDTO the alPacinoPointHistoryViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPacinoPointHistoryViDTO,
     * or with status {@code 400 (Bad Request)} if the alPacinoPointHistoryViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alPacinoPointHistoryViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alPacinoPointHistoryViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlPacinoPointHistoryViDTO> partialUpdateAlPacinoPointHistoryVi(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AlPacinoPointHistoryViDTO alPacinoPointHistoryViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlPacinoPointHistoryVi partially : {}, {}", id, alPacinoPointHistoryViDTO);
        if (alPacinoPointHistoryViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPacinoPointHistoryViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPacinoPointHistoryViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlPacinoPointHistoryViDTO> result = alPacinoPointHistoryViService.partialUpdate(alPacinoPointHistoryViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPacinoPointHistoryViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-pacino-point-history-vis} : get all the alPacinoPointHistoryVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alPacinoPointHistoryVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlPacinoPointHistoryViDTO>> getAllAlPacinoPointHistoryVis(
        AlPacinoPointHistoryViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlPacinoPointHistoryVis by criteria: {}", criteria);

        Page<AlPacinoPointHistoryViDTO> page = alPacinoPointHistoryViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-pacino-point-history-vis/count} : count all the alPacinoPointHistoryVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlPacinoPointHistoryVis(AlPacinoPointHistoryViCriteria criteria) {
        LOG.debug("REST request to count AlPacinoPointHistoryVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alPacinoPointHistoryViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-pacino-point-history-vis/:id} : get the "id" alPacinoPointHistoryVi.
     *
     * @param id the id of the alPacinoPointHistoryViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alPacinoPointHistoryViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlPacinoPointHistoryViDTO> getAlPacinoPointHistoryVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlPacinoPointHistoryVi : {}", id);
        Optional<AlPacinoPointHistoryViDTO> alPacinoPointHistoryViDTO = alPacinoPointHistoryViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alPacinoPointHistoryViDTO);
    }

    /**
     * {@code DELETE  /al-pacino-point-history-vis/:id} : delete the "id" alPacinoPointHistoryVi.
     *
     * @param id the id of the alPacinoPointHistoryViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlPacinoPointHistoryVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlPacinoPointHistoryVi : {}", id);
        alPacinoPointHistoryViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
