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
import xyz.jhmapstruct.domain.NextEmployeeGamma;
import xyz.jhmapstruct.repository.NextEmployeeGammaRepository;
import xyz.jhmapstruct.service.NextEmployeeGammaQueryService;
import xyz.jhmapstruct.service.NextEmployeeGammaService;
import xyz.jhmapstruct.service.criteria.NextEmployeeGammaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextEmployeeGamma}.
 */
@RestController
@RequestMapping("/api/next-employee-gammas")
public class NextEmployeeGammaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeGammaResource.class);

    private static final String ENTITY_NAME = "nextEmployeeGamma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextEmployeeGammaService nextEmployeeGammaService;

    private final NextEmployeeGammaRepository nextEmployeeGammaRepository;

    private final NextEmployeeGammaQueryService nextEmployeeGammaQueryService;

    public NextEmployeeGammaResource(
        NextEmployeeGammaService nextEmployeeGammaService,
        NextEmployeeGammaRepository nextEmployeeGammaRepository,
        NextEmployeeGammaQueryService nextEmployeeGammaQueryService
    ) {
        this.nextEmployeeGammaService = nextEmployeeGammaService;
        this.nextEmployeeGammaRepository = nextEmployeeGammaRepository;
        this.nextEmployeeGammaQueryService = nextEmployeeGammaQueryService;
    }

    /**
     * {@code POST  /next-employee-gammas} : Create a new nextEmployeeGamma.
     *
     * @param nextEmployeeGamma the nextEmployeeGamma to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextEmployeeGamma, or with status {@code 400 (Bad Request)} if the nextEmployeeGamma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextEmployeeGamma> createNextEmployeeGamma(@Valid @RequestBody NextEmployeeGamma nextEmployeeGamma)
        throws URISyntaxException {
        LOG.debug("REST request to save NextEmployeeGamma : {}", nextEmployeeGamma);
        if (nextEmployeeGamma.getId() != null) {
            throw new BadRequestAlertException("A new nextEmployeeGamma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextEmployeeGamma = nextEmployeeGammaService.save(nextEmployeeGamma);
        return ResponseEntity.created(new URI("/api/next-employee-gammas/" + nextEmployeeGamma.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextEmployeeGamma.getId().toString()))
            .body(nextEmployeeGamma);
    }

    /**
     * {@code PUT  /next-employee-gammas/:id} : Updates an existing nextEmployeeGamma.
     *
     * @param id the id of the nextEmployeeGamma to save.
     * @param nextEmployeeGamma the nextEmployeeGamma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextEmployeeGamma,
     * or with status {@code 400 (Bad Request)} if the nextEmployeeGamma is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextEmployeeGamma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextEmployeeGamma> updateNextEmployeeGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextEmployeeGamma nextEmployeeGamma
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextEmployeeGamma : {}, {}", id, nextEmployeeGamma);
        if (nextEmployeeGamma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextEmployeeGamma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextEmployeeGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextEmployeeGamma = nextEmployeeGammaService.update(nextEmployeeGamma);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextEmployeeGamma.getId().toString()))
            .body(nextEmployeeGamma);
    }

    /**
     * {@code PATCH  /next-employee-gammas/:id} : Partial updates given fields of an existing nextEmployeeGamma, field will ignore if it is null
     *
     * @param id the id of the nextEmployeeGamma to save.
     * @param nextEmployeeGamma the nextEmployeeGamma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextEmployeeGamma,
     * or with status {@code 400 (Bad Request)} if the nextEmployeeGamma is not valid,
     * or with status {@code 404 (Not Found)} if the nextEmployeeGamma is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextEmployeeGamma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextEmployeeGamma> partialUpdateNextEmployeeGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextEmployeeGamma nextEmployeeGamma
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextEmployeeGamma partially : {}, {}", id, nextEmployeeGamma);
        if (nextEmployeeGamma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextEmployeeGamma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextEmployeeGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextEmployeeGamma> result = nextEmployeeGammaService.partialUpdate(nextEmployeeGamma);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextEmployeeGamma.getId().toString())
        );
    }

    /**
     * {@code GET  /next-employee-gammas} : get all the nextEmployeeGammas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextEmployeeGammas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextEmployeeGamma>> getAllNextEmployeeGammas(
        NextEmployeeGammaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextEmployeeGammas by criteria: {}", criteria);

        Page<NextEmployeeGamma> page = nextEmployeeGammaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-employee-gammas/count} : count all the nextEmployeeGammas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextEmployeeGammas(NextEmployeeGammaCriteria criteria) {
        LOG.debug("REST request to count NextEmployeeGammas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextEmployeeGammaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-employee-gammas/:id} : get the "id" nextEmployeeGamma.
     *
     * @param id the id of the nextEmployeeGamma to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextEmployeeGamma, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextEmployeeGamma> getNextEmployeeGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextEmployeeGamma : {}", id);
        Optional<NextEmployeeGamma> nextEmployeeGamma = nextEmployeeGammaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextEmployeeGamma);
    }

    /**
     * {@code DELETE  /next-employee-gammas/:id} : delete the "id" nextEmployeeGamma.
     *
     * @param id the id of the nextEmployeeGamma to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextEmployeeGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextEmployeeGamma : {}", id);
        nextEmployeeGammaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
