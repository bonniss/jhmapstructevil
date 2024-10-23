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
import xyz.jhmapstruct.domain.NextCustomerMi;
import xyz.jhmapstruct.repository.NextCustomerMiRepository;
import xyz.jhmapstruct.service.NextCustomerMiQueryService;
import xyz.jhmapstruct.service.NextCustomerMiService;
import xyz.jhmapstruct.service.criteria.NextCustomerMiCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextCustomerMi}.
 */
@RestController
@RequestMapping("/api/next-customer-mis")
public class NextCustomerMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerMiResource.class);

    private static final String ENTITY_NAME = "nextCustomerMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextCustomerMiService nextCustomerMiService;

    private final NextCustomerMiRepository nextCustomerMiRepository;

    private final NextCustomerMiQueryService nextCustomerMiQueryService;

    public NextCustomerMiResource(
        NextCustomerMiService nextCustomerMiService,
        NextCustomerMiRepository nextCustomerMiRepository,
        NextCustomerMiQueryService nextCustomerMiQueryService
    ) {
        this.nextCustomerMiService = nextCustomerMiService;
        this.nextCustomerMiRepository = nextCustomerMiRepository;
        this.nextCustomerMiQueryService = nextCustomerMiQueryService;
    }

    /**
     * {@code POST  /next-customer-mis} : Create a new nextCustomerMi.
     *
     * @param nextCustomerMi the nextCustomerMi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextCustomerMi, or with status {@code 400 (Bad Request)} if the nextCustomerMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextCustomerMi> createNextCustomerMi(@Valid @RequestBody NextCustomerMi nextCustomerMi)
        throws URISyntaxException {
        LOG.debug("REST request to save NextCustomerMi : {}", nextCustomerMi);
        if (nextCustomerMi.getId() != null) {
            throw new BadRequestAlertException("A new nextCustomerMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextCustomerMi = nextCustomerMiService.save(nextCustomerMi);
        return ResponseEntity.created(new URI("/api/next-customer-mis/" + nextCustomerMi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextCustomerMi.getId().toString()))
            .body(nextCustomerMi);
    }

    /**
     * {@code PUT  /next-customer-mis/:id} : Updates an existing nextCustomerMi.
     *
     * @param id the id of the nextCustomerMi to save.
     * @param nextCustomerMi the nextCustomerMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCustomerMi,
     * or with status {@code 400 (Bad Request)} if the nextCustomerMi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextCustomerMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextCustomerMi> updateNextCustomerMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextCustomerMi nextCustomerMi
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextCustomerMi : {}, {}", id, nextCustomerMi);
        if (nextCustomerMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCustomerMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCustomerMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextCustomerMi = nextCustomerMiService.update(nextCustomerMi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCustomerMi.getId().toString()))
            .body(nextCustomerMi);
    }

    /**
     * {@code PATCH  /next-customer-mis/:id} : Partial updates given fields of an existing nextCustomerMi, field will ignore if it is null
     *
     * @param id the id of the nextCustomerMi to save.
     * @param nextCustomerMi the nextCustomerMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCustomerMi,
     * or with status {@code 400 (Bad Request)} if the nextCustomerMi is not valid,
     * or with status {@code 404 (Not Found)} if the nextCustomerMi is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextCustomerMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextCustomerMi> partialUpdateNextCustomerMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextCustomerMi nextCustomerMi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextCustomerMi partially : {}, {}", id, nextCustomerMi);
        if (nextCustomerMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCustomerMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCustomerMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextCustomerMi> result = nextCustomerMiService.partialUpdate(nextCustomerMi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCustomerMi.getId().toString())
        );
    }

    /**
     * {@code GET  /next-customer-mis} : get all the nextCustomerMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextCustomerMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextCustomerMi>> getAllNextCustomerMis(
        NextCustomerMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextCustomerMis by criteria: {}", criteria);

        Page<NextCustomerMi> page = nextCustomerMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-customer-mis/count} : count all the nextCustomerMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextCustomerMis(NextCustomerMiCriteria criteria) {
        LOG.debug("REST request to count NextCustomerMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextCustomerMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-customer-mis/:id} : get the "id" nextCustomerMi.
     *
     * @param id the id of the nextCustomerMi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextCustomerMi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextCustomerMi> getNextCustomerMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextCustomerMi : {}", id);
        Optional<NextCustomerMi> nextCustomerMi = nextCustomerMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextCustomerMi);
    }

    /**
     * {@code DELETE  /next-customer-mis/:id} : delete the "id" nextCustomerMi.
     *
     * @param id the id of the nextCustomerMi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextCustomerMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextCustomerMi : {}", id);
        nextCustomerMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
