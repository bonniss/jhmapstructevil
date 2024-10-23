package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextShipmentViAsserts.*;
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
import xyz.jhmapstruct.domain.NextShipmentVi;
import xyz.jhmapstruct.repository.NextShipmentViRepository;
import xyz.jhmapstruct.service.dto.NextShipmentViDTO;
import xyz.jhmapstruct.service.mapper.NextShipmentViMapper;

/**
 * Integration tests for the {@link NextShipmentViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextShipmentViResourceIT {

    private static final String DEFAULT_TRACKING_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TRACKING_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_SHIPPED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SHIPPED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELIVERY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELIVERY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/next-shipment-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextShipmentViRepository nextShipmentViRepository;

    @Autowired
    private NextShipmentViMapper nextShipmentViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextShipmentViMockMvc;

    private NextShipmentVi nextShipmentVi;

    private NextShipmentVi insertedNextShipmentVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextShipmentVi createEntity() {
        return new NextShipmentVi()
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
    public static NextShipmentVi createUpdatedEntity() {
        return new NextShipmentVi()
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);
    }

    @BeforeEach
    public void initTest() {
        nextShipmentVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextShipmentVi != null) {
            nextShipmentViRepository.delete(insertedNextShipmentVi);
            insertedNextShipmentVi = null;
        }
    }

    @Test
    @Transactional
    void createNextShipmentVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextShipmentVi
        NextShipmentViDTO nextShipmentViDTO = nextShipmentViMapper.toDto(nextShipmentVi);
        var returnedNextShipmentViDTO = om.readValue(
            restNextShipmentViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipmentViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextShipmentViDTO.class
        );

        // Validate the NextShipmentVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextShipmentVi = nextShipmentViMapper.toEntity(returnedNextShipmentViDTO);
        assertNextShipmentViUpdatableFieldsEquals(returnedNextShipmentVi, getPersistedNextShipmentVi(returnedNextShipmentVi));

        insertedNextShipmentVi = returnedNextShipmentVi;
    }

    @Test
    @Transactional
    void createNextShipmentViWithExistingId() throws Exception {
        // Create the NextShipmentVi with an existing ID
        nextShipmentVi.setId(1L);
        NextShipmentViDTO nextShipmentViDTO = nextShipmentViMapper.toDto(nextShipmentVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextShipmentViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipmentViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTrackingNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextShipmentVi.setTrackingNumber(null);

        // Create the NextShipmentVi, which fails.
        NextShipmentViDTO nextShipmentViDTO = nextShipmentViMapper.toDto(nextShipmentVi);

        restNextShipmentViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipmentViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextShipmentVis() throws Exception {
        // Initialize the database
        insertedNextShipmentVi = nextShipmentViRepository.saveAndFlush(nextShipmentVi);

        // Get all the nextShipmentViList
        restNextShipmentViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextShipmentVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));
    }

    @Test
    @Transactional
    void getNextShipmentVi() throws Exception {
        // Initialize the database
        insertedNextShipmentVi = nextShipmentViRepository.saveAndFlush(nextShipmentVi);

        // Get the nextShipmentVi
        restNextShipmentViMockMvc
            .perform(get(ENTITY_API_URL_ID, nextShipmentVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextShipmentVi.getId().intValue()))
            .andExpect(jsonPath("$.trackingNumber").value(DEFAULT_TRACKING_NUMBER))
            .andExpect(jsonPath("$.shippedDate").value(DEFAULT_SHIPPED_DATE.toString()))
            .andExpect(jsonPath("$.deliveryDate").value(DEFAULT_DELIVERY_DATE.toString()));
    }

    @Test
    @Transactional
    void getNextShipmentVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextShipmentVi = nextShipmentViRepository.saveAndFlush(nextShipmentVi);

        Long id = nextShipmentVi.getId();

        defaultNextShipmentViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextShipmentViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextShipmentViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextShipmentVisByTrackingNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentVi = nextShipmentViRepository.saveAndFlush(nextShipmentVi);

        // Get all the nextShipmentViList where trackingNumber equals to
        defaultNextShipmentViFiltering(
            "trackingNumber.equals=" + DEFAULT_TRACKING_NUMBER,
            "trackingNumber.equals=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentVisByTrackingNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextShipmentVi = nextShipmentViRepository.saveAndFlush(nextShipmentVi);

        // Get all the nextShipmentViList where trackingNumber in
        defaultNextShipmentViFiltering(
            "trackingNumber.in=" + DEFAULT_TRACKING_NUMBER + "," + UPDATED_TRACKING_NUMBER,
            "trackingNumber.in=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentVisByTrackingNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextShipmentVi = nextShipmentViRepository.saveAndFlush(nextShipmentVi);

        // Get all the nextShipmentViList where trackingNumber is not null
        defaultNextShipmentViFiltering("trackingNumber.specified=true", "trackingNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextShipmentVisByTrackingNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentVi = nextShipmentViRepository.saveAndFlush(nextShipmentVi);

        // Get all the nextShipmentViList where trackingNumber contains
        defaultNextShipmentViFiltering(
            "trackingNumber.contains=" + DEFAULT_TRACKING_NUMBER,
            "trackingNumber.contains=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentVisByTrackingNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentVi = nextShipmentViRepository.saveAndFlush(nextShipmentVi);

        // Get all the nextShipmentViList where trackingNumber does not contain
        defaultNextShipmentViFiltering(
            "trackingNumber.doesNotContain=" + UPDATED_TRACKING_NUMBER,
            "trackingNumber.doesNotContain=" + DEFAULT_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentVisByShippedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentVi = nextShipmentViRepository.saveAndFlush(nextShipmentVi);

        // Get all the nextShipmentViList where shippedDate equals to
        defaultNextShipmentViFiltering("shippedDate.equals=" + DEFAULT_SHIPPED_DATE, "shippedDate.equals=" + UPDATED_SHIPPED_DATE);
    }

    @Test
    @Transactional
    void getAllNextShipmentVisByShippedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextShipmentVi = nextShipmentViRepository.saveAndFlush(nextShipmentVi);

        // Get all the nextShipmentViList where shippedDate in
        defaultNextShipmentViFiltering(
            "shippedDate.in=" + DEFAULT_SHIPPED_DATE + "," + UPDATED_SHIPPED_DATE,
            "shippedDate.in=" + UPDATED_SHIPPED_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentVisByShippedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextShipmentVi = nextShipmentViRepository.saveAndFlush(nextShipmentVi);

        // Get all the nextShipmentViList where shippedDate is not null
        defaultNextShipmentViFiltering("shippedDate.specified=true", "shippedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextShipmentVisByDeliveryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentVi = nextShipmentViRepository.saveAndFlush(nextShipmentVi);

        // Get all the nextShipmentViList where deliveryDate equals to
        defaultNextShipmentViFiltering("deliveryDate.equals=" + DEFAULT_DELIVERY_DATE, "deliveryDate.equals=" + UPDATED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    void getAllNextShipmentVisByDeliveryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextShipmentVi = nextShipmentViRepository.saveAndFlush(nextShipmentVi);

        // Get all the nextShipmentViList where deliveryDate in
        defaultNextShipmentViFiltering(
            "deliveryDate.in=" + DEFAULT_DELIVERY_DATE + "," + UPDATED_DELIVERY_DATE,
            "deliveryDate.in=" + UPDATED_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentVisByDeliveryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextShipmentVi = nextShipmentViRepository.saveAndFlush(nextShipmentVi);

        // Get all the nextShipmentViList where deliveryDate is not null
        defaultNextShipmentViFiltering("deliveryDate.specified=true", "deliveryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextShipmentVisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextShipmentViRepository.saveAndFlush(nextShipmentVi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextShipmentVi.setTenant(tenant);
        nextShipmentViRepository.saveAndFlush(nextShipmentVi);
        Long tenantId = tenant.getId();
        // Get all the nextShipmentViList where tenant equals to tenantId
        defaultNextShipmentViShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextShipmentViList where tenant equals to (tenantId + 1)
        defaultNextShipmentViShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextShipmentViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextShipmentViShouldBeFound(shouldBeFound);
        defaultNextShipmentViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextShipmentViShouldBeFound(String filter) throws Exception {
        restNextShipmentViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextShipmentVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));

        // Check, that the count call also returns 1
        restNextShipmentViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextShipmentViShouldNotBeFound(String filter) throws Exception {
        restNextShipmentViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextShipmentViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextShipmentVi() throws Exception {
        // Get the nextShipmentVi
        restNextShipmentViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextShipmentVi() throws Exception {
        // Initialize the database
        insertedNextShipmentVi = nextShipmentViRepository.saveAndFlush(nextShipmentVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextShipmentVi
        NextShipmentVi updatedNextShipmentVi = nextShipmentViRepository.findById(nextShipmentVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextShipmentVi are not directly saved in db
        em.detach(updatedNextShipmentVi);
        updatedNextShipmentVi.trackingNumber(UPDATED_TRACKING_NUMBER).shippedDate(UPDATED_SHIPPED_DATE).deliveryDate(UPDATED_DELIVERY_DATE);
        NextShipmentViDTO nextShipmentViDTO = nextShipmentViMapper.toDto(updatedNextShipmentVi);

        restNextShipmentViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextShipmentViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextShipmentViDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextShipmentVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextShipmentViToMatchAllProperties(updatedNextShipmentVi);
    }

    @Test
    @Transactional
    void putNonExistingNextShipmentVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentVi.setId(longCount.incrementAndGet());

        // Create the NextShipmentVi
        NextShipmentViDTO nextShipmentViDTO = nextShipmentViMapper.toDto(nextShipmentVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextShipmentViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextShipmentViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextShipmentViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextShipmentVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentVi.setId(longCount.incrementAndGet());

        // Create the NextShipmentVi
        NextShipmentViDTO nextShipmentViDTO = nextShipmentViMapper.toDto(nextShipmentVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextShipmentViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextShipmentVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentVi.setId(longCount.incrementAndGet());

        // Create the NextShipmentVi
        NextShipmentViDTO nextShipmentViDTO = nextShipmentViMapper.toDto(nextShipmentVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipmentViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextShipmentVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextShipmentViWithPatch() throws Exception {
        // Initialize the database
        insertedNextShipmentVi = nextShipmentViRepository.saveAndFlush(nextShipmentVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextShipmentVi using partial update
        NextShipmentVi partialUpdatedNextShipmentVi = new NextShipmentVi();
        partialUpdatedNextShipmentVi.setId(nextShipmentVi.getId());

        partialUpdatedNextShipmentVi.shippedDate(UPDATED_SHIPPED_DATE);

        restNextShipmentViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextShipmentVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextShipmentVi))
            )
            .andExpect(status().isOk());

        // Validate the NextShipmentVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextShipmentViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextShipmentVi, nextShipmentVi),
            getPersistedNextShipmentVi(nextShipmentVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextShipmentViWithPatch() throws Exception {
        // Initialize the database
        insertedNextShipmentVi = nextShipmentViRepository.saveAndFlush(nextShipmentVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextShipmentVi using partial update
        NextShipmentVi partialUpdatedNextShipmentVi = new NextShipmentVi();
        partialUpdatedNextShipmentVi.setId(nextShipmentVi.getId());

        partialUpdatedNextShipmentVi
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);

        restNextShipmentViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextShipmentVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextShipmentVi))
            )
            .andExpect(status().isOk());

        // Validate the NextShipmentVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextShipmentViUpdatableFieldsEquals(partialUpdatedNextShipmentVi, getPersistedNextShipmentVi(partialUpdatedNextShipmentVi));
    }

    @Test
    @Transactional
    void patchNonExistingNextShipmentVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentVi.setId(longCount.incrementAndGet());

        // Create the NextShipmentVi
        NextShipmentViDTO nextShipmentViDTO = nextShipmentViMapper.toDto(nextShipmentVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextShipmentViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextShipmentViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextShipmentViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextShipmentVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentVi.setId(longCount.incrementAndGet());

        // Create the NextShipmentVi
        NextShipmentViDTO nextShipmentViDTO = nextShipmentViMapper.toDto(nextShipmentVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextShipmentViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextShipmentVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentVi.setId(longCount.incrementAndGet());

        // Create the NextShipmentVi
        NextShipmentViDTO nextShipmentViDTO = nextShipmentViMapper.toDto(nextShipmentVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextShipmentViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextShipmentVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextShipmentVi() throws Exception {
        // Initialize the database
        insertedNextShipmentVi = nextShipmentViRepository.saveAndFlush(nextShipmentVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextShipmentVi
        restNextShipmentViMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextShipmentVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextShipmentViRepository.count();
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

    protected NextShipmentVi getPersistedNextShipmentVi(NextShipmentVi nextShipmentVi) {
        return nextShipmentViRepository.findById(nextShipmentVi.getId()).orElseThrow();
    }

    protected void assertPersistedNextShipmentViToMatchAllProperties(NextShipmentVi expectedNextShipmentVi) {
        assertNextShipmentViAllPropertiesEquals(expectedNextShipmentVi, getPersistedNextShipmentVi(expectedNextShipmentVi));
    }

    protected void assertPersistedNextShipmentViToMatchUpdatableProperties(NextShipmentVi expectedNextShipmentVi) {
        assertNextShipmentViAllUpdatablePropertiesEquals(expectedNextShipmentVi, getPersistedNextShipmentVi(expectedNextShipmentVi));
    }
}
