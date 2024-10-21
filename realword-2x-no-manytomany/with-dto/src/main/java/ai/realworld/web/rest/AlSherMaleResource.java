package ai.realworld.web.rest;

import ai.realworld.repository.AlSherMaleRepository;
import ai.realworld.service.AlSherMaleQueryService;
import ai.realworld.service.AlSherMaleService;
import ai.realworld.service.criteria.AlSherMaleCriteria;
import ai.realworld.service.dto.AlSherMaleDTO;
import ai.realworld.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link ai.realworld.domain.AlSherMale}.
 */
@RestController
@RequestMapping("/api/al-sher-males")
public class AlSherMaleResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlSherMaleResource.class);

    private static final String ENTITY_NAME = "alSherMale";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlSherMaleService alSherMaleService;

    private final AlSherMaleRepository alSherMaleRepository;

    private final AlSherMaleQueryService alSherMaleQueryService;

    public AlSherMaleResource(
        AlSherMaleService alSherMaleService,
        AlSherMaleRepository alSherMaleRepository,
        AlSherMaleQueryService alSherMaleQueryService
    ) {
        this.alSherMaleService = alSherMaleService;
        this.alSherMaleRepository = alSherMaleRepository;
        this.alSherMaleQueryService = alSherMaleQueryService;
    }

    /**
     * {@code POST  /al-sher-males} : Create a new alSherMale.
     *
     * @param alSherMaleDTO the alSherMaleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alSherMaleDTO, or with status {@code 400 (Bad Request)} if the alSherMale has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlSherMaleDTO> createAlSherMale(@RequestBody AlSherMaleDTO alSherMaleDTO) throws URISyntaxException {
        LOG.debug("REST request to save AlSherMale : {}", alSherMaleDTO);
        if (alSherMaleDTO.getId() != null) {
            throw new BadRequestAlertException("A new alSherMale cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alSherMaleDTO = alSherMaleService.save(alSherMaleDTO);
        return ResponseEntity.created(new URI("/api/al-sher-males/" + alSherMaleDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alSherMaleDTO.getId().toString()))
            .body(alSherMaleDTO);
    }

    /**
     * {@code PUT  /al-sher-males/:id} : Updates an existing alSherMale.
     *
     * @param id the id of the alSherMaleDTO to save.
     * @param alSherMaleDTO the alSherMaleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alSherMaleDTO,
     * or with status {@code 400 (Bad Request)} if the alSherMaleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alSherMaleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlSherMaleDTO> updateAlSherMale(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AlSherMaleDTO alSherMaleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlSherMale : {}, {}", id, alSherMaleDTO);
        if (alSherMaleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alSherMaleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alSherMaleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alSherMaleDTO = alSherMaleService.update(alSherMaleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alSherMaleDTO.getId().toString()))
            .body(alSherMaleDTO);
    }

    /**
     * {@code PATCH  /al-sher-males/:id} : Partial updates given fields of an existing alSherMale, field will ignore if it is null
     *
     * @param id the id of the alSherMaleDTO to save.
     * @param alSherMaleDTO the alSherMaleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alSherMaleDTO,
     * or with status {@code 400 (Bad Request)} if the alSherMaleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alSherMaleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alSherMaleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlSherMaleDTO> partialUpdateAlSherMale(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AlSherMaleDTO alSherMaleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlSherMale partially : {}, {}", id, alSherMaleDTO);
        if (alSherMaleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alSherMaleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alSherMaleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlSherMaleDTO> result = alSherMaleService.partialUpdate(alSherMaleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alSherMaleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-sher-males} : get all the alSherMales.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alSherMales in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlSherMaleDTO>> getAllAlSherMales(
        AlSherMaleCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlSherMales by criteria: {}", criteria);

        Page<AlSherMaleDTO> page = alSherMaleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-sher-males/count} : count all the alSherMales.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlSherMales(AlSherMaleCriteria criteria) {
        LOG.debug("REST request to count AlSherMales by criteria: {}", criteria);
        return ResponseEntity.ok().body(alSherMaleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-sher-males/:id} : get the "id" alSherMale.
     *
     * @param id the id of the alSherMaleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alSherMaleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlSherMaleDTO> getAlSherMale(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlSherMale : {}", id);
        Optional<AlSherMaleDTO> alSherMaleDTO = alSherMaleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alSherMaleDTO);
    }

    /**
     * {@code DELETE  /al-sher-males/:id} : delete the "id" alSherMale.
     *
     * @param id the id of the alSherMaleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlSherMale(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlSherMale : {}", id);
        alSherMaleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}