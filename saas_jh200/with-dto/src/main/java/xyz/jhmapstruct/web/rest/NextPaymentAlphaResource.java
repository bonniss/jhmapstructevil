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
import xyz.jhmapstruct.repository.NextPaymentAlphaRepository;
import xyz.jhmapstruct.service.NextPaymentAlphaQueryService;
import xyz.jhmapstruct.service.NextPaymentAlphaService;
import xyz.jhmapstruct.service.criteria.NextPaymentAlphaCriteria;
import xyz.jhmapstruct.service.dto.NextPaymentAlphaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextPaymentAlpha}.
 */
@RestController
@RequestMapping("/api/next-payment-alphas")
public class NextPaymentAlphaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentAlphaResource.class);

    private static final String ENTITY_NAME = "nextPaymentAlpha";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextPaymentAlphaService nextPaymentAlphaService;

    private final NextPaymentAlphaRepository nextPaymentAlphaRepository;

    private final NextPaymentAlphaQueryService nextPaymentAlphaQueryService;

    public NextPaymentAlphaResource(
        NextPaymentAlphaService nextPaymentAlphaService,
        NextPaymentAlphaRepository nextPaymentAlphaRepository,
        NextPaymentAlphaQueryService nextPaymentAlphaQueryService
    ) {
        this.nextPaymentAlphaService = nextPaymentAlphaService;
        this.nextPaymentAlphaRepository = nextPaymentAlphaRepository;
        this.nextPaymentAlphaQueryService = nextPaymentAlphaQueryService;
    }

    /**
     * {@code POST  /next-payment-alphas} : Create a new nextPaymentAlpha.
     *
     * @param nextPaymentAlphaDTO the nextPaymentAlphaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextPaymentAlphaDTO, or with status {@code 400 (Bad Request)} if the nextPaymentAlpha has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextPaymentAlphaDTO> createNextPaymentAlpha(@Valid @RequestBody NextPaymentAlphaDTO nextPaymentAlphaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextPaymentAlpha : {}", nextPaymentAlphaDTO);
        if (nextPaymentAlphaDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextPaymentAlpha cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextPaymentAlphaDTO = nextPaymentAlphaService.save(nextPaymentAlphaDTO);
        return ResponseEntity.created(new URI("/api/next-payment-alphas/" + nextPaymentAlphaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextPaymentAlphaDTO.getId().toString()))
            .body(nextPaymentAlphaDTO);
    }

    /**
     * {@code PUT  /next-payment-alphas/:id} : Updates an existing nextPaymentAlpha.
     *
     * @param id the id of the nextPaymentAlphaDTO to save.
     * @param nextPaymentAlphaDTO the nextPaymentAlphaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextPaymentAlphaDTO,
     * or with status {@code 400 (Bad Request)} if the nextPaymentAlphaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextPaymentAlphaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextPaymentAlphaDTO> updateNextPaymentAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextPaymentAlphaDTO nextPaymentAlphaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextPaymentAlpha : {}, {}", id, nextPaymentAlphaDTO);
        if (nextPaymentAlphaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextPaymentAlphaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextPaymentAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextPaymentAlphaDTO = nextPaymentAlphaService.update(nextPaymentAlphaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextPaymentAlphaDTO.getId().toString()))
            .body(nextPaymentAlphaDTO);
    }

    /**
     * {@code PATCH  /next-payment-alphas/:id} : Partial updates given fields of an existing nextPaymentAlpha, field will ignore if it is null
     *
     * @param id the id of the nextPaymentAlphaDTO to save.
     * @param nextPaymentAlphaDTO the nextPaymentAlphaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextPaymentAlphaDTO,
     * or with status {@code 400 (Bad Request)} if the nextPaymentAlphaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextPaymentAlphaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextPaymentAlphaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextPaymentAlphaDTO> partialUpdateNextPaymentAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextPaymentAlphaDTO nextPaymentAlphaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextPaymentAlpha partially : {}, {}", id, nextPaymentAlphaDTO);
        if (nextPaymentAlphaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextPaymentAlphaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextPaymentAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextPaymentAlphaDTO> result = nextPaymentAlphaService.partialUpdate(nextPaymentAlphaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextPaymentAlphaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-payment-alphas} : get all the nextPaymentAlphas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextPaymentAlphas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextPaymentAlphaDTO>> getAllNextPaymentAlphas(
        NextPaymentAlphaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextPaymentAlphas by criteria: {}", criteria);

        Page<NextPaymentAlphaDTO> page = nextPaymentAlphaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-payment-alphas/count} : count all the nextPaymentAlphas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextPaymentAlphas(NextPaymentAlphaCriteria criteria) {
        LOG.debug("REST request to count NextPaymentAlphas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextPaymentAlphaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-payment-alphas/:id} : get the "id" nextPaymentAlpha.
     *
     * @param id the id of the nextPaymentAlphaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextPaymentAlphaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextPaymentAlphaDTO> getNextPaymentAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextPaymentAlpha : {}", id);
        Optional<NextPaymentAlphaDTO> nextPaymentAlphaDTO = nextPaymentAlphaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextPaymentAlphaDTO);
    }

    /**
     * {@code DELETE  /next-payment-alphas/:id} : delete the "id" nextPaymentAlpha.
     *
     * @param id the id of the nextPaymentAlphaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextPaymentAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextPaymentAlpha : {}", id);
        nextPaymentAlphaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
