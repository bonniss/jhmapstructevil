package ai.realworld.web.rest;

import ai.realworld.repository.AlZorroTemptationViRepository;
import ai.realworld.service.AlZorroTemptationViQueryService;
import ai.realworld.service.AlZorroTemptationViService;
import ai.realworld.service.criteria.AlZorroTemptationViCriteria;
import ai.realworld.service.dto.AlZorroTemptationViDTO;
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
 * REST controller for managing {@link ai.realworld.domain.AlZorroTemptationVi}.
 */
@RestController
@RequestMapping("/api/al-zorro-temptation-vis")
public class AlZorroTemptationViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlZorroTemptationViResource.class);

    private static final String ENTITY_NAME = "alZorroTemptationVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlZorroTemptationViService alZorroTemptationViService;

    private final AlZorroTemptationViRepository alZorroTemptationViRepository;

    private final AlZorroTemptationViQueryService alZorroTemptationViQueryService;

    public AlZorroTemptationViResource(
        AlZorroTemptationViService alZorroTemptationViService,
        AlZorroTemptationViRepository alZorroTemptationViRepository,
        AlZorroTemptationViQueryService alZorroTemptationViQueryService
    ) {
        this.alZorroTemptationViService = alZorroTemptationViService;
        this.alZorroTemptationViRepository = alZorroTemptationViRepository;
        this.alZorroTemptationViQueryService = alZorroTemptationViQueryService;
    }

    /**
     * {@code POST  /al-zorro-temptation-vis} : Create a new alZorroTemptationVi.
     *
     * @param alZorroTemptationViDTO the alZorroTemptationViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alZorroTemptationViDTO, or with status {@code 400 (Bad Request)} if the alZorroTemptationVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlZorroTemptationViDTO> createAlZorroTemptationVi(
        @Valid @RequestBody AlZorroTemptationViDTO alZorroTemptationViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save AlZorroTemptationVi : {}", alZorroTemptationViDTO);
        if (alZorroTemptationViDTO.getId() != null) {
            throw new BadRequestAlertException("A new alZorroTemptationVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alZorroTemptationViDTO = alZorroTemptationViService.save(alZorroTemptationViDTO);
        return ResponseEntity.created(new URI("/api/al-zorro-temptation-vis/" + alZorroTemptationViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alZorroTemptationViDTO.getId().toString()))
            .body(alZorroTemptationViDTO);
    }

    /**
     * {@code PUT  /al-zorro-temptation-vis/:id} : Updates an existing alZorroTemptationVi.
     *
     * @param id the id of the alZorroTemptationViDTO to save.
     * @param alZorroTemptationViDTO the alZorroTemptationViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alZorroTemptationViDTO,
     * or with status {@code 400 (Bad Request)} if the alZorroTemptationViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alZorroTemptationViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlZorroTemptationViDTO> updateAlZorroTemptationVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlZorroTemptationViDTO alZorroTemptationViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlZorroTemptationVi : {}, {}", id, alZorroTemptationViDTO);
        if (alZorroTemptationViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alZorroTemptationViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alZorroTemptationViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alZorroTemptationViDTO = alZorroTemptationViService.update(alZorroTemptationViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alZorroTemptationViDTO.getId().toString()))
            .body(alZorroTemptationViDTO);
    }

    /**
     * {@code PATCH  /al-zorro-temptation-vis/:id} : Partial updates given fields of an existing alZorroTemptationVi, field will ignore if it is null
     *
     * @param id the id of the alZorroTemptationViDTO to save.
     * @param alZorroTemptationViDTO the alZorroTemptationViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alZorroTemptationViDTO,
     * or with status {@code 400 (Bad Request)} if the alZorroTemptationViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alZorroTemptationViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alZorroTemptationViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlZorroTemptationViDTO> partialUpdateAlZorroTemptationVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlZorroTemptationViDTO alZorroTemptationViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlZorroTemptationVi partially : {}, {}", id, alZorroTemptationViDTO);
        if (alZorroTemptationViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alZorroTemptationViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alZorroTemptationViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlZorroTemptationViDTO> result = alZorroTemptationViService.partialUpdate(alZorroTemptationViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alZorroTemptationViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-zorro-temptation-vis} : get all the alZorroTemptationVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alZorroTemptationVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlZorroTemptationViDTO>> getAllAlZorroTemptationVis(
        AlZorroTemptationViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlZorroTemptationVis by criteria: {}", criteria);

        Page<AlZorroTemptationViDTO> page = alZorroTemptationViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-zorro-temptation-vis/count} : count all the alZorroTemptationVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlZorroTemptationVis(AlZorroTemptationViCriteria criteria) {
        LOG.debug("REST request to count AlZorroTemptationVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alZorroTemptationViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-zorro-temptation-vis/:id} : get the "id" alZorroTemptationVi.
     *
     * @param id the id of the alZorroTemptationViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alZorroTemptationViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlZorroTemptationViDTO> getAlZorroTemptationVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlZorroTemptationVi : {}", id);
        Optional<AlZorroTemptationViDTO> alZorroTemptationViDTO = alZorroTemptationViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alZorroTemptationViDTO);
    }

    /**
     * {@code DELETE  /al-zorro-temptation-vis/:id} : delete the "id" alZorroTemptationVi.
     *
     * @param id the id of the alZorroTemptationViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlZorroTemptationVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlZorroTemptationVi : {}", id);
        alZorroTemptationViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
