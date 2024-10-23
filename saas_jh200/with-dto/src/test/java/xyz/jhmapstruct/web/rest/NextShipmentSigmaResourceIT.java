package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextShipmentSigmaAsserts.*;
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
import xyz.jhmapstruct.domain.NextShipmentSigma;
import xyz.jhmapstruct.repository.NextShipmentSigmaRepository;
import xyz.jhmapstruct.service.dto.NextShipmentSigmaDTO;
import xyz.jhmapstruct.service.mapper.NextShipmentSigmaMapper;

/**
 * Integration tests for the {@link NextShipmentSigmaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextShipmentSigmaResourceIT {

    private static final String DEFAULT_TRACKING_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TRACKING_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_SHIPPED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SHIPPED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELIVERY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELIVERY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/next-shipment-sigmas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextShipmentSigmaRepository nextShipmentSigmaRepository;

    @Autowired
    private NextShipmentSigmaMapper nextShipmentSigmaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextShipmentSigmaMockMvc;

    private NextShipmentSigma nextShipmentSigma;

    private NextShipmentSigma insertedNextShipmentSigma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextShipmentSigma createEntity() {
        return new NextShipmentSigma()
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
    public static NextShipmentSigma createUpdatedEntity() {
        return new NextShipmentSigma()
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);
    }

    @BeforeEach
    public void initTest() {
        nextShipmentSigma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextShipmentSigma != null) {
            nextShipmentSigmaRepository.delete(insertedNextShipmentSigma);
            insertedNextShipmentSigma = null;
        }
    }

    @Test
    @Transactional
    void createNextShipmentSigma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextShipmentSigma
        NextShipmentSigmaDTO nextShipmentSigmaDTO = nextShipmentSigmaMapper.toDto(nextShipmentSigma);
        var returnedNextShipmentSigmaDTO = om.readValue(
            restNextShipmentSigmaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipmentSigmaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextShipmentSigmaDTO.class
        );

        // Validate the NextShipmentSigma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextShipmentSigma = nextShipmentSigmaMapper.toEntity(returnedNextShipmentSigmaDTO);
        assertNextShipmentSigmaUpdatableFieldsEquals(returnedNextShipmentSigma, getPersistedNextShipmentSigma(returnedNextShipmentSigma));

        insertedNextShipmentSigma = returnedNextShipmentSigma;
    }

    @Test
    @Transactional
    void createNextShipmentSigmaWithExistingId() throws Exception {
        // Create the NextShipmentSigma with an existing ID
        nextShipmentSigma.setId(1L);
        NextShipmentSigmaDTO nextShipmentSigmaDTO = nextShipmentSigmaMapper.toDto(nextShipmentSigma);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextShipmentSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipmentSigmaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTrackingNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextShipmentSigma.setTrackingNumber(null);

        // Create the NextShipmentSigma, which fails.
        NextShipmentSigmaDTO nextShipmentSigmaDTO = nextShipmentSigmaMapper.toDto(nextShipmentSigma);

        restNextShipmentSigmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipmentSigmaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextShipmentSigmas() throws Exception {
        // Initialize the database
        insertedNextShipmentSigma = nextShipmentSigmaRepository.saveAndFlush(nextShipmentSigma);

        // Get all the nextShipmentSigmaList
        restNextShipmentSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextShipmentSigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));
    }

    @Test
    @Transactional
    void getNextShipmentSigma() throws Exception {
        // Initialize the database
        insertedNextShipmentSigma = nextShipmentSigmaRepository.saveAndFlush(nextShipmentSigma);

        // Get the nextShipmentSigma
        restNextShipmentSigmaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextShipmentSigma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextShipmentSigma.getId().intValue()))
            .andExpect(jsonPath("$.trackingNumber").value(DEFAULT_TRACKING_NUMBER))
            .andExpect(jsonPath("$.shippedDate").value(DEFAULT_SHIPPED_DATE.toString()))
            .andExpect(jsonPath("$.deliveryDate").value(DEFAULT_DELIVERY_DATE.toString()));
    }

    @Test
    @Transactional
    void getNextShipmentSigmasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextShipmentSigma = nextShipmentSigmaRepository.saveAndFlush(nextShipmentSigma);

        Long id = nextShipmentSigma.getId();

        defaultNextShipmentSigmaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextShipmentSigmaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextShipmentSigmaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextShipmentSigmasByTrackingNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentSigma = nextShipmentSigmaRepository.saveAndFlush(nextShipmentSigma);

        // Get all the nextShipmentSigmaList where trackingNumber equals to
        defaultNextShipmentSigmaFiltering(
            "trackingNumber.equals=" + DEFAULT_TRACKING_NUMBER,
            "trackingNumber.equals=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentSigmasByTrackingNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextShipmentSigma = nextShipmentSigmaRepository.saveAndFlush(nextShipmentSigma);

        // Get all the nextShipmentSigmaList where trackingNumber in
        defaultNextShipmentSigmaFiltering(
            "trackingNumber.in=" + DEFAULT_TRACKING_NUMBER + "," + UPDATED_TRACKING_NUMBER,
            "trackingNumber.in=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentSigmasByTrackingNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextShipmentSigma = nextShipmentSigmaRepository.saveAndFlush(nextShipmentSigma);

        // Get all the nextShipmentSigmaList where trackingNumber is not null
        defaultNextShipmentSigmaFiltering("trackingNumber.specified=true", "trackingNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextShipmentSigmasByTrackingNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentSigma = nextShipmentSigmaRepository.saveAndFlush(nextShipmentSigma);

        // Get all the nextShipmentSigmaList where trackingNumber contains
        defaultNextShipmentSigmaFiltering(
            "trackingNumber.contains=" + DEFAULT_TRACKING_NUMBER,
            "trackingNumber.contains=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentSigmasByTrackingNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentSigma = nextShipmentSigmaRepository.saveAndFlush(nextShipmentSigma);

        // Get all the nextShipmentSigmaList where trackingNumber does not contain
        defaultNextShipmentSigmaFiltering(
            "trackingNumber.doesNotContain=" + UPDATED_TRACKING_NUMBER,
            "trackingNumber.doesNotContain=" + DEFAULT_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentSigmasByShippedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentSigma = nextShipmentSigmaRepository.saveAndFlush(nextShipmentSigma);

        // Get all the nextShipmentSigmaList where shippedDate equals to
        defaultNextShipmentSigmaFiltering("shippedDate.equals=" + DEFAULT_SHIPPED_DATE, "shippedDate.equals=" + UPDATED_SHIPPED_DATE);
    }

    @Test
    @Transactional
    void getAllNextShipmentSigmasByShippedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextShipmentSigma = nextShipmentSigmaRepository.saveAndFlush(nextShipmentSigma);

        // Get all the nextShipmentSigmaList where shippedDate in
        defaultNextShipmentSigmaFiltering(
            "shippedDate.in=" + DEFAULT_SHIPPED_DATE + "," + UPDATED_SHIPPED_DATE,
            "shippedDate.in=" + UPDATED_SHIPPED_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentSigmasByShippedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextShipmentSigma = nextShipmentSigmaRepository.saveAndFlush(nextShipmentSigma);

        // Get all the nextShipmentSigmaList where shippedDate is not null
        defaultNextShipmentSigmaFiltering("shippedDate.specified=true", "shippedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextShipmentSigmasByDeliveryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentSigma = nextShipmentSigmaRepository.saveAndFlush(nextShipmentSigma);

        // Get all the nextShipmentSigmaList where deliveryDate equals to
        defaultNextShipmentSigmaFiltering("deliveryDate.equals=" + DEFAULT_DELIVERY_DATE, "deliveryDate.equals=" + UPDATED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    void getAllNextShipmentSigmasByDeliveryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextShipmentSigma = nextShipmentSigmaRepository.saveAndFlush(nextShipmentSigma);

        // Get all the nextShipmentSigmaList where deliveryDate in
        defaultNextShipmentSigmaFiltering(
            "deliveryDate.in=" + DEFAULT_DELIVERY_DATE + "," + UPDATED_DELIVERY_DATE,
            "deliveryDate.in=" + UPDATED_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentSigmasByDeliveryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextShipmentSigma = nextShipmentSigmaRepository.saveAndFlush(nextShipmentSigma);

        // Get all the nextShipmentSigmaList where deliveryDate is not null
        defaultNextShipmentSigmaFiltering("deliveryDate.specified=true", "deliveryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextShipmentSigmasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextShipmentSigmaRepository.saveAndFlush(nextShipmentSigma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextShipmentSigma.setTenant(tenant);
        nextShipmentSigmaRepository.saveAndFlush(nextShipmentSigma);
        Long tenantId = tenant.getId();
        // Get all the nextShipmentSigmaList where tenant equals to tenantId
        defaultNextShipmentSigmaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextShipmentSigmaList where tenant equals to (tenantId + 1)
        defaultNextShipmentSigmaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextShipmentSigmaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextShipmentSigmaShouldBeFound(shouldBeFound);
        defaultNextShipmentSigmaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextShipmentSigmaShouldBeFound(String filter) throws Exception {
        restNextShipmentSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextShipmentSigma.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));

        // Check, that the count call also returns 1
        restNextShipmentSigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextShipmentSigmaShouldNotBeFound(String filter) throws Exception {
        restNextShipmentSigmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextShipmentSigmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextShipmentSigma() throws Exception {
        // Get the nextShipmentSigma
        restNextShipmentSigmaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextShipmentSigma() throws Exception {
        // Initialize the database
        insertedNextShipmentSigma = nextShipmentSigmaRepository.saveAndFlush(nextShipmentSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextShipmentSigma
        NextShipmentSigma updatedNextShipmentSigma = nextShipmentSigmaRepository.findById(nextShipmentSigma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextShipmentSigma are not directly saved in db
        em.detach(updatedNextShipmentSigma);
        updatedNextShipmentSigma
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);
        NextShipmentSigmaDTO nextShipmentSigmaDTO = nextShipmentSigmaMapper.toDto(updatedNextShipmentSigma);

        restNextShipmentSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextShipmentSigmaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextShipmentSigmaDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextShipmentSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextShipmentSigmaToMatchAllProperties(updatedNextShipmentSigma);
    }

    @Test
    @Transactional
    void putNonExistingNextShipmentSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentSigma.setId(longCount.incrementAndGet());

        // Create the NextShipmentSigma
        NextShipmentSigmaDTO nextShipmentSigmaDTO = nextShipmentSigmaMapper.toDto(nextShipmentSigma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextShipmentSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextShipmentSigmaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextShipmentSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextShipmentSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentSigma.setId(longCount.incrementAndGet());

        // Create the NextShipmentSigma
        NextShipmentSigmaDTO nextShipmentSigmaDTO = nextShipmentSigmaMapper.toDto(nextShipmentSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentSigmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextShipmentSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextShipmentSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentSigma.setId(longCount.incrementAndGet());

        // Create the NextShipmentSigma
        NextShipmentSigmaDTO nextShipmentSigmaDTO = nextShipmentSigmaMapper.toDto(nextShipmentSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentSigmaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipmentSigmaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextShipmentSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextShipmentSigmaWithPatch() throws Exception {
        // Initialize the database
        insertedNextShipmentSigma = nextShipmentSigmaRepository.saveAndFlush(nextShipmentSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextShipmentSigma using partial update
        NextShipmentSigma partialUpdatedNextShipmentSigma = new NextShipmentSigma();
        partialUpdatedNextShipmentSigma.setId(nextShipmentSigma.getId());

        partialUpdatedNextShipmentSigma.trackingNumber(UPDATED_TRACKING_NUMBER);

        restNextShipmentSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextShipmentSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextShipmentSigma))
            )
            .andExpect(status().isOk());

        // Validate the NextShipmentSigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextShipmentSigmaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextShipmentSigma, nextShipmentSigma),
            getPersistedNextShipmentSigma(nextShipmentSigma)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextShipmentSigmaWithPatch() throws Exception {
        // Initialize the database
        insertedNextShipmentSigma = nextShipmentSigmaRepository.saveAndFlush(nextShipmentSigma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextShipmentSigma using partial update
        NextShipmentSigma partialUpdatedNextShipmentSigma = new NextShipmentSigma();
        partialUpdatedNextShipmentSigma.setId(nextShipmentSigma.getId());

        partialUpdatedNextShipmentSigma
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);

        restNextShipmentSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextShipmentSigma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextShipmentSigma))
            )
            .andExpect(status().isOk());

        // Validate the NextShipmentSigma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextShipmentSigmaUpdatableFieldsEquals(
            partialUpdatedNextShipmentSigma,
            getPersistedNextShipmentSigma(partialUpdatedNextShipmentSigma)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextShipmentSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentSigma.setId(longCount.incrementAndGet());

        // Create the NextShipmentSigma
        NextShipmentSigmaDTO nextShipmentSigmaDTO = nextShipmentSigmaMapper.toDto(nextShipmentSigma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextShipmentSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextShipmentSigmaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextShipmentSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextShipmentSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentSigma.setId(longCount.incrementAndGet());

        // Create the NextShipmentSigma
        NextShipmentSigmaDTO nextShipmentSigmaDTO = nextShipmentSigmaMapper.toDto(nextShipmentSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentSigmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextShipmentSigmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextShipmentSigma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentSigma.setId(longCount.incrementAndGet());

        // Create the NextShipmentSigma
        NextShipmentSigmaDTO nextShipmentSigmaDTO = nextShipmentSigmaMapper.toDto(nextShipmentSigma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentSigmaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextShipmentSigmaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextShipmentSigma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextShipmentSigma() throws Exception {
        // Initialize the database
        insertedNextShipmentSigma = nextShipmentSigmaRepository.saveAndFlush(nextShipmentSigma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextShipmentSigma
        restNextShipmentSigmaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextShipmentSigma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextShipmentSigmaRepository.count();
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

    protected NextShipmentSigma getPersistedNextShipmentSigma(NextShipmentSigma nextShipmentSigma) {
        return nextShipmentSigmaRepository.findById(nextShipmentSigma.getId()).orElseThrow();
    }

    protected void assertPersistedNextShipmentSigmaToMatchAllProperties(NextShipmentSigma expectedNextShipmentSigma) {
        assertNextShipmentSigmaAllPropertiesEquals(expectedNextShipmentSigma, getPersistedNextShipmentSigma(expectedNextShipmentSigma));
    }

    protected void assertPersistedNextShipmentSigmaToMatchUpdatableProperties(NextShipmentSigma expectedNextShipmentSigma) {
        assertNextShipmentSigmaAllUpdatablePropertiesEquals(
            expectedNextShipmentSigma,
            getPersistedNextShipmentSigma(expectedNextShipmentSigma)
        );
    }
}
