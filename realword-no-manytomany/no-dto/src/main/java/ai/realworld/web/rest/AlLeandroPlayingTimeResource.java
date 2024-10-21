package ai.realworld.web.rest;

import ai.realworld.domain.AlLeandroPlayingTime;
import ai.realworld.repository.AlLeandroPlayingTimeRepository;
import ai.realworld.service.AlLeandroPlayingTimeQueryService;
import ai.realworld.service.AlLeandroPlayingTimeService;
import ai.realworld.service.criteria.AlLeandroPlayingTimeCriteria;
import ai.realworld.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link ai.realworld.domain.AlLeandroPlayingTime}.
 */
@RestController
@RequestMapping("/api/al-leandro-playing-times")
public class AlLeandroPlayingTimeResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlLeandroPlayingTimeResource.class);

    private static final String ENTITY_NAME = "alLeandroPlayingTime";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlLeandroPlayingTimeService alLeandroPlayingTimeService;

    private final AlLeandroPlayingTimeRepository alLeandroPlayingTimeRepository;

    private final AlLeandroPlayingTimeQueryService alLeandroPlayingTimeQueryService;

    public AlLeandroPlayingTimeResource(
        AlLeandroPlayingTimeService alLeandroPlayingTimeService,
        AlLeandroPlayingTimeRepository alLeandroPlayingTimeRepository,
        AlLeandroPlayingTimeQueryService alLeandroPlayingTimeQueryService
    ) {
        this.alLeandroPlayingTimeService = alLeandroPlayingTimeService;
        this.alLeandroPlayingTimeRepository = alLeandroPlayingTimeRepository;
        this.alLeandroPlayingTimeQueryService = alLeandroPlayingTimeQueryService;
    }

    /**
     * {@code POST  /al-leandro-playing-times} : Create a new alLeandroPlayingTime.
     *
     * @param alLeandroPlayingTime the alLeandroPlayingTime to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alLeandroPlayingTime, or with status {@code 400 (Bad Request)} if the alLeandroPlayingTime has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlLeandroPlayingTime> createAlLeandroPlayingTime(@RequestBody AlLeandroPlayingTime alLeandroPlayingTime)
        throws URISyntaxException {
        LOG.debug("REST request to save AlLeandroPlayingTime : {}", alLeandroPlayingTime);
        if (alLeandroPlayingTime.getId() != null) {
            throw new BadRequestAlertException("A new alLeandroPlayingTime cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alLeandroPlayingTime = alLeandroPlayingTimeService.save(alLeandroPlayingTime);
        return ResponseEntity.created(new URI("/api/al-leandro-playing-times/" + alLeandroPlayingTime.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alLeandroPlayingTime.getId().toString()))
            .body(alLeandroPlayingTime);
    }

    /**
     * {@code PUT  /al-leandro-playing-times/:id} : Updates an existing alLeandroPlayingTime.
     *
     * @param id the id of the alLeandroPlayingTime to save.
     * @param alLeandroPlayingTime the alLeandroPlayingTime to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alLeandroPlayingTime,
     * or with status {@code 400 (Bad Request)} if the alLeandroPlayingTime is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alLeandroPlayingTime couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlLeandroPlayingTime> updateAlLeandroPlayingTime(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody AlLeandroPlayingTime alLeandroPlayingTime
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlLeandroPlayingTime : {}, {}", id, alLeandroPlayingTime);
        if (alLeandroPlayingTime.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alLeandroPlayingTime.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alLeandroPlayingTimeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alLeandroPlayingTime = alLeandroPlayingTimeService.update(alLeandroPlayingTime);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alLeandroPlayingTime.getId().toString()))
            .body(alLeandroPlayingTime);
    }

    /**
     * {@code PATCH  /al-leandro-playing-times/:id} : Partial updates given fields of an existing alLeandroPlayingTime, field will ignore if it is null
     *
     * @param id the id of the alLeandroPlayingTime to save.
     * @param alLeandroPlayingTime the alLeandroPlayingTime to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alLeandroPlayingTime,
     * or with status {@code 400 (Bad Request)} if the alLeandroPlayingTime is not valid,
     * or with status {@code 404 (Not Found)} if the alLeandroPlayingTime is not found,
     * or with status {@code 500 (Internal Server Error)} if the alLeandroPlayingTime couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlLeandroPlayingTime> partialUpdateAlLeandroPlayingTime(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody AlLeandroPlayingTime alLeandroPlayingTime
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlLeandroPlayingTime partially : {}, {}", id, alLeandroPlayingTime);
        if (alLeandroPlayingTime.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alLeandroPlayingTime.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alLeandroPlayingTimeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlLeandroPlayingTime> result = alLeandroPlayingTimeService.partialUpdate(alLeandroPlayingTime);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alLeandroPlayingTime.getId().toString())
        );
    }

    /**
     * {@code GET  /al-leandro-playing-times} : get all the alLeandroPlayingTimes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alLeandroPlayingTimes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlLeandroPlayingTime>> getAllAlLeandroPlayingTimes(
        AlLeandroPlayingTimeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlLeandroPlayingTimes by criteria: {}", criteria);

        Page<AlLeandroPlayingTime> page = alLeandroPlayingTimeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-leandro-playing-times/count} : count all the alLeandroPlayingTimes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlLeandroPlayingTimes(AlLeandroPlayingTimeCriteria criteria) {
        LOG.debug("REST request to count AlLeandroPlayingTimes by criteria: {}", criteria);
        return ResponseEntity.ok().body(alLeandroPlayingTimeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-leandro-playing-times/:id} : get the "id" alLeandroPlayingTime.
     *
     * @param id the id of the alLeandroPlayingTime to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alLeandroPlayingTime, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlLeandroPlayingTime> getAlLeandroPlayingTime(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AlLeandroPlayingTime : {}", id);
        Optional<AlLeandroPlayingTime> alLeandroPlayingTime = alLeandroPlayingTimeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alLeandroPlayingTime);
    }

    /**
     * {@code DELETE  /al-leandro-playing-times/:id} : delete the "id" alLeandroPlayingTime.
     *
     * @param id the id of the alLeandroPlayingTime to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlLeandroPlayingTime(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AlLeandroPlayingTime : {}", id);
        alLeandroPlayingTimeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
