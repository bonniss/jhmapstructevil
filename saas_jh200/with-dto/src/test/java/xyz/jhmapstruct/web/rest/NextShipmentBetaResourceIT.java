package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextShipmentBetaAsserts.*;
import static xyz.jhmapstruct.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.IntegrationTest;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextShipmentBeta;
import xyz.jhmapstruct.repository.NextShipmentBetaRepository;
import xyz.jhmapstruct.service.dto.NextShipmentBetaDTO;
import xyz.jhmapstruct.service.mapper.NextShipmentBetaMapper;

/**
 * Integration tests for the {@link NextShipmentBetaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextShipmentBetaResourceIT {

    private static final String DEFAULT_TRACKING_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TRACKING_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_SHIPPED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SHIPPED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELIVERY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELIVERY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/next-shipment-betas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextShipmentBetaRepository nextShipmentBetaRepository;

    @Autowired
    private NextShipmentBetaMapper nextShipmentBetaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextShipmentBetaMockMvc;

    private NextShipmentBeta nextShipmentBeta;

    private NextShipmentBeta insertedNextShipmentBeta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextShipmentBeta createEntity() {
        return new NextShipmentBeta()
            .trackingNumber(DEFAULT_TRACKING_NUMBER)
            .shippedDate(DEFAULT_SHIPPED_DATE)
            .deliveryDate(DEFAULT_DELIVERY_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextShipmentBeta createUpdatedEntity() {
        return new NextShipmentBeta()
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);
    }

    @BeforeEach
    public void initTest() {
        nextShipmentBeta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextShipmentBeta != null) {
            nextShipmentBetaRepository.delete(insertedNextShipmentBeta);
            insertedNextShipmentBeta = null;
        }
    }

    @Test
    @Transactional
    void createNextShipmentBeta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextShipmentBeta
        NextShipmentBetaDTO nextShipmentBetaDTO = nextShipmentBetaMapper.toDto(nextShipmentBeta);
        var returnedNextShipmentBetaDTO = om.readValue(
            restNextShipmentBetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipmentBetaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextShipmentBetaDTO.class
        );

        // Validate the NextShipmentBeta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextShipmentBeta = nextShipmentBetaMapper.toEntity(returnedNextShipmentBetaDTO);
        assertNextShipmentBetaUpdatableFieldsEquals(returnedNextShipmentBeta, getPersistedNextShipmentBeta(returnedNextShipmentBeta));

        insertedNextShipmentBeta = returnedNextShipmentBeta;
    }

    @Test
    @Transactional
    void createNextShipmentBetaWithExistingId() throws Exception {
        // Create the NextShipmentBeta with an existing ID
        nextShipmentBeta.setId(1L);
        NextShipmentBetaDTO nextShipmentBetaDTO = nextShipmentBetaMapper.toDto(nextShipmentBeta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextShipmentBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipmentBetaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTrackingNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextShipmentBeta.setTrackingNumber(null);

        // Create the NextShipmentBeta, which fails.
        NextShipmentBetaDTO nextShipmentBetaDTO = nextShipmentBetaMapper.toDto(nextShipmentBeta);

        restNextShipmentBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipmentBetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextShipmentBetas() throws Exception {
        // Initialize the database
        insertedNextShipmentBeta = nextShipmentBetaRepository.saveAndFlush(nextShipmentBeta);

        // Get all the nextShipmentBetaList
        restNextShipmentBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextShipmentBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));
    }

    @Test
    @Transactional
    void getNextShipmentBeta() throws Exception {
        // Initialize the database
        insertedNextShipmentBeta = nextShipmentBetaRepository.saveAndFlush(nextShipmentBeta);

        // Get the nextShipmentBeta
        restNextShipmentBetaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextShipmentBeta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextShipmentBeta.getId().intValue()))
            .andExpect(jsonPath("$.trackingNumber").value(DEFAULT_TRACKING_NUMBER))
            .andExpect(jsonPath("$.shippedDate").value(DEFAULT_SHIPPED_DATE.toString()))
            .andExpect(jsonPath("$.deliveryDate").value(DEFAULT_DELIVERY_DATE.toString()));
    }

    @Test
    @Transactional
    void getNextShipmentBetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextShipmentBeta = nextShipmentBetaRepository.saveAndFlush(nextShipmentBeta);

        Long id = nextShipmentBeta.getId();

        defaultNextShipmentBetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextShipmentBetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextShipmentBetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextShipmentBetasByTrackingNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentBeta = nextShipmentBetaRepository.saveAndFlush(nextShipmentBeta);

        // Get all the nextShipmentBetaList where trackingNumber equals to
        defaultNextShipmentBetaFiltering(
            "trackingNumber.equals=" + DEFAULT_TRACKING_NUMBER,
            "trackingNumber.equals=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentBetasByTrackingNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextShipmentBeta = nextShipmentBetaRepository.saveAndFlush(nextShipmentBeta);

        // Get all the nextShipmentBetaList where trackingNumber in
        defaultNextShipmentBetaFiltering(
            "trackingNumber.in=" + DEFAULT_TRACKING_NUMBER + "," + UPDATED_TRACKING_NUMBER,
            "trackingNumber.in=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentBetasByTrackingNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextShipmentBeta = nextShipmentBetaRepository.saveAndFlush(nextShipmentBeta);

        // Get all the nextShipmentBetaList where trackingNumber is not null
        defaultNextShipmentBetaFiltering("trackingNumber.specified=true", "trackingNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextShipmentBetasByTrackingNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentBeta = nextShipmentBetaRepository.saveAndFlush(nextShipmentBeta);

        // Get all the nextShipmentBetaList where trackingNumber contains
        defaultNextShipmentBetaFiltering(
            "trackingNumber.contains=" + DEFAULT_TRACKING_NUMBER,
            "trackingNumber.contains=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentBetasByTrackingNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentBeta = nextShipmentBetaRepository.saveAndFlush(nextShipmentBeta);

        // Get all the nextShipmentBetaList where trackingNumber does not contain
        defaultNextShipmentBetaFiltering(
            "trackingNumber.doesNotContain=" + UPDATED_TRACKING_NUMBER,
            "trackingNumber.doesNotContain=" + DEFAULT_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentBetasByShippedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentBeta = nextShipmentBetaRepository.saveAndFlush(nextShipmentBeta);

        // Get all the nextShipmentBetaList where shippedDate equals to
        defaultNextShipmentBetaFiltering("shippedDate.equals=" + DEFAULT_SHIPPED_DATE, "shippedDate.equals=" + UPDATED_SHIPPED_DATE);
    }

    @Test
    @Transactional
    void getAllNextShipmentBetasByShippedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextShipmentBeta = nextShipmentBetaRepository.saveAndFlush(nextShipmentBeta);

        // Get all the nextShipmentBetaList where shippedDate in
        defaultNextShipmentBetaFiltering(
            "shippedDate.in=" + DEFAULT_SHIPPED_DATE + "," + UPDATED_SHIPPED_DATE,
            "shippedDate.in=" + UPDATED_SHIPPED_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentBetasByShippedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextShipmentBeta = nextShipmentBetaRepository.saveAndFlush(nextShipmentBeta);

        // Get all the nextShipmentBetaList where shippedDate is not null
        defaultNextShipmentBetaFiltering("shippedDate.specified=true", "shippedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextShipmentBetasByDeliveryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentBeta = nextShipmentBetaRepository.saveAndFlush(nextShipmentBeta);

        // Get all the nextShipmentBetaList where deliveryDate equals to
        defaultNextShipmentBetaFiltering("deliveryDate.equals=" + DEFAULT_DELIVERY_DATE, "deliveryDate.equals=" + UPDATED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    void getAllNextShipmentBetasByDeliveryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextShipmentBeta = nextShipmentBetaRepository.saveAndFlush(nextShipmentBeta);

        // Get all the nextShipmentBetaList where deliveryDate in
        defaultNextShipmentBetaFiltering(
            "deliveryDate.in=" + DEFAULT_DELIVERY_DATE + "," + UPDATED_DELIVERY_DATE,
            "deliveryDate.in=" + UPDATED_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentBetasByDeliveryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextShipmentBeta = nextShipmentBetaRepository.saveAndFlush(nextShipmentBeta);

        // Get all the nextShipmentBetaList where deliveryDate is not null
        defaultNextShipmentBetaFiltering("deliveryDate.specified=true", "deliveryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextShipmentBetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextShipmentBetaRepository.saveAndFlush(nextShipmentBeta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextShipmentBeta.setTenant(tenant);
        nextShipmentBetaRepository.saveAndFlush(nextShipmentBeta);
        Long tenantId = tenant.getId();
        // Get all the nextShipmentBetaList where tenant equals to tenantId
        defaultNextShipmentBetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextShipmentBetaList where tenant equals to (tenantId + 1)
        defaultNextShipmentBetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextShipmentBetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextShipmentBetaShouldBeFound(shouldBeFound);
        defaultNextShipmentBetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextShipmentBetaShouldBeFound(String filter) throws Exception {
        restNextShipmentBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextShipmentBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));

        // Check, that the count call also returns 1
        restNextShipmentBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextShipmentBetaShouldNotBeFound(String filter) throws Exception {
        restNextShipmentBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextShipmentBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextShipmentBeta() throws Exception {
        // Get the nextShipmentBeta
        restNextShipmentBetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextShipmentBeta() throws Exception {
        // Initialize the database
        insertedNextShipmentBeta = nextShipmentBetaRepository.saveAndFlush(nextShipmentBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextShipmentBeta
        NextShipmentBeta updatedNextShipmentBeta = nextShipmentBetaRepository.findById(nextShipmentBeta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextShipmentBeta are not directly saved in db
        em.detach(updatedNextShipmentBeta);
        updatedNextShipmentBeta
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);
        NextShipmentBetaDTO nextShipmentBetaDTO = nextShipmentBetaMapper.toDto(updatedNextShipmentBeta);

        restNextShipmentBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextShipmentBetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextShipmentBetaDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextShipmentBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextShipmentBetaToMatchAllProperties(updatedNextShipmentBeta);
    }

    @Test
    @Transactional
    void putNonExistingNextShipmentBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentBeta.setId(longCount.incrementAndGet());

        // Create the NextShipmentBeta
        NextShipmentBetaDTO nextShipmentBetaDTO = nextShipmentBetaMapper.toDto(nextShipmentBeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextShipmentBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextShipmentBetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextShipmentBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextShipmentBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentBeta.setId(longCount.incrementAndGet());

        // Create the NextShipmentBeta
        NextShipmentBetaDTO nextShipmentBetaDTO = nextShipmentBetaMapper.toDto(nextShipmentBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextShipmentBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextShipmentBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentBeta.setId(longCount.incrementAndGet());

        // Create the NextShipmentBeta
        NextShipmentBetaDTO nextShipmentBetaDTO = nextShipmentBetaMapper.toDto(nextShipmentBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentBetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipmentBetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextShipmentBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextShipmentBetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextShipmentBeta = nextShipmentBetaRepository.saveAndFlush(nextShipmentBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextShipmentBeta using partial update
        NextShipmentBeta partialUpdatedNextShipmentBeta = new NextShipmentBeta();
        partialUpdatedNextShipmentBeta.setId(nextShipmentBeta.getId());

        partialUpdatedNextShipmentBeta.shippedDate(UPDATED_SHIPPED_DATE).deliveryDate(UPDATED_DELIVERY_DATE);

        restNextShipmentBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextShipmentBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextShipmentBeta))
            )
            .andExpect(status().isOk());

        // Validate the NextShipmentBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextShipmentBetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextShipmentBeta, nextShipmentBeta),
            getPersistedNextShipmentBeta(nextShipmentBeta)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextShipmentBetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextShipmentBeta = nextShipmentBetaRepository.saveAndFlush(nextShipmentBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextShipmentBeta using partial update
        NextShipmentBeta partialUpdatedNextShipmentBeta = new NextShipmentBeta();
        partialUpdatedNextShipmentBeta.setId(nextShipmentBeta.getId());

        partialUpdatedNextShipmentBeta
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);

        restNextShipmentBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextShipmentBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextShipmentBeta))
            )
            .andExpect(status().isOk());

        // Validate the NextShipmentBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextShipmentBetaUpdatableFieldsEquals(
            partialUpdatedNextShipmentBeta,
            getPersistedNextShipmentBeta(partialUpdatedNextShipmentBeta)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextShipmentBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentBeta.setId(longCount.incrementAndGet());

        // Create the NextShipmentBeta
        NextShipmentBetaDTO nextShipmentBetaDTO = nextShipmentBetaMapper.toDto(nextShipmentBeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextShipmentBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextShipmentBetaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextShipmentBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextShipmentBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentBeta.setId(longCount.incrementAndGet());

        // Create the NextShipmentBeta
        NextShipmentBetaDTO nextShipmentBetaDTO = nextShipmentBetaMapper.toDto(nextShipmentBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextShipmentBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextShipmentBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentBeta.setId(longCount.incrementAndGet());

        // Create the NextShipmentBeta
        NextShipmentBetaDTO nextShipmentBetaDTO = nextShipmentBetaMapper.toDto(nextShipmentBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentBetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextShipmentBetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextShipmentBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextShipmentBeta() throws Exception {
        // Initialize the database
        insertedNextShipmentBeta = nextShipmentBetaRepository.saveAndFlush(nextShipmentBeta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextShipmentBeta
        restNextShipmentBetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextShipmentBeta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextShipmentBetaRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected NextShipmentBeta getPersistedNextShipmentBeta(NextShipmentBeta nextShipmentBeta) {
        return nextShipmentBetaRepository.findById(nextShipmentBeta.getId()).orElseThrow();
    }

    protected void assertPersistedNextShipmentBetaToMatchAllProperties(NextShipmentBeta expectedNextShipmentBeta) {
        assertNextShipmentBetaAllPropertiesEquals(expectedNextShipmentBeta, getPersistedNextShipmentBeta(expectedNextShipmentBeta));
    }

    protected void assertPersistedNextShipmentBetaToMatchUpdatableProperties(NextShipmentBeta expectedNextShipmentBeta) {
        assertNextShipmentBetaAllUpdatablePropertiesEquals(
            expectedNextShipmentBeta,
            getPersistedNextShipmentBeta(expectedNextShipmentBeta)
        );
    }
}
