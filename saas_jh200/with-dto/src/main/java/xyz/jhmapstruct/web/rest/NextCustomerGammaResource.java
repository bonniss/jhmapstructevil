package xyz.jhmapstruct.web.rest;

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
import xyz.jhmapstruct.repository.NextCustomerGammaRepository;
import xyz.jhmapstruct.service.NextCustomerGammaQueryService;
import xyz.jhmapstruct.service.NextCustomerGammaService;
import xyz.jhmapstruct.service.criteria.NextCustomerGammaCriteria;
import xyz.jhmapstruct.service.dto.NextCustomerGammaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextCustomerGamma}.
 */
@RestController
@RequestMapping("/api/next-customer-gammas")
public class NextCustomerGammaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerGammaResource.class);

    private static final String ENTITY_NAME = "nextCustomerGamma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextCustomerGammaService nextCustomerGammaService;

    private final NextCustomerGammaRepository nextCustomerGammaRepository;

    private final NextCustomerGammaQueryService nextCustomerGammaQueryService;

    public NextCustomerGammaResource(
        NextCustomerGammaService nextCustomerGammaService,
        NextCustomerGammaRepository nextCustomerGammaRepository,
        NextCustomerGammaQueryService nextCustomerGammaQueryService
    ) {
        this.nextCustomerGammaService = nextCustomerGammaService;
        this.nextCustomerGammaRepository = nextCustomerGammaRepository;
        this.nextCustomerGammaQueryService = nextCustomerGammaQueryService;
    }

    /**
     * {@code POST  /next-customer-gammas} : Create a new nextCustomerGamma.
     *
     * @param nextCustomerGammaDTO the nextCustomerGammaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextCustomerGammaDTO, or with status {@code 400 (Bad Request)} if the nextCustomerGamma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextCustomerGammaDTO> createNextCustomerGamma(@Valid @RequestBody NextCustomerGammaDTO nextCustomerGammaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextCustomerGamma : {}", nextCustomerGammaDTO);
        if (nextCustomerGammaDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextCustomerGamma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextCustomerGammaDTO = nextCustomerGammaService.save(nextCustomerGammaDTO);
        return ResponseEntity.created(new URI("/api/next-customer-gammas/" + nextCustomerGammaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextCustomerGammaDTO.getId().toString()))
            .body(nextCustomerGammaDTO);
    }

    /**
     * {@code PUT  /next-customer-gammas/:id} : Updates an existing nextCustomerGamma.
     *
     * @param id the id of the nextCustomerGammaDTO to save.
     * @param nextCustomerGammaDTO the nextCustomerGammaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCustomerGammaDTO,
     * or with status {@code 400 (Bad Request)} if the nextCustomerGammaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextCustomerGammaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextCustomerGammaDTO> updateNextCustomerGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextCustomerGammaDTO nextCustomerGammaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextCustomerGamma : {}, {}", id, nextCustomerGammaDTO);
        if (nextCustomerGammaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCustomerGammaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCustomerGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextCustomerGammaDTO = nextCustomerGammaService.update(nextCustomerGammaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCustomerGammaDTO.getId().toString()))
            .body(nextCustomerGammaDTO);
    }

    /**
     * {@code PATCH  /next-customer-gammas/:id} : Partial updates given fields of an existing nextCustomerGamma, field will ignore if it is null
     *
     * @param id the id of the nextCustomerGammaDTO to save.
     * @param nextCustomerGammaDTO the nextCustomerGammaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCustomerGammaDTO,
     * or with status {@code 400 (Bad Request)} if the nextCustomerGammaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextCustomerGammaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextCustomerGammaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextCustomerGammaDTO> partialUpdateNextCustomerGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextCustomerGammaDTO nextCustomerGammaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextCustomerGamma partially : {}, {}", id, nextCustomerGammaDTO);
        if (nextCustomerGammaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCustomerGammaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCustomerGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextCustomerGammaDTO> result = nextCustomerGammaService.partialUpdate(nextCustomerGammaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCustomerGammaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-customer-gammas} : get all the nextCustomerGammas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextCustomerGammas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextCustomerGammaDTO>> getAllNextCustomerGammas(
        NextCustomerGammaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextCustomerGammas by criteria: {}", criteria);

        Page<NextCustomerGammaDTO> page = nextCustomerGammaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-customer-gammas/count} : count all the nextCustomerGammas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextCustomerGammas(NextCustomerGammaCriteria criteria) {
        LOG.debug("REST request to count NextCustomerGammas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextCustomerGammaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-customer-gammas/:id} : get the "id" nextCustomerGamma.
     *
     * @param id the id of the nextCustomerGammaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextCustomerGammaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextCustomerGammaDTO> getNextCustomerGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextCustomerGamma : {}", id);
        Optional<NextCustomerGammaDTO> nextCustomerGammaDTO = nextCustomerGammaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextCustomerGammaDTO);
    }

    /**
     * {@code DELETE  /next-customer-gammas/:id} : delete the "id" nextCustomerGamma.
     *
     * @param id the id of the nextCustomerGammaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextCustomerGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextCustomerGamma : {}", id);
        nextCustomerGammaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
