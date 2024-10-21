package ai.realworld.web.rest;

import ai.realworld.repository.AlInquiryViRepository;
import ai.realworld.service.AlInquiryViQueryService;
import ai.realworld.service.AlInquiryViService;
import ai.realworld.service.criteria.AlInquiryViCriteria;
import ai.realworld.service.dto.AlInquiryViDTO;
import ai.realworld.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
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
 * REST controller for managing {@link ai.realworld.domain.AlInquiryVi}.
 */
@RestController
@RequestMapping("/api/al-inquiry-vis")
public class AlInquiryViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlInquiryViResource.class);

    private static final String ENTITY_NAME = "alInquiryVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlInquiryViService alInquiryViService;

    private final AlInquiryViRepository alInquiryViRepository;

    private final AlInquiryViQueryService alInquiryViQueryService;

    public AlInquiryViResource(
        AlInquiryViService alInquiryViService,
        AlInquiryViRepository alInquiryViRepository,
        AlInquiryViQueryService alInquiryViQueryService
    ) {
        this.alInquiryViService = alInquiryViService;
        this.alInquiryViRepository = alInquiryViRepository;
        this.alInquiryViQueryService = alInquiryViQueryService;
    }

    /**
     * {@code POST  /al-inquiry-vis} : Create a new alInquiryVi.
     *
     * @param alInquiryViDTO the alInquiryViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alInquiryViDTO, or with status {@code 400 (Bad Request)} if the alInquiryVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlInquiryViDTO> createAlInquiryVi(@Valid @RequestBody AlInquiryViDTO alInquiryViDTO) throws URISyntaxException {
        LOG.debug("REST request to save AlInquiryVi : {}", alInquiryViDTO);
        if (alInquiryViDTO.getId() != null) {
            throw new BadRequestAlertException("A new alInquiryVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alInquiryViDTO = alInquiryViService.save(alInquiryViDTO);
        return ResponseEntity.created(new URI("/api/al-inquiry-vis/" + alInquiryViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alInquiryViDTO.getId().toString()))
            .body(alInquiryViDTO);
    }

    /**
     * {@code PUT  /al-inquiry-vis/:id} : Updates an existing alInquiryVi.
     *
     * @param id the id of the alInquiryViDTO to save.
     * @param alInquiryViDTO the alInquiryViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alInquiryViDTO,
     * or with status {@code 400 (Bad Request)} if the alInquiryViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alInquiryViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlInquiryViDTO> updateAlInquiryVi(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody AlInquiryViDTO alInquiryViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlInquiryVi : {}, {}", id, alInquiryViDTO);
        if (alInquiryViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alInquiryViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alInquiryViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alInquiryViDTO = alInquiryViService.update(alInquiryViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alInquiryViDTO.getId().toString()))
            .body(alInquiryViDTO);
    }

    /**
     * {@code PATCH  /al-inquiry-vis/:id} : Partial updates given fields of an existing alInquiryVi, field will ignore if it is null
     *
     * @param id the id of the alInquiryViDTO to save.
     * @param alInquiryViDTO the alInquiryViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alInquiryViDTO,
     * or with status {@code 400 (Bad Request)} if the alInquiryViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alInquiryViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alInquiryViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlInquiryViDTO> partialUpdateAlInquiryVi(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody AlInquiryViDTO alInquiryViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlInquiryVi partially : {}, {}", id, alInquiryViDTO);
        if (alInquiryViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alInquiryViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alInquiryViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlInquiryViDTO> result = alInquiryViService.partialUpdate(alInquiryViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alInquiryViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-inquiry-vis} : get all the alInquiryVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alInquiryVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlInquiryViDTO>> getAllAlInquiryVis(
        AlInquiryViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlInquiryVis by criteria: {}", criteria);

        Page<AlInquiryViDTO> page = alInquiryViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-inquiry-vis/count} : count all the alInquiryVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlInquiryVis(AlInquiryViCriteria criteria) {
        LOG.debug("REST request to count AlInquiryVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alInquiryViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-inquiry-vis/:id} : get the "id" alInquiryVi.
     *
     * @param id the id of the alInquiryViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alInquiryViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlInquiryViDTO> getAlInquiryVi(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AlInquiryVi : {}", id);
        Optional<AlInquiryViDTO> alInquiryViDTO = alInquiryViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alInquiryViDTO);
    }

    /**
     * {@code DELETE  /al-inquiry-vis/:id} : delete the "id" alInquiryVi.
     *
     * @param id the id of the alInquiryViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlInquiryVi(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AlInquiryVi : {}", id);
        alInquiryViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
