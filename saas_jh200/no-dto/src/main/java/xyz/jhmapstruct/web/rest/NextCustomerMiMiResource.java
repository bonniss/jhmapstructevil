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
import xyz.jhmapstruct.domain.NextCustomerMiMi;
import xyz.jhmapstruct.repository.NextCustomerMiMiRepository;
import xyz.jhmapstruct.service.NextCustomerMiMiQueryService;
import xyz.jhmapstruct.service.NextCustomerMiMiService;
import xyz.jhmapstruct.service.criteria.NextCustomerMiMiCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextCustomerMiMi}.
 */
@RestController
@RequestMapping("/api/next-customer-mi-mis")
public class NextCustomerMiMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerMiMiResource.class);

    private static final String ENTITY_NAME = "nextCustomerMiMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextCustomerMiMiService nextCustomerMiMiService;

    private final NextCustomerMiMiRepository nextCustomerMiMiRepository;

    private final NextCustomerMiMiQueryService nextCustomerMiMiQueryService;

    public NextCustomerMiMiResource(
        NextCustomerMiMiService nextCustomerMiMiService,
        NextCustomerMiMiRepository nextCustomerMiMiRepository,
        NextCustomerMiMiQueryService nextCustomerMiMiQueryService
    ) {
        this.nextCustomerMiMiService = nextCustomerMiMiService;
        this.nextCustomerMiMiRepository = nextCustomerMiMiRepository;
        this.nextCustomerMiMiQueryService = nextCustomerMiMiQueryService;
    }

    /**
     * {@code POST  /next-customer-mi-mis} : Create a new nextCustomerMiMi.
     *
     * @param nextCustomerMiMi the nextCustomerMiMi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextCustomerMiMi, or with status {@code 400 (Bad Request)} if the nextCustomerMiMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextCustomerMiMi> createNextCustomerMiMi(@Valid @RequestBody NextCustomerMiMi nextCustomerMiMi)
        throws URISyntaxException {
        LOG.debug("REST request to save NextCustomerMiMi : {}", nextCustomerMiMi);
        if (nextCustomerMiMi.getId() != null) {
            throw new BadRequestAlertException("A new nextCustomerMiMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextCustomerMiMi = nextCustomerMiMiService.save(nextCustomerMiMi);
        return ResponseEntity.created(new URI("/api/next-customer-mi-mis/" + nextCustomerMiMi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextCustomerMiMi.getId().toString()))
            .body(nextCustomerMiMi);
    }

    /**
     * {@code PUT  /next-customer-mi-mis/:id} : Updates an existing nextCustomerMiMi.
     *
     * @param id the id of the nextCustomerMiMi to save.
     * @param nextCustomerMiMi the nextCustomerMiMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCustomerMiMi,
     * or with status {@code 400 (Bad Request)} if the nextCustomerMiMi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextCustomerMiMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextCustomerMiMi> updateNextCustomerMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextCustomerMiMi nextCustomerMiMi
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextCustomerMiMi : {}, {}", id, nextCustomerMiMi);
        if (nextCustomerMiMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCustomerMiMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCustomerMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextCustomerMiMi = nextCustomerMiMiService.update(nextCustomerMiMi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCustomerMiMi.getId().toString()))
            .body(nextCustomerMiMi);
    }

    /**
     * {@code PATCH  /next-customer-mi-mis/:id} : Partial updates given fields of an existing nextCustomerMiMi, field will ignore if it is null
     *
     * @param id the id of the nextCustomerMiMi to save.
     * @param nextCustomerMiMi the nextCustomerMiMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCustomerMiMi,
     * or with status {@code 400 (Bad Request)} if the nextCustomerMiMi is not valid,
     * or with status {@code 404 (Not Found)} if the nextCustomerMiMi is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextCustomerMiMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextCustomerMiMi> partialUpdateNextCustomerMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextCustomerMiMi nextCustomerMiMi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextCustomerMiMi partially : {}, {}", id, nextCustomerMiMi);
        if (nextCustomerMiMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCustomerMiMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCustomerMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextCustomerMiMi> result = nextCustomerMiMiService.partialUpdate(nextCustomerMiMi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCustomerMiMi.getId().toString())
        );
    }

    /**
     * {@code GET  /next-customer-mi-mis} : get all the nextCustomerMiMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextCustomerMiMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextCustomerMiMi>> getAllNextCustomerMiMis(
        NextCustomerMiMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextCustomerMiMis by criteria: {}", criteria);

        Page<NextCustomerMiMi> page = nextCustomerMiMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-customer-mi-mis/count} : count all the nextCustomerMiMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextCustomerMiMis(NextCustomerMiMiCriteria criteria) {
        LOG.debug("REST request to count NextCustomerMiMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextCustomerMiMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-customer-mi-mis/:id} : get the "id" nextCustomerMiMi.
     *
     * @param id the id of the nextCustomerMiMi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextCustomerMiMi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextCustomerMiMi> getNextCustomerMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextCustomerMiMi : {}", id);
        Optional<NextCustomerMiMi> nextCustomerMiMi = nextCustomerMiMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextCustomerMiMi);
    }

    /**
     * {@code DELETE  /next-customer-mi-mis/:id} : delete the "id" nextCustomerMiMi.
     *
     * @param id the id of the nextCustomerMiMi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextCustomerMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextCustomerMiMi : {}", id);
        nextCustomerMiMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
