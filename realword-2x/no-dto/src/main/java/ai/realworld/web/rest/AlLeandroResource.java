package ai.realworld.web.rest;

import ai.realworld.domain.AlLeandro;
import ai.realworld.repository.AlLeandroRepository;
import ai.realworld.service.AlLeandroQueryService;
import ai.realworld.service.AlLeandroService;
import ai.realworld.service.criteria.AlLeandroCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.AlLeandro}.
 */
@RestController
@RequestMapping("/api/al-leandros")
public class AlLeandroResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlLeandroResource.class);

    private static final String ENTITY_NAME = "alLeandro";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlLeandroService alLeandroService;

    private final AlLeandroRepository alLeandroRepository;

    private final AlLeandroQueryService alLeandroQueryService;

    public AlLeandroResource(
        AlLeandroService alLeandroService,
        AlLeandroRepository alLeandroRepository,
        AlLeandroQueryService alLeandroQueryService
    ) {
        this.alLeandroService = alLeandroService;
        this.alLeandroRepository = alLeandroRepository;
        this.alLeandroQueryService = alLeandroQueryService;
    }

    /**
     * {@code POST  /al-leandros} : Create a new alLeandro.
     *
     * @param alLeandro the alLeandro to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alLeandro, or with status {@code 400 (Bad Request)} if the alLeandro has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlLeandro> createAlLeandro(@Valid @RequestBody AlLeandro alLeandro) throws URISyntaxException {
        LOG.debug("REST request to save AlLeandro : {}", alLeandro);
        if (alLeandro.getId() != null) {
            throw new BadRequestAlertException("A new alLeandro cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alLeandro = alLeandroService.save(alLeandro);
        return ResponseEntity.created(new URI("/api/al-leandros/" + alLeandro.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alLeandro.getId().toString()))
            .body(alLeandro);
    }

    /**
     * {@code PUT  /al-leandros/:id} : Updates an existing alLeandro.
     *
     * @param id the id of the alLeandro to save.
     * @param alLeandro the alLeandro to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alLeandro,
     * or with status {@code 400 (Bad Request)} if the alLeandro is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alLeandro couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlLeandro> updateAlLeandro(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody AlLeandro alLeandro
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlLeandro : {}, {}", id, alLeandro);
        if (alLeandro.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alLeandro.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alLeandroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alLeandro = alLeandroService.update(alLeandro);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alLeandro.getId().toString()))
            .body(alLeandro);
    }

    /**
     * {@code PATCH  /al-leandros/:id} : Partial updates given fields of an existing alLeandro, field will ignore if it is null
     *
     * @param id the id of the alLeandro to save.
     * @param alLeandro the alLeandro to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alLeandro,
     * or with status {@code 400 (Bad Request)} if the alLeandro is not valid,
     * or with status {@code 404 (Not Found)} if the alLeandro is not found,
     * or with status {@code 500 (Internal Server Error)} if the alLeandro couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlLeandro> partialUpdateAlLeandro(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody AlLeandro alLeandro
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlLeandro partially : {}, {}", id, alLeandro);
        if (alLeandro.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alLeandro.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alLeandroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlLeandro> result = alLeandroService.partialUpdate(alLeandro);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alLeandro.getId().toString())
        );
    }

    /**
     * {@code GET  /al-leandros} : get all the alLeandros.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alLeandros in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlLeandro>> getAllAlLeandros(
        AlLeandroCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlLeandros by criteria: {}", criteria);

        Page<AlLeandro> page = alLeandroQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-leandros/count} : count all the alLeandros.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlLeandros(AlLeandroCriteria criteria) {
        LOG.debug("REST request to count AlLeandros by criteria: {}", criteria);
        return ResponseEntity.ok().body(alLeandroQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-leandros/:id} : get the "id" alLeandro.
     *
     * @param id the id of the alLeandro to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alLeandro, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlLeandro> getAlLeandro(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AlLeandro : {}", id);
        Optional<AlLeandro> alLeandro = alLeandroService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alLeandro);
    }

    /**
     * {@code DELETE  /al-leandros/:id} : delete the "id" alLeandro.
     *
     * @param id the id of the alLeandro to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlLeandro(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AlLeandro : {}", id);
        alLeandroService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
