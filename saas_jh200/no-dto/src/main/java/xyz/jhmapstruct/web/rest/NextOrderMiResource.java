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
import xyz.jhmapstruct.domain.NextOrderMi;
import xyz.jhmapstruct.repository.NextOrderMiRepository;
import xyz.jhmapstruct.service.NextOrderMiQueryService;
import xyz.jhmapstruct.service.NextOrderMiService;
import xyz.jhmapstruct.service.criteria.NextOrderMiCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextOrderMi}.
 */
@RestController
@RequestMapping("/api/next-order-mis")
public class NextOrderMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderMiResource.class);

    private static final String ENTITY_NAME = "nextOrderMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextOrderMiService nextOrderMiService;

    private final NextOrderMiRepository nextOrderMiRepository;

    private final NextOrderMiQueryService nextOrderMiQueryService;

    public NextOrderMiResource(
        NextOrderMiService nextOrderMiService,
        NextOrderMiRepository nextOrderMiRepository,
        NextOrderMiQueryService nextOrderMiQueryService
    ) {
        this.nextOrderMiService = nextOrderMiService;
        this.nextOrderMiRepository = nextOrderMiRepository;
        this.nextOrderMiQueryService = nextOrderMiQueryService;
    }

    /**
     * {@code POST  /next-order-mis} : Create a new nextOrderMi.
     *
     * @param nextOrderMi the nextOrderMi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextOrderMi, or with status {@code 400 (Bad Request)} if the nextOrderMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextOrderMi> createNextOrderMi(@Valid @RequestBody NextOrderMi nextOrderMi) throws URISyntaxException {
        LOG.debug("REST request to save NextOrderMi : {}", nextOrderMi);
        if (nextOrderMi.getId() != null) {
            throw new BadRequestAlertException("A new nextOrderMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextOrderMi = nextOrderMiService.save(nextOrderMi);
        return ResponseEntity.created(new URI("/api/next-order-mis/" + nextOrderMi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextOrderMi.getId().toString()))
            .body(nextOrderMi);
    }

    /**
     * {@code PUT  /next-order-mis/:id} : Updates an existing nextOrderMi.
     *
     * @param id the id of the nextOrderMi to save.
     * @param nextOrderMi the nextOrderMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextOrderMi,
     * or with status {@code 400 (Bad Request)} if the nextOrderMi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextOrderMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextOrderMi> updateNextOrderMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextOrderMi nextOrderMi
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextOrderMi : {}, {}", id, nextOrderMi);
        if (nextOrderMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextOrderMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextOrderMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextOrderMi = nextOrderMiService.update(nextOrderMi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextOrderMi.getId().toString()))
            .body(nextOrderMi);
    }

    /**
     * {@code PATCH  /next-order-mis/:id} : Partial updates given fields of an existing nextOrderMi, field will ignore if it is null
     *
     * @param id the id of the nextOrderMi to save.
     * @param nextOrderMi the nextOrderMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextOrderMi,
     * or with status {@code 400 (Bad Request)} if the nextOrderMi is not valid,
     * or with status {@code 404 (Not Found)} if the nextOrderMi is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextOrderMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextOrderMi> partialUpdateNextOrderMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextOrderMi nextOrderMi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextOrderMi partially : {}, {}", id, nextOrderMi);
        if (nextOrderMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextOrderMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextOrderMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextOrderMi> result = nextOrderMiService.partialUpdate(nextOrderMi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextOrderMi.getId().toString())
        );
    }

    /**
     * {@code GET  /next-order-mis} : get all the nextOrderMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextOrderMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextOrderMi>> getAllNextOrderMis(
        NextOrderMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextOrderMis by criteria: {}", criteria);

        Page<NextOrderMi> page = nextOrderMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-order-mis/count} : count all the nextOrderMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextOrderMis(NextOrderMiCriteria criteria) {
        LOG.debug("REST request to count NextOrderMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextOrderMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-order-mis/:id} : get the "id" nextOrderMi.
     *
     * @param id the id of the nextOrderMi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextOrderMi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextOrderMi> getNextOrderMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextOrderMi : {}", id);
        Optional<NextOrderMi> nextOrderMi = nextOrderMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextOrderMi);
    }

    /**
     * {@code DELETE  /next-order-mis/:id} : delete the "id" nextOrderMi.
     *
     * @param id the id of the nextOrderMi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextOrderMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextOrderMi : {}", id);
        nextOrderMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
