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
import xyz.jhmapstruct.domain.NextProductMiMi;
import xyz.jhmapstruct.repository.NextProductMiMiRepository;
import xyz.jhmapstruct.service.NextProductMiMiQueryService;
import xyz.jhmapstruct.service.NextProductMiMiService;
import xyz.jhmapstruct.service.criteria.NextProductMiMiCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextProductMiMi}.
 */
@RestController
@RequestMapping("/api/next-product-mi-mis")
public class NextProductMiMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductMiMiResource.class);

    private static final String ENTITY_NAME = "nextProductMiMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextProductMiMiService nextProductMiMiService;

    private final NextProductMiMiRepository nextProductMiMiRepository;

    private final NextProductMiMiQueryService nextProductMiMiQueryService;

    public NextProductMiMiResource(
        NextProductMiMiService nextProductMiMiService,
        NextProductMiMiRepository nextProductMiMiRepository,
        NextProductMiMiQueryService nextProductMiMiQueryService
    ) {
        this.nextProductMiMiService = nextProductMiMiService;
        this.nextProductMiMiRepository = nextProductMiMiRepository;
        this.nextProductMiMiQueryService = nextProductMiMiQueryService;
    }

    /**
     * {@code POST  /next-product-mi-mis} : Create a new nextProductMiMi.
     *
     * @param nextProductMiMi the nextProductMiMi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextProductMiMi, or with status {@code 400 (Bad Request)} if the nextProductMiMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextProductMiMi> createNextProductMiMi(@Valid @RequestBody NextProductMiMi nextProductMiMi)
        throws URISyntaxException {
        LOG.debug("REST request to save NextProductMiMi : {}", nextProductMiMi);
        if (nextProductMiMi.getId() != null) {
            throw new BadRequestAlertException("A new nextProductMiMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextProductMiMi = nextProductMiMiService.save(nextProductMiMi);
        return ResponseEntity.created(new URI("/api/next-product-mi-mis/" + nextProductMiMi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextProductMiMi.getId().toString()))
            .body(nextProductMiMi);
    }

    /**
     * {@code PUT  /next-product-mi-mis/:id} : Updates an existing nextProductMiMi.
     *
     * @param id the id of the nextProductMiMi to save.
     * @param nextProductMiMi the nextProductMiMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextProductMiMi,
     * or with status {@code 400 (Bad Request)} if the nextProductMiMi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextProductMiMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextProductMiMi> updateNextProductMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextProductMiMi nextProductMiMi
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextProductMiMi : {}, {}", id, nextProductMiMi);
        if (nextProductMiMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextProductMiMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextProductMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextProductMiMi = nextProductMiMiService.update(nextProductMiMi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextProductMiMi.getId().toString()))
            .body(nextProductMiMi);
    }

    /**
     * {@code PATCH  /next-product-mi-mis/:id} : Partial updates given fields of an existing nextProductMiMi, field will ignore if it is null
     *
     * @param id the id of the nextProductMiMi to save.
     * @param nextProductMiMi the nextProductMiMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextProductMiMi,
     * or with status {@code 400 (Bad Request)} if the nextProductMiMi is not valid,
     * or with status {@code 404 (Not Found)} if the nextProductMiMi is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextProductMiMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextProductMiMi> partialUpdateNextProductMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextProductMiMi nextProductMiMi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextProductMiMi partially : {}, {}", id, nextProductMiMi);
        if (nextProductMiMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextProductMiMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextProductMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextProductMiMi> result = nextProductMiMiService.partialUpdate(nextProductMiMi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextProductMiMi.getId().toString())
        );
    }

    /**
     * {@code GET  /next-product-mi-mis} : get all the nextProductMiMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextProductMiMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextProductMiMi>> getAllNextProductMiMis(
        NextProductMiMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextProductMiMis by criteria: {}", criteria);

        Page<NextProductMiMi> page = nextProductMiMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-product-mi-mis/count} : count all the nextProductMiMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextProductMiMis(NextProductMiMiCriteria criteria) {
        LOG.debug("REST request to count NextProductMiMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextProductMiMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-product-mi-mis/:id} : get the "id" nextProductMiMi.
     *
     * @param id the id of the nextProductMiMi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextProductMiMi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextProductMiMi> getNextProductMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextProductMiMi : {}", id);
        Optional<NextProductMiMi> nextProductMiMi = nextProductMiMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextProductMiMi);
    }

    /**
     * {@code DELETE  /next-product-mi-mis/:id} : delete the "id" nextProductMiMi.
     *
     * @param id the id of the nextProductMiMi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextProductMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextProductMiMi : {}", id);
        nextProductMiMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
