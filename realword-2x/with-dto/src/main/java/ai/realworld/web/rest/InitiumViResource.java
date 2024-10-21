package ai.realworld.web.rest;

import ai.realworld.repository.InitiumViRepository;
import ai.realworld.service.InitiumViQueryService;
import ai.realworld.service.InitiumViService;
import ai.realworld.service.criteria.InitiumViCriteria;
import ai.realworld.service.dto.InitiumViDTO;
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
 * REST controller for managing {@link ai.realworld.domain.InitiumVi}.
 */
@RestController
@RequestMapping("/api/initium-vis")
public class InitiumViResource {

    private static final Logger LOG = LoggerFactory.getLogger(InitiumViResource.class);

    private static final String ENTITY_NAME = "initiumVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InitiumViService initiumViService;

    private final InitiumViRepository initiumViRepository;

    private final InitiumViQueryService initiumViQueryService;

    public InitiumViResource(
        InitiumViService initiumViService,
        InitiumViRepository initiumViRepository,
        InitiumViQueryService initiumViQueryService
    ) {
        this.initiumViService = initiumViService;
        this.initiumViRepository = initiumViRepository;
        this.initiumViQueryService = initiumViQueryService;
    }

    /**
     * {@code POST  /initium-vis} : Create a new initiumVi.
     *
     * @param initiumViDTO the initiumViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new initiumViDTO, or with status {@code 400 (Bad Request)} if the initiumVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InitiumViDTO> createInitiumVi(@Valid @RequestBody InitiumViDTO initiumViDTO) throws URISyntaxException {
        LOG.debug("REST request to save InitiumVi : {}", initiumViDTO);
        if (initiumViDTO.getId() != null) {
            throw new BadRequestAlertException("A new initiumVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        initiumViDTO = initiumViService.save(initiumViDTO);
        return ResponseEntity.created(new URI("/api/initium-vis/" + initiumViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, initiumViDTO.getId().toString()))
            .body(initiumViDTO);
    }

    /**
     * {@code PUT  /initium-vis/:id} : Updates an existing initiumVi.
     *
     * @param id the id of the initiumViDTO to save.
     * @param initiumViDTO the initiumViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated initiumViDTO,
     * or with status {@code 400 (Bad Request)} if the initiumViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the initiumViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InitiumViDTO> updateInitiumVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InitiumViDTO initiumViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update InitiumVi : {}, {}", id, initiumViDTO);
        if (initiumViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, initiumViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!initiumViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        initiumViDTO = initiumViService.update(initiumViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, initiumViDTO.getId().toString()))
            .body(initiumViDTO);
    }

    /**
     * {@code PATCH  /initium-vis/:id} : Partial updates given fields of an existing initiumVi, field will ignore if it is null
     *
     * @param id the id of the initiumViDTO to save.
     * @param initiumViDTO the initiumViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated initiumViDTO,
     * or with status {@code 400 (Bad Request)} if the initiumViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the initiumViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the initiumViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InitiumViDTO> partialUpdateInitiumVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InitiumViDTO initiumViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update InitiumVi partially : {}, {}", id, initiumViDTO);
        if (initiumViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, initiumViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!initiumViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InitiumViDTO> result = initiumViService.partialUpdate(initiumViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, initiumViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /initium-vis} : get all the initiumVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of initiumVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InitiumViDTO>> getAllInitiumVis(
        InitiumViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get InitiumVis by criteria: {}", criteria);

        Page<InitiumViDTO> page = initiumViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /initium-vis/count} : count all the initiumVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInitiumVis(InitiumViCriteria criteria) {
        LOG.debug("REST request to count InitiumVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(initiumViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /initium-vis/:id} : get the "id" initiumVi.
     *
     * @param id the id of the initiumViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the initiumViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InitiumViDTO> getInitiumVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InitiumVi : {}", id);
        Optional<InitiumViDTO> initiumViDTO = initiumViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(initiumViDTO);
    }

    /**
     * {@code DELETE  /initium-vis/:id} : delete the "id" initiumVi.
     *
     * @param id the id of the initiumViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInitiumVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InitiumVi : {}", id);
        initiumViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
