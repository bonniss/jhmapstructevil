package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextShipmentMiMiAsserts.*;
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
import xyz.jhmapstruct.domain.NextShipmentMiMi;
import xyz.jhmapstruct.repository.NextShipmentMiMiRepository;

/**
 * Integration tests for the {@link NextShipmentMiMiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextShipmentMiMiResourceIT {

    private static final String DEFAULT_TRACKING_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TRACKING_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_SHIPPED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SHIPPED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELIVERY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELIVERY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/next-shipment-mi-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextShipmentMiMiRepository nextShipmentMiMiRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextShipmentMiMiMockMvc;

    private NextShipmentMiMi nextShipmentMiMi;

    private NextShipmentMiMi insertedNextShipmentMiMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextShipmentMiMi createEntity() {
        return new NextShipmentMiMi()
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
    public static NextShipmentMiMi createUpdatedEntity() {
        return new NextShipmentMiMi()
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);
    }

    @BeforeEach
    public void initTest() {
        nextShipmentMiMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextShipmentMiMi != null) {
            nextShipmentMiMiRepository.delete(insertedNextShipmentMiMi);
            insertedNextShipmentMiMi = null;
        }
    }

    @Test
    @Transactional
    void createNextShipmentMiMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextShipmentMiMi
        var returnedNextShipmentMiMi = om.readValue(
            restNextShipmentMiMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipmentMiMi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextShipmentMiMi.class
        );

        // Validate the NextShipmentMiMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextShipmentMiMiUpdatableFieldsEquals(returnedNextShipmentMiMi, getPersistedNextShipmentMiMi(returnedNextShipmentMiMi));

        insertedNextShipmentMiMi = returnedNextShipmentMiMi;
    }

    @Test
    @Transactional
    void createNextShipmentMiMiWithExistingId() throws Exception {
        // Create the NextShipmentMiMi with an existing ID
        nextShipmentMiMi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextShipmentMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipmentMiMi)))
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTrackingNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextShipmentMiMi.setTrackingNumber(null);

        // Create the NextShipmentMiMi, which fails.

        restNextShipmentMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipmentMiMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextShipmentMiMis() throws Exception {
        // Initialize the database
        insertedNextShipmentMiMi = nextShipmentMiMiRepository.saveAndFlush(nextShipmentMiMi);

        // Get all the nextShipmentMiMiList
        restNextShipmentMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextShipmentMiMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));
    }

    @Test
    @Transactional
    void getNextShipmentMiMi() throws Exception {
        // Initialize the database
        insertedNextShipmentMiMi = nextShipmentMiMiRepository.saveAndFlush(nextShipmentMiMi);

        // Get the nextShipmentMiMi
        restNextShipmentMiMiMockMvc
            .perform(get(ENTITY_API_URL_ID, nextShipmentMiMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextShipmentMiMi.getId().intValue()))
            .andExpect(jsonPath("$.trackingNumber").value(DEFAULT_TRACKING_NUMBER))
            .andExpect(jsonPath("$.shippedDate").value(DEFAULT_SHIPPED_DATE.toString()))
            .andExpect(jsonPath("$.deliveryDate").value(DEFAULT_DELIVERY_DATE.toString()));
    }

    @Test
    @Transactional
    void getNextShipmentMiMisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextShipmentMiMi = nextShipmentMiMiRepository.saveAndFlush(nextShipmentMiMi);

        Long id = nextShipmentMiMi.getId();

        defaultNextShipmentMiMiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextShipmentMiMiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextShipmentMiMiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextShipmentMiMisByTrackingNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentMiMi = nextShipmentMiMiRepository.saveAndFlush(nextShipmentMiMi);

        // Get all the nextShipmentMiMiList where trackingNumber equals to
        defaultNextShipmentMiMiFiltering(
            "trackingNumber.equals=" + DEFAULT_TRACKING_NUMBER,
            "trackingNumber.equals=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentMiMisByTrackingNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextShipmentMiMi = nextShipmentMiMiRepository.saveAndFlush(nextShipmentMiMi);

        // Get all the nextShipmentMiMiList where trackingNumber in
        defaultNextShipmentMiMiFiltering(
            "trackingNumber.in=" + DEFAULT_TRACKING_NUMBER + "," + UPDATED_TRACKING_NUMBER,
            "trackingNumber.in=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentMiMisByTrackingNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextShipmentMiMi = nextShipmentMiMiRepository.saveAndFlush(nextShipmentMiMi);

        // Get all the nextShipmentMiMiList where trackingNumber is not null
        defaultNextShipmentMiMiFiltering("trackingNumber.specified=true", "trackingNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextShipmentMiMisByTrackingNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentMiMi = nextShipmentMiMiRepository.saveAndFlush(nextShipmentMiMi);

        // Get all the nextShipmentMiMiList where trackingNumber contains
        defaultNextShipmentMiMiFiltering(
            "trackingNumber.contains=" + DEFAULT_TRACKING_NUMBER,
            "trackingNumber.contains=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentMiMisByTrackingNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentMiMi = nextShipmentMiMiRepository.saveAndFlush(nextShipmentMiMi);

        // Get all the nextShipmentMiMiList where trackingNumber does not contain
        defaultNextShipmentMiMiFiltering(
            "trackingNumber.doesNotContain=" + UPDATED_TRACKING_NUMBER,
            "trackingNumber.doesNotContain=" + DEFAULT_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentMiMisByShippedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentMiMi = nextShipmentMiMiRepository.saveAndFlush(nextShipmentMiMi);

        // Get all the nextShipmentMiMiList where shippedDate equals to
        defaultNextShipmentMiMiFiltering("shippedDate.equals=" + DEFAULT_SHIPPED_DATE, "shippedDate.equals=" + UPDATED_SHIPPED_DATE);
    }

    @Test
    @Transactional
    void getAllNextShipmentMiMisByShippedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextShipmentMiMi = nextShipmentMiMiRepository.saveAndFlush(nextShipmentMiMi);

        // Get all the nextShipmentMiMiList where shippedDate in
        defaultNextShipmentMiMiFiltering(
            "shippedDate.in=" + DEFAULT_SHIPPED_DATE + "," + UPDATED_SHIPPED_DATE,
            "shippedDate.in=" + UPDATED_SHIPPED_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentMiMisByShippedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextShipmentMiMi = nextShipmentMiMiRepository.saveAndFlush(nextShipmentMiMi);

        // Get all the nextShipmentMiMiList where shippedDate is not null
        defaultNextShipmentMiMiFiltering("shippedDate.specified=true", "shippedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextShipmentMiMisByDeliveryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentMiMi = nextShipmentMiMiRepository.saveAndFlush(nextShipmentMiMi);

        // Get all the nextShipmentMiMiList where deliveryDate equals to
        defaultNextShipmentMiMiFiltering("deliveryDate.equals=" + DEFAULT_DELIVERY_DATE, "deliveryDate.equals=" + UPDATED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    void getAllNextShipmentMiMisByDeliveryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextShipmentMiMi = nextShipmentMiMiRepository.saveAndFlush(nextShipmentMiMi);

        // Get all the nextShipmentMiMiList where deliveryDate in
        defaultNextShipmentMiMiFiltering(
            "deliveryDate.in=" + DEFAULT_DELIVERY_DATE + "," + UPDATED_DELIVERY_DATE,
            "deliveryDate.in=" + UPDATED_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentMiMisByDeliveryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextShipmentMiMi = nextShipmentMiMiRepository.saveAndFlush(nextShipmentMiMi);

        // Get all the nextShipmentMiMiList where deliveryDate is not null
        defaultNextShipmentMiMiFiltering("deliveryDate.specified=true", "deliveryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextShipmentMiMisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextShipmentMiMiRepository.saveAndFlush(nextShipmentMiMi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextShipmentMiMi.setTenant(tenant);
        nextShipmentMiMiRepository.saveAndFlush(nextShipmentMiMi);
        Long tenantId = tenant.getId();
        // Get all the nextShipmentMiMiList where tenant equals to tenantId
        defaultNextShipmentMiMiShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextShipmentMiMiList where tenant equals to (tenantId + 1)
        defaultNextShipmentMiMiShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextShipmentMiMiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextShipmentMiMiShouldBeFound(shouldBeFound);
        defaultNextShipmentMiMiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextShipmentMiMiShouldBeFound(String filter) throws Exception {
        restNextShipmentMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextShipmentMiMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));

        // Check, that the count call also returns 1
        restNextShipmentMiMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextShipmentMiMiShouldNotBeFound(String filter) throws Exception {
        restNextShipmentMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextShipmentMiMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextShipmentMiMi() throws Exception {
        // Get the nextShipmentMiMi
        restNextShipmentMiMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextShipmentMiMi() throws Exception {
        // Initialize the database
        insertedNextShipmentMiMi = nextShipmentMiMiRepository.saveAndFlush(nextShipmentMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextShipmentMiMi
        NextShipmentMiMi updatedNextShipmentMiMi = nextShipmentMiMiRepository.findById(nextShipmentMiMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextShipmentMiMi are not directly saved in db
        em.detach(updatedNextShipmentMiMi);
        updatedNextShipmentMiMi
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);

        restNextShipmentMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextShipmentMiMi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextShipmentMiMi))
            )
            .andExpect(status().isOk());

        // Validate the NextShipmentMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextShipmentMiMiToMatchAllProperties(updatedNextShipmentMiMi);
    }

    @Test
    @Transactional
    void putNonExistingNextShipmentMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentMiMi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextShipmentMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextShipmentMiMi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextShipmentMiMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextShipmentMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentMiMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextShipmentMiMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextShipmentMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentMiMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentMiMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipmentMiMi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextShipmentMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextShipmentMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextShipmentMiMi = nextShipmentMiMiRepository.saveAndFlush(nextShipmentMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextShipmentMiMi using partial update
        NextShipmentMiMi partialUpdatedNextShipmentMiMi = new NextShipmentMiMi();
        partialUpdatedNextShipmentMiMi.setId(nextShipmentMiMi.getId());

        partialUpdatedNextShipmentMiMi.shippedDate(UPDATED_SHIPPED_DATE).deliveryDate(UPDATED_DELIVERY_DATE);

        restNextShipmentMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextShipmentMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextShipmentMiMi))
            )
            .andExpect(status().isOk());

        // Validate the NextShipmentMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextShipmentMiMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextShipmentMiMi, nextShipmentMiMi),
            getPersistedNextShipmentMiMi(nextShipmentMiMi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextShipmentMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextShipmentMiMi = nextShipmentMiMiRepository.saveAndFlush(nextShipmentMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextShipmentMiMi using partial update
        NextShipmentMiMi partialUpdatedNextShipmentMiMi = new NextShipmentMiMi();
        partialUpdatedNextShipmentMiMi.setId(nextShipmentMiMi.getId());

        partialUpdatedNextShipmentMiMi
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);

        restNextShipmentMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextShipmentMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextShipmentMiMi))
            )
            .andExpect(status().isOk());

        // Validate the NextShipmentMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextShipmentMiMiUpdatableFieldsEquals(
            partialUpdatedNextShipmentMiMi,
            getPersistedNextShipmentMiMi(partialUpdatedNextShipmentMiMi)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextShipmentMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentMiMi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextShipmentMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextShipmentMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextShipmentMiMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextShipmentMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentMiMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextShipmentMiMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextShipmentMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentMiMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentMiMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextShipmentMiMi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextShipmentMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextShipmentMiMi() throws Exception {
        // Initialize the database
        insertedNextShipmentMiMi = nextShipmentMiMiRepository.saveAndFlush(nextShipmentMiMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextShipmentMiMi
        restNextShipmentMiMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextShipmentMiMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextShipmentMiMiRepository.count();
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

    protected NextShipmentMiMi getPersistedNextShipmentMiMi(NextShipmentMiMi nextShipmentMiMi) {
        return nextShipmentMiMiRepository.findById(nextShipmentMiMi.getId()).orElseThrow();
    }

    protected void assertPersistedNextShipmentMiMiToMatchAllProperties(NextShipmentMiMi expectedNextShipmentMiMi) {
        assertNextShipmentMiMiAllPropertiesEquals(expectedNextShipmentMiMi, getPersistedNextShipmentMiMi(expectedNextShipmentMiMi));
    }

    protected void assertPersistedNextShipmentMiMiToMatchUpdatableProperties(NextShipmentMiMi expectedNextShipmentMiMi) {
        assertNextShipmentMiMiAllUpdatablePropertiesEquals(
            expectedNextShipmentMiMi,
            getPersistedNextShipmentMiMi(expectedNextShipmentMiMi)
        );
    }
}
