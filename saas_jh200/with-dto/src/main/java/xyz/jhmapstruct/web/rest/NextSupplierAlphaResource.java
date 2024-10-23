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
import xyz.jhmapstruct.repository.NextSupplierAlphaRepository;
import xyz.jhmapstruct.service.NextSupplierAlphaQueryService;
import xyz.jhmapstruct.service.NextSupplierAlphaService;
import xyz.jhmapstruct.service.criteria.NextSupplierAlphaCriteria;
import xyz.jhmapstruct.service.dto.NextSupplierAlphaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextSupplierAlpha}.
 */
@RestController
@RequestMapping("/api/next-supplier-alphas")
public class NextSupplierAlphaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierAlphaResource.class);

    private static final String ENTITY_NAME = "nextSupplierAlpha";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextSupplierAlphaService nextSupplierAlphaService;

    private final NextSupplierAlphaRepository nextSupplierAlphaRepository;

    private final NextSupplierAlphaQueryService nextSupplierAlphaQueryService;

    public NextSupplierAlphaResource(
        NextSupplierAlphaService nextSupplierAlphaService,
        NextSupplierAlphaRepository nextSupplierAlphaRepository,
        NextSupplierAlphaQueryService nextSupplierAlphaQueryService
    ) {
        this.nextSupplierAlphaService = nextSupplierAlphaService;
        this.nextSupplierAlphaRepository = nextSupplierAlphaRepository;
        this.nextSupplierAlphaQueryService = nextSupplierAlphaQueryService;
    }

    /**
     * {@code POST  /next-supplier-alphas} : Create a new nextSupplierAlpha.
     *
     * @param nextSupplierAlphaDTO the nextSupplierAlphaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextSupplierAlphaDTO, or with status {@code 400 (Bad Request)} if the nextSupplierAlpha has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextSupplierAlphaDTO> createNextSupplierAlpha(@Valid @RequestBody NextSupplierAlphaDTO nextSupplierAlphaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextSupplierAlpha : {}", nextSupplierAlphaDTO);
        if (nextSupplierAlphaDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextSupplierAlpha cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextSupplierAlphaDTO = nextSupplierAlphaService.save(nextSupplierAlphaDTO);
        return ResponseEntity.created(new URI("/api/next-supplier-alphas/" + nextSupplierAlphaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextSupplierAlphaDTO.getId().toString()))
            .body(nextSupplierAlphaDTO);
    }

    /**
     * {@code PUT  /next-supplier-alphas/:id} : Updates an existing nextSupplierAlpha.
     *
     * @param id the id of the nextSupplierAlphaDTO to save.
     * @param nextSupplierAlphaDTO the nextSupplierAlphaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextSupplierAlphaDTO,
     * or with status {@code 400 (Bad Request)} if the nextSupplierAlphaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextSupplierAlphaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextSupplierAlphaDTO> updateNextSupplierAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextSupplierAlphaDTO nextSupplierAlphaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextSupplierAlpha : {}, {}", id, nextSupplierAlphaDTO);
        if (nextSupplierAlphaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextSupplierAlphaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextSupplierAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextSupplierAlphaDTO = nextSupplierAlphaService.update(nextSupplierAlphaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextSupplierAlphaDTO.getId().toString()))
            .body(nextSupplierAlphaDTO);
    }

    /**
     * {@code PATCH  /next-supplier-alphas/:id} : Partial updates given fields of an existing nextSupplierAlpha, field will ignore if it is null
     *
     * @param id the id of the nextSupplierAlphaDTO to save.
     * @param nextSupplierAlphaDTO the nextSupplierAlphaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextSupplierAlphaDTO,
     * or with status {@code 400 (Bad Request)} if the nextSupplierAlphaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextSupplierAlphaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextSupplierAlphaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextSupplierAlphaDTO> partialUpdateNextSupplierAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextSupplierAlphaDTO nextSupplierAlphaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextSupplierAlpha partially : {}, {}", id, nextSupplierAlphaDTO);
        if (nextSupplierAlphaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextSupplierAlphaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextSupplierAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextSupplierAlphaDTO> result = nextSupplierAlphaService.partialUpdate(nextSupplierAlphaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextSupplierAlphaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-supplier-alphas} : get all the nextSupplierAlphas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextSupplierAlphas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextSupplierAlphaDTO>> getAllNextSupplierAlphas(
        NextSupplierAlphaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextSupplierAlphas by criteria: {}", criteria);

        Page<NextSupplierAlphaDTO> page = nextSupplierAlphaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-supplier-alphas/count} : count all the nextSupplierAlphas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextSupplierAlphas(NextSupplierAlphaCriteria criteria) {
        LOG.debug("REST request to count NextSupplierAlphas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextSupplierAlphaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-supplier-alphas/:id} : get the "id" nextSupplierAlpha.
     *
     * @param id the id of the nextSupplierAlphaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextSupplierAlphaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextSupplierAlphaDTO> getNextSupplierAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextSupplierAlpha : {}", id);
        Optional<NextSupplierAlphaDTO> nextSupplierAlphaDTO = nextSupplierAlphaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextSupplierAlphaDTO);
    }

    /**
     * {@code DELETE  /next-supplier-alphas/:id} : delete the "id" nextSupplierAlpha.
     *
     * @param id the id of the nextSupplierAlphaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextSupplierAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextSupplierAlpha : {}", id);
        nextSupplierAlphaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
