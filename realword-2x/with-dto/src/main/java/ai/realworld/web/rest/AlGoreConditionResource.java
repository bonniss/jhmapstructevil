package ai.realworld.web.rest;

import ai.realworld.repository.AlGoreConditionRepository;
import ai.realworld.service.AlGoreConditionQueryService;
import ai.realworld.service.AlGoreConditionService;
import ai.realworld.service.criteria.AlGoreConditionCriteria;
import ai.realworld.service.dto.AlGoreConditionDTO;
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
 * REST controller for managing {@link ai.realworld.domain.AlGoreCondition}.
 */
@RestController
@RequestMapping("/api/al-gore-conditions")
public class AlGoreConditionResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlGoreConditionResource.class);

    private static final String ENTITY_NAME = "alGoreCondition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlGoreConditionService alGoreConditionService;

    private final AlGoreConditionRepository alGoreConditionRepository;

    private final AlGoreConditionQueryService alGoreConditionQueryService;

    public AlGoreConditionResource(
        AlGoreConditionService alGoreConditionService,
        AlGoreConditionRepository alGoreConditionRepository,
        AlGoreConditionQueryService alGoreConditionQueryService
    ) {
        this.alGoreConditionService = alGoreConditionService;
        this.alGoreConditionRepository = alGoreConditionRepository;
        this.alGoreConditionQueryService = alGoreConditionQueryService;
    }

    /**
     * {@code POST  /al-gore-conditions} : Create a new alGoreCondition.
     *
     * @param alGoreConditionDTO the alGoreConditionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alGoreConditionDTO, or with status {@code 400 (Bad Request)} if the alGoreCondition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlGoreConditionDTO> createAlGoreCondition(@Valid @RequestBody AlGoreConditionDTO alGoreConditionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AlGoreCondition : {}", alGoreConditionDTO);
        if (alGoreConditionDTO.getId() != null) {
            throw new BadRequestAlertException("A new alGoreCondition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alGoreConditionDTO = alGoreConditionService.save(alGoreConditionDTO);
        return ResponseEntity.created(new URI("/api/al-gore-conditions/" + alGoreConditionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alGoreConditionDTO.getId().toString()))
            .body(alGoreConditionDTO);
    }

    /**
     * {@code PUT  /al-gore-conditions/:id} : Updates an existing alGoreCondition.
     *
     * @param id the id of the alGoreConditionDTO to save.
     * @param alGoreConditionDTO the alGoreConditionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alGoreConditionDTO,
     * or with status {@code 400 (Bad Request)} if the alGoreConditionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alGoreConditionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlGoreConditionDTO> updateAlGoreCondition(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlGoreConditionDTO alGoreConditionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlGoreCondition : {}, {}", id, alGoreConditionDTO);
        if (alGoreConditionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alGoreConditionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alGoreConditionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alGoreConditionDTO = alGoreConditionService.update(alGoreConditionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alGoreConditionDTO.getId().toString()))
            .body(alGoreConditionDTO);
    }

    /**
     * {@code PATCH  /al-gore-conditions/:id} : Partial updates given fields of an existing alGoreCondition, field will ignore if it is null
     *
     * @param id the id of the alGoreConditionDTO to save.
     * @param alGoreConditionDTO the alGoreConditionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alGoreConditionDTO,
     * or with status {@code 400 (Bad Request)} if the alGoreConditionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alGoreConditionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alGoreConditionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlGoreConditionDTO> partialUpdateAlGoreCondition(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlGoreConditionDTO alGoreConditionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlGoreCondition partially : {}, {}", id, alGoreConditionDTO);
        if (alGoreConditionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alGoreConditionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alGoreConditionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlGoreConditionDTO> result = alGoreConditionService.partialUpdate(alGoreConditionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alGoreConditionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-gore-conditions} : get all the alGoreConditions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alGoreConditions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlGoreConditionDTO>> getAllAlGoreConditions(
        AlGoreConditionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlGoreConditions by criteria: {}", criteria);

        Page<AlGoreConditionDTO> page = alGoreConditionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-gore-conditions/count} : count all the alGoreConditions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlGoreConditions(AlGoreConditionCriteria criteria) {
        LOG.debug("REST request to count AlGoreConditions by criteria: {}", criteria);
        return ResponseEntity.ok().body(alGoreConditionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-gore-conditions/:id} : get the "id" alGoreCondition.
     *
     * @param id the id of the alGoreConditionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alGoreConditionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlGoreConditionDTO> getAlGoreCondition(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlGoreCondition : {}", id);
        Optional<AlGoreConditionDTO> alGoreConditionDTO = alGoreConditionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alGoreConditionDTO);
    }

    /**
     * {@code DELETE  /al-gore-conditions/:id} : delete the "id" alGoreCondition.
     *
     * @param id the id of the alGoreConditionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlGoreCondition(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlGoreCondition : {}", id);
        alGoreConditionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
