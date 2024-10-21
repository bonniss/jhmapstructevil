package ai.realworld.web.rest;

import ai.realworld.repository.HashRossViRepository;
import ai.realworld.service.HashRossViQueryService;
import ai.realworld.service.HashRossViService;
import ai.realworld.service.criteria.HashRossViCriteria;
import ai.realworld.service.dto.HashRossViDTO;
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
 * REST controller for managing {@link ai.realworld.domain.HashRossVi}.
 */
@RestController
@RequestMapping("/api/hash-ross-vis")
public class HashRossViResource {

    private static final Logger LOG = LoggerFactory.getLogger(HashRossViResource.class);

    private static final String ENTITY_NAME = "hashRossVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HashRossViService hashRossViService;

    private final HashRossViRepository hashRossViRepository;

    private final HashRossViQueryService hashRossViQueryService;

    public HashRossViResource(
        HashRossViService hashRossViService,
        HashRossViRepository hashRossViRepository,
        HashRossViQueryService hashRossViQueryService
    ) {
        this.hashRossViService = hashRossViService;
        this.hashRossViRepository = hashRossViRepository;
        this.hashRossViQueryService = hashRossViQueryService;
    }

    /**
     * {@code POST  /hash-ross-vis} : Create a new hashRossVi.
     *
     * @param hashRossViDTO the hashRossViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hashRossViDTO, or with status {@code 400 (Bad Request)} if the hashRossVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HashRossViDTO> createHashRossVi(@Valid @RequestBody HashRossViDTO hashRossViDTO) throws URISyntaxException {
        LOG.debug("REST request to save HashRossVi : {}", hashRossViDTO);
        if (hashRossViDTO.getId() != null) {
            throw new BadRequestAlertException("A new hashRossVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        hashRossViDTO = hashRossViService.save(hashRossViDTO);
        return ResponseEntity.created(new URI("/api/hash-ross-vis/" + hashRossViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, hashRossViDTO.getId().toString()))
            .body(hashRossViDTO);
    }

    /**
     * {@code PUT  /hash-ross-vis/:id} : Updates an existing hashRossVi.
     *
     * @param id the id of the hashRossViDTO to save.
     * @param hashRossViDTO the hashRossViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hashRossViDTO,
     * or with status {@code 400 (Bad Request)} if the hashRossViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hashRossViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HashRossViDTO> updateHashRossVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HashRossViDTO hashRossViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update HashRossVi : {}, {}", id, hashRossViDTO);
        if (hashRossViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hashRossViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hashRossViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        hashRossViDTO = hashRossViService.update(hashRossViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hashRossViDTO.getId().toString()))
            .body(hashRossViDTO);
    }

    /**
     * {@code PATCH  /hash-ross-vis/:id} : Partial updates given fields of an existing hashRossVi, field will ignore if it is null
     *
     * @param id the id of the hashRossViDTO to save.
     * @param hashRossViDTO the hashRossViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hashRossViDTO,
     * or with status {@code 400 (Bad Request)} if the hashRossViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the hashRossViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the hashRossViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HashRossViDTO> partialUpdateHashRossVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HashRossViDTO hashRossViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update HashRossVi partially : {}, {}", id, hashRossViDTO);
        if (hashRossViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hashRossViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hashRossViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HashRossViDTO> result = hashRossViService.partialUpdate(hashRossViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hashRossViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /hash-ross-vis} : get all the hashRossVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hashRossVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HashRossViDTO>> getAllHashRossVis(
        HashRossViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get HashRossVis by criteria: {}", criteria);

        Page<HashRossViDTO> page = hashRossViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hash-ross-vis/count} : count all the hashRossVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countHashRossVis(HashRossViCriteria criteria) {
        LOG.debug("REST request to count HashRossVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(hashRossViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /hash-ross-vis/:id} : get the "id" hashRossVi.
     *
     * @param id the id of the hashRossViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hashRossViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HashRossViDTO> getHashRossVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get HashRossVi : {}", id);
        Optional<HashRossViDTO> hashRossViDTO = hashRossViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hashRossViDTO);
    }

    /**
     * {@code DELETE  /hash-ross-vis/:id} : delete the "id" hashRossVi.
     *
     * @param id the id of the hashRossViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHashRossVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete HashRossVi : {}", id);
        hashRossViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
