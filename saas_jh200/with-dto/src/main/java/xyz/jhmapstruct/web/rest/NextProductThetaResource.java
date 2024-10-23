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
import xyz.jhmapstruct.repository.NextProductThetaRepository;
import xyz.jhmapstruct.service.NextProductThetaQueryService;
import xyz.jhmapstruct.service.NextProductThetaService;
import xyz.jhmapstruct.service.criteria.NextProductThetaCriteria;
import xyz.jhmapstruct.service.dto.NextProductThetaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextProductTheta}.
 */
@RestController
@RequestMapping("/api/next-product-thetas")
public class NextProductThetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductThetaResource.class);

    private static final String ENTITY_NAME = "nextProductTheta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextProductThetaService nextProductThetaService;

    private final NextProductThetaRepository nextProductThetaRepository;

    private final NextProductThetaQueryService nextProductThetaQueryService;

    public NextProductThetaResource(
        NextProductThetaService nextProductThetaService,
        NextProductThetaRepository nextProductThetaRepository,
        NextProductThetaQueryService nextProductThetaQueryService
    ) {
        this.nextProductThetaService = nextProductThetaService;
        this.nextProductThetaRepository = nextProductThetaRepository;
        this.nextProductThetaQueryService = nextProductThetaQueryService;
    }

    /**
     * {@code POST  /next-product-thetas} : Create a new nextProductTheta.
     *
     * @param nextProductThetaDTO the nextProductThetaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextProductThetaDTO, or with status {@code 400 (Bad Request)} if the nextProductTheta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextProductThetaDTO> createNextProductTheta(@Valid @RequestBody NextProductThetaDTO nextProductThetaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextProductTheta : {}", nextProductThetaDTO);
        if (nextProductThetaDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextProductTheta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextProductThetaDTO = nextProductThetaService.save(nextProductThetaDTO);
        return ResponseEntity.created(new URI("/api/next-product-thetas/" + nextProductThetaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextProductThetaDTO.getId().toString()))
            .body(nextProductThetaDTO);
    }

    /**
     * {@code PUT  /next-product-thetas/:id} : Updates an existing nextProductTheta.
     *
     * @param id the id of the nextProductThetaDTO to save.
     * @param nextProductThetaDTO the nextProductThetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextProductThetaDTO,
     * or with status {@code 400 (Bad Request)} if the nextProductThetaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextProductThetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextProductThetaDTO> updateNextProductTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextProductThetaDTO nextProductThetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextProductTheta : {}, {}", id, nextProductThetaDTO);
        if (nextProductThetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextProductThetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextProductThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextProductThetaDTO = nextProductThetaService.update(nextProductThetaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextProductThetaDTO.getId().toString()))
            .body(nextProductThetaDTO);
    }

    /**
     * {@code PATCH  /next-product-thetas/:id} : Partial updates given fields of an existing nextProductTheta, field will ignore if it is null
     *
     * @param id the id of the nextProductThetaDTO to save.
     * @param nextProductThetaDTO the nextProductThetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextProductThetaDTO,
     * or with status {@code 400 (Bad Request)} if the nextProductThetaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextProductThetaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextProductThetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextProductThetaDTO> partialUpdateNextProductTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextProductThetaDTO nextProductThetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextProductTheta partially : {}, {}", id, nextProductThetaDTO);
        if (nextProductThetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextProductThetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextProductThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextProductThetaDTO> result = nextProductThetaService.partialUpdate(nextProductThetaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextProductThetaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-product-thetas} : get all the nextProductThetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextProductThetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextProductThetaDTO>> getAllNextProductThetas(
        NextProductThetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextProductThetas by criteria: {}", criteria);

        Page<NextProductThetaDTO> page = nextProductThetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-product-thetas/count} : count all the nextProductThetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextProductThetas(NextProductThetaCriteria criteria) {
        LOG.debug("REST request to count NextProductThetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextProductThetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-product-thetas/:id} : get the "id" nextProductTheta.
     *
     * @param id the id of the nextProductThetaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextProductThetaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextProductThetaDTO> getNextProductTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextProductTheta : {}", id);
        Optional<NextProductThetaDTO> nextProductThetaDTO = nextProductThetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextProductThetaDTO);
    }

    /**
     * {@code DELETE  /next-product-thetas/:id} : delete the "id" nextProductTheta.
     *
     * @param id the id of the nextProductThetaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextProductTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextProductTheta : {}", id);
        nextProductThetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
