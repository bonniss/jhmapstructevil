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
import xyz.jhmapstruct.domain.NextPaymentMiMi;
import xyz.jhmapstruct.repository.NextPaymentMiMiRepository;
import xyz.jhmapstruct.service.NextPaymentMiMiQueryService;
import xyz.jhmapstruct.service.NextPaymentMiMiService;
import xyz.jhmapstruct.service.criteria.NextPaymentMiMiCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextPaymentMiMi}.
 */
@RestController
@RequestMapping("/api/next-payment-mi-mis")
public class NextPaymentMiMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentMiMiResource.class);

    private static final String ENTITY_NAME = "nextPaymentMiMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextPaymentMiMiService nextPaymentMiMiService;

    private final NextPaymentMiMiRepository nextPaymentMiMiRepository;

    private final NextPaymentMiMiQueryService nextPaymentMiMiQueryService;

    public NextPaymentMiMiResource(
        NextPaymentMiMiService nextPaymentMiMiService,
        NextPaymentMiMiRepository nextPaymentMiMiRepository,
        NextPaymentMiMiQueryService nextPaymentMiMiQueryService
    ) {
        this.nextPaymentMiMiService = nextPaymentMiMiService;
        this.nextPaymentMiMiRepository = nextPaymentMiMiRepository;
        this.nextPaymentMiMiQueryService = nextPaymentMiMiQueryService;
    }

    /**
     * {@code POST  /next-payment-mi-mis} : Create a new nextPaymentMiMi.
     *
     * @param nextPaymentMiMi the nextPaymentMiMi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextPaymentMiMi, or with status {@code 400 (Bad Request)} if the nextPaymentMiMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextPaymentMiMi> createNextPaymentMiMi(@Valid @RequestBody NextPaymentMiMi nextPaymentMiMi)
        throws URISyntaxException {
        LOG.debug("REST request to save NextPaymentMiMi : {}", nextPaymentMiMi);
        if (nextPaymentMiMi.getId() != null) {
            throw new BadRequestAlertException("A new nextPaymentMiMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextPaymentMiMi = nextPaymentMiMiService.save(nextPaymentMiMi);
        return ResponseEntity.created(new URI("/api/next-payment-mi-mis/" + nextPaymentMiMi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextPaymentMiMi.getId().toString()))
            .body(nextPaymentMiMi);
    }

    /**
     * {@code PUT  /next-payment-mi-mis/:id} : Updates an existing nextPaymentMiMi.
     *
     * @param id the id of the nextPaymentMiMi to save.
     * @param nextPaymentMiMi the nextPaymentMiMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextPaymentMiMi,
     * or with status {@code 400 (Bad Request)} if the nextPaymentMiMi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextPaymentMiMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextPaymentMiMi> updateNextPaymentMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextPaymentMiMi nextPaymentMiMi
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextPaymentMiMi : {}, {}", id, nextPaymentMiMi);
        if (nextPaymentMiMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextPaymentMiMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextPaymentMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextPaymentMiMi = nextPaymentMiMiService.update(nextPaymentMiMi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextPaymentMiMi.getId().toString()))
            .body(nextPaymentMiMi);
    }

    /**
     * {@code PATCH  /next-payment-mi-mis/:id} : Partial updates given fields of an existing nextPaymentMiMi, field will ignore if it is null
     *
     * @param id the id of the nextPaymentMiMi to save.
     * @param nextPaymentMiMi the nextPaymentMiMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextPaymentMiMi,
     * or with status {@code 400 (Bad Request)} if the nextPaymentMiMi is not valid,
     * or with status {@code 404 (Not Found)} if the nextPaymentMiMi is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextPaymentMiMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextPaymentMiMi> partialUpdateNextPaymentMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextPaymentMiMi nextPaymentMiMi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextPaymentMiMi partially : {}, {}", id, nextPaymentMiMi);
        if (nextPaymentMiMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextPaymentMiMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextPaymentMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextPaymentMiMi> result = nextPaymentMiMiService.partialUpdate(nextPaymentMiMi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextPaymentMiMi.getId().toString())
        );
    }

    /**
     * {@code GET  /next-payment-mi-mis} : get all the nextPaymentMiMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextPaymentMiMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextPaymentMiMi>> getAllNextPaymentMiMis(
        NextPaymentMiMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextPaymentMiMis by criteria: {}", criteria);

        Page<NextPaymentMiMi> page = nextPaymentMiMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-payment-mi-mis/count} : count all the nextPaymentMiMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextPaymentMiMis(NextPaymentMiMiCriteria criteria) {
        LOG.debug("REST request to count NextPaymentMiMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextPaymentMiMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-payment-mi-mis/:id} : get the "id" nextPaymentMiMi.
     *
     * @param id the id of the nextPaymentMiMi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextPaymentMiMi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextPaymentMiMi> getNextPaymentMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextPaymentMiMi : {}", id);
        Optional<NextPaymentMiMi> nextPaymentMiMi = nextPaymentMiMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextPaymentMiMi);
    }

    /**
     * {@code DELETE  /next-payment-mi-mis/:id} : delete the "id" nextPaymentMiMi.
     *
     * @param id the id of the nextPaymentMiMi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextPaymentMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextPaymentMiMi : {}", id);
        nextPaymentMiMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
