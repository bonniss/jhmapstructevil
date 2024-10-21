package ai.realworld.web.rest;

import ai.realworld.domain.JohnLennon;
import ai.realworld.repository.JohnLennonRepository;
import ai.realworld.service.JohnLennonQueryService;
import ai.realworld.service.JohnLennonService;
import ai.realworld.service.criteria.JohnLennonCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.JohnLennon}.
 */
@RestController
@RequestMapping("/api/john-lennons")
public class JohnLennonResource {

    private static final Logger LOG = LoggerFactory.getLogger(JohnLennonResource.class);

    private static final String ENTITY_NAME = "johnLennon";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JohnLennonService johnLennonService;

    private final JohnLennonRepository johnLennonRepository;

    private final JohnLennonQueryService johnLennonQueryService;

    public JohnLennonResource(
        JohnLennonService johnLennonService,
        JohnLennonRepository johnLennonRepository,
        JohnLennonQueryService johnLennonQueryService
    ) {
        this.johnLennonService = johnLennonService;
        this.johnLennonRepository = johnLennonRepository;
        this.johnLennonQueryService = johnLennonQueryService;
    }

    /**
     * {@code POST  /john-lennons} : Create a new johnLennon.
     *
     * @param johnLennon the johnLennon to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new johnLennon, or with status {@code 400 (Bad Request)} if the johnLennon has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<JohnLennon> createJohnLennon(@Valid @RequestBody JohnLennon johnLennon) throws URISyntaxException {
        LOG.debug("REST request to save JohnLennon : {}", johnLennon);
        if (johnLennon.getId() != null) {
            throw new BadRequestAlertException("A new johnLennon cannot already have an ID", ENTITY_NAME, "idexists");
        }
        johnLennon = johnLennonService.save(johnLennon);
        return ResponseEntity.created(new URI("/api/john-lennons/" + johnLennon.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, johnLennon.getId().toString()))
            .body(johnLennon);
    }

    /**
     * {@code PUT  /john-lennons/:id} : Updates an existing johnLennon.
     *
     * @param id the id of the johnLennon to save.
     * @param johnLennon the johnLennon to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated johnLennon,
     * or with status {@code 400 (Bad Request)} if the johnLennon is not valid,
     * or with status {@code 500 (Internal Server Error)} if the johnLennon couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<JohnLennon> updateJohnLennon(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody JohnLennon johnLennon
    ) throws URISyntaxException {
        LOG.debug("REST request to update JohnLennon : {}, {}", id, johnLennon);
        if (johnLennon.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, johnLennon.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!johnLennonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        johnLennon = johnLennonService.update(johnLennon);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, johnLennon.getId().toString()))
            .body(johnLennon);
    }

    /**
     * {@code PATCH  /john-lennons/:id} : Partial updates given fields of an existing johnLennon, field will ignore if it is null
     *
     * @param id the id of the johnLennon to save.
     * @param johnLennon the johnLennon to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated johnLennon,
     * or with status {@code 400 (Bad Request)} if the johnLennon is not valid,
     * or with status {@code 404 (Not Found)} if the johnLennon is not found,
     * or with status {@code 500 (Internal Server Error)} if the johnLennon couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<JohnLennon> partialUpdateJohnLennon(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody JohnLennon johnLennon
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update JohnLennon partially : {}, {}", id, johnLennon);
        if (johnLennon.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, johnLennon.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!johnLennonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<JohnLennon> result = johnLennonService.partialUpdate(johnLennon);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, johnLennon.getId().toString())
        );
    }

    /**
     * {@code GET  /john-lennons} : get all the johnLennons.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of johnLennons in body.
     */
    @GetMapping("")
    public ResponseEntity<List<JohnLennon>> getAllJohnLennons(
        JohnLennonCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get JohnLennons by criteria: {}", criteria);

        Page<JohnLennon> page = johnLennonQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /john-lennons/count} : count all the johnLennons.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countJohnLennons(JohnLennonCriteria criteria) {
        LOG.debug("REST request to count JohnLennons by criteria: {}", criteria);
        return ResponseEntity.ok().body(johnLennonQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /john-lennons/:id} : get the "id" johnLennon.
     *
     * @param id the id of the johnLennon to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the johnLennon, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<JohnLennon> getJohnLennon(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get JohnLennon : {}", id);
        Optional<JohnLennon> johnLennon = johnLennonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(johnLennon);
    }

    /**
     * {@code DELETE  /john-lennons/:id} : delete the "id" johnLennon.
     *
     * @param id the id of the johnLennon to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJohnLennon(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete JohnLennon : {}", id);
        johnLennonService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
