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
import xyz.jhmapstruct.repository.NextProductGammaRepository;
import xyz.jhmapstruct.service.NextProductGammaQueryService;
import xyz.jhmapstruct.service.NextProductGammaService;
import xyz.jhmapstruct.service.criteria.NextProductGammaCriteria;
import xyz.jhmapstruct.service.dto.NextProductGammaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextProductGamma}.
 */
@RestController
@RequestMapping("/api/next-product-gammas")
public class NextProductGammaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductGammaResource.class);

    private static final String ENTITY_NAME = "nextProductGamma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextProductGammaService nextProductGammaService;

    private final NextProductGammaRepository nextProductGammaRepository;

    private final NextProductGammaQueryService nextProductGammaQueryService;

    public NextProductGammaResource(
        NextProductGammaService nextProductGammaService,
        NextProductGammaRepository nextProductGammaRepository,
        NextProductGammaQueryService nextProductGammaQueryService
    ) {
        this.nextProductGammaService = nextProductGammaService;
        this.nextProductGammaRepository = nextProductGammaRepository;
        this.nextProductGammaQueryService = nextProductGammaQueryService;
    }

    /**
     * {@code POST  /next-product-gammas} : Create a new nextProductGamma.
     *
     * @param nextProductGammaDTO the nextProductGammaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextProductGammaDTO, or with status {@code 400 (Bad Request)} if the nextProductGamma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextProductGammaDTO> createNextProductGamma(@Valid @RequestBody NextProductGammaDTO nextProductGammaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextProductGamma : {}", nextProductGammaDTO);
        if (nextProductGammaDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextProductGamma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextProductGammaDTO = nextProductGammaService.save(nextProductGammaDTO);
        return ResponseEntity.created(new URI("/api/next-product-gammas/" + nextProductGammaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextProductGammaDTO.getId().toString()))
            .body(nextProductGammaDTO);
    }

    /**
     * {@code PUT  /next-product-gammas/:id} : Updates an existing nextProductGamma.
     *
     * @param id the id of the nextProductGammaDTO to save.
     * @param nextProductGammaDTO the nextProductGammaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextProductGammaDTO,
     * or with status {@code 400 (Bad Request)} if the nextProductGammaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextProductGammaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextProductGammaDTO> updateNextProductGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextProductGammaDTO nextProductGammaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextProductGamma : {}, {}", id, nextProductGammaDTO);
        if (nextProductGammaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextProductGammaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextProductGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextProductGammaDTO = nextProductGammaService.update(nextProductGammaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextProductGammaDTO.getId().toString()))
            .body(nextProductGammaDTO);
    }

    /**
     * {@code PATCH  /next-product-gammas/:id} : Partial updates given fields of an existing nextProductGamma, field will ignore if it is null
     *
     * @param id the id of the nextProductGammaDTO to save.
     * @param nextProductGammaDTO the nextProductGammaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextProductGammaDTO,
     * or with status {@code 400 (Bad Request)} if the nextProductGammaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextProductGammaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextProductGammaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextProductGammaDTO> partialUpdateNextProductGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextProductGammaDTO nextProductGammaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextProductGamma partially : {}, {}", id, nextProductGammaDTO);
        if (nextProductGammaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextProductGammaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextProductGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextProductGammaDTO> result = nextProductGammaService.partialUpdate(nextProductGammaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextProductGammaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-product-gammas} : get all the nextProductGammas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextProductGammas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextProductGammaDTO>> getAllNextProductGammas(
        NextProductGammaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextProductGammas by criteria: {}", criteria);

        Page<NextProductGammaDTO> page = nextProductGammaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-product-gammas/count} : count all the nextProductGammas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextProductGammas(NextProductGammaCriteria criteria) {
        LOG.debug("REST request to count NextProductGammas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextProductGammaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-product-gammas/:id} : get the "id" nextProductGamma.
     *
     * @param id the id of the nextProductGammaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextProductGammaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextProductGammaDTO> getNextProductGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextProductGamma : {}", id);
        Optional<NextProductGammaDTO> nextProductGammaDTO = nextProductGammaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextProductGammaDTO);
    }

    /**
     * {@code DELETE  /next-product-gammas/:id} : delete the "id" nextProductGamma.
     *
     * @param id the id of the nextProductGammaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextProductGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextProductGamma : {}", id);
        nextProductGammaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
