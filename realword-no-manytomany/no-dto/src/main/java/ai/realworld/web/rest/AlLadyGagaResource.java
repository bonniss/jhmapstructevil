package ai.realworld.web.rest;

import ai.realworld.domain.AlLadyGaga;
import ai.realworld.repository.AlLadyGagaRepository;
import ai.realworld.service.AlLadyGagaQueryService;
import ai.realworld.service.AlLadyGagaService;
import ai.realworld.service.criteria.AlLadyGagaCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.AlLadyGaga}.
 */
@RestController
@RequestMapping("/api/al-lady-gagas")
public class AlLadyGagaResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlLadyGagaResource.class);

    private static final String ENTITY_NAME = "alLadyGaga";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlLadyGagaService alLadyGagaService;

    private final AlLadyGagaRepository alLadyGagaRepository;

    private final AlLadyGagaQueryService alLadyGagaQueryService;

    public AlLadyGagaResource(
        AlLadyGagaService alLadyGagaService,
        AlLadyGagaRepository alLadyGagaRepository,
        AlLadyGagaQueryService alLadyGagaQueryService
    ) {
        this.alLadyGagaService = alLadyGagaService;
        this.alLadyGagaRepository = alLadyGagaRepository;
        this.alLadyGagaQueryService = alLadyGagaQueryService;
    }

    /**
     * {@code POST  /al-lady-gagas} : Create a new alLadyGaga.
     *
     * @param alLadyGaga the alLadyGaga to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alLadyGaga, or with status {@code 400 (Bad Request)} if the alLadyGaga has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlLadyGaga> createAlLadyGaga(@Valid @RequestBody AlLadyGaga alLadyGaga) throws URISyntaxException {
        LOG.debug("REST request to save AlLadyGaga : {}", alLadyGaga);
        if (alLadyGaga.getId() != null) {
            throw new BadRequestAlertException("A new alLadyGaga cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alLadyGaga = alLadyGagaService.save(alLadyGaga);
        return ResponseEntity.created(new URI("/api/al-lady-gagas/" + alLadyGaga.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alLadyGaga.getId().toString()))
            .body(alLadyGaga);
    }

    /**
     * {@code PUT  /al-lady-gagas/:id} : Updates an existing alLadyGaga.
     *
     * @param id the id of the alLadyGaga to save.
     * @param alLadyGaga the alLadyGaga to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alLadyGaga,
     * or with status {@code 400 (Bad Request)} if the alLadyGaga is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alLadyGaga couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlLadyGaga> updateAlLadyGaga(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody AlLadyGaga alLadyGaga
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlLadyGaga : {}, {}", id, alLadyGaga);
        if (alLadyGaga.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alLadyGaga.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alLadyGagaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alLadyGaga = alLadyGagaService.update(alLadyGaga);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alLadyGaga.getId().toString()))
            .body(alLadyGaga);
    }

    /**
     * {@code PATCH  /al-lady-gagas/:id} : Partial updates given fields of an existing alLadyGaga, field will ignore if it is null
     *
     * @param id the id of the alLadyGaga to save.
     * @param alLadyGaga the alLadyGaga to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alLadyGaga,
     * or with status {@code 400 (Bad Request)} if the alLadyGaga is not valid,
     * or with status {@code 404 (Not Found)} if the alLadyGaga is not found,
     * or with status {@code 500 (Internal Server Error)} if the alLadyGaga couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlLadyGaga> partialUpdateAlLadyGaga(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody AlLadyGaga alLadyGaga
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlLadyGaga partially : {}, {}", id, alLadyGaga);
        if (alLadyGaga.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alLadyGaga.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alLadyGagaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlLadyGaga> result = alLadyGagaService.partialUpdate(alLadyGaga);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alLadyGaga.getId().toString())
        );
    }

    /**
     * {@code GET  /al-lady-gagas} : get all the alLadyGagas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alLadyGagas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlLadyGaga>> getAllAlLadyGagas(
        AlLadyGagaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlLadyGagas by criteria: {}", criteria);

        Page<AlLadyGaga> page = alLadyGagaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-lady-gagas/count} : count all the alLadyGagas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlLadyGagas(AlLadyGagaCriteria criteria) {
        LOG.debug("REST request to count AlLadyGagas by criteria: {}", criteria);
        return ResponseEntity.ok().body(alLadyGagaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-lady-gagas/:id} : get the "id" alLadyGaga.
     *
     * @param id the id of the alLadyGaga to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alLadyGaga, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlLadyGaga> getAlLadyGaga(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AlLadyGaga : {}", id);
        Optional<AlLadyGaga> alLadyGaga = alLadyGagaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alLadyGaga);
    }

    /**
     * {@code DELETE  /al-lady-gagas/:id} : delete the "id" alLadyGaga.
     *
     * @param id the id of the alLadyGaga to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlLadyGaga(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AlLadyGaga : {}", id);
        alLadyGagaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
