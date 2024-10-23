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
import xyz.jhmapstruct.repository.NextCustomerAlphaRepository;
import xyz.jhmapstruct.service.NextCustomerAlphaQueryService;
import xyz.jhmapstruct.service.NextCustomerAlphaService;
import xyz.jhmapstruct.service.criteria.NextCustomerAlphaCriteria;
import xyz.jhmapstruct.service.dto.NextCustomerAlphaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextCustomerAlpha}.
 */
@RestController
@RequestMapping("/api/next-customer-alphas")
public class NextCustomerAlphaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerAlphaResource.class);

    private static final String ENTITY_NAME = "nextCustomerAlpha";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextCustomerAlphaService nextCustomerAlphaService;

    private final NextCustomerAlphaRepository nextCustomerAlphaRepository;

    private final NextCustomerAlphaQueryService nextCustomerAlphaQueryService;

    public NextCustomerAlphaResource(
        NextCustomerAlphaService nextCustomerAlphaService,
        NextCustomerAlphaRepository nextCustomerAlphaRepository,
        NextCustomerAlphaQueryService nextCustomerAlphaQueryService
    ) {
        this.nextCustomerAlphaService = nextCustomerAlphaService;
        this.nextCustomerAlphaRepository = nextCustomerAlphaRepository;
        this.nextCustomerAlphaQueryService = nextCustomerAlphaQueryService;
    }

    /**
     * {@code POST  /next-customer-alphas} : Create a new nextCustomerAlpha.
     *
     * @param nextCustomerAlphaDTO the nextCustomerAlphaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextCustomerAlphaDTO, or with status {@code 400 (Bad Request)} if the nextCustomerAlpha has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextCustomerAlphaDTO> createNextCustomerAlpha(@Valid @RequestBody NextCustomerAlphaDTO nextCustomerAlphaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextCustomerAlpha : {}", nextCustomerAlphaDTO);
        if (nextCustomerAlphaDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextCustomerAlpha cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextCustomerAlphaDTO = nextCustomerAlphaService.save(nextCustomerAlphaDTO);
        return ResponseEntity.created(new URI("/api/next-customer-alphas/" + nextCustomerAlphaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextCustomerAlphaDTO.getId().toString()))
            .body(nextCustomerAlphaDTO);
    }

    /**
     * {@code PUT  /next-customer-alphas/:id} : Updates an existing nextCustomerAlpha.
     *
     * @param id the id of the nextCustomerAlphaDTO to save.
     * @param nextCustomerAlphaDTO the nextCustomerAlphaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCustomerAlphaDTO,
     * or with status {@code 400 (Bad Request)} if the nextCustomerAlphaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextCustomerAlphaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextCustomerAlphaDTO> updateNextCustomerAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextCustomerAlphaDTO nextCustomerAlphaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextCustomerAlpha : {}, {}", id, nextCustomerAlphaDTO);
        if (nextCustomerAlphaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCustomerAlphaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCustomerAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextCustomerAlphaDTO = nextCustomerAlphaService.update(nextCustomerAlphaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCustomerAlphaDTO.getId().toString()))
            .body(nextCustomerAlphaDTO);
    }

    /**
     * {@code PATCH  /next-customer-alphas/:id} : Partial updates given fields of an existing nextCustomerAlpha, field will ignore if it is null
     *
     * @param id the id of the nextCustomerAlphaDTO to save.
     * @param nextCustomerAlphaDTO the nextCustomerAlphaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCustomerAlphaDTO,
     * or with status {@code 400 (Bad Request)} if the nextCustomerAlphaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextCustomerAlphaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextCustomerAlphaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextCustomerAlphaDTO> partialUpdateNextCustomerAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextCustomerAlphaDTO nextCustomerAlphaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextCustomerAlpha partially : {}, {}", id, nextCustomerAlphaDTO);
        if (nextCustomerAlphaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCustomerAlphaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCustomerAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextCustomerAlphaDTO> result = nextCustomerAlphaService.partialUpdate(nextCustomerAlphaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCustomerAlphaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-customer-alphas} : get all the nextCustomerAlphas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextCustomerAlphas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextCustomerAlphaDTO>> getAllNextCustomerAlphas(
        NextCustomerAlphaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextCustomerAlphas by criteria: {}", criteria);

        Page<NextCustomerAlphaDTO> page = nextCustomerAlphaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-customer-alphas/count} : count all the nextCustomerAlphas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextCustomerAlphas(NextCustomerAlphaCriteria criteria) {
        LOG.debug("REST request to count NextCustomerAlphas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextCustomerAlphaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-customer-alphas/:id} : get the "id" nextCustomerAlpha.
     *
     * @param id the id of the nextCustomerAlphaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextCustomerAlphaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextCustomerAlphaDTO> getNextCustomerAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextCustomerAlpha : {}", id);
        Optional<NextCustomerAlphaDTO> nextCustomerAlphaDTO = nextCustomerAlphaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextCustomerAlphaDTO);
    }

    /**
     * {@code DELETE  /next-customer-alphas/:id} : delete the "id" nextCustomerAlpha.
     *
     * @param id the id of the nextCustomerAlphaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextCustomerAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextCustomerAlpha : {}", id);
        nextCustomerAlphaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
