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
import xyz.jhmapstruct.repository.NextCustomerMiMiRepository;
import xyz.jhmapstruct.service.NextCustomerMiMiQueryService;
import xyz.jhmapstruct.service.NextCustomerMiMiService;
import xyz.jhmapstruct.service.criteria.NextCustomerMiMiCriteria;
import xyz.jhmapstruct.service.dto.NextCustomerMiMiDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextCustomerMiMi}.
 */
@RestController
@RequestMapping("/api/next-customer-mi-mis")
public class NextCustomerMiMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerMiMiResource.class);

    private static final String ENTITY_NAME = "nextCustomerMiMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextCustomerMiMiService nextCustomerMiMiService;

    private final NextCustomerMiMiRepository nextCustomerMiMiRepository;

    private final NextCustomerMiMiQueryService nextCustomerMiMiQueryService;

    public NextCustomerMiMiResource(
        NextCustomerMiMiService nextCustomerMiMiService,
        NextCustomerMiMiRepository nextCustomerMiMiRepository,
        NextCustomerMiMiQueryService nextCustomerMiMiQueryService
    ) {
        this.nextCustomerMiMiService = nextCustomerMiMiService;
        this.nextCustomerMiMiRepository = nextCustomerMiMiRepository;
        this.nextCustomerMiMiQueryService = nextCustomerMiMiQueryService;
    }

    /**
     * {@code POST  /next-customer-mi-mis} : Create a new nextCustomerMiMi.
     *
     * @param nextCustomerMiMiDTO the nextCustomerMiMiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextCustomerMiMiDTO, or with status {@code 400 (Bad Request)} if the nextCustomerMiMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextCustomerMiMiDTO> createNextCustomerMiMi(@Valid @RequestBody NextCustomerMiMiDTO nextCustomerMiMiDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextCustomerMiMi : {}", nextCustomerMiMiDTO);
        if (nextCustomerMiMiDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextCustomerMiMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextCustomerMiMiDTO = nextCustomerMiMiService.save(nextCustomerMiMiDTO);
        return ResponseEntity.created(new URI("/api/next-customer-mi-mis/" + nextCustomerMiMiDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextCustomerMiMiDTO.getId().toString()))
            .body(nextCustomerMiMiDTO);
    }

    /**
     * {@code PUT  /next-customer-mi-mis/:id} : Updates an existing nextCustomerMiMi.
     *
     * @param id the id of the nextCustomerMiMiDTO to save.
     * @param nextCustomerMiMiDTO the nextCustomerMiMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCustomerMiMiDTO,
     * or with status {@code 400 (Bad Request)} if the nextCustomerMiMiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextCustomerMiMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextCustomerMiMiDTO> updateNextCustomerMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextCustomerMiMiDTO nextCustomerMiMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextCustomerMiMi : {}, {}", id, nextCustomerMiMiDTO);
        if (nextCustomerMiMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCustomerMiMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCustomerMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextCustomerMiMiDTO = nextCustomerMiMiService.update(nextCustomerMiMiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCustomerMiMiDTO.getId().toString()))
            .body(nextCustomerMiMiDTO);
    }

    /**
     * {@code PATCH  /next-customer-mi-mis/:id} : Partial updates given fields of an existing nextCustomerMiMi, field will ignore if it is null
     *
     * @param id the id of the nextCustomerMiMiDTO to save.
     * @param nextCustomerMiMiDTO the nextCustomerMiMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCustomerMiMiDTO,
     * or with status {@code 400 (Bad Request)} if the nextCustomerMiMiDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextCustomerMiMiDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextCustomerMiMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextCustomerMiMiDTO> partialUpdateNextCustomerMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextCustomerMiMiDTO nextCustomerMiMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextCustomerMiMi partially : {}, {}", id, nextCustomerMiMiDTO);
        if (nextCustomerMiMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCustomerMiMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCustomerMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextCustomerMiMiDTO> result = nextCustomerMiMiService.partialUpdate(nextCustomerMiMiDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCustomerMiMiDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-customer-mi-mis} : get all the nextCustomerMiMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextCustomerMiMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextCustomerMiMiDTO>> getAllNextCustomerMiMis(
        NextCustomerMiMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextCustomerMiMis by criteria: {}", criteria);

        Page<NextCustomerMiMiDTO> page = nextCustomerMiMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-customer-mi-mis/count} : count all the nextCustomerMiMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextCustomerMiMis(NextCustomerMiMiCriteria criteria) {
        LOG.debug("REST request to count NextCustomerMiMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextCustomerMiMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-customer-mi-mis/:id} : get the "id" nextCustomerMiMi.
     *
     * @param id the id of the nextCustomerMiMiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextCustomerMiMiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextCustomerMiMiDTO> getNextCustomerMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextCustomerMiMi : {}", id);
        Optional<NextCustomerMiMiDTO> nextCustomerMiMiDTO = nextCustomerMiMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextCustomerMiMiDTO);
    }

    /**
     * {@code DELETE  /next-customer-mi-mis/:id} : delete the "id" nextCustomerMiMi.
     *
     * @param id the id of the nextCustomerMiMiDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextCustomerMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextCustomerMiMi : {}", id);
        nextCustomerMiMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
