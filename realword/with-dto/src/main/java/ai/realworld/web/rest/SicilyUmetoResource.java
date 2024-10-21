package ai.realworld.web.rest;

import ai.realworld.repository.SicilyUmetoRepository;
import ai.realworld.service.SicilyUmetoQueryService;
import ai.realworld.service.SicilyUmetoService;
import ai.realworld.service.criteria.SicilyUmetoCriteria;
import ai.realworld.service.dto.SicilyUmetoDTO;
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
 * REST controller for managing {@link ai.realworld.domain.SicilyUmeto}.
 */
@RestController
@RequestMapping("/api/sicily-umetos")
public class SicilyUmetoResource {

    private static final Logger LOG = LoggerFactory.getLogger(SicilyUmetoResource.class);

    private static final String ENTITY_NAME = "sicilyUmeto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SicilyUmetoService sicilyUmetoService;

    private final SicilyUmetoRepository sicilyUmetoRepository;

    private final SicilyUmetoQueryService sicilyUmetoQueryService;

    public SicilyUmetoResource(
        SicilyUmetoService sicilyUmetoService,
        SicilyUmetoRepository sicilyUmetoRepository,
        SicilyUmetoQueryService sicilyUmetoQueryService
    ) {
        this.sicilyUmetoService = sicilyUmetoService;
        this.sicilyUmetoRepository = sicilyUmetoRepository;
        this.sicilyUmetoQueryService = sicilyUmetoQueryService;
    }

    /**
     * {@code POST  /sicily-umetos} : Create a new sicilyUmeto.
     *
     * @param sicilyUmetoDTO the sicilyUmetoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sicilyUmetoDTO, or with status {@code 400 (Bad Request)} if the sicilyUmeto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SicilyUmetoDTO> createSicilyUmeto(@Valid @RequestBody SicilyUmetoDTO sicilyUmetoDTO) throws URISyntaxException {
        LOG.debug("REST request to save SicilyUmeto : {}", sicilyUmetoDTO);
        if (sicilyUmetoDTO.getId() != null) {
            throw new BadRequestAlertException("A new sicilyUmeto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        sicilyUmetoDTO = sicilyUmetoService.save(sicilyUmetoDTO);
        return ResponseEntity.created(new URI("/api/sicily-umetos/" + sicilyUmetoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, sicilyUmetoDTO.getId().toString()))
            .body(sicilyUmetoDTO);
    }

    /**
     * {@code PUT  /sicily-umetos/:id} : Updates an existing sicilyUmeto.
     *
     * @param id the id of the sicilyUmetoDTO to save.
     * @param sicilyUmetoDTO the sicilyUmetoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sicilyUmetoDTO,
     * or with status {@code 400 (Bad Request)} if the sicilyUmetoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sicilyUmetoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SicilyUmetoDTO> updateSicilyUmeto(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SicilyUmetoDTO sicilyUmetoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SicilyUmeto : {}, {}", id, sicilyUmetoDTO);
        if (sicilyUmetoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sicilyUmetoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sicilyUmetoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        sicilyUmetoDTO = sicilyUmetoService.update(sicilyUmetoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sicilyUmetoDTO.getId().toString()))
            .body(sicilyUmetoDTO);
    }

    /**
     * {@code PATCH  /sicily-umetos/:id} : Partial updates given fields of an existing sicilyUmeto, field will ignore if it is null
     *
     * @param id the id of the sicilyUmetoDTO to save.
     * @param sicilyUmetoDTO the sicilyUmetoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sicilyUmetoDTO,
     * or with status {@code 400 (Bad Request)} if the sicilyUmetoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sicilyUmetoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sicilyUmetoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SicilyUmetoDTO> partialUpdateSicilyUmeto(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SicilyUmetoDTO sicilyUmetoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SicilyUmeto partially : {}, {}", id, sicilyUmetoDTO);
        if (sicilyUmetoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sicilyUmetoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sicilyUmetoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SicilyUmetoDTO> result = sicilyUmetoService.partialUpdate(sicilyUmetoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sicilyUmetoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sicily-umetos} : get all the sicilyUmetos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sicilyUmetos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SicilyUmetoDTO>> getAllSicilyUmetos(
        SicilyUmetoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SicilyUmetos by criteria: {}", criteria);

        Page<SicilyUmetoDTO> page = sicilyUmetoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sicily-umetos/count} : count all the sicilyUmetos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSicilyUmetos(SicilyUmetoCriteria criteria) {
        LOG.debug("REST request to count SicilyUmetos by criteria: {}", criteria);
        return ResponseEntity.ok().body(sicilyUmetoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sicily-umetos/:id} : get the "id" sicilyUmeto.
     *
     * @param id the id of the sicilyUmetoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sicilyUmetoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SicilyUmetoDTO> getSicilyUmeto(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SicilyUmeto : {}", id);
        Optional<SicilyUmetoDTO> sicilyUmetoDTO = sicilyUmetoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sicilyUmetoDTO);
    }

    /**
     * {@code DELETE  /sicily-umetos/:id} : delete the "id" sicilyUmeto.
     *
     * @param id the id of the sicilyUmetoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSicilyUmeto(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SicilyUmeto : {}", id);
        sicilyUmetoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
