package ai.realworld.web.rest;

import ai.realworld.repository.SaisanCogViRepository;
import ai.realworld.service.SaisanCogViQueryService;
import ai.realworld.service.SaisanCogViService;
import ai.realworld.service.criteria.SaisanCogViCriteria;
import ai.realworld.service.dto.SaisanCogViDTO;
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
 * REST controller for managing {@link ai.realworld.domain.SaisanCogVi}.
 */
@RestController
@RequestMapping("/api/saisan-cog-vis")
public class SaisanCogViResource {

    private static final Logger LOG = LoggerFactory.getLogger(SaisanCogViResource.class);

    private static final String ENTITY_NAME = "saisanCogVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SaisanCogViService saisanCogViService;

    private final SaisanCogViRepository saisanCogViRepository;

    private final SaisanCogViQueryService saisanCogViQueryService;

    public SaisanCogViResource(
        SaisanCogViService saisanCogViService,
        SaisanCogViRepository saisanCogViRepository,
        SaisanCogViQueryService saisanCogViQueryService
    ) {
        this.saisanCogViService = saisanCogViService;
        this.saisanCogViRepository = saisanCogViRepository;
        this.saisanCogViQueryService = saisanCogViQueryService;
    }

    /**
     * {@code POST  /saisan-cog-vis} : Create a new saisanCogVi.
     *
     * @param saisanCogViDTO the saisanCogViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new saisanCogViDTO, or with status {@code 400 (Bad Request)} if the saisanCogVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SaisanCogViDTO> createSaisanCogVi(@Valid @RequestBody SaisanCogViDTO saisanCogViDTO) throws URISyntaxException {
        LOG.debug("REST request to save SaisanCogVi : {}", saisanCogViDTO);
        if (saisanCogViDTO.getId() != null) {
            throw new BadRequestAlertException("A new saisanCogVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        saisanCogViDTO = saisanCogViService.save(saisanCogViDTO);
        return ResponseEntity.created(new URI("/api/saisan-cog-vis/" + saisanCogViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, saisanCogViDTO.getId().toString()))
            .body(saisanCogViDTO);
    }

    /**
     * {@code PUT  /saisan-cog-vis/:id} : Updates an existing saisanCogVi.
     *
     * @param id the id of the saisanCogViDTO to save.
     * @param saisanCogViDTO the saisanCogViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated saisanCogViDTO,
     * or with status {@code 400 (Bad Request)} if the saisanCogViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the saisanCogViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SaisanCogViDTO> updateSaisanCogVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SaisanCogViDTO saisanCogViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SaisanCogVi : {}, {}", id, saisanCogViDTO);
        if (saisanCogViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, saisanCogViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!saisanCogViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        saisanCogViDTO = saisanCogViService.update(saisanCogViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, saisanCogViDTO.getId().toString()))
            .body(saisanCogViDTO);
    }

    /**
     * {@code PATCH  /saisan-cog-vis/:id} : Partial updates given fields of an existing saisanCogVi, field will ignore if it is null
     *
     * @param id the id of the saisanCogViDTO to save.
     * @param saisanCogViDTO the saisanCogViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated saisanCogViDTO,
     * or with status {@code 400 (Bad Request)} if the saisanCogViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the saisanCogViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the saisanCogViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SaisanCogViDTO> partialUpdateSaisanCogVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SaisanCogViDTO saisanCogViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SaisanCogVi partially : {}, {}", id, saisanCogViDTO);
        if (saisanCogViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, saisanCogViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!saisanCogViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SaisanCogViDTO> result = saisanCogViService.partialUpdate(saisanCogViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, saisanCogViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /saisan-cog-vis} : get all the saisanCogVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of saisanCogVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SaisanCogViDTO>> getAllSaisanCogVis(
        SaisanCogViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SaisanCogVis by criteria: {}", criteria);

        Page<SaisanCogViDTO> page = saisanCogViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /saisan-cog-vis/count} : count all the saisanCogVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSaisanCogVis(SaisanCogViCriteria criteria) {
        LOG.debug("REST request to count SaisanCogVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(saisanCogViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /saisan-cog-vis/:id} : get the "id" saisanCogVi.
     *
     * @param id the id of the saisanCogViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the saisanCogViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SaisanCogViDTO> getSaisanCogVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SaisanCogVi : {}", id);
        Optional<SaisanCogViDTO> saisanCogViDTO = saisanCogViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(saisanCogViDTO);
    }

    /**
     * {@code DELETE  /saisan-cog-vis/:id} : delete the "id" saisanCogVi.
     *
     * @param id the id of the saisanCogViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSaisanCogVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SaisanCogVi : {}", id);
        saisanCogViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
