package ai.realworld.web.rest;

import ai.realworld.repository.AlPounderRepository;
import ai.realworld.service.AlPounderQueryService;
import ai.realworld.service.AlPounderService;
import ai.realworld.service.criteria.AlPounderCriteria;
import ai.realworld.service.dto.AlPounderDTO;
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
 * REST controller for managing {@link ai.realworld.domain.AlPounder}.
 */
@RestController
@RequestMapping("/api/al-pounders")
public class AlPounderResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlPounderResource.class);

    private static final String ENTITY_NAME = "alPounder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlPounderService alPounderService;

    private final AlPounderRepository alPounderRepository;

    private final AlPounderQueryService alPounderQueryService;

    public AlPounderResource(
        AlPounderService alPounderService,
        AlPounderRepository alPounderRepository,
        AlPounderQueryService alPounderQueryService
    ) {
        this.alPounderService = alPounderService;
        this.alPounderRepository = alPounderRepository;
        this.alPounderQueryService = alPounderQueryService;
    }

    /**
     * {@code POST  /al-pounders} : Create a new alPounder.
     *
     * @param alPounderDTO the alPounderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alPounderDTO, or with status {@code 400 (Bad Request)} if the alPounder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlPounderDTO> createAlPounder(@Valid @RequestBody AlPounderDTO alPounderDTO) throws URISyntaxException {
        LOG.debug("REST request to save AlPounder : {}", alPounderDTO);
        if (alPounderDTO.getId() != null) {
            throw new BadRequestAlertException("A new alPounder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alPounderDTO = alPounderService.save(alPounderDTO);
        return ResponseEntity.created(new URI("/api/al-pounders/" + alPounderDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alPounderDTO.getId().toString()))
            .body(alPounderDTO);
    }

    /**
     * {@code PUT  /al-pounders/:id} : Updates an existing alPounder.
     *
     * @param id the id of the alPounderDTO to save.
     * @param alPounderDTO the alPounderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPounderDTO,
     * or with status {@code 400 (Bad Request)} if the alPounderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alPounderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlPounderDTO> updateAlPounder(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlPounderDTO alPounderDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlPounder : {}, {}", id, alPounderDTO);
        if (alPounderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPounderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPounderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alPounderDTO = alPounderService.update(alPounderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPounderDTO.getId().toString()))
            .body(alPounderDTO);
    }

    /**
     * {@code PATCH  /al-pounders/:id} : Partial updates given fields of an existing alPounder, field will ignore if it is null
     *
     * @param id the id of the alPounderDTO to save.
     * @param alPounderDTO the alPounderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPounderDTO,
     * or with status {@code 400 (Bad Request)} if the alPounderDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alPounderDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alPounderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlPounderDTO> partialUpdateAlPounder(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlPounderDTO alPounderDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlPounder partially : {}, {}", id, alPounderDTO);
        if (alPounderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPounderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPounderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlPounderDTO> result = alPounderService.partialUpdate(alPounderDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPounderDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-pounders} : get all the alPounders.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alPounders in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlPounderDTO>> getAllAlPounders(
        AlPounderCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlPounders by criteria: {}", criteria);

        Page<AlPounderDTO> page = alPounderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-pounders/count} : count all the alPounders.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlPounders(AlPounderCriteria criteria) {
        LOG.debug("REST request to count AlPounders by criteria: {}", criteria);
        return ResponseEntity.ok().body(alPounderQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-pounders/:id} : get the "id" alPounder.
     *
     * @param id the id of the alPounderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alPounderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlPounderDTO> getAlPounder(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlPounder : {}", id);
        Optional<AlPounderDTO> alPounderDTO = alPounderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alPounderDTO);
    }

    /**
     * {@code DELETE  /al-pounders/:id} : delete the "id" alPounder.
     *
     * @param id the id of the alPounderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlPounder(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlPounder : {}", id);
        alPounderService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
