package ai.realworld.web.rest;

import ai.realworld.repository.AlPacinoVoucherRepository;
import ai.realworld.service.AlPacinoVoucherQueryService;
import ai.realworld.service.AlPacinoVoucherService;
import ai.realworld.service.criteria.AlPacinoVoucherCriteria;
import ai.realworld.service.dto.AlPacinoVoucherDTO;
import ai.realworld.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
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
 * REST controller for managing {@link ai.realworld.domain.AlPacinoVoucher}.
 */
@RestController
@RequestMapping("/api/al-pacino-vouchers")
public class AlPacinoVoucherResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlPacinoVoucherResource.class);

    private static final String ENTITY_NAME = "alPacinoVoucher";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlPacinoVoucherService alPacinoVoucherService;

    private final AlPacinoVoucherRepository alPacinoVoucherRepository;

    private final AlPacinoVoucherQueryService alPacinoVoucherQueryService;

    public AlPacinoVoucherResource(
        AlPacinoVoucherService alPacinoVoucherService,
        AlPacinoVoucherRepository alPacinoVoucherRepository,
        AlPacinoVoucherQueryService alPacinoVoucherQueryService
    ) {
        this.alPacinoVoucherService = alPacinoVoucherService;
        this.alPacinoVoucherRepository = alPacinoVoucherRepository;
        this.alPacinoVoucherQueryService = alPacinoVoucherQueryService;
    }

    /**
     * {@code POST  /al-pacino-vouchers} : Create a new alPacinoVoucher.
     *
     * @param alPacinoVoucherDTO the alPacinoVoucherDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alPacinoVoucherDTO, or with status {@code 400 (Bad Request)} if the alPacinoVoucher has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlPacinoVoucherDTO> createAlPacinoVoucher(@RequestBody AlPacinoVoucherDTO alPacinoVoucherDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AlPacinoVoucher : {}", alPacinoVoucherDTO);
        if (alPacinoVoucherDTO.getId() != null) {
            throw new BadRequestAlertException("A new alPacinoVoucher cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alPacinoVoucherDTO = alPacinoVoucherService.save(alPacinoVoucherDTO);
        return ResponseEntity.created(new URI("/api/al-pacino-vouchers/" + alPacinoVoucherDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alPacinoVoucherDTO.getId().toString()))
            .body(alPacinoVoucherDTO);
    }

    /**
     * {@code PUT  /al-pacino-vouchers/:id} : Updates an existing alPacinoVoucher.
     *
     * @param id the id of the alPacinoVoucherDTO to save.
     * @param alPacinoVoucherDTO the alPacinoVoucherDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPacinoVoucherDTO,
     * or with status {@code 400 (Bad Request)} if the alPacinoVoucherDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alPacinoVoucherDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlPacinoVoucherDTO> updateAlPacinoVoucher(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody AlPacinoVoucherDTO alPacinoVoucherDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlPacinoVoucher : {}, {}", id, alPacinoVoucherDTO);
        if (alPacinoVoucherDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPacinoVoucherDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPacinoVoucherRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alPacinoVoucherDTO = alPacinoVoucherService.update(alPacinoVoucherDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPacinoVoucherDTO.getId().toString()))
            .body(alPacinoVoucherDTO);
    }

    /**
     * {@code PATCH  /al-pacino-vouchers/:id} : Partial updates given fields of an existing alPacinoVoucher, field will ignore if it is null
     *
     * @param id the id of the alPacinoVoucherDTO to save.
     * @param alPacinoVoucherDTO the alPacinoVoucherDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPacinoVoucherDTO,
     * or with status {@code 400 (Bad Request)} if the alPacinoVoucherDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alPacinoVoucherDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alPacinoVoucherDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlPacinoVoucherDTO> partialUpdateAlPacinoVoucher(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody AlPacinoVoucherDTO alPacinoVoucherDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlPacinoVoucher partially : {}, {}", id, alPacinoVoucherDTO);
        if (alPacinoVoucherDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPacinoVoucherDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPacinoVoucherRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlPacinoVoucherDTO> result = alPacinoVoucherService.partialUpdate(alPacinoVoucherDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPacinoVoucherDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-pacino-vouchers} : get all the alPacinoVouchers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alPacinoVouchers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlPacinoVoucherDTO>> getAllAlPacinoVouchers(
        AlPacinoVoucherCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlPacinoVouchers by criteria: {}", criteria);

        Page<AlPacinoVoucherDTO> page = alPacinoVoucherQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-pacino-vouchers/count} : count all the alPacinoVouchers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlPacinoVouchers(AlPacinoVoucherCriteria criteria) {
        LOG.debug("REST request to count AlPacinoVouchers by criteria: {}", criteria);
        return ResponseEntity.ok().body(alPacinoVoucherQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-pacino-vouchers/:id} : get the "id" alPacinoVoucher.
     *
     * @param id the id of the alPacinoVoucherDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alPacinoVoucherDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlPacinoVoucherDTO> getAlPacinoVoucher(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AlPacinoVoucher : {}", id);
        Optional<AlPacinoVoucherDTO> alPacinoVoucherDTO = alPacinoVoucherService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alPacinoVoucherDTO);
    }

    /**
     * {@code DELETE  /al-pacino-vouchers/:id} : delete the "id" alPacinoVoucher.
     *
     * @param id the id of the alPacinoVoucherDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlPacinoVoucher(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AlPacinoVoucher : {}", id);
        alPacinoVoucherService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
