package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextShipmentThetaAsserts.*;
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
import xyz.jhmapstruct.domain.NextShipmentTheta;
import xyz.jhmapstruct.repository.NextShipmentThetaRepository;

/**
 * Integration tests for the {@link NextShipmentThetaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextShipmentThetaResourceIT {

    private static final String DEFAULT_TRACKING_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TRACKING_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_SHIPPED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SHIPPED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELIVERY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELIVERY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/next-shipment-thetas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextShipmentThetaRepository nextShipmentThetaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextShipmentThetaMockMvc;

    private NextShipmentTheta nextShipmentTheta;

    private NextShipmentTheta insertedNextShipmentTheta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextShipmentTheta createEntity() {
        return new NextShipmentTheta()
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
    public static NextShipmentTheta createUpdatedEntity() {
        return new NextShipmentTheta()
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);
    }

    @BeforeEach
    public void initTest() {
        nextShipmentTheta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextShipmentTheta != null) {
            nextShipmentThetaRepository.delete(insertedNextShipmentTheta);
            insertedNextShipmentTheta = null;
        }
    }

    @Test
    @Transactional
    void createNextShipmentTheta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextShipmentTheta
        var returnedNextShipmentTheta = om.readValue(
            restNextShipmentThetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipmentTheta)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextShipmentTheta.class
        );

        // Validate the NextShipmentTheta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextShipmentThetaUpdatableFieldsEquals(returnedNextShipmentTheta, getPersistedNextShipmentTheta(returnedNextShipmentTheta));

        insertedNextShipmentTheta = returnedNextShipmentTheta;
    }

    @Test
    @Transactional
    void createNextShipmentThetaWithExistingId() throws Exception {
        // Create the NextShipmentTheta with an existing ID
        nextShipmentTheta.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextShipmentThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipmentTheta)))
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTrackingNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextShipmentTheta.setTrackingNumber(null);

        // Create the NextShipmentTheta, which fails.

        restNextShipmentThetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipmentTheta)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextShipmentThetas() throws Exception {
        // Initialize the database
        insertedNextShipmentTheta = nextShipmentThetaRepository.saveAndFlush(nextShipmentTheta);

        // Get all the nextShipmentThetaList
        restNextShipmentThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextShipmentTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));
    }

    @Test
    @Transactional
    void getNextShipmentTheta() throws Exception {
        // Initialize the database
        insertedNextShipmentTheta = nextShipmentThetaRepository.saveAndFlush(nextShipmentTheta);

        // Get the nextShipmentTheta
        restNextShipmentThetaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextShipmentTheta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextShipmentTheta.getId().intValue()))
            .andExpect(jsonPath("$.trackingNumber").value(DEFAULT_TRACKING_NUMBER))
            .andExpect(jsonPath("$.shippedDate").value(DEFAULT_SHIPPED_DATE.toString()))
            .andExpect(jsonPath("$.deliveryDate").value(DEFAULT_DELIVERY_DATE.toString()));
    }

    @Test
    @Transactional
    void getNextShipmentThetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextShipmentTheta = nextShipmentThetaRepository.saveAndFlush(nextShipmentTheta);

        Long id = nextShipmentTheta.getId();

        defaultNextShipmentThetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextShipmentThetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextShipmentThetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextShipmentThetasByTrackingNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentTheta = nextShipmentThetaRepository.saveAndFlush(nextShipmentTheta);

        // Get all the nextShipmentThetaList where trackingNumber equals to
        defaultNextShipmentThetaFiltering(
            "trackingNumber.equals=" + DEFAULT_TRACKING_NUMBER,
            "trackingNumber.equals=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentThetasByTrackingNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextShipmentTheta = nextShipmentThetaRepository.saveAndFlush(nextShipmentTheta);

        // Get all the nextShipmentThetaList where trackingNumber in
        defaultNextShipmentThetaFiltering(
            "trackingNumber.in=" + DEFAULT_TRACKING_NUMBER + "," + UPDATED_TRACKING_NUMBER,
            "trackingNumber.in=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentThetasByTrackingNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextShipmentTheta = nextShipmentThetaRepository.saveAndFlush(nextShipmentTheta);

        // Get all the nextShipmentThetaList where trackingNumber is not null
        defaultNextShipmentThetaFiltering("trackingNumber.specified=true", "trackingNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextShipmentThetasByTrackingNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentTheta = nextShipmentThetaRepository.saveAndFlush(nextShipmentTheta);

        // Get all the nextShipmentThetaList where trackingNumber contains
        defaultNextShipmentThetaFiltering(
            "trackingNumber.contains=" + DEFAULT_TRACKING_NUMBER,
            "trackingNumber.contains=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentThetasByTrackingNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentTheta = nextShipmentThetaRepository.saveAndFlush(nextShipmentTheta);

        // Get all the nextShipmentThetaList where trackingNumber does not contain
        defaultNextShipmentThetaFiltering(
            "trackingNumber.doesNotContain=" + UPDATED_TRACKING_NUMBER,
            "trackingNumber.doesNotContain=" + DEFAULT_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentThetasByShippedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentTheta = nextShipmentThetaRepository.saveAndFlush(nextShipmentTheta);

        // Get all the nextShipmentThetaList where shippedDate equals to
        defaultNextShipmentThetaFiltering("shippedDate.equals=" + DEFAULT_SHIPPED_DATE, "shippedDate.equals=" + UPDATED_SHIPPED_DATE);
    }

    @Test
    @Transactional
    void getAllNextShipmentThetasByShippedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextShipmentTheta = nextShipmentThetaRepository.saveAndFlush(nextShipmentTheta);

        // Get all the nextShipmentThetaList where shippedDate in
        defaultNextShipmentThetaFiltering(
            "shippedDate.in=" + DEFAULT_SHIPPED_DATE + "," + UPDATED_SHIPPED_DATE,
            "shippedDate.in=" + UPDATED_SHIPPED_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentThetasByShippedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextShipmentTheta = nextShipmentThetaRepository.saveAndFlush(nextShipmentTheta);

        // Get all the nextShipmentThetaList where shippedDate is not null
        defaultNextShipmentThetaFiltering("shippedDate.specified=true", "shippedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextShipmentThetasByDeliveryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentTheta = nextShipmentThetaRepository.saveAndFlush(nextShipmentTheta);

        // Get all the nextShipmentThetaList where deliveryDate equals to
        defaultNextShipmentThetaFiltering("deliveryDate.equals=" + DEFAULT_DELIVERY_DATE, "deliveryDate.equals=" + UPDATED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    void getAllNextShipmentThetasByDeliveryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextShipmentTheta = nextShipmentThetaRepository.saveAndFlush(nextShipmentTheta);

        // Get all the nextShipmentThetaList where deliveryDate in
        defaultNextShipmentThetaFiltering(
            "deliveryDate.in=" + DEFAULT_DELIVERY_DATE + "," + UPDATED_DELIVERY_DATE,
            "deliveryDate.in=" + UPDATED_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentThetasByDeliveryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextShipmentTheta = nextShipmentThetaRepository.saveAndFlush(nextShipmentTheta);

        // Get all the nextShipmentThetaList where deliveryDate is not null
        defaultNextShipmentThetaFiltering("deliveryDate.specified=true", "deliveryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextShipmentThetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextShipmentThetaRepository.saveAndFlush(nextShipmentTheta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextShipmentTheta.setTenant(tenant);
        nextShipmentThetaRepository.saveAndFlush(nextShipmentTheta);
        Long tenantId = tenant.getId();
        // Get all the nextShipmentThetaList where tenant equals to tenantId
        defaultNextShipmentThetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextShipmentThetaList where tenant equals to (tenantId + 1)
        defaultNextShipmentThetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextShipmentThetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextShipmentThetaShouldBeFound(shouldBeFound);
        defaultNextShipmentThetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextShipmentThetaShouldBeFound(String filter) throws Exception {
        restNextShipmentThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextShipmentTheta.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));

        // Check, that the count call also returns 1
        restNextShipmentThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextShipmentThetaShouldNotBeFound(String filter) throws Exception {
        restNextShipmentThetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextShipmentThetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextShipmentTheta() throws Exception {
        // Get the nextShipmentTheta
        restNextShipmentThetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextShipmentTheta() throws Exception {
        // Initialize the database
        insertedNextShipmentTheta = nextShipmentThetaRepository.saveAndFlush(nextShipmentTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextShipmentTheta
        NextShipmentTheta updatedNextShipmentTheta = nextShipmentThetaRepository.findById(nextShipmentTheta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextShipmentTheta are not directly saved in db
        em.detach(updatedNextShipmentTheta);
        updatedNextShipmentTheta
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);

        restNextShipmentThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextShipmentTheta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextShipmentTheta))
            )
            .andExpect(status().isOk());

        // Validate the NextShipmentTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextShipmentThetaToMatchAllProperties(updatedNextShipmentTheta);
    }

    @Test
    @Transactional
    void putNonExistingNextShipmentTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentTheta.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextShipmentThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextShipmentTheta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextShipmentTheta))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextShipmentTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentTheta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentThetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextShipmentTheta))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextShipmentTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentTheta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentThetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipmentTheta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextShipmentTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextShipmentThetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextShipmentTheta = nextShipmentThetaRepository.saveAndFlush(nextShipmentTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextShipmentTheta using partial update
        NextShipmentTheta partialUpdatedNextShipmentTheta = new NextShipmentTheta();
        partialUpdatedNextShipmentTheta.setId(nextShipmentTheta.getId());

        partialUpdatedNextShipmentTheta.shippedDate(UPDATED_SHIPPED_DATE).deliveryDate(UPDATED_DELIVERY_DATE);

        restNextShipmentThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextShipmentTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextShipmentTheta))
            )
            .andExpect(status().isOk());

        // Validate the NextShipmentTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextShipmentThetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextShipmentTheta, nextShipmentTheta),
            getPersistedNextShipmentTheta(nextShipmentTheta)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextShipmentThetaWithPatch() throws Exception {
        // Initialize the database
        insertedNextShipmentTheta = nextShipmentThetaRepository.saveAndFlush(nextShipmentTheta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextShipmentTheta using partial update
        NextShipmentTheta partialUpdatedNextShipmentTheta = new NextShipmentTheta();
        partialUpdatedNextShipmentTheta.setId(nextShipmentTheta.getId());

        partialUpdatedNextShipmentTheta
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);

        restNextShipmentThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextShipmentTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextShipmentTheta))
            )
            .andExpect(status().isOk());

        // Validate the NextShipmentTheta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextShipmentThetaUpdatableFieldsEquals(
            partialUpdatedNextShipmentTheta,
            getPersistedNextShipmentTheta(partialUpdatedNextShipmentTheta)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextShipmentTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentTheta.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextShipmentThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextShipmentTheta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextShipmentTheta))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextShipmentTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentTheta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentThetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextShipmentTheta))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextShipmentTheta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentTheta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentThetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextShipmentTheta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextShipmentTheta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextShipmentTheta() throws Exception {
        // Initialize the database
        insertedNextShipmentTheta = nextShipmentThetaRepository.saveAndFlush(nextShipmentTheta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextShipmentTheta
        restNextShipmentThetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextShipmentTheta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextShipmentThetaRepository.count();
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

    protected NextShipmentTheta getPersistedNextShipmentTheta(NextShipmentTheta nextShipmentTheta) {
        return nextShipmentThetaRepository.findById(nextShipmentTheta.getId()).orElseThrow();
    }

    protected void assertPersistedNextShipmentThetaToMatchAllProperties(NextShipmentTheta expectedNextShipmentTheta) {
        assertNextShipmentThetaAllPropertiesEquals(expectedNextShipmentTheta, getPersistedNextShipmentTheta(expectedNextShipmentTheta));
    }

    protected void assertPersistedNextShipmentThetaToMatchUpdatableProperties(NextShipmentTheta expectedNextShipmentTheta) {
        assertNextShipmentThetaAllUpdatablePropertiesEquals(
            expectedNextShipmentTheta,
            getPersistedNextShipmentTheta(expectedNextShipmentTheta)
        );
    }
}
