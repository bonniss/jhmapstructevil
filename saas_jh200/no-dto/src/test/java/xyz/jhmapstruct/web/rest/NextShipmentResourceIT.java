package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextShipmentAsserts.*;
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
import xyz.jhmapstruct.domain.NextShipment;
import xyz.jhmapstruct.repository.NextShipmentRepository;

/**
 * Integration tests for the {@link NextShipmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextShipmentResourceIT {

    private static final String DEFAULT_TRACKING_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TRACKING_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_SHIPPED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SHIPPED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELIVERY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELIVERY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/next-shipments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextShipmentRepository nextShipmentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextShipmentMockMvc;

    private NextShipment nextShipment;

    private NextShipment insertedNextShipment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextShipment createEntity() {
        return new NextShipment()
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
    public static NextShipment createUpdatedEntity() {
        return new NextShipment()
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);
    }

    @BeforeEach
    public void initTest() {
        nextShipment = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextShipment != null) {
            nextShipmentRepository.delete(insertedNextShipment);
            insertedNextShipment = null;
        }
    }

    @Test
    @Transactional
    void createNextShipment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextShipment
        var returnedNextShipment = om.readValue(
            restNextShipmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipment)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextShipment.class
        );

        // Validate the NextShipment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextShipmentUpdatableFieldsEquals(returnedNextShipment, getPersistedNextShipment(returnedNextShipment));

        insertedNextShipment = returnedNextShipment;
    }

    @Test
    @Transactional
    void createNextShipmentWithExistingId() throws Exception {
        // Create the NextShipment with an existing ID
        nextShipment.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextShipmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipment)))
            .andExpect(status().isBadRequest());

        // Validate the NextShipment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTrackingNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextShipment.setTrackingNumber(null);

        // Create the NextShipment, which fails.

        restNextShipmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipment)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextShipments() throws Exception {
        // Initialize the database
        insertedNextShipment = nextShipmentRepository.saveAndFlush(nextShipment);

        // Get all the nextShipmentList
        restNextShipmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextShipment.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));
    }

    @Test
    @Transactional
    void getNextShipment() throws Exception {
        // Initialize the database
        insertedNextShipment = nextShipmentRepository.saveAndFlush(nextShipment);

        // Get the nextShipment
        restNextShipmentMockMvc
            .perform(get(ENTITY_API_URL_ID, nextShipment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextShipment.getId().intValue()))
            .andExpect(jsonPath("$.trackingNumber").value(DEFAULT_TRACKING_NUMBER))
            .andExpect(jsonPath("$.shippedDate").value(DEFAULT_SHIPPED_DATE.toString()))
            .andExpect(jsonPath("$.deliveryDate").value(DEFAULT_DELIVERY_DATE.toString()));
    }

    @Test
    @Transactional
    void getNextShipmentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextShipment = nextShipmentRepository.saveAndFlush(nextShipment);

        Long id = nextShipment.getId();

        defaultNextShipmentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextShipmentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextShipmentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextShipmentsByTrackingNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextShipment = nextShipmentRepository.saveAndFlush(nextShipment);

        // Get all the nextShipmentList where trackingNumber equals to
        defaultNextShipmentFiltering(
            "trackingNumber.equals=" + DEFAULT_TRACKING_NUMBER,
            "trackingNumber.equals=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentsByTrackingNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextShipment = nextShipmentRepository.saveAndFlush(nextShipment);

        // Get all the nextShipmentList where trackingNumber in
        defaultNextShipmentFiltering(
            "trackingNumber.in=" + DEFAULT_TRACKING_NUMBER + "," + UPDATED_TRACKING_NUMBER,
            "trackingNumber.in=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentsByTrackingNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextShipment = nextShipmentRepository.saveAndFlush(nextShipment);

        // Get all the nextShipmentList where trackingNumber is not null
        defaultNextShipmentFiltering("trackingNumber.specified=true", "trackingNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextShipmentsByTrackingNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextShipment = nextShipmentRepository.saveAndFlush(nextShipment);

        // Get all the nextShipmentList where trackingNumber contains
        defaultNextShipmentFiltering(
            "trackingNumber.contains=" + DEFAULT_TRACKING_NUMBER,
            "trackingNumber.contains=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentsByTrackingNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextShipment = nextShipmentRepository.saveAndFlush(nextShipment);

        // Get all the nextShipmentList where trackingNumber does not contain
        defaultNextShipmentFiltering(
            "trackingNumber.doesNotContain=" + UPDATED_TRACKING_NUMBER,
            "trackingNumber.doesNotContain=" + DEFAULT_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentsByShippedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextShipment = nextShipmentRepository.saveAndFlush(nextShipment);

        // Get all the nextShipmentList where shippedDate equals to
        defaultNextShipmentFiltering("shippedDate.equals=" + DEFAULT_SHIPPED_DATE, "shippedDate.equals=" + UPDATED_SHIPPED_DATE);
    }

    @Test
    @Transactional
    void getAllNextShipmentsByShippedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextShipment = nextShipmentRepository.saveAndFlush(nextShipment);

        // Get all the nextShipmentList where shippedDate in
        defaultNextShipmentFiltering(
            "shippedDate.in=" + DEFAULT_SHIPPED_DATE + "," + UPDATED_SHIPPED_DATE,
            "shippedDate.in=" + UPDATED_SHIPPED_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentsByShippedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextShipment = nextShipmentRepository.saveAndFlush(nextShipment);

        // Get all the nextShipmentList where shippedDate is not null
        defaultNextShipmentFiltering("shippedDate.specified=true", "shippedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextShipmentsByDeliveryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextShipment = nextShipmentRepository.saveAndFlush(nextShipment);

        // Get all the nextShipmentList where deliveryDate equals to
        defaultNextShipmentFiltering("deliveryDate.equals=" + DEFAULT_DELIVERY_DATE, "deliveryDate.equals=" + UPDATED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    void getAllNextShipmentsByDeliveryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextShipment = nextShipmentRepository.saveAndFlush(nextShipment);

        // Get all the nextShipmentList where deliveryDate in
        defaultNextShipmentFiltering(
            "deliveryDate.in=" + DEFAULT_DELIVERY_DATE + "," + UPDATED_DELIVERY_DATE,
            "deliveryDate.in=" + UPDATED_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentsByDeliveryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextShipment = nextShipmentRepository.saveAndFlush(nextShipment);

        // Get all the nextShipmentList where deliveryDate is not null
        defaultNextShipmentFiltering("deliveryDate.specified=true", "deliveryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextShipmentsByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextShipmentRepository.saveAndFlush(nextShipment);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextShipment.setTenant(tenant);
        nextShipmentRepository.saveAndFlush(nextShipment);
        Long tenantId = tenant.getId();
        // Get all the nextShipmentList where tenant equals to tenantId
        defaultNextShipmentShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextShipmentList where tenant equals to (tenantId + 1)
        defaultNextShipmentShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextShipmentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextShipmentShouldBeFound(shouldBeFound);
        defaultNextShipmentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextShipmentShouldBeFound(String filter) throws Exception {
        restNextShipmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextShipment.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));

        // Check, that the count call also returns 1
        restNextShipmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextShipmentShouldNotBeFound(String filter) throws Exception {
        restNextShipmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextShipmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextShipment() throws Exception {
        // Get the nextShipment
        restNextShipmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextShipment() throws Exception {
        // Initialize the database
        insertedNextShipment = nextShipmentRepository.saveAndFlush(nextShipment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextShipment
        NextShipment updatedNextShipment = nextShipmentRepository.findById(nextShipment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextShipment are not directly saved in db
        em.detach(updatedNextShipment);
        updatedNextShipment.trackingNumber(UPDATED_TRACKING_NUMBER).shippedDate(UPDATED_SHIPPED_DATE).deliveryDate(UPDATED_DELIVERY_DATE);

        restNextShipmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextShipment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextShipment))
            )
            .andExpect(status().isOk());

        // Validate the NextShipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextShipmentToMatchAllProperties(updatedNextShipment);
    }

    @Test
    @Transactional
    void putNonExistingNextShipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextShipmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextShipment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextShipment))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextShipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextShipment))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextShipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextShipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextShipmentWithPatch() throws Exception {
        // Initialize the database
        insertedNextShipment = nextShipmentRepository.saveAndFlush(nextShipment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextShipment using partial update
        NextShipment partialUpdatedNextShipment = new NextShipment();
        partialUpdatedNextShipment.setId(nextShipment.getId());

        partialUpdatedNextShipment.shippedDate(UPDATED_SHIPPED_DATE).deliveryDate(UPDATED_DELIVERY_DATE);

        restNextShipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextShipment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextShipment))
            )
            .andExpect(status().isOk());

        // Validate the NextShipment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextShipmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextShipment, nextShipment),
            getPersistedNextShipment(nextShipment)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextShipmentWithPatch() throws Exception {
        // Initialize the database
        insertedNextShipment = nextShipmentRepository.saveAndFlush(nextShipment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextShipment using partial update
        NextShipment partialUpdatedNextShipment = new NextShipment();
        partialUpdatedNextShipment.setId(nextShipment.getId());

        partialUpdatedNextShipment
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);

        restNextShipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextShipment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextShipment))
            )
            .andExpect(status().isOk());

        // Validate the NextShipment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextShipmentUpdatableFieldsEquals(partialUpdatedNextShipment, getPersistedNextShipment(partialUpdatedNextShipment));
    }

    @Test
    @Transactional
    void patchNonExistingNextShipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextShipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextShipment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextShipment))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextShipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextShipment))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextShipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextShipment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextShipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextShipment() throws Exception {
        // Initialize the database
        insertedNextShipment = nextShipmentRepository.saveAndFlush(nextShipment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextShipment
        restNextShipmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextShipment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextShipmentRepository.count();
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

    protected NextShipment getPersistedNextShipment(NextShipment nextShipment) {
        return nextShipmentRepository.findById(nextShipment.getId()).orElseThrow();
    }

    protected void assertPersistedNextShipmentToMatchAllProperties(NextShipment expectedNextShipment) {
        assertNextShipmentAllPropertiesEquals(expectedNextShipment, getPersistedNextShipment(expectedNextShipment));
    }

    protected void assertPersistedNextShipmentToMatchUpdatableProperties(NextShipment expectedNextShipment) {
        assertNextShipmentAllUpdatablePropertiesEquals(expectedNextShipment, getPersistedNextShipment(expectedNextShipment));
    }
}
