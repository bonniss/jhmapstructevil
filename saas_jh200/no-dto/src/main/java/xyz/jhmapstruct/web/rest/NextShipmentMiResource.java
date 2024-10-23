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
import xyz.jhmapstruct.domain.NextShipmentMi;
import xyz.jhmapstruct.repository.NextShipmentMiRepository;
import xyz.jhmapstruct.service.NextShipmentMiQueryService;
import xyz.jhmapstruct.service.NextShipmentMiService;
import xyz.jhmapstruct.service.criteria.NextShipmentMiCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextShipmentMi}.
 */
@RestController
@RequestMapping("/api/next-shipment-mis")
public class NextShipmentMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentMiResource.class);

    private static final String ENTITY_NAME = "nextShipmentMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextShipmentMiService nextShipmentMiService;

    private final NextShipmentMiRepository nextShipmentMiRepository;

    private final NextShipmentMiQueryService nextShipmentMiQueryService;

    public NextShipmentMiResource(
        NextShipmentMiService nextShipmentMiService,
        NextShipmentMiRepository nextShipmentMiRepository,
        NextShipmentMiQueryService nextShipmentMiQueryService
    ) {
        this.nextShipmentMiService = nextShipmentMiService;
        this.nextShipmentMiRepository = nextShipmentMiRepository;
        this.nextShipmentMiQueryService = nextShipmentMiQueryService;
    }

    /**
     * {@code POST  /next-shipment-mis} : Create a new nextShipmentMi.
     *
     * @param nextShipmentMi the nextShipmentMi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextShipmentMi, or with status {@code 400 (Bad Request)} if the nextShipmentMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextShipmentMi> createNextShipmentMi(@Valid @RequestBody NextShipmentMi nextShipmentMi)
        throws URISyntaxException {
        LOG.debug("REST request to save NextShipmentMi : {}", nextShipmentMi);
        if (nextShipmentMi.getId() != null) {
            throw new BadRequestAlertException("A new nextShipmentMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextShipmentMi = nextShipmentMiService.save(nextShipmentMi);
        return ResponseEntity.created(new URI("/api/next-shipment-mis/" + nextShipmentMi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextShipmentMi.getId().toString()))
            .body(nextShipmentMi);
    }

    /**
     * {@code PUT  /next-shipment-mis/:id} : Updates an existing nextShipmentMi.
     *
     * @param id the id of the nextShipmentMi to save.
     * @param nextShipmentMi the nextShipmentMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextShipmentMi,
     * or with status {@code 400 (Bad Request)} if the nextShipmentMi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextShipmentMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextShipmentMi> updateNextShipmentMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextShipmentMi nextShipmentMi
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextShipmentMi : {}, {}", id, nextShipmentMi);
        if (nextShipmentMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextShipmentMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextShipmentMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextShipmentMi = nextShipmentMiService.update(nextShipmentMi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextShipmentMi.getId().toString()))
            .body(nextShipmentMi);
    }

    /**
     * {@code PATCH  /next-shipment-mis/:id} : Partial updates given fields of an existing nextShipmentMi, field will ignore if it is null
     *
     * @param id the id of the nextShipmentMi to save.
     * @param nextShipmentMi the nextShipmentMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextShipmentMi,
     * or with status {@code 400 (Bad Request)} if the nextShipmentMi is not valid,
     * or with status {@code 404 (Not Found)} if the nextShipmentMi is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextShipmentMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextShipmentMi> partialUpdateNextShipmentMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextShipmentMi nextShipmentMi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextShipmentMi partially : {}, {}", id, nextShipmentMi);
        if (nextShipmentMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextShipmentMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextShipmentMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextShipmentMi> result = nextShipmentMiService.partialUpdate(nextShipmentMi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextShipmentMi.getId().toString())
        );
    }

    /**
     * {@code GET  /next-shipment-mis} : get all the nextShipmentMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextShipmentMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextShipmentMi>> getAllNextShipmentMis(
        NextShipmentMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextShipmentMis by criteria: {}", criteria);

        Page<NextShipmentMi> page = nextShipmentMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-shipment-mis/count} : count all the nextShipmentMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextShipmentMis(NextShipmentMiCriteria criteria) {
        LOG.debug("REST request to count NextShipmentMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextShipmentMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-shipment-mis/:id} : get the "id" nextShipmentMi.
     *
     * @param id the id of the nextShipmentMi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextShipmentMi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextShipmentMi> getNextShipmentMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextShipmentMi : {}", id);
        Optional<NextShipmentMi> nextShipmentMi = nextShipmentMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextShipmentMi);
    }

    /**
     * {@code DELETE  /next-shipment-mis/:id} : delete the "id" nextShipmentMi.
     *
     * @param id the id of the nextShipmentMi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextShipmentMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextShipmentMi : {}", id);
        nextShipmentMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
