package ai.realworld.web.rest;

import ai.realworld.repository.AlPowerShellViRepository;
import ai.realworld.service.AlPowerShellViQueryService;
import ai.realworld.service.AlPowerShellViService;
import ai.realworld.service.criteria.AlPowerShellViCriteria;
import ai.realworld.service.dto.AlPowerShellViDTO;
import ai.realworld.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link ai.realworld.domain.AlPowerShellVi}.
 */
@RestController
@RequestMapping("/api/al-power-shell-vis")
public class AlPowerShellViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlPowerShellViResource.class);

    private static final String ENTITY_NAME = "alPowerShellVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlPowerShellViService alPowerShellViService;

    private final AlPowerShellViRepository alPowerShellViRepository;

    private final AlPowerShellViQueryService alPowerShellViQueryService;

    public AlPowerShellViResource(
        AlPowerShellViService alPowerShellViService,
        AlPowerShellViRepository alPowerShellViRepository,
        AlPowerShellViQueryService alPowerShellViQueryService
    ) {
        this.alPowerShellViService = alPowerShellViService;
        this.alPowerShellViRepository = alPowerShellViRepository;
        this.alPowerShellViQueryService = alPowerShellViQueryService;
    }

    /**
     * {@code POST  /al-power-shell-vis} : Create a new alPowerShellVi.
     *
     * @param alPowerShellViDTO the alPowerShellViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alPowerShellViDTO, or with status {@code 400 (Bad Request)} if the alPowerShellVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlPowerShellViDTO> createAlPowerShellVi(@RequestBody AlPowerShellViDTO alPowerShellViDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AlPowerShellVi : {}", alPowerShellViDTO);
        if (alPowerShellViDTO.getId() != null) {
            throw new BadRequestAlertException("A new alPowerShellVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alPowerShellViDTO = alPowerShellViService.save(alPowerShellViDTO);
        return ResponseEntity.created(new URI("/api/al-power-shell-vis/" + alPowerShellViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alPowerShellViDTO.getId().toString()))
            .body(alPowerShellViDTO);
    }

    /**
     * {@code PUT  /al-power-shell-vis/:id} : Updates an existing alPowerShellVi.
     *
     * @param id the id of the alPowerShellViDTO to save.
     * @param alPowerShellViDTO the alPowerShellViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPowerShellViDTO,
     * or with status {@code 400 (Bad Request)} if the alPowerShellViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alPowerShellViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlPowerShellViDTO> updateAlPowerShellVi(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AlPowerShellViDTO alPowerShellViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlPowerShellVi : {}, {}", id, alPowerShellViDTO);
        if (alPowerShellViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPowerShellViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPowerShellViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alPowerShellViDTO = alPowerShellViService.update(alPowerShellViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPowerShellViDTO.getId().toString()))
            .body(alPowerShellViDTO);
    }

    /**
     * {@code PATCH  /al-power-shell-vis/:id} : Partial updates given fields of an existing alPowerShellVi, field will ignore if it is null
     *
     * @param id the id of the alPowerShellViDTO to save.
     * @param alPowerShellViDTO the alPowerShellViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPowerShellViDTO,
     * or with status {@code 400 (Bad Request)} if the alPowerShellViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alPowerShellViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alPowerShellViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlPowerShellViDTO> partialUpdateAlPowerShellVi(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AlPowerShellViDTO alPowerShellViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlPowerShellVi partially : {}, {}", id, alPowerShellViDTO);
        if (alPowerShellViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPowerShellViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPowerShellViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlPowerShellViDTO> result = alPowerShellViService.partialUpdate(alPowerShellViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPowerShellViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-power-shell-vis} : get all the alPowerShellVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alPowerShellVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlPowerShellViDTO>> getAllAlPowerShellVis(
        AlPowerShellViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlPowerShellVis by criteria: {}", criteria);

        Page<AlPowerShellViDTO> page = alPowerShellViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-power-shell-vis/count} : count all the alPowerShellVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlPowerShellVis(AlPowerShellViCriteria criteria) {
        LOG.debug("REST request to count AlPowerShellVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alPowerShellViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-power-shell-vis/:id} : get the "id" alPowerShellVi.
     *
     * @param id the id of the alPowerShellViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alPowerShellViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlPowerShellViDTO> getAlPowerShellVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlPowerShellVi : {}", id);
        Optional<AlPowerShellViDTO> alPowerShellViDTO = alPowerShellViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alPowerShellViDTO);
    }

    /**
     * {@code DELETE  /al-power-shell-vis/:id} : delete the "id" alPowerShellVi.
     *
     * @param id the id of the alPowerShellViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlPowerShellVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlPowerShellVi : {}", id);
        alPowerShellViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
