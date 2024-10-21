package ai.realworld.web.rest;

import ai.realworld.domain.AlPacino;
import ai.realworld.repository.AlPacinoRepository;
import ai.realworld.service.AlPacinoQueryService;
import ai.realworld.service.AlPacinoService;
import ai.realworld.service.criteria.AlPacinoCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.AlPacino}.
 */
@RestController
@RequestMapping("/api/al-pacinos")
public class AlPacinoResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlPacinoResource.class);

    private static final String ENTITY_NAME = "alPacino";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlPacinoService alPacinoService;

    private final AlPacinoRepository alPacinoRepository;

    private final AlPacinoQueryService alPacinoQueryService;

    public AlPacinoResource(
        AlPacinoService alPacinoService,
        AlPacinoRepository alPacinoRepository,
        AlPacinoQueryService alPacinoQueryService
    ) {
        this.alPacinoService = alPacinoService;
        this.alPacinoRepository = alPacinoRepository;
        this.alPacinoQueryService = alPacinoQueryService;
    }

    /**
     * {@code POST  /al-pacinos} : Create a new alPacino.
     *
     * @param alPacino the alPacino to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alPacino, or with status {@code 400 (Bad Request)} if the alPacino has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlPacino> createAlPacino(@Valid @RequestBody AlPacino alPacino) throws URISyntaxException {
        LOG.debug("REST request to save AlPacino : {}", alPacino);
        if (alPacino.getId() != null) {
            throw new BadRequestAlertException("A new alPacino cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alPacino = alPacinoService.save(alPacino);
        return ResponseEntity.created(new URI("/api/al-pacinos/" + alPacino.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alPacino.getId().toString()))
            .body(alPacino);
    }

    /**
     * {@code PUT  /al-pacinos/:id} : Updates an existing alPacino.
     *
     * @param id the id of the alPacino to save.
     * @param alPacino the alPacino to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPacino,
     * or with status {@code 400 (Bad Request)} if the alPacino is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alPacino couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlPacino> updateAlPacino(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody AlPacino alPacino
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlPacino : {}, {}", id, alPacino);
        if (alPacino.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPacino.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPacinoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alPacino = alPacinoService.update(alPacino);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPacino.getId().toString()))
            .body(alPacino);
    }

    /**
     * {@code PATCH  /al-pacinos/:id} : Partial updates given fields of an existing alPacino, field will ignore if it is null
     *
     * @param id the id of the alPacino to save.
     * @param alPacino the alPacino to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPacino,
     * or with status {@code 400 (Bad Request)} if the alPacino is not valid,
     * or with status {@code 404 (Not Found)} if the alPacino is not found,
     * or with status {@code 500 (Internal Server Error)} if the alPacino couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlPacino> partialUpdateAlPacino(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody AlPacino alPacino
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlPacino partially : {}, {}", id, alPacino);
        if (alPacino.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPacino.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPacinoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlPacino> result = alPacinoService.partialUpdate(alPacino);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPacino.getId().toString())
        );
    }

    /**
     * {@code GET  /al-pacinos} : get all the alPacinos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alPacinos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlPacino>> getAllAlPacinos(
        AlPacinoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlPacinos by criteria: {}", criteria);

        Page<AlPacino> page = alPacinoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-pacinos/count} : count all the alPacinos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlPacinos(AlPacinoCriteria criteria) {
        LOG.debug("REST request to count AlPacinos by criteria: {}", criteria);
        return ResponseEntity.ok().body(alPacinoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-pacinos/:id} : get the "id" alPacino.
     *
     * @param id the id of the alPacino to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alPacino, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlPacino> getAlPacino(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AlPacino : {}", id);
        Optional<AlPacino> alPacino = alPacinoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alPacino);
    }

    /**
     * {@code DELETE  /al-pacinos/:id} : delete the "id" alPacino.
     *
     * @param id the id of the alPacino to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlPacino(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AlPacino : {}", id);
        alPacinoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
