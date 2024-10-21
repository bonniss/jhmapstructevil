package ai.realworld.web.rest;

import ai.realworld.repository.AlActisoRepository;
import ai.realworld.service.AlActisoQueryService;
import ai.realworld.service.AlActisoService;
import ai.realworld.service.criteria.AlActisoCriteria;
import ai.realworld.service.dto.AlActisoDTO;
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
 * REST controller for managing {@link ai.realworld.domain.AlActiso}.
 */
@RestController
@RequestMapping("/api/al-actisos")
public class AlActisoResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlActisoResource.class);

    private static final String ENTITY_NAME = "alActiso";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlActisoService alActisoService;

    private final AlActisoRepository alActisoRepository;

    private final AlActisoQueryService alActisoQueryService;

    public AlActisoResource(
        AlActisoService alActisoService,
        AlActisoRepository alActisoRepository,
        AlActisoQueryService alActisoQueryService
    ) {
        this.alActisoService = alActisoService;
        this.alActisoRepository = alActisoRepository;
        this.alActisoQueryService = alActisoQueryService;
    }

    /**
     * {@code POST  /al-actisos} : Create a new alActiso.
     *
     * @param alActisoDTO the alActisoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alActisoDTO, or with status {@code 400 (Bad Request)} if the alActiso has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlActisoDTO> createAlActiso(@Valid @RequestBody AlActisoDTO alActisoDTO) throws URISyntaxException {
        LOG.debug("REST request to save AlActiso : {}", alActisoDTO);
        if (alActisoDTO.getId() != null) {
            throw new BadRequestAlertException("A new alActiso cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alActisoDTO = alActisoService.save(alActisoDTO);
        return ResponseEntity.created(new URI("/api/al-actisos/" + alActisoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alActisoDTO.getId().toString()))
            .body(alActisoDTO);
    }

    /**
     * {@code PUT  /al-actisos/:id} : Updates an existing alActiso.
     *
     * @param id the id of the alActisoDTO to save.
     * @param alActisoDTO the alActisoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alActisoDTO,
     * or with status {@code 400 (Bad Request)} if the alActisoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alActisoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlActisoDTO> updateAlActiso(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlActisoDTO alActisoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlActiso : {}, {}", id, alActisoDTO);
        if (alActisoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alActisoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alActisoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alActisoDTO = alActisoService.update(alActisoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alActisoDTO.getId().toString()))
            .body(alActisoDTO);
    }

    /**
     * {@code PATCH  /al-actisos/:id} : Partial updates given fields of an existing alActiso, field will ignore if it is null
     *
     * @param id the id of the alActisoDTO to save.
     * @param alActisoDTO the alActisoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alActisoDTO,
     * or with status {@code 400 (Bad Request)} if the alActisoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alActisoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alActisoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlActisoDTO> partialUpdateAlActiso(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlActisoDTO alActisoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlActiso partially : {}, {}", id, alActisoDTO);
        if (alActisoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alActisoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alActisoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlActisoDTO> result = alActisoService.partialUpdate(alActisoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alActisoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-actisos} : get all the alActisos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alActisos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlActisoDTO>> getAllAlActisos(
        AlActisoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlActisos by criteria: {}", criteria);

        Page<AlActisoDTO> page = alActisoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-actisos/count} : count all the alActisos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlActisos(AlActisoCriteria criteria) {
        LOG.debug("REST request to count AlActisos by criteria: {}", criteria);
        return ResponseEntity.ok().body(alActisoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-actisos/:id} : get the "id" alActiso.
     *
     * @param id the id of the alActisoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alActisoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlActisoDTO> getAlActiso(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlActiso : {}", id);
        Optional<AlActisoDTO> alActisoDTO = alActisoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alActisoDTO);
    }

    /**
     * {@code DELETE  /al-actisos/:id} : delete the "id" alActiso.
     *
     * @param id the id of the alActisoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlActiso(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlActiso : {}", id);
        alActisoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
