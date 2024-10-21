package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.EmployeeMiMiAsserts.*;
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
import xyz.jhmapstruct.domain.EmployeeMiMi;
import xyz.jhmapstruct.repository.EmployeeMiMiRepository;

/**
 * Integration tests for the {@link EmployeeMiMiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmployeeMiMiResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Instant DEFAULT_HIRE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_HIRE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_POSITION = "AAAAAAAAAA";
    private static final String UPDATED_POSITION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/employee-mi-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmployeeMiMiRepository employeeMiMiRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeMiMiMockMvc;

    private EmployeeMiMi employeeMiMi;

    private EmployeeMiMi insertedEmployeeMiMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeMiMi createEntity() {
        return new EmployeeMiMi()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .hireDate(DEFAULT_HIRE_DATE)
            .position(DEFAULT_POSITION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeMiMi createUpdatedEntity() {
        return new EmployeeMiMi()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);
    }

    @BeforeEach
    public void initTest() {
        employeeMiMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedEmployeeMiMi != null) {
            employeeMiMiRepository.delete(insertedEmployeeMiMi);
            insertedEmployeeMiMi = null;
        }
    }

    @Test
    @Transactional
    void createEmployeeMiMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EmployeeMiMi
        var returnedEmployeeMiMi = om.readValue(
            restEmployeeMiMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeMiMi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EmployeeMiMi.class
        );

        // Validate the EmployeeMiMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEmployeeMiMiUpdatableFieldsEquals(returnedEmployeeMiMi, getPersistedEmployeeMiMi(returnedEmployeeMiMi));

        insertedEmployeeMiMi = returnedEmployeeMiMi;
    }

    @Test
    @Transactional
    void createEmployeeMiMiWithExistingId() throws Exception {
        // Create the EmployeeMiMi with an existing ID
        employeeMiMi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeMiMi)))
            .andExpect(status().isBadRequest());

        // Validate the EmployeeMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employeeMiMi.setFirstName(null);

        // Create the EmployeeMiMi, which fails.

        restEmployeeMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeMiMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employeeMiMi.setLastName(null);

        // Create the EmployeeMiMi, which fails.

        restEmployeeMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeMiMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employeeMiMi.setEmail(null);

        // Create the EmployeeMiMi, which fails.

        restEmployeeMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeMiMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmployeeMiMis() throws Exception {
        // Initialize the database
        insertedEmployeeMiMi = employeeMiMiRepository.saveAndFlush(employeeMiMi);

        // Get all the employeeMiMiList
        restEmployeeMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeMiMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));
    }

    @Test
    @Transactional
    void getEmployeeMiMi() throws Exception {
        // Initialize the database
        insertedEmployeeMiMi = employeeMiMiRepository.saveAndFlush(employeeMiMi);

        // Get the employeeMiMi
        restEmployeeMiMiMockMvc
            .perform(get(ENTITY_API_URL_ID, employeeMiMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employeeMiMi.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.hireDate").value(DEFAULT_HIRE_DATE.toString()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION));
    }

    @Test
    @Transactional
    void getNonExistingEmployeeMiMi() throws Exception {
        // Get the employeeMiMi
        restEmployeeMiMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmployeeMiMi() throws Exception {
        // Initialize the database
        insertedEmployeeMiMi = employeeMiMiRepository.saveAndFlush(employeeMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeMiMi
        EmployeeMiMi updatedEmployeeMiMi = employeeMiMiRepository.findById(employeeMiMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmployeeMiMi are not directly saved in db
        em.detach(updatedEmployeeMiMi);
        updatedEmployeeMiMi
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);

        restEmployeeMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmployeeMiMi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedEmployeeMiMi))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmployeeMiMiToMatchAllProperties(updatedEmployeeMiMi);
    }

    @Test
    @Transactional
    void putNonExistingEmployeeMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeMiMi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeMiMi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employeeMiMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployeeMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeMiMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employeeMiMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployeeMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeMiMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMiMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeMiMi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedEmployeeMiMi = employeeMiMiRepository.saveAndFlush(employeeMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeMiMi using partial update
        EmployeeMiMi partialUpdatedEmployeeMiMi = new EmployeeMiMi();
        partialUpdatedEmployeeMiMi.setId(employeeMiMi.getId());

        partialUpdatedEmployeeMiMi.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME);

        restEmployeeMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmployeeMiMi))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployeeMiMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEmployeeMiMi, employeeMiMi),
            getPersistedEmployeeMiMi(employeeMiMi)
        );
    }

    @Test
    @Transactional
    void fullUpdateEmployeeMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedEmployeeMiMi = employeeMiMiRepository.saveAndFlush(employeeMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeMiMi using partial update
        EmployeeMiMi partialUpdatedEmployeeMiMi = new EmployeeMiMi();
        partialUpdatedEmployeeMiMi.setId(employeeMiMi.getId());

        partialUpdatedEmployeeMiMi
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);

        restEmployeeMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmployeeMiMi))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployeeMiMiUpdatableFieldsEquals(partialUpdatedEmployeeMiMi, getPersistedEmployeeMiMi(partialUpdatedEmployeeMiMi));
    }

    @Test
    @Transactional
    void patchNonExistingEmployeeMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeMiMi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeeMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employeeMiMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployeeMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeMiMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employeeMiMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployeeMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeMiMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMiMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(employeeMiMi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployeeMiMi() throws Exception {
        // Initialize the database
        insertedEmployeeMiMi = employeeMiMiRepository.saveAndFlush(employeeMiMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the employeeMiMi
        restEmployeeMiMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, employeeMiMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return employeeMiMiRepository.count();
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

    protected EmployeeMiMi getPersistedEmployeeMiMi(EmployeeMiMi employeeMiMi) {
        return employeeMiMiRepository.findById(employeeMiMi.getId()).orElseThrow();
    }

    protected void assertPersistedEmployeeMiMiToMatchAllProperties(EmployeeMiMi expectedEmployeeMiMi) {
        assertEmployeeMiMiAllPropertiesEquals(expectedEmployeeMiMi, getPersistedEmployeeMiMi(expectedEmployeeMiMi));
    }

    protected void assertPersistedEmployeeMiMiToMatchUpdatableProperties(EmployeeMiMi expectedEmployeeMiMi) {
        assertEmployeeMiMiAllUpdatablePropertiesEquals(expectedEmployeeMiMi, getPersistedEmployeeMiMi(expectedEmployeeMiMi));
    }
}
