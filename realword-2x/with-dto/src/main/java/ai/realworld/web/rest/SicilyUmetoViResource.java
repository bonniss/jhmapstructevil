package ai.realworld.web.rest;

import ai.realworld.repository.SicilyUmetoViRepository;
import ai.realworld.service.SicilyUmetoViQueryService;
import ai.realworld.service.SicilyUmetoViService;
import ai.realworld.service.criteria.SicilyUmetoViCriteria;
import ai.realworld.service.dto.SicilyUmetoViDTO;
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
 * REST controller for managing {@link ai.realworld.domain.SicilyUmetoVi}.
 */
@RestController
@RequestMapping("/api/sicily-umeto-vis")
public class SicilyUmetoViResource {

    private static final Logger LOG = LoggerFactory.getLogger(SicilyUmetoViResource.class);

    private static final String ENTITY_NAME = "sicilyUmetoVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SicilyUmetoViService sicilyUmetoViService;

    private final SicilyUmetoViRepository sicilyUmetoViRepository;

    private final SicilyUmetoViQueryService sicilyUmetoViQueryService;

    public SicilyUmetoViResource(
        SicilyUmetoViService sicilyUmetoViService,
        SicilyUmetoViRepository sicilyUmetoViRepository,
        SicilyUmetoViQueryService sicilyUmetoViQueryService
    ) {
        this.sicilyUmetoViService = sicilyUmetoViService;
        this.sicilyUmetoViRepository = sicilyUmetoViRepository;
        this.sicilyUmetoViQueryService = sicilyUmetoViQueryService;
    }

    /**
     * {@code POST  /sicily-umeto-vis} : Create a new sicilyUmetoVi.
     *
     * @param sicilyUmetoViDTO the sicilyUmetoViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sicilyUmetoViDTO, or with status {@code 400 (Bad Request)} if the sicilyUmetoVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SicilyUmetoViDTO> createSicilyUmetoVi(@Valid @RequestBody SicilyUmetoViDTO sicilyUmetoViDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save SicilyUmetoVi : {}", sicilyUmetoViDTO);
        if (sicilyUmetoViDTO.getId() != null) {
            throw new BadRequestAlertException("A new sicilyUmetoVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        sicilyUmetoViDTO = sicilyUmetoViService.save(sicilyUmetoViDTO);
        return ResponseEntity.created(new URI("/api/sicily-umeto-vis/" + sicilyUmetoViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, sicilyUmetoViDTO.getId().toString()))
            .body(sicilyUmetoViDTO);
    }

    /**
     * {@code PUT  /sicily-umeto-vis/:id} : Updates an existing sicilyUmetoVi.
     *
     * @param id the id of the sicilyUmetoViDTO to save.
     * @param sicilyUmetoViDTO the sicilyUmetoViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sicilyUmetoViDTO,
     * or with status {@code 400 (Bad Request)} if the sicilyUmetoViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sicilyUmetoViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SicilyUmetoViDTO> updateSicilyUmetoVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SicilyUmetoViDTO sicilyUmetoViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SicilyUmetoVi : {}, {}", id, sicilyUmetoViDTO);
        if (sicilyUmetoViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sicilyUmetoViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sicilyUmetoViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        sicilyUmetoViDTO = sicilyUmetoViService.update(sicilyUmetoViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sicilyUmetoViDTO.getId().toString()))
            .body(sicilyUmetoViDTO);
    }

    /**
     * {@code PATCH  /sicily-umeto-vis/:id} : Partial updates given fields of an existing sicilyUmetoVi, field will ignore if it is null
     *
     * @param id the id of the sicilyUmetoViDTO to save.
     * @param sicilyUmetoViDTO the sicilyUmetoViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sicilyUmetoViDTO,
     * or with status {@code 400 (Bad Request)} if the sicilyUmetoViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sicilyUmetoViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sicilyUmetoViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SicilyUmetoViDTO> partialUpdateSicilyUmetoVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SicilyUmetoViDTO sicilyUmetoViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SicilyUmetoVi partially : {}, {}", id, sicilyUmetoViDTO);
        if (sicilyUmetoViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sicilyUmetoViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sicilyUmetoViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SicilyUmetoViDTO> result = sicilyUmetoViService.partialUpdate(sicilyUmetoViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sicilyUmetoViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sicily-umeto-vis} : get all the sicilyUmetoVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sicilyUmetoVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SicilyUmetoViDTO>> getAllSicilyUmetoVis(
        SicilyUmetoViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SicilyUmetoVis by criteria: {}", criteria);

        Page<SicilyUmetoViDTO> page = sicilyUmetoViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sicily-umeto-vis/count} : count all the sicilyUmetoVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSicilyUmetoVis(SicilyUmetoViCriteria criteria) {
        LOG.debug("REST request to count SicilyUmetoVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(sicilyUmetoViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sicily-umeto-vis/:id} : get the "id" sicilyUmetoVi.
     *
     * @param id the id of the sicilyUmetoViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sicilyUmetoViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SicilyUmetoViDTO> getSicilyUmetoVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SicilyUmetoVi : {}", id);
        Optional<SicilyUmetoViDTO> sicilyUmetoViDTO = sicilyUmetoViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sicilyUmetoViDTO);
    }

    /**
     * {@code DELETE  /sicily-umeto-vis/:id} : delete the "id" sicilyUmetoVi.
     *
     * @param id the id of the sicilyUmetoViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSicilyUmetoVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SicilyUmetoVi : {}", id);
        sicilyUmetoViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
