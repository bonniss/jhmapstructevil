package ai.realworld.web.rest;

import ai.realworld.repository.HandCraftViRepository;
import ai.realworld.service.HandCraftViQueryService;
import ai.realworld.service.HandCraftViService;
import ai.realworld.service.criteria.HandCraftViCriteria;
import ai.realworld.service.dto.HandCraftViDTO;
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
 * REST controller for managing {@link ai.realworld.domain.HandCraftVi}.
 */
@RestController
@RequestMapping("/api/hand-craft-vis")
public class HandCraftViResource {

    private static final Logger LOG = LoggerFactory.getLogger(HandCraftViResource.class);

    private static final String ENTITY_NAME = "handCraftVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HandCraftViService handCraftViService;

    private final HandCraftViRepository handCraftViRepository;

    private final HandCraftViQueryService handCraftViQueryService;

    public HandCraftViResource(
        HandCraftViService handCraftViService,
        HandCraftViRepository handCraftViRepository,
        HandCraftViQueryService handCraftViQueryService
    ) {
        this.handCraftViService = handCraftViService;
        this.handCraftViRepository = handCraftViRepository;
        this.handCraftViQueryService = handCraftViQueryService;
    }

    /**
     * {@code POST  /hand-craft-vis} : Create a new handCraftVi.
     *
     * @param handCraftViDTO the handCraftViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new handCraftViDTO, or with status {@code 400 (Bad Request)} if the handCraftVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HandCraftViDTO> createHandCraftVi(@Valid @RequestBody HandCraftViDTO handCraftViDTO) throws URISyntaxException {
        LOG.debug("REST request to save HandCraftVi : {}", handCraftViDTO);
        if (handCraftViDTO.getId() != null) {
            throw new BadRequestAlertException("A new handCraftVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        handCraftViDTO = handCraftViService.save(handCraftViDTO);
        return ResponseEntity.created(new URI("/api/hand-craft-vis/" + handCraftViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, handCraftViDTO.getId().toString()))
            .body(handCraftViDTO);
    }

    /**
     * {@code PUT  /hand-craft-vis/:id} : Updates an existing handCraftVi.
     *
     * @param id the id of the handCraftViDTO to save.
     * @param handCraftViDTO the handCraftViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated handCraftViDTO,
     * or with status {@code 400 (Bad Request)} if the handCraftViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the handCraftViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HandCraftViDTO> updateHandCraftVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HandCraftViDTO handCraftViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update HandCraftVi : {}, {}", id, handCraftViDTO);
        if (handCraftViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, handCraftViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!handCraftViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        handCraftViDTO = handCraftViService.update(handCraftViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, handCraftViDTO.getId().toString()))
            .body(handCraftViDTO);
    }

    /**
     * {@code PATCH  /hand-craft-vis/:id} : Partial updates given fields of an existing handCraftVi, field will ignore if it is null
     *
     * @param id the id of the handCraftViDTO to save.
     * @param handCraftViDTO the handCraftViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated handCraftViDTO,
     * or with status {@code 400 (Bad Request)} if the handCraftViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the handCraftViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the handCraftViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HandCraftViDTO> partialUpdateHandCraftVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HandCraftViDTO handCraftViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update HandCraftVi partially : {}, {}", id, handCraftViDTO);
        if (handCraftViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, handCraftViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!handCraftViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HandCraftViDTO> result = handCraftViService.partialUpdate(handCraftViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, handCraftViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /hand-craft-vis} : get all the handCraftVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of handCraftVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HandCraftViDTO>> getAllHandCraftVis(
        HandCraftViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get HandCraftVis by criteria: {}", criteria);

        Page<HandCraftViDTO> page = handCraftViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hand-craft-vis/count} : count all the handCraftVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countHandCraftVis(HandCraftViCriteria criteria) {
        LOG.debug("REST request to count HandCraftVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(handCraftViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /hand-craft-vis/:id} : get the "id" handCraftVi.
     *
     * @param id the id of the handCraftViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the handCraftViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HandCraftViDTO> getHandCraftVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get HandCraftVi : {}", id);
        Optional<HandCraftViDTO> handCraftViDTO = handCraftViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(handCraftViDTO);
    }

    /**
     * {@code DELETE  /hand-craft-vis/:id} : delete the "id" handCraftVi.
     *
     * @param id the id of the handCraftViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHandCraftVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete HandCraftVi : {}", id);
        handCraftViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
