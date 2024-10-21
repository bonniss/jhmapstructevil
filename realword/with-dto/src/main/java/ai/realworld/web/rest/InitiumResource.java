package ai.realworld.web.rest;

import ai.realworld.repository.InitiumRepository;
import ai.realworld.service.InitiumQueryService;
import ai.realworld.service.InitiumService;
import ai.realworld.service.criteria.InitiumCriteria;
import ai.realworld.service.dto.InitiumDTO;
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
 * REST controller for managing {@link ai.realworld.domain.Initium}.
 */
@RestController
@RequestMapping("/api/initiums")
public class InitiumResource {

    private static final Logger LOG = LoggerFactory.getLogger(InitiumResource.class);

    private static final String ENTITY_NAME = "initium";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InitiumService initiumService;

    private final InitiumRepository initiumRepository;

    private final InitiumQueryService initiumQueryService;

    public InitiumResource(InitiumService initiumService, InitiumRepository initiumRepository, InitiumQueryService initiumQueryService) {
        this.initiumService = initiumService;
        this.initiumRepository = initiumRepository;
        this.initiumQueryService = initiumQueryService;
    }

    /**
     * {@code POST  /initiums} : Create a new initium.
     *
     * @param initiumDTO the initiumDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new initiumDTO, or with status {@code 400 (Bad Request)} if the initium has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InitiumDTO> createInitium(@Valid @RequestBody InitiumDTO initiumDTO) throws URISyntaxException {
        LOG.debug("REST request to save Initium : {}", initiumDTO);
        if (initiumDTO.getId() != null) {
            throw new BadRequestAlertException("A new initium cannot already have an ID", ENTITY_NAME, "idexists");
        }
        initiumDTO = initiumService.save(initiumDTO);
        return ResponseEntity.created(new URI("/api/initiums/" + initiumDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, initiumDTO.getId().toString()))
            .body(initiumDTO);
    }

    /**
     * {@code PUT  /initiums/:id} : Updates an existing initium.
     *
     * @param id the id of the initiumDTO to save.
     * @param initiumDTO the initiumDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated initiumDTO,
     * or with status {@code 400 (Bad Request)} if the initiumDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the initiumDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InitiumDTO> updateInitium(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InitiumDTO initiumDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Initium : {}, {}", id, initiumDTO);
        if (initiumDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, initiumDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!initiumRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        initiumDTO = initiumService.update(initiumDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, initiumDTO.getId().toString()))
            .body(initiumDTO);
    }

    /**
     * {@code PATCH  /initiums/:id} : Partial updates given fields of an existing initium, field will ignore if it is null
     *
     * @param id the id of the initiumDTO to save.
     * @param initiumDTO the initiumDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated initiumDTO,
     * or with status {@code 400 (Bad Request)} if the initiumDTO is not valid,
     * or with status {@code 404 (Not Found)} if the initiumDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the initiumDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InitiumDTO> partialUpdateInitium(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InitiumDTO initiumDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Initium partially : {}, {}", id, initiumDTO);
        if (initiumDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, initiumDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!initiumRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InitiumDTO> result = initiumService.partialUpdate(initiumDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, initiumDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /initiums} : get all the initiums.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of initiums in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InitiumDTO>> getAllInitiums(
        InitiumCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Initiums by criteria: {}", criteria);

        Page<InitiumDTO> page = initiumQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /initiums/count} : count all the initiums.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInitiums(InitiumCriteria criteria) {
        LOG.debug("REST request to count Initiums by criteria: {}", criteria);
        return ResponseEntity.ok().body(initiumQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /initiums/:id} : get the "id" initium.
     *
     * @param id the id of the initiumDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the initiumDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InitiumDTO> getInitium(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Initium : {}", id);
        Optional<InitiumDTO> initiumDTO = initiumService.findOne(id);
        return ResponseUtil.wrapOrNotFound(initiumDTO);
    }

    /**
     * {@code DELETE  /initiums/:id} : delete the "id" initium.
     *
     * @param id the id of the initiumDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInitium(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Initium : {}", id);
        initiumService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
