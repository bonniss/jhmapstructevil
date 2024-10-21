package ai.realworld.web.rest;

import ai.realworld.repository.MagharettiViRepository;
import ai.realworld.service.MagharettiViQueryService;
import ai.realworld.service.MagharettiViService;
import ai.realworld.service.criteria.MagharettiViCriteria;
import ai.realworld.service.dto.MagharettiViDTO;
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
 * REST controller for managing {@link ai.realworld.domain.MagharettiVi}.
 */
@RestController
@RequestMapping("/api/magharetti-vis")
public class MagharettiViResource {

    private static final Logger LOG = LoggerFactory.getLogger(MagharettiViResource.class);

    private static final String ENTITY_NAME = "magharettiVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MagharettiViService magharettiViService;

    private final MagharettiViRepository magharettiViRepository;

    private final MagharettiViQueryService magharettiViQueryService;

    public MagharettiViResource(
        MagharettiViService magharettiViService,
        MagharettiViRepository magharettiViRepository,
        MagharettiViQueryService magharettiViQueryService
    ) {
        this.magharettiViService = magharettiViService;
        this.magharettiViRepository = magharettiViRepository;
        this.magharettiViQueryService = magharettiViQueryService;
    }

    /**
     * {@code POST  /magharetti-vis} : Create a new magharettiVi.
     *
     * @param magharettiViDTO the magharettiViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new magharettiViDTO, or with status {@code 400 (Bad Request)} if the magharettiVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MagharettiViDTO> createMagharettiVi(@Valid @RequestBody MagharettiViDTO magharettiViDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save MagharettiVi : {}", magharettiViDTO);
        if (magharettiViDTO.getId() != null) {
            throw new BadRequestAlertException("A new magharettiVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        magharettiViDTO = magharettiViService.save(magharettiViDTO);
        return ResponseEntity.created(new URI("/api/magharetti-vis/" + magharettiViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, magharettiViDTO.getId().toString()))
            .body(magharettiViDTO);
    }

    /**
     * {@code PUT  /magharetti-vis/:id} : Updates an existing magharettiVi.
     *
     * @param id the id of the magharettiViDTO to save.
     * @param magharettiViDTO the magharettiViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated magharettiViDTO,
     * or with status {@code 400 (Bad Request)} if the magharettiViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the magharettiViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MagharettiViDTO> updateMagharettiVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MagharettiViDTO magharettiViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MagharettiVi : {}, {}", id, magharettiViDTO);
        if (magharettiViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, magharettiViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!magharettiViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        magharettiViDTO = magharettiViService.update(magharettiViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, magharettiViDTO.getId().toString()))
            .body(magharettiViDTO);
    }

    /**
     * {@code PATCH  /magharetti-vis/:id} : Partial updates given fields of an existing magharettiVi, field will ignore if it is null
     *
     * @param id the id of the magharettiViDTO to save.
     * @param magharettiViDTO the magharettiViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated magharettiViDTO,
     * or with status {@code 400 (Bad Request)} if the magharettiViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the magharettiViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the magharettiViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MagharettiViDTO> partialUpdateMagharettiVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MagharettiViDTO magharettiViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MagharettiVi partially : {}, {}", id, magharettiViDTO);
        if (magharettiViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, magharettiViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!magharettiViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MagharettiViDTO> result = magharettiViService.partialUpdate(magharettiViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, magharettiViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /magharetti-vis} : get all the magharettiVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of magharettiVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MagharettiViDTO>> getAllMagharettiVis(
        MagharettiViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get MagharettiVis by criteria: {}", criteria);

        Page<MagharettiViDTO> page = magharettiViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /magharetti-vis/count} : count all the magharettiVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMagharettiVis(MagharettiViCriteria criteria) {
        LOG.debug("REST request to count MagharettiVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(magharettiViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /magharetti-vis/:id} : get the "id" magharettiVi.
     *
     * @param id the id of the magharettiViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the magharettiViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MagharettiViDTO> getMagharettiVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MagharettiVi : {}", id);
        Optional<MagharettiViDTO> magharettiViDTO = magharettiViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(magharettiViDTO);
    }

    /**
     * {@code DELETE  /magharetti-vis/:id} : delete the "id" magharettiVi.
     *
     * @param id the id of the magharettiViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMagharettiVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MagharettiVi : {}", id);
        magharettiViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
