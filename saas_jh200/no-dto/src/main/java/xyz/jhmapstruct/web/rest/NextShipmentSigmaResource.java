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
import xyz.jhmapstruct.domain.NextShipmentSigma;
import xyz.jhmapstruct.repository.NextShipmentSigmaRepository;
import xyz.jhmapstruct.service.NextShipmentSigmaQueryService;
import xyz.jhmapstruct.service.NextShipmentSigmaService;
import xyz.jhmapstruct.service.criteria.NextShipmentSigmaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextShipmentSigma}.
 */
@RestController
@RequestMapping("/api/next-shipment-sigmas")
public class NextShipmentSigmaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentSigmaResource.class);

    private static final String ENTITY_NAME = "nextShipmentSigma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextShipmentSigmaService nextShipmentSigmaService;

    private final NextShipmentSigmaRepository nextShipmentSigmaRepository;

    private final NextShipmentSigmaQueryService nextShipmentSigmaQueryService;

    public NextShipmentSigmaResource(
        NextShipmentSigmaService nextShipmentSigmaService,
        NextShipmentSigmaRepository nextShipmentSigmaRepository,
        NextShipmentSigmaQueryService nextShipmentSigmaQueryService
    ) {
        this.nextShipmentSigmaService = nextShipmentSigmaService;
        this.nextShipmentSigmaRepository = nextShipmentSigmaRepository;
        this.nextShipmentSigmaQueryService = nextShipmentSigmaQueryService;
    }

    /**
     * {@code POST  /next-shipment-sigmas} : Create a new nextShipmentSigma.
     *
     * @param nextShipmentSigma the nextShipmentSigma to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextShipmentSigma, or with status {@code 400 (Bad Request)} if the nextShipmentSigma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextShipmentSigma> createNextShipmentSigma(@Valid @RequestBody NextShipmentSigma nextShipmentSigma)
        throws URISyntaxException {
        LOG.debug("REST request to save NextShipmentSigma : {}", nextShipmentSigma);
        if (nextShipmentSigma.getId() != null) {
            throw new BadRequestAlertException("A new nextShipmentSigma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextShipmentSigma = nextShipmentSigmaService.save(nextShipmentSigma);
        return ResponseEntity.created(new URI("/api/next-shipment-sigmas/" + nextShipmentSigma.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextShipmentSigma.getId().toString()))
            .body(nextShipmentSigma);
    }

    /**
     * {@code PUT  /next-shipment-sigmas/:id} : Updates an existing nextShipmentSigma.
     *
     * @param id the id of the nextShipmentSigma to save.
     * @param nextShipmentSigma the nextShipmentSigma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextShipmentSigma,
     * or with status {@code 400 (Bad Request)} if the nextShipmentSigma is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextShipmentSigma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextShipmentSigma> updateNextShipmentSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextShipmentSigma nextShipmentSigma
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextShipmentSigma : {}, {}", id, nextShipmentSigma);
        if (nextShipmentSigma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextShipmentSigma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextShipmentSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextShipmentSigma = nextShipmentSigmaService.update(nextShipmentSigma);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextShipmentSigma.getId().toString()))
            .body(nextShipmentSigma);
    }

    /**
     * {@code PATCH  /next-shipment-sigmas/:id} : Partial updates given fields of an existing nextShipmentSigma, field will ignore if it is null
     *
     * @param id the id of the nextShipmentSigma to save.
     * @param nextShipmentSigma the nextShipmentSigma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextShipmentSigma,
     * or with status {@code 400 (Bad Request)} if the nextShipmentSigma is not valid,
     * or with status {@code 404 (Not Found)} if the nextShipmentSigma is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextShipmentSigma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextShipmentSigma> partialUpdateNextShipmentSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextShipmentSigma nextShipmentSigma
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextShipmentSigma partially : {}, {}", id, nextShipmentSigma);
        if (nextShipmentSigma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextShipmentSigma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextShipmentSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextShipmentSigma> result = nextShipmentSigmaService.partialUpdate(nextShipmentSigma);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextShipmentSigma.getId().toString())
        );
    }

    /**
     * {@code GET  /next-shipment-sigmas} : get all the nextShipmentSigmas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextShipmentSigmas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextShipmentSigma>> getAllNextShipmentSigmas(
        NextShipmentSigmaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextShipmentSigmas by criteria: {}", criteria);

        Page<NextShipmentSigma> page = nextShipmentSigmaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-shipment-sigmas/count} : count all the nextShipmentSigmas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextShipmentSigmas(NextShipmentSigmaCriteria criteria) {
        LOG.debug("REST request to count NextShipmentSigmas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextShipmentSigmaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-shipment-sigmas/:id} : get the "id" nextShipmentSigma.
     *
     * @param id the id of the nextShipmentSigma to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextShipmentSigma, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextShipmentSigma> getNextShipmentSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextShipmentSigma : {}", id);
        Optional<NextShipmentSigma> nextShipmentSigma = nextShipmentSigmaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextShipmentSigma);
    }

    /**
     * {@code DELETE  /next-shipment-sigmas/:id} : delete the "id" nextShipmentSigma.
     *
     * @param id the id of the nextShipmentSigma to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextShipmentSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextShipmentSigma : {}", id);
        nextShipmentSigmaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
