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
import xyz.jhmapstruct.domain.NextProductAlpha;
import xyz.jhmapstruct.repository.NextProductAlphaRepository;
import xyz.jhmapstruct.service.NextProductAlphaQueryService;
import xyz.jhmapstruct.service.NextProductAlphaService;
import xyz.jhmapstruct.service.criteria.NextProductAlphaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextProductAlpha}.
 */
@RestController
@RequestMapping("/api/next-product-alphas")
public class NextProductAlphaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductAlphaResource.class);

    private static final String ENTITY_NAME = "nextProductAlpha";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextProductAlphaService nextProductAlphaService;

    private final NextProductAlphaRepository nextProductAlphaRepository;

    private final NextProductAlphaQueryService nextProductAlphaQueryService;

    public NextProductAlphaResource(
        NextProductAlphaService nextProductAlphaService,
        NextProductAlphaRepository nextProductAlphaRepository,
        NextProductAlphaQueryService nextProductAlphaQueryService
    ) {
        this.nextProductAlphaService = nextProductAlphaService;
        this.nextProductAlphaRepository = nextProductAlphaRepository;
        this.nextProductAlphaQueryService = nextProductAlphaQueryService;
    }

    /**
     * {@code POST  /next-product-alphas} : Create a new nextProductAlpha.
     *
     * @param nextProductAlpha the nextProductAlpha to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextProductAlpha, or with status {@code 400 (Bad Request)} if the nextProductAlpha has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextProductAlpha> createNextProductAlpha(@Valid @RequestBody NextProductAlpha nextProductAlpha)
        throws URISyntaxException {
        LOG.debug("REST request to save NextProductAlpha : {}", nextProductAlpha);
        if (nextProductAlpha.getId() != null) {
            throw new BadRequestAlertException("A new nextProductAlpha cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextProductAlpha = nextProductAlphaService.save(nextProductAlpha);
        return ResponseEntity.created(new URI("/api/next-product-alphas/" + nextProductAlpha.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextProductAlpha.getId().toString()))
            .body(nextProductAlpha);
    }

    /**
     * {@code PUT  /next-product-alphas/:id} : Updates an existing nextProductAlpha.
     *
     * @param id the id of the nextProductAlpha to save.
     * @param nextProductAlpha the nextProductAlpha to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextProductAlpha,
     * or with status {@code 400 (Bad Request)} if the nextProductAlpha is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextProductAlpha couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextProductAlpha> updateNextProductAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextProductAlpha nextProductAlpha
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextProductAlpha : {}, {}", id, nextProductAlpha);
        if (nextProductAlpha.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextProductAlpha.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextProductAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextProductAlpha = nextProductAlphaService.update(nextProductAlpha);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextProductAlpha.getId().toString()))
            .body(nextProductAlpha);
    }

    /**
     * {@code PATCH  /next-product-alphas/:id} : Partial updates given fields of an existing nextProductAlpha, field will ignore if it is null
     *
     * @param id the id of the nextProductAlpha to save.
     * @param nextProductAlpha the nextProductAlpha to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextProductAlpha,
     * or with status {@code 400 (Bad Request)} if the nextProductAlpha is not valid,
     * or with status {@code 404 (Not Found)} if the nextProductAlpha is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextProductAlpha couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextProductAlpha> partialUpdateNextProductAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextProductAlpha nextProductAlpha
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextProductAlpha partially : {}, {}", id, nextProductAlpha);
        if (nextProductAlpha.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextProductAlpha.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextProductAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextProductAlpha> result = nextProductAlphaService.partialUpdate(nextProductAlpha);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextProductAlpha.getId().toString())
        );
    }

    /**
     * {@code GET  /next-product-alphas} : get all the nextProductAlphas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextProductAlphas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextProductAlpha>> getAllNextProductAlphas(
        NextProductAlphaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextProductAlphas by criteria: {}", criteria);

        Page<NextProductAlpha> page = nextProductAlphaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-product-alphas/count} : count all the nextProductAlphas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextProductAlphas(NextProductAlphaCriteria criteria) {
        LOG.debug("REST request to count NextProductAlphas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextProductAlphaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-product-alphas/:id} : get the "id" nextProductAlpha.
     *
     * @param id the id of the nextProductAlpha to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextProductAlpha, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextProductAlpha> getNextProductAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextProductAlpha : {}", id);
        Optional<NextProductAlpha> nextProductAlpha = nextProductAlphaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextProductAlpha);
    }

    /**
     * {@code DELETE  /next-product-alphas/:id} : delete the "id" nextProductAlpha.
     *
     * @param id the id of the nextProductAlpha to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextProductAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextProductAlpha : {}", id);
        nextProductAlphaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
