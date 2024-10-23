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
import xyz.jhmapstruct.domain.NextPaymentViVi;
import xyz.jhmapstruct.repository.NextPaymentViViRepository;
import xyz.jhmapstruct.service.NextPaymentViViQueryService;
import xyz.jhmapstruct.service.NextPaymentViViService;
import xyz.jhmapstruct.service.criteria.NextPaymentViViCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextPaymentViVi}.
 */
@RestController
@RequestMapping("/api/next-payment-vi-vis")
public class NextPaymentViViResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentViViResource.class);

    private static final String ENTITY_NAME = "nextPaymentViVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextPaymentViViService nextPaymentViViService;

    private final NextPaymentViViRepository nextPaymentViViRepository;

    private final NextPaymentViViQueryService nextPaymentViViQueryService;

    public NextPaymentViViResource(
        NextPaymentViViService nextPaymentViViService,
        NextPaymentViViRepository nextPaymentViViRepository,
        NextPaymentViViQueryService nextPaymentViViQueryService
    ) {
        this.nextPaymentViViService = nextPaymentViViService;
        this.nextPaymentViViRepository = nextPaymentViViRepository;
        this.nextPaymentViViQueryService = nextPaymentViViQueryService;
    }

    /**
     * {@code POST  /next-payment-vi-vis} : Create a new nextPaymentViVi.
     *
     * @param nextPaymentViVi the nextPaymentViVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextPaymentViVi, or with status {@code 400 (Bad Request)} if the nextPaymentViVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextPaymentViVi> createNextPaymentViVi(@Valid @RequestBody NextPaymentViVi nextPaymentViVi)
        throws URISyntaxException {
        LOG.debug("REST request to save NextPaymentViVi : {}", nextPaymentViVi);
        if (nextPaymentViVi.getId() != null) {
            throw new BadRequestAlertException("A new nextPaymentViVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextPaymentViVi = nextPaymentViViService.save(nextPaymentViVi);
        return ResponseEntity.created(new URI("/api/next-payment-vi-vis/" + nextPaymentViVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextPaymentViVi.getId().toString()))
            .body(nextPaymentViVi);
    }

    /**
     * {@code PUT  /next-payment-vi-vis/:id} : Updates an existing nextPaymentViVi.
     *
     * @param id the id of the nextPaymentViVi to save.
     * @param nextPaymentViVi the nextPaymentViVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextPaymentViVi,
     * or with status {@code 400 (Bad Request)} if the nextPaymentViVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextPaymentViVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextPaymentViVi> updateNextPaymentViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextPaymentViVi nextPaymentViVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextPaymentViVi : {}, {}", id, nextPaymentViVi);
        if (nextPaymentViVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextPaymentViVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextPaymentViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextPaymentViVi = nextPaymentViViService.update(nextPaymentViVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextPaymentViVi.getId().toString()))
            .body(nextPaymentViVi);
    }

    /**
     * {@code PATCH  /next-payment-vi-vis/:id} : Partial updates given fields of an existing nextPaymentViVi, field will ignore if it is null
     *
     * @param id the id of the nextPaymentViVi to save.
     * @param nextPaymentViVi the nextPaymentViVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextPaymentViVi,
     * or with status {@code 400 (Bad Request)} if the nextPaymentViVi is not valid,
     * or with status {@code 404 (Not Found)} if the nextPaymentViVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextPaymentViVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextPaymentViVi> partialUpdateNextPaymentViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextPaymentViVi nextPaymentViVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextPaymentViVi partially : {}, {}", id, nextPaymentViVi);
        if (nextPaymentViVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextPaymentViVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextPaymentViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextPaymentViVi> result = nextPaymentViViService.partialUpdate(nextPaymentViVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextPaymentViVi.getId().toString())
        );
    }

    /**
     * {@code GET  /next-payment-vi-vis} : get all the nextPaymentViVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextPaymentViVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextPaymentViVi>> getAllNextPaymentViVis(
        NextPaymentViViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextPaymentViVis by criteria: {}", criteria);

        Page<NextPaymentViVi> page = nextPaymentViViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-payment-vi-vis/count} : count all the nextPaymentViVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextPaymentViVis(NextPaymentViViCriteria criteria) {
        LOG.debug("REST request to count NextPaymentViVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextPaymentViViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-payment-vi-vis/:id} : get the "id" nextPaymentViVi.
     *
     * @param id the id of the nextPaymentViVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextPaymentViVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextPaymentViVi> getNextPaymentViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextPaymentViVi : {}", id);
        Optional<NextPaymentViVi> nextPaymentViVi = nextPaymentViViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextPaymentViVi);
    }

    /**
     * {@code DELETE  /next-payment-vi-vis/:id} : delete the "id" nextPaymentViVi.
     *
     * @param id the id of the nextPaymentViVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextPaymentViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextPaymentViVi : {}", id);
        nextPaymentViViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
