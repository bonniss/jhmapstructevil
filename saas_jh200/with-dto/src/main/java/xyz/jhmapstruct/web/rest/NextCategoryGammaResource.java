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
import xyz.jhmapstruct.repository.NextCategoryGammaRepository;
import xyz.jhmapstruct.service.NextCategoryGammaQueryService;
import xyz.jhmapstruct.service.NextCategoryGammaService;
import xyz.jhmapstruct.service.criteria.NextCategoryGammaCriteria;
import xyz.jhmapstruct.service.dto.NextCategoryGammaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextCategoryGamma}.
 */
@RestController
@RequestMapping("/api/next-category-gammas")
public class NextCategoryGammaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryGammaResource.class);

    private static final String ENTITY_NAME = "nextCategoryGamma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextCategoryGammaService nextCategoryGammaService;

    private final NextCategoryGammaRepository nextCategoryGammaRepository;

    private final NextCategoryGammaQueryService nextCategoryGammaQueryService;

    public NextCategoryGammaResource(
        NextCategoryGammaService nextCategoryGammaService,
        NextCategoryGammaRepository nextCategoryGammaRepository,
        NextCategoryGammaQueryService nextCategoryGammaQueryService
    ) {
        this.nextCategoryGammaService = nextCategoryGammaService;
        this.nextCategoryGammaRepository = nextCategoryGammaRepository;
        this.nextCategoryGammaQueryService = nextCategoryGammaQueryService;
    }

    /**
     * {@code POST  /next-category-gammas} : Create a new nextCategoryGamma.
     *
     * @param nextCategoryGammaDTO the nextCategoryGammaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextCategoryGammaDTO, or with status {@code 400 (Bad Request)} if the nextCategoryGamma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextCategoryGammaDTO> createNextCategoryGamma(@Valid @RequestBody NextCategoryGammaDTO nextCategoryGammaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextCategoryGamma : {}", nextCategoryGammaDTO);
        if (nextCategoryGammaDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextCategoryGamma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextCategoryGammaDTO = nextCategoryGammaService.save(nextCategoryGammaDTO);
        return ResponseEntity.created(new URI("/api/next-category-gammas/" + nextCategoryGammaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextCategoryGammaDTO.getId().toString()))
            .body(nextCategoryGammaDTO);
    }

    /**
     * {@code PUT  /next-category-gammas/:id} : Updates an existing nextCategoryGamma.
     *
     * @param id the id of the nextCategoryGammaDTO to save.
     * @param nextCategoryGammaDTO the nextCategoryGammaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCategoryGammaDTO,
     * or with status {@code 400 (Bad Request)} if the nextCategoryGammaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextCategoryGammaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextCategoryGammaDTO> updateNextCategoryGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextCategoryGammaDTO nextCategoryGammaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextCategoryGamma : {}, {}", id, nextCategoryGammaDTO);
        if (nextCategoryGammaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCategoryGammaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCategoryGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextCategoryGammaDTO = nextCategoryGammaService.update(nextCategoryGammaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCategoryGammaDTO.getId().toString()))
            .body(nextCategoryGammaDTO);
    }

    /**
     * {@code PATCH  /next-category-gammas/:id} : Partial updates given fields of an existing nextCategoryGamma, field will ignore if it is null
     *
     * @param id the id of the nextCategoryGammaDTO to save.
     * @param nextCategoryGammaDTO the nextCategoryGammaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCategoryGammaDTO,
     * or with status {@code 400 (Bad Request)} if the nextCategoryGammaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextCategoryGammaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextCategoryGammaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextCategoryGammaDTO> partialUpdateNextCategoryGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextCategoryGammaDTO nextCategoryGammaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextCategoryGamma partially : {}, {}", id, nextCategoryGammaDTO);
        if (nextCategoryGammaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCategoryGammaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCategoryGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextCategoryGammaDTO> result = nextCategoryGammaService.partialUpdate(nextCategoryGammaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCategoryGammaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-category-gammas} : get all the nextCategoryGammas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextCategoryGammas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextCategoryGammaDTO>> getAllNextCategoryGammas(
        NextCategoryGammaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextCategoryGammas by criteria: {}", criteria);

        Page<NextCategoryGammaDTO> page = nextCategoryGammaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-category-gammas/count} : count all the nextCategoryGammas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextCategoryGammas(NextCategoryGammaCriteria criteria) {
        LOG.debug("REST request to count NextCategoryGammas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextCategoryGammaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-category-gammas/:id} : get the "id" nextCategoryGamma.
     *
     * @param id the id of the nextCategoryGammaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextCategoryGammaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextCategoryGammaDTO> getNextCategoryGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextCategoryGamma : {}", id);
        Optional<NextCategoryGammaDTO> nextCategoryGammaDTO = nextCategoryGammaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextCategoryGammaDTO);
    }

    /**
     * {@code DELETE  /next-category-gammas/:id} : delete the "id" nextCategoryGamma.
     *
     * @param id the id of the nextCategoryGammaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextCategoryGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextCategoryGamma : {}", id);
        nextCategoryGammaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
