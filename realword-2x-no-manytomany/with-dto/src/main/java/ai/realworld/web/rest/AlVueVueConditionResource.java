package ai.realworld.web.rest;

import ai.realworld.repository.AlVueVueConditionRepository;
import ai.realworld.service.AlVueVueConditionQueryService;
import ai.realworld.service.AlVueVueConditionService;
import ai.realworld.service.criteria.AlVueVueConditionCriteria;
import ai.realworld.service.dto.AlVueVueConditionDTO;
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
 * REST controller for managing {@link ai.realworld.domain.AlVueVueCondition}.
 */
@RestController
@RequestMapping("/api/al-vue-vue-conditions")
public class AlVueVueConditionResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlVueVueConditionResource.class);

    private static final String ENTITY_NAME = "alVueVueCondition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlVueVueConditionService alVueVueConditionService;

    private final AlVueVueConditionRepository alVueVueConditionRepository;

    private final AlVueVueConditionQueryService alVueVueConditionQueryService;

    public AlVueVueConditionResource(
        AlVueVueConditionService alVueVueConditionService,
        AlVueVueConditionRepository alVueVueConditionRepository,
        AlVueVueConditionQueryService alVueVueConditionQueryService
    ) {
        this.alVueVueConditionService = alVueVueConditionService;
        this.alVueVueConditionRepository = alVueVueConditionRepository;
        this.alVueVueConditionQueryService = alVueVueConditionQueryService;
    }

    /**
     * {@code POST  /al-vue-vue-conditions} : Create a new alVueVueCondition.
     *
     * @param alVueVueConditionDTO the alVueVueConditionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alVueVueConditionDTO, or with status {@code 400 (Bad Request)} if the alVueVueCondition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlVueVueConditionDTO> createAlVueVueCondition(@Valid @RequestBody AlVueVueConditionDTO alVueVueConditionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AlVueVueCondition : {}", alVueVueConditionDTO);
        if (alVueVueConditionDTO.getId() != null) {
            throw new BadRequestAlertException("A new alVueVueCondition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alVueVueConditionDTO = alVueVueConditionService.save(alVueVueConditionDTO);
        return ResponseEntity.created(new URI("/api/al-vue-vue-conditions/" + alVueVueConditionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alVueVueConditionDTO.getId().toString()))
            .body(alVueVueConditionDTO);
    }

    /**
     * {@code PUT  /al-vue-vue-conditions/:id} : Updates an existing alVueVueCondition.
     *
     * @param id the id of the alVueVueConditionDTO to save.
     * @param alVueVueConditionDTO the alVueVueConditionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alVueVueConditionDTO,
     * or with status {@code 400 (Bad Request)} if the alVueVueConditionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alVueVueConditionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlVueVueConditionDTO> updateAlVueVueCondition(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlVueVueConditionDTO alVueVueConditionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlVueVueCondition : {}, {}", id, alVueVueConditionDTO);
        if (alVueVueConditionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alVueVueConditionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alVueVueConditionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alVueVueConditionDTO = alVueVueConditionService.update(alVueVueConditionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alVueVueConditionDTO.getId().toString()))
            .body(alVueVueConditionDTO);
    }

    /**
     * {@code PATCH  /al-vue-vue-conditions/:id} : Partial updates given fields of an existing alVueVueCondition, field will ignore if it is null
     *
     * @param id the id of the alVueVueConditionDTO to save.
     * @param alVueVueConditionDTO the alVueVueConditionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alVueVueConditionDTO,
     * or with status {@code 400 (Bad Request)} if the alVueVueConditionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alVueVueConditionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alVueVueConditionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlVueVueConditionDTO> partialUpdateAlVueVueCondition(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlVueVueConditionDTO alVueVueConditionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlVueVueCondition partially : {}, {}", id, alVueVueConditionDTO);
        if (alVueVueConditionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alVueVueConditionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alVueVueConditionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlVueVueConditionDTO> result = alVueVueConditionService.partialUpdate(alVueVueConditionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alVueVueConditionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-vue-vue-conditions} : get all the alVueVueConditions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alVueVueConditions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlVueVueConditionDTO>> getAllAlVueVueConditions(
        AlVueVueConditionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlVueVueConditions by criteria: {}", criteria);

        Page<AlVueVueConditionDTO> page = alVueVueConditionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-vue-vue-conditions/count} : count all the alVueVueConditions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlVueVueConditions(AlVueVueConditionCriteria criteria) {
        LOG.debug("REST request to count AlVueVueConditions by criteria: {}", criteria);
        return ResponseEntity.ok().body(alVueVueConditionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-vue-vue-conditions/:id} : get the "id" alVueVueCondition.
     *
     * @param id the id of the alVueVueConditionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alVueVueConditionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlVueVueConditionDTO> getAlVueVueCondition(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlVueVueCondition : {}", id);
        Optional<AlVueVueConditionDTO> alVueVueConditionDTO = alVueVueConditionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alVueVueConditionDTO);
    }

    /**
     * {@code DELETE  /al-vue-vue-conditions/:id} : delete the "id" alVueVueCondition.
     *
     * @param id the id of the alVueVueConditionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlVueVueCondition(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlVueVueCondition : {}", id);
        alVueVueConditionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
