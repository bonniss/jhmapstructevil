package ai.realworld.web.rest;

import ai.realworld.repository.AlGoreRepository;
import ai.realworld.service.AlGoreQueryService;
import ai.realworld.service.AlGoreService;
import ai.realworld.service.criteria.AlGoreCriteria;
import ai.realworld.service.dto.AlGoreDTO;
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
 * REST controller for managing {@link ai.realworld.domain.AlGore}.
 */
@RestController
@RequestMapping("/api/al-gores")
public class AlGoreResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlGoreResource.class);

    private static final String ENTITY_NAME = "alGore";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlGoreService alGoreService;

    private final AlGoreRepository alGoreRepository;

    private final AlGoreQueryService alGoreQueryService;

    public AlGoreResource(AlGoreService alGoreService, AlGoreRepository alGoreRepository, AlGoreQueryService alGoreQueryService) {
        this.alGoreService = alGoreService;
        this.alGoreRepository = alGoreRepository;
        this.alGoreQueryService = alGoreQueryService;
    }

    /**
     * {@code POST  /al-gores} : Create a new alGore.
     *
     * @param alGoreDTO the alGoreDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alGoreDTO, or with status {@code 400 (Bad Request)} if the alGore has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlGoreDTO> createAlGore(@Valid @RequestBody AlGoreDTO alGoreDTO) throws URISyntaxException {
        LOG.debug("REST request to save AlGore : {}", alGoreDTO);
        if (alGoreDTO.getId() != null) {
            throw new BadRequestAlertException("A new alGore cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alGoreDTO = alGoreService.save(alGoreDTO);
        return ResponseEntity.created(new URI("/api/al-gores/" + alGoreDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alGoreDTO.getId().toString()))
            .body(alGoreDTO);
    }

    /**
     * {@code PUT  /al-gores/:id} : Updates an existing alGore.
     *
     * @param id the id of the alGoreDTO to save.
     * @param alGoreDTO the alGoreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alGoreDTO,
     * or with status {@code 400 (Bad Request)} if the alGoreDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alGoreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlGoreDTO> updateAlGore(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlGoreDTO alGoreDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlGore : {}, {}", id, alGoreDTO);
        if (alGoreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alGoreDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alGoreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alGoreDTO = alGoreService.update(alGoreDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alGoreDTO.getId().toString()))
            .body(alGoreDTO);
    }

    /**
     * {@code PATCH  /al-gores/:id} : Partial updates given fields of an existing alGore, field will ignore if it is null
     *
     * @param id the id of the alGoreDTO to save.
     * @param alGoreDTO the alGoreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alGoreDTO,
     * or with status {@code 400 (Bad Request)} if the alGoreDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alGoreDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alGoreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlGoreDTO> partialUpdateAlGore(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlGoreDTO alGoreDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlGore partially : {}, {}", id, alGoreDTO);
        if (alGoreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alGoreDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alGoreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlGoreDTO> result = alGoreService.partialUpdate(alGoreDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alGoreDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-gores} : get all the alGores.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alGores in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlGoreDTO>> getAllAlGores(
        AlGoreCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlGores by criteria: {}", criteria);

        Page<AlGoreDTO> page = alGoreQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-gores/count} : count all the alGores.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlGores(AlGoreCriteria criteria) {
        LOG.debug("REST request to count AlGores by criteria: {}", criteria);
        return ResponseEntity.ok().body(alGoreQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-gores/:id} : get the "id" alGore.
     *
     * @param id the id of the alGoreDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alGoreDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlGoreDTO> getAlGore(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlGore : {}", id);
        Optional<AlGoreDTO> alGoreDTO = alGoreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alGoreDTO);
    }

    /**
     * {@code DELETE  /al-gores/:id} : delete the "id" alGore.
     *
     * @param id the id of the alGoreDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlGore(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlGore : {}", id);
        alGoreService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
