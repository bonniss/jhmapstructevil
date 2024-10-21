package ai.realworld.web.rest;

import ai.realworld.repository.AlCatalinaViRepository;
import ai.realworld.service.AlCatalinaViQueryService;
import ai.realworld.service.AlCatalinaViService;
import ai.realworld.service.criteria.AlCatalinaViCriteria;
import ai.realworld.service.dto.AlCatalinaViDTO;
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
 * REST controller for managing {@link ai.realworld.domain.AlCatalinaVi}.
 */
@RestController
@RequestMapping("/api/al-catalina-vis")
public class AlCatalinaViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlCatalinaViResource.class);

    private static final String ENTITY_NAME = "alCatalinaVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlCatalinaViService alCatalinaViService;

    private final AlCatalinaViRepository alCatalinaViRepository;

    private final AlCatalinaViQueryService alCatalinaViQueryService;

    public AlCatalinaViResource(
        AlCatalinaViService alCatalinaViService,
        AlCatalinaViRepository alCatalinaViRepository,
        AlCatalinaViQueryService alCatalinaViQueryService
    ) {
        this.alCatalinaViService = alCatalinaViService;
        this.alCatalinaViRepository = alCatalinaViRepository;
        this.alCatalinaViQueryService = alCatalinaViQueryService;
    }

    /**
     * {@code POST  /al-catalina-vis} : Create a new alCatalinaVi.
     *
     * @param alCatalinaViDTO the alCatalinaViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alCatalinaViDTO, or with status {@code 400 (Bad Request)} if the alCatalinaVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlCatalinaViDTO> createAlCatalinaVi(@Valid @RequestBody AlCatalinaViDTO alCatalinaViDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AlCatalinaVi : {}", alCatalinaViDTO);
        if (alCatalinaViDTO.getId() != null) {
            throw new BadRequestAlertException("A new alCatalinaVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alCatalinaViDTO = alCatalinaViService.save(alCatalinaViDTO);
        return ResponseEntity.created(new URI("/api/al-catalina-vis/" + alCatalinaViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alCatalinaViDTO.getId().toString()))
            .body(alCatalinaViDTO);
    }

    /**
     * {@code PUT  /al-catalina-vis/:id} : Updates an existing alCatalinaVi.
     *
     * @param id the id of the alCatalinaViDTO to save.
     * @param alCatalinaViDTO the alCatalinaViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alCatalinaViDTO,
     * or with status {@code 400 (Bad Request)} if the alCatalinaViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alCatalinaViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlCatalinaViDTO> updateAlCatalinaVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlCatalinaViDTO alCatalinaViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlCatalinaVi : {}, {}", id, alCatalinaViDTO);
        if (alCatalinaViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alCatalinaViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alCatalinaViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alCatalinaViDTO = alCatalinaViService.update(alCatalinaViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alCatalinaViDTO.getId().toString()))
            .body(alCatalinaViDTO);
    }

    /**
     * {@code PATCH  /al-catalina-vis/:id} : Partial updates given fields of an existing alCatalinaVi, field will ignore if it is null
     *
     * @param id the id of the alCatalinaViDTO to save.
     * @param alCatalinaViDTO the alCatalinaViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alCatalinaViDTO,
     * or with status {@code 400 (Bad Request)} if the alCatalinaViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alCatalinaViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alCatalinaViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlCatalinaViDTO> partialUpdateAlCatalinaVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlCatalinaViDTO alCatalinaViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlCatalinaVi partially : {}, {}", id, alCatalinaViDTO);
        if (alCatalinaViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alCatalinaViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alCatalinaViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlCatalinaViDTO> result = alCatalinaViService.partialUpdate(alCatalinaViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alCatalinaViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-catalina-vis} : get all the alCatalinaVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alCatalinaVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlCatalinaViDTO>> getAllAlCatalinaVis(
        AlCatalinaViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlCatalinaVis by criteria: {}", criteria);

        Page<AlCatalinaViDTO> page = alCatalinaViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-catalina-vis/count} : count all the alCatalinaVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlCatalinaVis(AlCatalinaViCriteria criteria) {
        LOG.debug("REST request to count AlCatalinaVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alCatalinaViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-catalina-vis/:id} : get the "id" alCatalinaVi.
     *
     * @param id the id of the alCatalinaViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alCatalinaViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlCatalinaViDTO> getAlCatalinaVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlCatalinaVi : {}", id);
        Optional<AlCatalinaViDTO> alCatalinaViDTO = alCatalinaViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alCatalinaViDTO);
    }

    /**
     * {@code DELETE  /al-catalina-vis/:id} : delete the "id" alCatalinaVi.
     *
     * @param id the id of the alCatalinaViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlCatalinaVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlCatalinaVi : {}", id);
        alCatalinaViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
