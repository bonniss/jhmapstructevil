package ai.realworld.web.rest;

import ai.realworld.repository.PamelaLouisRepository;
import ai.realworld.service.PamelaLouisQueryService;
import ai.realworld.service.PamelaLouisService;
import ai.realworld.service.criteria.PamelaLouisCriteria;
import ai.realworld.service.dto.PamelaLouisDTO;
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
 * REST controller for managing {@link ai.realworld.domain.PamelaLouis}.
 */
@RestController
@RequestMapping("/api/pamela-louis")
public class PamelaLouisResource {

    private static final Logger LOG = LoggerFactory.getLogger(PamelaLouisResource.class);

    private static final String ENTITY_NAME = "pamelaLouis";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PamelaLouisService pamelaLouisService;

    private final PamelaLouisRepository pamelaLouisRepository;

    private final PamelaLouisQueryService pamelaLouisQueryService;

    public PamelaLouisResource(
        PamelaLouisService pamelaLouisService,
        PamelaLouisRepository pamelaLouisRepository,
        PamelaLouisQueryService pamelaLouisQueryService
    ) {
        this.pamelaLouisService = pamelaLouisService;
        this.pamelaLouisRepository = pamelaLouisRepository;
        this.pamelaLouisQueryService = pamelaLouisQueryService;
    }

    /**
     * {@code POST  /pamela-louis} : Create a new pamelaLouis.
     *
     * @param pamelaLouisDTO the pamelaLouisDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pamelaLouisDTO, or with status {@code 400 (Bad Request)} if the pamelaLouis has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PamelaLouisDTO> createPamelaLouis(@Valid @RequestBody PamelaLouisDTO pamelaLouisDTO) throws URISyntaxException {
        LOG.debug("REST request to save PamelaLouis : {}", pamelaLouisDTO);
        if (pamelaLouisDTO.getId() != null) {
            throw new BadRequestAlertException("A new pamelaLouis cannot already have an ID", ENTITY_NAME, "idexists");
        }
        pamelaLouisDTO = pamelaLouisService.save(pamelaLouisDTO);
        return ResponseEntity.created(new URI("/api/pamela-louis/" + pamelaLouisDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, pamelaLouisDTO.getId().toString()))
            .body(pamelaLouisDTO);
    }

    /**
     * {@code PUT  /pamela-louis/:id} : Updates an existing pamelaLouis.
     *
     * @param id the id of the pamelaLouisDTO to save.
     * @param pamelaLouisDTO the pamelaLouisDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pamelaLouisDTO,
     * or with status {@code 400 (Bad Request)} if the pamelaLouisDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pamelaLouisDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PamelaLouisDTO> updatePamelaLouis(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PamelaLouisDTO pamelaLouisDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PamelaLouis : {}, {}", id, pamelaLouisDTO);
        if (pamelaLouisDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pamelaLouisDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pamelaLouisRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        pamelaLouisDTO = pamelaLouisService.update(pamelaLouisDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pamelaLouisDTO.getId().toString()))
            .body(pamelaLouisDTO);
    }

    /**
     * {@code PATCH  /pamela-louis/:id} : Partial updates given fields of an existing pamelaLouis, field will ignore if it is null
     *
     * @param id the id of the pamelaLouisDTO to save.
     * @param pamelaLouisDTO the pamelaLouisDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pamelaLouisDTO,
     * or with status {@code 400 (Bad Request)} if the pamelaLouisDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pamelaLouisDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pamelaLouisDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PamelaLouisDTO> partialUpdatePamelaLouis(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PamelaLouisDTO pamelaLouisDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PamelaLouis partially : {}, {}", id, pamelaLouisDTO);
        if (pamelaLouisDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pamelaLouisDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pamelaLouisRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PamelaLouisDTO> result = pamelaLouisService.partialUpdate(pamelaLouisDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pamelaLouisDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pamela-louis} : get all the pamelaLouis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pamelaLouis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PamelaLouisDTO>> getAllPamelaLouis(
        PamelaLouisCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PamelaLouis by criteria: {}", criteria);

        Page<PamelaLouisDTO> page = pamelaLouisQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pamela-louis/count} : count all the pamelaLouis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPamelaLouis(PamelaLouisCriteria criteria) {
        LOG.debug("REST request to count PamelaLouis by criteria: {}", criteria);
        return ResponseEntity.ok().body(pamelaLouisQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /pamela-louis/:id} : get the "id" pamelaLouis.
     *
     * @param id the id of the pamelaLouisDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pamelaLouisDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PamelaLouisDTO> getPamelaLouis(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PamelaLouis : {}", id);
        Optional<PamelaLouisDTO> pamelaLouisDTO = pamelaLouisService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pamelaLouisDTO);
    }

    /**
     * {@code DELETE  /pamela-louis/:id} : delete the "id" pamelaLouis.
     *
     * @param id the id of the pamelaLouisDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePamelaLouis(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PamelaLouis : {}", id);
        pamelaLouisService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
