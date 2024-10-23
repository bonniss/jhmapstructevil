package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextShipmentMiAsserts.*;
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
import xyz.jhmapstruct.domain.NextShipmentMi;
import xyz.jhmapstruct.repository.NextShipmentMiRepository;
import xyz.jhmapstruct.service.dto.NextShipmentMiDTO;
import xyz.jhmapstruct.service.mapper.NextShipmentMiMapper;

/**
 * Integration tests for the {@link NextShipmentMiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextShipmentMiResourceIT {

    private static final String DEFAULT_TRACKING_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TRACKING_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_SHIPPED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SHIPPED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELIVERY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELIVERY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/next-shipment-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextShipmentMiRepository nextShipmentMiRepository;

    @Autowired
    private NextShipmentMiMapper nextShipmentMiMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextShipmentMiMockMvc;

    private NextShipmentMi nextShipmentMi;

    private NextShipmentMi insertedNextShipmentMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextShipmentMi createEntity() {
        return new NextShipmentMi()
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
    public static NextShipmentMi createUpdatedEntity() {
        return new NextShipmentMi()
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);
    }

    @BeforeEach
    public void initTest() {
        nextShipmentMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextShipmentMi != null) {
            nextShipmentMiRepository.delete(insertedNextShipmentMi);
            insertedNextShipmentMi = null;
        }
    }

    @Test
    @Transactional
    void createNextShipmentMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextShipmentMi
        NextShipmentMiDTO nextShipmentMiDTO = nextShipmentMiMapper.toDto(nextShipmentMi);
        var returnedNextShipmentMiDTO = om.readValue(
            restNextShipmentMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipmentMiDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextShipmentMiDTO.class
        );

        // Validate the NextShipmentMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextShipmentMi = nextShipmentMiMapper.toEntity(returnedNextShipmentMiDTO);
        assertNextShipmentMiUpdatableFieldsEquals(returnedNextShipmentMi, getPersistedNextShipmentMi(returnedNextShipmentMi));

        insertedNextShipmentMi = returnedNextShipmentMi;
    }

    @Test
    @Transactional
    void createNextShipmentMiWithExistingId() throws Exception {
        // Create the NextShipmentMi with an existing ID
        nextShipmentMi.setId(1L);
        NextShipmentMiDTO nextShipmentMiDTO = nextShipmentMiMapper.toDto(nextShipmentMi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextShipmentMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipmentMiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTrackingNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextShipmentMi.setTrackingNumber(null);

        // Create the NextShipmentMi, which fails.
        NextShipmentMiDTO nextShipmentMiDTO = nextShipmentMiMapper.toDto(nextShipmentMi);

        restNextShipmentMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipmentMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextShipmentMis() throws Exception {
        // Initialize the database
        insertedNextShipmentMi = nextShipmentMiRepository.saveAndFlush(nextShipmentMi);

        // Get all the nextShipmentMiList
        restNextShipmentMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextShipmentMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));
    }

    @Test
    @Transactional
    void getNextShipmentMi() throws Exception {
        // Initialize the database
        insertedNextShipmentMi = nextShipmentMiRepository.saveAndFlush(nextShipmentMi);

        // Get the nextShipmentMi
        restNextShipmentMiMockMvc
            .perform(get(ENTITY_API_URL_ID, nextShipmentMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextShipmentMi.getId().intValue()))
            .andExpect(jsonPath("$.trackingNumber").value(DEFAULT_TRACKING_NUMBER))
            .andExpect(jsonPath("$.shippedDate").value(DEFAULT_SHIPPED_DATE.toString()))
            .andExpect(jsonPath("$.deliveryDate").value(DEFAULT_DELIVERY_DATE.toString()));
    }

    @Test
    @Transactional
    void getNextShipmentMisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextShipmentMi = nextShipmentMiRepository.saveAndFlush(nextShipmentMi);

        Long id = nextShipmentMi.getId();

        defaultNextShipmentMiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextShipmentMiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextShipmentMiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextShipmentMisByTrackingNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentMi = nextShipmentMiRepository.saveAndFlush(nextShipmentMi);

        // Get all the nextShipmentMiList where trackingNumber equals to
        defaultNextShipmentMiFiltering(
            "trackingNumber.equals=" + DEFAULT_TRACKING_NUMBER,
            "trackingNumber.equals=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentMisByTrackingNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextShipmentMi = nextShipmentMiRepository.saveAndFlush(nextShipmentMi);

        // Get all the nextShipmentMiList where trackingNumber in
        defaultNextShipmentMiFiltering(
            "trackingNumber.in=" + DEFAULT_TRACKING_NUMBER + "," + UPDATED_TRACKING_NUMBER,
            "trackingNumber.in=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentMisByTrackingNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextShipmentMi = nextShipmentMiRepository.saveAndFlush(nextShipmentMi);

        // Get all the nextShipmentMiList where trackingNumber is not null
        defaultNextShipmentMiFiltering("trackingNumber.specified=true", "trackingNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextShipmentMisByTrackingNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentMi = nextShipmentMiRepository.saveAndFlush(nextShipmentMi);

        // Get all the nextShipmentMiList where trackingNumber contains
        defaultNextShipmentMiFiltering(
            "trackingNumber.contains=" + DEFAULT_TRACKING_NUMBER,
            "trackingNumber.contains=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentMisByTrackingNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentMi = nextShipmentMiRepository.saveAndFlush(nextShipmentMi);

        // Get all the nextShipmentMiList where trackingNumber does not contain
        defaultNextShipmentMiFiltering(
            "trackingNumber.doesNotContain=" + UPDATED_TRACKING_NUMBER,
            "trackingNumber.doesNotContain=" + DEFAULT_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentMisByShippedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentMi = nextShipmentMiRepository.saveAndFlush(nextShipmentMi);

        // Get all the nextShipmentMiList where shippedDate equals to
        defaultNextShipmentMiFiltering("shippedDate.equals=" + DEFAULT_SHIPPED_DATE, "shippedDate.equals=" + UPDATED_SHIPPED_DATE);
    }

    @Test
    @Transactional
    void getAllNextShipmentMisByShippedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextShipmentMi = nextShipmentMiRepository.saveAndFlush(nextShipmentMi);

        // Get all the nextShipmentMiList where shippedDate in
        defaultNextShipmentMiFiltering(
            "shippedDate.in=" + DEFAULT_SHIPPED_DATE + "," + UPDATED_SHIPPED_DATE,
            "shippedDate.in=" + UPDATED_SHIPPED_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentMisByShippedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextShipmentMi = nextShipmentMiRepository.saveAndFlush(nextShipmentMi);

        // Get all the nextShipmentMiList where shippedDate is not null
        defaultNextShipmentMiFiltering("shippedDate.specified=true", "shippedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextShipmentMisByDeliveryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentMi = nextShipmentMiRepository.saveAndFlush(nextShipmentMi);

        // Get all the nextShipmentMiList where deliveryDate equals to
        defaultNextShipmentMiFiltering("deliveryDate.equals=" + DEFAULT_DELIVERY_DATE, "deliveryDate.equals=" + UPDATED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    void getAllNextShipmentMisByDeliveryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextShipmentMi = nextShipmentMiRepository.saveAndFlush(nextShipmentMi);

        // Get all the nextShipmentMiList where deliveryDate in
        defaultNextShipmentMiFiltering(
            "deliveryDate.in=" + DEFAULT_DELIVERY_DATE + "," + UPDATED_DELIVERY_DATE,
            "deliveryDate.in=" + UPDATED_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentMisByDeliveryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextShipmentMi = nextShipmentMiRepository.saveAndFlush(nextShipmentMi);

        // Get all the nextShipmentMiList where deliveryDate is not null
        defaultNextShipmentMiFiltering("deliveryDate.specified=true", "deliveryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextShipmentMisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextShipmentMiRepository.saveAndFlush(nextShipmentMi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextShipmentMi.setTenant(tenant);
        nextShipmentMiRepository.saveAndFlush(nextShipmentMi);
        Long tenantId = tenant.getId();
        // Get all the nextShipmentMiList where tenant equals to tenantId
        defaultNextShipmentMiShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextShipmentMiList where tenant equals to (tenantId + 1)
        defaultNextShipmentMiShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextShipmentMiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextShipmentMiShouldBeFound(shouldBeFound);
        defaultNextShipmentMiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextShipmentMiShouldBeFound(String filter) throws Exception {
        restNextShipmentMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextShipmentMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));

        // Check, that the count call also returns 1
        restNextShipmentMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextShipmentMiShouldNotBeFound(String filter) throws Exception {
        restNextShipmentMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextShipmentMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextShipmentMi() throws Exception {
        // Get the nextShipmentMi
        restNextShipmentMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextShipmentMi() throws Exception {
        // Initialize the database
        insertedNextShipmentMi = nextShipmentMiRepository.saveAndFlush(nextShipmentMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextShipmentMi
        NextShipmentMi updatedNextShipmentMi = nextShipmentMiRepository.findById(nextShipmentMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextShipmentMi are not directly saved in db
        em.detach(updatedNextShipmentMi);
        updatedNextShipmentMi.trackingNumber(UPDATED_TRACKING_NUMBER).shippedDate(UPDATED_SHIPPED_DATE).deliveryDate(UPDATED_DELIVERY_DATE);
        NextShipmentMiDTO nextShipmentMiDTO = nextShipmentMiMapper.toDto(updatedNextShipmentMi);

        restNextShipmentMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextShipmentMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextShipmentMiDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextShipmentMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextShipmentMiToMatchAllProperties(updatedNextShipmentMi);
    }

    @Test
    @Transactional
    void putNonExistingNextShipmentMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentMi.setId(longCount.incrementAndGet());

        // Create the NextShipmentMi
        NextShipmentMiDTO nextShipmentMiDTO = nextShipmentMiMapper.toDto(nextShipmentMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextShipmentMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextShipmentMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextShipmentMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextShipmentMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentMi.setId(longCount.incrementAndGet());

        // Create the NextShipmentMi
        NextShipmentMiDTO nextShipmentMiDTO = nextShipmentMiMapper.toDto(nextShipmentMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextShipmentMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextShipmentMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentMi.setId(longCount.incrementAndGet());

        // Create the NextShipmentMi
        NextShipmentMiDTO nextShipmentMiDTO = nextShipmentMiMapper.toDto(nextShipmentMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipmentMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextShipmentMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextShipmentMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextShipmentMi = nextShipmentMiRepository.saveAndFlush(nextShipmentMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextShipmentMi using partial update
        NextShipmentMi partialUpdatedNextShipmentMi = new NextShipmentMi();
        partialUpdatedNextShipmentMi.setId(nextShipmentMi.getId());

        partialUpdatedNextShipmentMi.shippedDate(UPDATED_SHIPPED_DATE).deliveryDate(UPDATED_DELIVERY_DATE);

        restNextShipmentMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextShipmentMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextShipmentMi))
            )
            .andExpect(status().isOk());

        // Validate the NextShipmentMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextShipmentMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextShipmentMi, nextShipmentMi),
            getPersistedNextShipmentMi(nextShipmentMi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextShipmentMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextShipmentMi = nextShipmentMiRepository.saveAndFlush(nextShipmentMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextShipmentMi using partial update
        NextShipmentMi partialUpdatedNextShipmentMi = new NextShipmentMi();
        partialUpdatedNextShipmentMi.setId(nextShipmentMi.getId());

        partialUpdatedNextShipmentMi
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);

        restNextShipmentMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextShipmentMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextShipmentMi))
            )
            .andExpect(status().isOk());

        // Validate the NextShipmentMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextShipmentMiUpdatableFieldsEquals(partialUpdatedNextShipmentMi, getPersistedNextShipmentMi(partialUpdatedNextShipmentMi));
    }

    @Test
    @Transactional
    void patchNonExistingNextShipmentMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentMi.setId(longCount.incrementAndGet());

        // Create the NextShipmentMi
        NextShipmentMiDTO nextShipmentMiDTO = nextShipmentMiMapper.toDto(nextShipmentMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextShipmentMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextShipmentMiDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextShipmentMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextShipmentMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentMi.setId(longCount.incrementAndGet());

        // Create the NextShipmentMi
        NextShipmentMiDTO nextShipmentMiDTO = nextShipmentMiMapper.toDto(nextShipmentMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextShipmentMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextShipmentMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentMi.setId(longCount.incrementAndGet());

        // Create the NextShipmentMi
        NextShipmentMiDTO nextShipmentMiDTO = nextShipmentMiMapper.toDto(nextShipmentMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextShipmentMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextShipmentMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextShipmentMi() throws Exception {
        // Initialize the database
        insertedNextShipmentMi = nextShipmentMiRepository.saveAndFlush(nextShipmentMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextShipmentMi
        restNextShipmentMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextShipmentMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextShipmentMiRepository.count();
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

    protected NextShipmentMi getPersistedNextShipmentMi(NextShipmentMi nextShipmentMi) {
        return nextShipmentMiRepository.findById(nextShipmentMi.getId()).orElseThrow();
    }

    protected void assertPersistedNextShipmentMiToMatchAllProperties(NextShipmentMi expectedNextShipmentMi) {
        assertNextShipmentMiAllPropertiesEquals(expectedNextShipmentMi, getPersistedNextShipmentMi(expectedNextShipmentMi));
    }

    protected void assertPersistedNextShipmentMiToMatchUpdatableProperties(NextShipmentMi expectedNextShipmentMi) {
        assertNextShipmentMiAllUpdatablePropertiesEquals(expectedNextShipmentMi, getPersistedNextShipmentMi(expectedNextShipmentMi));
    }
}
