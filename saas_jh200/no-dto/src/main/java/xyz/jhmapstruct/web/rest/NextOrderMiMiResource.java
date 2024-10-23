package xyz.jhmapstruct.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
import xyz.jhmapstruct.domain.NextOrderMiMi;
import xyz.jhmapstruct.repository.NextOrderMiMiRepository;
import xyz.jhmapstruct.service.NextOrderMiMiQueryService;
import xyz.jhmapstruct.service.NextOrderMiMiService;
import xyz.jhmapstruct.service.criteria.NextOrderMiMiCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextOrderMiMi}.
 */
@RestController
@RequestMapping("/api/next-order-mi-mis")
public class NextOrderMiMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderMiMiResource.class);

    private static final String ENTITY_NAME = "nextOrderMiMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextOrderMiMiService nextOrderMiMiService;

    private final NextOrderMiMiRepository nextOrderMiMiRepository;

    private final NextOrderMiMiQueryService nextOrderMiMiQueryService;

    public NextOrderMiMiResource(
        NextOrderMiMiService nextOrderMiMiService,
        NextOrderMiMiRepository nextOrderMiMiRepository,
        NextOrderMiMiQueryService nextOrderMiMiQueryService
    ) {
        this.nextOrderMiMiService = nextOrderMiMiService;
        this.nextOrderMiMiRepository = nextOrderMiMiRepository;
        this.nextOrderMiMiQueryService = nextOrderMiMiQueryService;
    }

    /**
     * {@code POST  /next-order-mi-mis} : Create a new nextOrderMiMi.
     *
     * @param nextOrderMiMi the nextOrderMiMi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextOrderMiMi, or with status {@code 400 (Bad Request)} if the nextOrderMiMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextOrderMiMi> createNextOrderMiMi(@Valid @RequestBody NextOrderMiMi nextOrderMiMi) throws URISyntaxException {
        LOG.debug("REST request to save NextOrderMiMi : {}", nextOrderMiMi);
        if (nextOrderMiMi.getId() != null) {
            throw new BadRequestAlertException("A new nextOrderMiMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextOrderMiMi = nextOrderMiMiService.save(nextOrderMiMi);
        return ResponseEntity.created(new URI("/api/next-order-mi-mis/" + nextOrderMiMi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextOrderMiMi.getId().toString()))
            .body(nextOrderMiMi);
    }

    /**
     * {@code PUT  /next-order-mi-mis/:id} : Updates an existing nextOrderMiMi.
     *
     * @param id the id of the nextOrderMiMi to save.
     * @param nextOrderMiMi the nextOrderMiMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextOrderMiMi,
     * or with status {@code 400 (Bad Request)} if the nextOrderMiMi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextOrderMiMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextOrderMiMi> updateNextOrderMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextOrderMiMi nextOrderMiMi
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextOrderMiMi : {}, {}", id, nextOrderMiMi);
        if (nextOrderMiMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextOrderMiMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextOrderMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextOrderMiMi = nextOrderMiMiService.update(nextOrderMiMi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextOrderMiMi.getId().toString()))
            .body(nextOrderMiMi);
    }

    /**
     * {@code PATCH  /next-order-mi-mis/:id} : Partial updates given fields of an existing nextOrderMiMi, field will ignore if it is null
     *
     * @param id the id of the nextOrderMiMi to save.
     * @param nextOrderMiMi the nextOrderMiMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextOrderMiMi,
     * or with status {@code 400 (Bad Request)} if the nextOrderMiMi is not valid,
     * or with status {@code 404 (Not Found)} if the nextOrderMiMi is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextOrderMiMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextOrderMiMi> partialUpdateNextOrderMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextOrderMiMi nextOrderMiMi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextOrderMiMi partially : {}, {}", id, nextOrderMiMi);
        if (nextOrderMiMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextOrderMiMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextOrderMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextOrderMiMi> result = nextOrderMiMiService.partialUpdate(nextOrderMiMi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextOrderMiMi.getId().toString())
        );
    }

    /**
     * {@code GET  /next-order-mi-mis} : get all the nextOrderMiMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextOrderMiMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextOrderMiMi>> getAllNextOrderMiMis(
        NextOrderMiMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextOrderMiMis by criteria: {}", criteria);

        Page<NextOrderMiMi> page = nextOrderMiMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-order-mi-mis/count} : count all the nextOrderMiMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextOrderMiMis(NextOrderMiMiCriteria criteria) {
        LOG.debug("REST request to count NextOrderMiMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextOrderMiMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-order-mi-mis/:id} : get the "id" nextOrderMiMi.
     *
     * @param id the id of the nextOrderMiMi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextOrderMiMi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextOrderMiMi> getNextOrderMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextOrderMiMi : {}", id);
        Optional<NextOrderMiMi> nextOrderMiMi = nextOrderMiMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextOrderMiMi);
    }

    /**
     * {@code DELETE  /next-order-mi-mis/:id} : delete the "id" nextOrderMiMi.
     *
     * @param id the id of the nextOrderMiMi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextOrderMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextOrderMiMi : {}", id);
        nextOrderMiMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
