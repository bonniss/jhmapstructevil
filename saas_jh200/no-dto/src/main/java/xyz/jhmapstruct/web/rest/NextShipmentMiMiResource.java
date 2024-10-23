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
import xyz.jhmapstruct.domain.NextShipmentMiMi;
import xyz.jhmapstruct.repository.NextShipmentMiMiRepository;
import xyz.jhmapstruct.service.NextShipmentMiMiQueryService;
import xyz.jhmapstruct.service.NextShipmentMiMiService;
import xyz.jhmapstruct.service.criteria.NextShipmentMiMiCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextShipmentMiMi}.
 */
@RestController
@RequestMapping("/api/next-shipment-mi-mis")
public class NextShipmentMiMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentMiMiResource.class);

    private static final String ENTITY_NAME = "nextShipmentMiMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextShipmentMiMiService nextShipmentMiMiService;

    private final NextShipmentMiMiRepository nextShipmentMiMiRepository;

    private final NextShipmentMiMiQueryService nextShipmentMiMiQueryService;

    public NextShipmentMiMiResource(
        NextShipmentMiMiService nextShipmentMiMiService,
        NextShipmentMiMiRepository nextShipmentMiMiRepository,
        NextShipmentMiMiQueryService nextShipmentMiMiQueryService
    ) {
        this.nextShipmentMiMiService = nextShipmentMiMiService;
        this.nextShipmentMiMiRepository = nextShipmentMiMiRepository;
        this.nextShipmentMiMiQueryService = nextShipmentMiMiQueryService;
    }

    /**
     * {@code POST  /next-shipment-mi-mis} : Create a new nextShipmentMiMi.
     *
     * @param nextShipmentMiMi the nextShipmentMiMi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextShipmentMiMi, or with status {@code 400 (Bad Request)} if the nextShipmentMiMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextShipmentMiMi> createNextShipmentMiMi(@Valid @RequestBody NextShipmentMiMi nextShipmentMiMi)
        throws URISyntaxException {
        LOG.debug("REST request to save NextShipmentMiMi : {}", nextShipmentMiMi);
        if (nextShipmentMiMi.getId() != null) {
            throw new BadRequestAlertException("A new nextShipmentMiMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextShipmentMiMi = nextShipmentMiMiService.save(nextShipmentMiMi);
        return ResponseEntity.created(new URI("/api/next-shipment-mi-mis/" + nextShipmentMiMi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextShipmentMiMi.getId().toString()))
            .body(nextShipmentMiMi);
    }

    /**
     * {@code PUT  /next-shipment-mi-mis/:id} : Updates an existing nextShipmentMiMi.
     *
     * @param id the id of the nextShipmentMiMi to save.
     * @param nextShipmentMiMi the nextShipmentMiMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextShipmentMiMi,
     * or with status {@code 400 (Bad Request)} if the nextShipmentMiMi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextShipmentMiMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextShipmentMiMi> updateNextShipmentMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextShipmentMiMi nextShipmentMiMi
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextShipmentMiMi : {}, {}", id, nextShipmentMiMi);
        if (nextShipmentMiMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextShipmentMiMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextShipmentMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextShipmentMiMi = nextShipmentMiMiService.update(nextShipmentMiMi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextShipmentMiMi.getId().toString()))
            .body(nextShipmentMiMi);
    }

    /**
     * {@code PATCH  /next-shipment-mi-mis/:id} : Partial updates given fields of an existing nextShipmentMiMi, field will ignore if it is null
     *
     * @param id the id of the nextShipmentMiMi to save.
     * @param nextShipmentMiMi the nextShipmentMiMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextShipmentMiMi,
     * or with status {@code 400 (Bad Request)} if the nextShipmentMiMi is not valid,
     * or with status {@code 404 (Not Found)} if the nextShipmentMiMi is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextShipmentMiMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextShipmentMiMi> partialUpdateNextShipmentMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextShipmentMiMi nextShipmentMiMi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextShipmentMiMi partially : {}, {}", id, nextShipmentMiMi);
        if (nextShipmentMiMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextShipmentMiMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextShipmentMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextShipmentMiMi> result = nextShipmentMiMiService.partialUpdate(nextShipmentMiMi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextShipmentMiMi.getId().toString())
        );
    }

    /**
     * {@code GET  /next-shipment-mi-mis} : get all the nextShipmentMiMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextShipmentMiMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextShipmentMiMi>> getAllNextShipmentMiMis(
        NextShipmentMiMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextShipmentMiMis by criteria: {}", criteria);

        Page<NextShipmentMiMi> page = nextShipmentMiMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-shipment-mi-mis/count} : count all the nextShipmentMiMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextShipmentMiMis(NextShipmentMiMiCriteria criteria) {
        LOG.debug("REST request to count NextShipmentMiMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextShipmentMiMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-shipment-mi-mis/:id} : get the "id" nextShipmentMiMi.
     *
     * @param id the id of the nextShipmentMiMi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextShipmentMiMi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextShipmentMiMi> getNextShipmentMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextShipmentMiMi : {}", id);
        Optional<NextShipmentMiMi> nextShipmentMiMi = nextShipmentMiMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextShipmentMiMi);
    }

    /**
     * {@code DELETE  /next-shipment-mi-mis/:id} : delete the "id" nextShipmentMiMi.
     *
     * @param id the id of the nextShipmentMiMi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextShipmentMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextShipmentMiMi : {}", id);
        nextShipmentMiMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
