package ai.realworld.web.rest;

import ai.realworld.repository.PamelaLouisViRepository;
import ai.realworld.service.PamelaLouisViQueryService;
import ai.realworld.service.PamelaLouisViService;
import ai.realworld.service.criteria.PamelaLouisViCriteria;
import ai.realworld.service.dto.PamelaLouisViDTO;
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
 * REST controller for managing {@link ai.realworld.domain.PamelaLouisVi}.
 */
@RestController
@RequestMapping("/api/pamela-louis-vis")
public class PamelaLouisViResource {

    private static final Logger LOG = LoggerFactory.getLogger(PamelaLouisViResource.class);

    private static final String ENTITY_NAME = "pamelaLouisVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PamelaLouisViService pamelaLouisViService;

    private final PamelaLouisViRepository pamelaLouisViRepository;

    private final PamelaLouisViQueryService pamelaLouisViQueryService;

    public PamelaLouisViResource(
        PamelaLouisViService pamelaLouisViService,
        PamelaLouisViRepository pamelaLouisViRepository,
        PamelaLouisViQueryService pamelaLouisViQueryService
    ) {
        this.pamelaLouisViService = pamelaLouisViService;
        this.pamelaLouisViRepository = pamelaLouisViRepository;
        this.pamelaLouisViQueryService = pamelaLouisViQueryService;
    }

    /**
     * {@code POST  /pamela-louis-vis} : Create a new pamelaLouisVi.
     *
     * @param pamelaLouisViDTO the pamelaLouisViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pamelaLouisViDTO, or with status {@code 400 (Bad Request)} if the pamelaLouisVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PamelaLouisViDTO> createPamelaLouisVi(@Valid @RequestBody PamelaLouisViDTO pamelaLouisViDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save PamelaLouisVi : {}", pamelaLouisViDTO);
        if (pamelaLouisViDTO.getId() != null) {
            throw new BadRequestAlertException("A new pamelaLouisVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        pamelaLouisViDTO = pamelaLouisViService.save(pamelaLouisViDTO);
        return ResponseEntity.created(new URI("/api/pamela-louis-vis/" + pamelaLouisViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, pamelaLouisViDTO.getId().toString()))
            .body(pamelaLouisViDTO);
    }

    /**
     * {@code PUT  /pamela-louis-vis/:id} : Updates an existing pamelaLouisVi.
     *
     * @param id the id of the pamelaLouisViDTO to save.
     * @param pamelaLouisViDTO the pamelaLouisViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pamelaLouisViDTO,
     * or with status {@code 400 (Bad Request)} if the pamelaLouisViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pamelaLouisViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PamelaLouisViDTO> updatePamelaLouisVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PamelaLouisViDTO pamelaLouisViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PamelaLouisVi : {}, {}", id, pamelaLouisViDTO);
        if (pamelaLouisViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pamelaLouisViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pamelaLouisViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        pamelaLouisViDTO = pamelaLouisViService.update(pamelaLouisViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pamelaLouisViDTO.getId().toString()))
            .body(pamelaLouisViDTO);
    }

    /**
     * {@code PATCH  /pamela-louis-vis/:id} : Partial updates given fields of an existing pamelaLouisVi, field will ignore if it is null
     *
     * @param id the id of the pamelaLouisViDTO to save.
     * @param pamelaLouisViDTO the pamelaLouisViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pamelaLouisViDTO,
     * or with status {@code 400 (Bad Request)} if the pamelaLouisViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pamelaLouisViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pamelaLouisViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PamelaLouisViDTO> partialUpdatePamelaLouisVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PamelaLouisViDTO pamelaLouisViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PamelaLouisVi partially : {}, {}", id, pamelaLouisViDTO);
        if (pamelaLouisViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pamelaLouisViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pamelaLouisViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PamelaLouisViDTO> result = pamelaLouisViService.partialUpdate(pamelaLouisViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pamelaLouisViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pamela-louis-vis} : get all the pamelaLouisVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pamelaLouisVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PamelaLouisViDTO>> getAllPamelaLouisVis(
        PamelaLouisViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PamelaLouisVis by criteria: {}", criteria);

        Page<PamelaLouisViDTO> page = pamelaLouisViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pamela-louis-vis/count} : count all the pamelaLouisVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPamelaLouisVis(PamelaLouisViCriteria criteria) {
        LOG.debug("REST request to count PamelaLouisVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(pamelaLouisViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /pamela-louis-vis/:id} : get the "id" pamelaLouisVi.
     *
     * @param id the id of the pamelaLouisViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pamelaLouisViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PamelaLouisViDTO> getPamelaLouisVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PamelaLouisVi : {}", id);
        Optional<PamelaLouisViDTO> pamelaLouisViDTO = pamelaLouisViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pamelaLouisViDTO);
    }

    /**
     * {@code DELETE  /pamela-louis-vis/:id} : delete the "id" pamelaLouisVi.
     *
     * @param id the id of the pamelaLouisViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePamelaLouisVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PamelaLouisVi : {}", id);
        pamelaLouisViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
