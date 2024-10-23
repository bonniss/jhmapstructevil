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
import xyz.jhmapstruct.repository.NextCustomerThetaRepository;
import xyz.jhmapstruct.service.NextCustomerThetaQueryService;
import xyz.jhmapstruct.service.NextCustomerThetaService;
import xyz.jhmapstruct.service.criteria.NextCustomerThetaCriteria;
import xyz.jhmapstruct.service.dto.NextCustomerThetaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextCustomerTheta}.
 */
@RestController
@RequestMapping("/api/next-customer-thetas")
public class NextCustomerThetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerThetaResource.class);

    private static final String ENTITY_NAME = "nextCustomerTheta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextCustomerThetaService nextCustomerThetaService;

    private final NextCustomerThetaRepository nextCustomerThetaRepository;

    private final NextCustomerThetaQueryService nextCustomerThetaQueryService;

    public NextCustomerThetaResource(
        NextCustomerThetaService nextCustomerThetaService,
        NextCustomerThetaRepository nextCustomerThetaRepository,
        NextCustomerThetaQueryService nextCustomerThetaQueryService
    ) {
        this.nextCustomerThetaService = nextCustomerThetaService;
        this.nextCustomerThetaRepository = nextCustomerThetaRepository;
        this.nextCustomerThetaQueryService = nextCustomerThetaQueryService;
    }

    /**
     * {@code POST  /next-customer-thetas} : Create a new nextCustomerTheta.
     *
     * @param nextCustomerThetaDTO the nextCustomerThetaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextCustomerThetaDTO, or with status {@code 400 (Bad Request)} if the nextCustomerTheta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextCustomerThetaDTO> createNextCustomerTheta(@Valid @RequestBody NextCustomerThetaDTO nextCustomerThetaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextCustomerTheta : {}", nextCustomerThetaDTO);
        if (nextCustomerThetaDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextCustomerTheta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextCustomerThetaDTO = nextCustomerThetaService.save(nextCustomerThetaDTO);
        return ResponseEntity.created(new URI("/api/next-customer-thetas/" + nextCustomerThetaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextCustomerThetaDTO.getId().toString()))
            .body(nextCustomerThetaDTO);
    }

    /**
     * {@code PUT  /next-customer-thetas/:id} : Updates an existing nextCustomerTheta.
     *
     * @param id the id of the nextCustomerThetaDTO to save.
     * @param nextCustomerThetaDTO the nextCustomerThetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCustomerThetaDTO,
     * or with status {@code 400 (Bad Request)} if the nextCustomerThetaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextCustomerThetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextCustomerThetaDTO> updateNextCustomerTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextCustomerThetaDTO nextCustomerThetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextCustomerTheta : {}, {}", id, nextCustomerThetaDTO);
        if (nextCustomerThetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCustomerThetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCustomerThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextCustomerThetaDTO = nextCustomerThetaService.update(nextCustomerThetaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCustomerThetaDTO.getId().toString()))
            .body(nextCustomerThetaDTO);
    }

    /**
     * {@code PATCH  /next-customer-thetas/:id} : Partial updates given fields of an existing nextCustomerTheta, field will ignore if it is null
     *
     * @param id the id of the nextCustomerThetaDTO to save.
     * @param nextCustomerThetaDTO the nextCustomerThetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCustomerThetaDTO,
     * or with status {@code 400 (Bad Request)} if the nextCustomerThetaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextCustomerThetaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextCustomerThetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextCustomerThetaDTO> partialUpdateNextCustomerTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextCustomerThetaDTO nextCustomerThetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextCustomerTheta partially : {}, {}", id, nextCustomerThetaDTO);
        if (nextCustomerThetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCustomerThetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCustomerThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextCustomerThetaDTO> result = nextCustomerThetaService.partialUpdate(nextCustomerThetaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCustomerThetaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-customer-thetas} : get all the nextCustomerThetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextCustomerThetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextCustomerThetaDTO>> getAllNextCustomerThetas(
        NextCustomerThetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextCustomerThetas by criteria: {}", criteria);

        Page<NextCustomerThetaDTO> page = nextCustomerThetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-customer-thetas/count} : count all the nextCustomerThetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextCustomerThetas(NextCustomerThetaCriteria criteria) {
        LOG.debug("REST request to count NextCustomerThetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextCustomerThetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-customer-thetas/:id} : get the "id" nextCustomerTheta.
     *
     * @param id the id of the nextCustomerThetaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextCustomerThetaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextCustomerThetaDTO> getNextCustomerTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextCustomerTheta : {}", id);
        Optional<NextCustomerThetaDTO> nextCustomerThetaDTO = nextCustomerThetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextCustomerThetaDTO);
    }

    /**
     * {@code DELETE  /next-customer-thetas/:id} : delete the "id" nextCustomerTheta.
     *
     * @param id the id of the nextCustomerThetaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextCustomerTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextCustomerTheta : {}", id);
        nextCustomerThetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
