package ai.realworld.web.rest;

import ai.realworld.repository.AlPacinoPointHistoryRepository;
import ai.realworld.service.AlPacinoPointHistoryQueryService;
import ai.realworld.service.AlPacinoPointHistoryService;
import ai.realworld.service.criteria.AlPacinoPointHistoryCriteria;
import ai.realworld.service.dto.AlPacinoPointHistoryDTO;
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
 * REST controller for managing {@link ai.realworld.domain.AlPacinoPointHistory}.
 */
@RestController
@RequestMapping("/api/al-pacino-point-histories")
public class AlPacinoPointHistoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlPacinoPointHistoryResource.class);

    private static final String ENTITY_NAME = "alPacinoPointHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlPacinoPointHistoryService alPacinoPointHistoryService;

    private final AlPacinoPointHistoryRepository alPacinoPointHistoryRepository;

    private final AlPacinoPointHistoryQueryService alPacinoPointHistoryQueryService;

    public AlPacinoPointHistoryResource(
        AlPacinoPointHistoryService alPacinoPointHistoryService,
        AlPacinoPointHistoryRepository alPacinoPointHistoryRepository,
        AlPacinoPointHistoryQueryService alPacinoPointHistoryQueryService
    ) {
        this.alPacinoPointHistoryService = alPacinoPointHistoryService;
        this.alPacinoPointHistoryRepository = alPacinoPointHistoryRepository;
        this.alPacinoPointHistoryQueryService = alPacinoPointHistoryQueryService;
    }

    /**
     * {@code POST  /al-pacino-point-histories} : Create a new alPacinoPointHistory.
     *
     * @param alPacinoPointHistoryDTO the alPacinoPointHistoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alPacinoPointHistoryDTO, or with status {@code 400 (Bad Request)} if the alPacinoPointHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlPacinoPointHistoryDTO> createAlPacinoPointHistory(@RequestBody AlPacinoPointHistoryDTO alPacinoPointHistoryDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AlPacinoPointHistory : {}", alPacinoPointHistoryDTO);
        if (alPacinoPointHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new alPacinoPointHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alPacinoPointHistoryDTO = alPacinoPointHistoryService.save(alPacinoPointHistoryDTO);
        return ResponseEntity.created(new URI("/api/al-pacino-point-histories/" + alPacinoPointHistoryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alPacinoPointHistoryDTO.getId().toString()))
            .body(alPacinoPointHistoryDTO);
    }

    /**
     * {@code PUT  /al-pacino-point-histories/:id} : Updates an existing alPacinoPointHistory.
     *
     * @param id the id of the alPacinoPointHistoryDTO to save.
     * @param alPacinoPointHistoryDTO the alPacinoPointHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPacinoPointHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the alPacinoPointHistoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alPacinoPointHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlPacinoPointHistoryDTO> updateAlPacinoPointHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AlPacinoPointHistoryDTO alPacinoPointHistoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlPacinoPointHistory : {}, {}", id, alPacinoPointHistoryDTO);
        if (alPacinoPointHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPacinoPointHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPacinoPointHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alPacinoPointHistoryDTO = alPacinoPointHistoryService.update(alPacinoPointHistoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPacinoPointHistoryDTO.getId().toString()))
            .body(alPacinoPointHistoryDTO);
    }

    /**
     * {@code PATCH  /al-pacino-point-histories/:id} : Partial updates given fields of an existing alPacinoPointHistory, field will ignore if it is null
     *
     * @param id the id of the alPacinoPointHistoryDTO to save.
     * @param alPacinoPointHistoryDTO the alPacinoPointHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPacinoPointHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the alPacinoPointHistoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alPacinoPointHistoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alPacinoPointHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlPacinoPointHistoryDTO> partialUpdateAlPacinoPointHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AlPacinoPointHistoryDTO alPacinoPointHistoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlPacinoPointHistory partially : {}, {}", id, alPacinoPointHistoryDTO);
        if (alPacinoPointHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPacinoPointHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPacinoPointHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlPacinoPointHistoryDTO> result = alPacinoPointHistoryService.partialUpdate(alPacinoPointHistoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPacinoPointHistoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-pacino-point-histories} : get all the alPacinoPointHistories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alPacinoPointHistories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlPacinoPointHistoryDTO>> getAllAlPacinoPointHistories(
        AlPacinoPointHistoryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlPacinoPointHistories by criteria: {}", criteria);

        Page<AlPacinoPointHistoryDTO> page = alPacinoPointHistoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-pacino-point-histories/count} : count all the alPacinoPointHistories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlPacinoPointHistories(AlPacinoPointHistoryCriteria criteria) {
        LOG.debug("REST request to count AlPacinoPointHistories by criteria: {}", criteria);
        return ResponseEntity.ok().body(alPacinoPointHistoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-pacino-point-histories/:id} : get the "id" alPacinoPointHistory.
     *
     * @param id the id of the alPacinoPointHistoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alPacinoPointHistoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlPacinoPointHistoryDTO> getAlPacinoPointHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlPacinoPointHistory : {}", id);
        Optional<AlPacinoPointHistoryDTO> alPacinoPointHistoryDTO = alPacinoPointHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alPacinoPointHistoryDTO);
    }

    /**
     * {@code DELETE  /al-pacino-point-histories/:id} : delete the "id" alPacinoPointHistory.
     *
     * @param id the id of the alPacinoPointHistoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlPacinoPointHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlPacinoPointHistory : {}", id);
        alPacinoPointHistoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
