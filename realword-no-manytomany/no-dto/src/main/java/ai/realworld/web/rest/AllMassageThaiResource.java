package ai.realworld.web.rest;

import ai.realworld.domain.AllMassageThai;
import ai.realworld.repository.AllMassageThaiRepository;
import ai.realworld.service.AllMassageThaiQueryService;
import ai.realworld.service.AllMassageThaiService;
import ai.realworld.service.criteria.AllMassageThaiCriteria;
import ai.realworld.web.rest.errors.BadRequestAlertException;
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

/**
 * REST controller for managing {@link ai.realworld.domain.AllMassageThai}.
 */
@RestController
@RequestMapping("/api/all-massage-thais")
public class AllMassageThaiResource {

    private static final Logger LOG = LoggerFactory.getLogger(AllMassageThaiResource.class);

    private static final String ENTITY_NAME = "allMassageThai";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AllMassageThaiService allMassageThaiService;

    private final AllMassageThaiRepository allMassageThaiRepository;

    private final AllMassageThaiQueryService allMassageThaiQueryService;

    public AllMassageThaiResource(
        AllMassageThaiService allMassageThaiService,
        AllMassageThaiRepository allMassageThaiRepository,
        AllMassageThaiQueryService allMassageThaiQueryService
    ) {
        this.allMassageThaiService = allMassageThaiService;
        this.allMassageThaiRepository = allMassageThaiRepository;
        this.allMassageThaiQueryService = allMassageThaiQueryService;
    }

    /**
     * {@code POST  /all-massage-thais} : Create a new allMassageThai.
     *
     * @param allMassageThai the allMassageThai to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new allMassageThai, or with status {@code 400 (Bad Request)} if the allMassageThai has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AllMassageThai> createAllMassageThai(@Valid @RequestBody AllMassageThai allMassageThai)
        throws URISyntaxException {
        LOG.debug("REST request to save AllMassageThai : {}", allMassageThai);
        if (allMassageThai.getId() != null) {
            throw new BadRequestAlertException("A new allMassageThai cannot already have an ID", ENTITY_NAME, "idexists");
        }
        allMassageThai = allMassageThaiService.save(allMassageThai);
        return ResponseEntity.created(new URI("/api/all-massage-thais/" + allMassageThai.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, allMassageThai.getId().toString()))
            .body(allMassageThai);
    }

    /**
     * {@code PUT  /all-massage-thais/:id} : Updates an existing allMassageThai.
     *
     * @param id the id of the allMassageThai to save.
     * @param allMassageThai the allMassageThai to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated allMassageThai,
     * or with status {@code 400 (Bad Request)} if the allMassageThai is not valid,
     * or with status {@code 500 (Internal Server Error)} if the allMassageThai couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AllMassageThai> updateAllMassageThai(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AllMassageThai allMassageThai
    ) throws URISyntaxException {
        LOG.debug("REST request to update AllMassageThai : {}, {}", id, allMassageThai);
        if (allMassageThai.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, allMassageThai.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!allMassageThaiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        allMassageThai = allMassageThaiService.update(allMassageThai);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, allMassageThai.getId().toString()))
            .body(allMassageThai);
    }

    /**
     * {@code PATCH  /all-massage-thais/:id} : Partial updates given fields of an existing allMassageThai, field will ignore if it is null
     *
     * @param id the id of the allMassageThai to save.
     * @param allMassageThai the allMassageThai to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated allMassageThai,
     * or with status {@code 400 (Bad Request)} if the allMassageThai is not valid,
     * or with status {@code 404 (Not Found)} if the allMassageThai is not found,
     * or with status {@code 500 (Internal Server Error)} if the allMassageThai couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AllMassageThai> partialUpdateAllMassageThai(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AllMassageThai allMassageThai
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AllMassageThai partially : {}, {}", id, allMassageThai);
        if (allMassageThai.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, allMassageThai.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!allMassageThaiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AllMassageThai> result = allMassageThaiService.partialUpdate(allMassageThai);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, allMassageThai.getId().toString())
        );
    }

    /**
     * {@code GET  /all-massage-thais} : get all the allMassageThais.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of allMassageThais in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AllMassageThai>> getAllAllMassageThais(
        AllMassageThaiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AllMassageThais by criteria: {}", criteria);

        Page<AllMassageThai> page = allMassageThaiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /all-massage-thais/count} : count all the allMassageThais.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAllMassageThais(AllMassageThaiCriteria criteria) {
        LOG.debug("REST request to count AllMassageThais by criteria: {}", criteria);
        return ResponseEntity.ok().body(allMassageThaiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /all-massage-thais/:id} : get the "id" allMassageThai.
     *
     * @param id the id of the allMassageThai to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the allMassageThai, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AllMassageThai> getAllMassageThai(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AllMassageThai : {}", id);
        Optional<AllMassageThai> allMassageThai = allMassageThaiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(allMassageThai);
    }

    /**
     * {@code DELETE  /all-massage-thais/:id} : delete the "id" allMassageThai.
     *
     * @param id the id of the allMassageThai to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAllMassageThai(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AllMassageThai : {}", id);
        allMassageThaiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
