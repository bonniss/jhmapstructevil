package ai.realworld.web.rest;

import ai.realworld.repository.AlPedroTaxRepository;
import ai.realworld.service.AlPedroTaxQueryService;
import ai.realworld.service.AlPedroTaxService;
import ai.realworld.service.criteria.AlPedroTaxCriteria;
import ai.realworld.service.dto.AlPedroTaxDTO;
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
 * REST controller for managing {@link ai.realworld.domain.AlPedroTax}.
 */
@RestController
@RequestMapping("/api/al-pedro-taxes")
public class AlPedroTaxResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlPedroTaxResource.class);

    private static final String ENTITY_NAME = "alPedroTax";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlPedroTaxService alPedroTaxService;

    private final AlPedroTaxRepository alPedroTaxRepository;

    private final AlPedroTaxQueryService alPedroTaxQueryService;

    public AlPedroTaxResource(
        AlPedroTaxService alPedroTaxService,
        AlPedroTaxRepository alPedroTaxRepository,
        AlPedroTaxQueryService alPedroTaxQueryService
    ) {
        this.alPedroTaxService = alPedroTaxService;
        this.alPedroTaxRepository = alPedroTaxRepository;
        this.alPedroTaxQueryService = alPedroTaxQueryService;
    }

    /**
     * {@code POST  /al-pedro-taxes} : Create a new alPedroTax.
     *
     * @param alPedroTaxDTO the alPedroTaxDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alPedroTaxDTO, or with status {@code 400 (Bad Request)} if the alPedroTax has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlPedroTaxDTO> createAlPedroTax(@Valid @RequestBody AlPedroTaxDTO alPedroTaxDTO) throws URISyntaxException {
        LOG.debug("REST request to save AlPedroTax : {}", alPedroTaxDTO);
        if (alPedroTaxDTO.getId() != null) {
            throw new BadRequestAlertException("A new alPedroTax cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alPedroTaxDTO = alPedroTaxService.save(alPedroTaxDTO);
        return ResponseEntity.created(new URI("/api/al-pedro-taxes/" + alPedroTaxDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alPedroTaxDTO.getId().toString()))
            .body(alPedroTaxDTO);
    }

    /**
     * {@code PUT  /al-pedro-taxes/:id} : Updates an existing alPedroTax.
     *
     * @param id the id of the alPedroTaxDTO to save.
     * @param alPedroTaxDTO the alPedroTaxDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPedroTaxDTO,
     * or with status {@code 400 (Bad Request)} if the alPedroTaxDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alPedroTaxDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlPedroTaxDTO> updateAlPedroTax(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlPedroTaxDTO alPedroTaxDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlPedroTax : {}, {}", id, alPedroTaxDTO);
        if (alPedroTaxDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPedroTaxDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPedroTaxRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alPedroTaxDTO = alPedroTaxService.update(alPedroTaxDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPedroTaxDTO.getId().toString()))
            .body(alPedroTaxDTO);
    }

    /**
     * {@code PATCH  /al-pedro-taxes/:id} : Partial updates given fields of an existing alPedroTax, field will ignore if it is null
     *
     * @param id the id of the alPedroTaxDTO to save.
     * @param alPedroTaxDTO the alPedroTaxDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPedroTaxDTO,
     * or with status {@code 400 (Bad Request)} if the alPedroTaxDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alPedroTaxDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alPedroTaxDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlPedroTaxDTO> partialUpdateAlPedroTax(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlPedroTaxDTO alPedroTaxDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlPedroTax partially : {}, {}", id, alPedroTaxDTO);
        if (alPedroTaxDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPedroTaxDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPedroTaxRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlPedroTaxDTO> result = alPedroTaxService.partialUpdate(alPedroTaxDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPedroTaxDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-pedro-taxes} : get all the alPedroTaxes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alPedroTaxes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlPedroTaxDTO>> getAllAlPedroTaxes(
        AlPedroTaxCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlPedroTaxes by criteria: {}", criteria);

        Page<AlPedroTaxDTO> page = alPedroTaxQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-pedro-taxes/count} : count all the alPedroTaxes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlPedroTaxes(AlPedroTaxCriteria criteria) {
        LOG.debug("REST request to count AlPedroTaxes by criteria: {}", criteria);
        return ResponseEntity.ok().body(alPedroTaxQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-pedro-taxes/:id} : get the "id" alPedroTax.
     *
     * @param id the id of the alPedroTaxDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alPedroTaxDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlPedroTaxDTO> getAlPedroTax(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlPedroTax : {}", id);
        Optional<AlPedroTaxDTO> alPedroTaxDTO = alPedroTaxService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alPedroTaxDTO);
    }

    /**
     * {@code DELETE  /al-pedro-taxes/:id} : delete the "id" alPedroTax.
     *
     * @param id the id of the alPedroTaxDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlPedroTax(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlPedroTax : {}", id);
        alPedroTaxService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
