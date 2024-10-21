package ai.realworld.web.rest;

import ai.realworld.domain.HexCharVi;
import ai.realworld.repository.HexCharViRepository;
import ai.realworld.service.HexCharViQueryService;
import ai.realworld.service.HexCharViService;
import ai.realworld.service.criteria.HexCharViCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.HexCharVi}.
 */
@RestController
@RequestMapping("/api/hex-char-vis")
public class HexCharViResource {

    private static final Logger LOG = LoggerFactory.getLogger(HexCharViResource.class);

    private static final String ENTITY_NAME = "hexCharVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HexCharViService hexCharViService;

    private final HexCharViRepository hexCharViRepository;

    private final HexCharViQueryService hexCharViQueryService;

    public HexCharViResource(
        HexCharViService hexCharViService,
        HexCharViRepository hexCharViRepository,
        HexCharViQueryService hexCharViQueryService
    ) {
        this.hexCharViService = hexCharViService;
        this.hexCharViRepository = hexCharViRepository;
        this.hexCharViQueryService = hexCharViQueryService;
    }

    /**
     * {@code POST  /hex-char-vis} : Create a new hexCharVi.
     *
     * @param hexCharVi the hexCharVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hexCharVi, or with status {@code 400 (Bad Request)} if the hexCharVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HexCharVi> createHexCharVi(@Valid @RequestBody HexCharVi hexCharVi) throws URISyntaxException {
        LOG.debug("REST request to save HexCharVi : {}", hexCharVi);
        if (hexCharVi.getId() != null) {
            throw new BadRequestAlertException("A new hexCharVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        hexCharVi = hexCharViService.save(hexCharVi);
        return ResponseEntity.created(new URI("/api/hex-char-vis/" + hexCharVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, hexCharVi.getId().toString()))
            .body(hexCharVi);
    }

    /**
     * {@code PUT  /hex-char-vis/:id} : Updates an existing hexCharVi.
     *
     * @param id the id of the hexCharVi to save.
     * @param hexCharVi the hexCharVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hexCharVi,
     * or with status {@code 400 (Bad Request)} if the hexCharVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hexCharVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HexCharVi> updateHexCharVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HexCharVi hexCharVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update HexCharVi : {}, {}", id, hexCharVi);
        if (hexCharVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hexCharVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hexCharViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        hexCharVi = hexCharViService.update(hexCharVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hexCharVi.getId().toString()))
            .body(hexCharVi);
    }

    /**
     * {@code PATCH  /hex-char-vis/:id} : Partial updates given fields of an existing hexCharVi, field will ignore if it is null
     *
     * @param id the id of the hexCharVi to save.
     * @param hexCharVi the hexCharVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hexCharVi,
     * or with status {@code 400 (Bad Request)} if the hexCharVi is not valid,
     * or with status {@code 404 (Not Found)} if the hexCharVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the hexCharVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HexCharVi> partialUpdateHexCharVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HexCharVi hexCharVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update HexCharVi partially : {}, {}", id, hexCharVi);
        if (hexCharVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hexCharVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hexCharViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HexCharVi> result = hexCharViService.partialUpdate(hexCharVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hexCharVi.getId().toString())
        );
    }

    /**
     * {@code GET  /hex-char-vis} : get all the hexCharVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hexCharVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HexCharVi>> getAllHexCharVis(
        HexCharViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get HexCharVis by criteria: {}", criteria);

        Page<HexCharVi> page = hexCharViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hex-char-vis/count} : count all the hexCharVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countHexCharVis(HexCharViCriteria criteria) {
        LOG.debug("REST request to count HexCharVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(hexCharViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /hex-char-vis/:id} : get the "id" hexCharVi.
     *
     * @param id the id of the hexCharVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hexCharVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HexCharVi> getHexCharVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get HexCharVi : {}", id);
        Optional<HexCharVi> hexCharVi = hexCharViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hexCharVi);
    }

    /**
     * {@code DELETE  /hex-char-vis/:id} : delete the "id" hexCharVi.
     *
     * @param id the id of the hexCharVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHexCharVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete HexCharVi : {}", id);
        hexCharViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
