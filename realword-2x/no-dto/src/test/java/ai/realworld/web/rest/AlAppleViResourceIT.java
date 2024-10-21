package ai.realworld.web.rest;

import static ai.realworld.domain.AlAppleViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlAlexTypeVi;
import ai.realworld.domain.AlAppleVi;
import ai.realworld.domain.AndreiRightHandVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.repository.AlAppleViRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AlAppleViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlAppleViResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_HOTLINE = "AAAAAAAAAA";
    private static final String UPDATED_HOTLINE = "BBBBBBBBBB";

    private static final String DEFAULT_TAX_CODE = "AAAAAAAAAA";
    private static final String UPDATED_TAX_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACTS_JASON = "AAAAAAAAAA";
    private static final String UPDATED_CONTACTS_JASON = "BBBBBBBBBB";

    private static final String DEFAULT_EXTENSION_JASON = "AAAAAAAAAA";
    private static final String UPDATED_EXTENSION_JASON = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ENABLED = false;
    private static final Boolean UPDATED_IS_ENABLED = true;

    private static final String ENTITY_API_URL = "/api/al-apple-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlAppleViRepository alAppleViRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlAppleViMockMvc;

    private AlAppleVi alAppleVi;

    private AlAppleVi insertedAlAppleVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlAppleVi createEntity(EntityManager em) {
        AlAppleVi alAppleVi = new AlAppleVi()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .hotline(DEFAULT_HOTLINE)
            .taxCode(DEFAULT_TAX_CODE)
            .contactsJason(DEFAULT_CONTACTS_JASON)
            .extensionJason(DEFAULT_EXTENSION_JASON)
            .isEnabled(DEFAULT_IS_ENABLED);
        // Add required entity
        AlAlexTypeVi alAlexTypeVi;
        if (TestUtil.findAll(em, AlAlexTypeVi.class).isEmpty()) {
            alAlexTypeVi = AlAlexTypeViResourceIT.createEntity();
            em.persist(alAlexTypeVi);
            em.flush();
        } else {
            alAlexTypeVi = TestUtil.findAll(em, AlAlexTypeVi.class).get(0);
        }
        alAppleVi.setAgencyType(alAlexTypeVi);
        return alAppleVi;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlAppleVi createUpdatedEntity(EntityManager em) {
        AlAppleVi updatedAlAppleVi = new AlAppleVi()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .hotline(UPDATED_HOTLINE)
            .taxCode(UPDATED_TAX_CODE)
            .contactsJason(UPDATED_CONTACTS_JASON)
            .extensionJason(UPDATED_EXTENSION_JASON)
            .isEnabled(UPDATED_IS_ENABLED);
        // Add required entity
        AlAlexTypeVi alAlexTypeVi;
        if (TestUtil.findAll(em, AlAlexTypeVi.class).isEmpty()) {
            alAlexTypeVi = AlAlexTypeViResourceIT.createUpdatedEntity();
            em.persist(alAlexTypeVi);
            em.flush();
        } else {
            alAlexTypeVi = TestUtil.findAll(em, AlAlexTypeVi.class).get(0);
        }
        updatedAlAppleVi.setAgencyType(alAlexTypeVi);
        return updatedAlAppleVi;
    }

    @BeforeEach
    public void initTest() {
        alAppleVi = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlAppleVi != null) {
            alAppleViRepository.delete(insertedAlAppleVi);
            insertedAlAppleVi = null;
        }
    }

    @Test
    @Transactional
    void createAlAppleVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlAppleVi
        var returnedAlAppleVi = om.readValue(
            restAlAppleViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alAppleVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlAppleVi.class
        );

        // Validate the AlAppleVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlAppleViUpdatableFieldsEquals(returnedAlAppleVi, getPersistedAlAppleVi(returnedAlAppleVi));

        insertedAlAppleVi = returnedAlAppleVi;
    }

    @Test
    @Transactional
    void createAlAppleViWithExistingId() throws Exception {
        // Create the AlAppleVi with an existing ID
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlAppleViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alAppleVi)))
            .andExpect(status().isBadRequest());

        // Validate the AlAppleVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alAppleVi.setName(null);

        // Create the AlAppleVi, which fails.

        restAlAppleViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alAppleVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlAppleVis() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        // Get all the alAppleViList
        restAlAppleViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alAppleVi.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].hotline").value(hasItem(DEFAULT_HOTLINE)))
            .andExpect(jsonPath("$.[*].taxCode").value(hasItem(DEFAULT_TAX_CODE)))
            .andExpect(jsonPath("$.[*].contactsJason").value(hasItem(DEFAULT_CONTACTS_JASON)))
            .andExpect(jsonPath("$.[*].extensionJason").value(hasItem(DEFAULT_EXTENSION_JASON)))
            .andExpect(jsonPath("$.[*].isEnabled").value(hasItem(DEFAULT_IS_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    void getAlAppleVi() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        // Get the alAppleVi
        restAlAppleViMockMvc
            .perform(get(ENTITY_API_URL_ID, alAppleVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alAppleVi.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.hotline").value(DEFAULT_HOTLINE))
            .andExpect(jsonPath("$.taxCode").value(DEFAULT_TAX_CODE))
            .andExpect(jsonPath("$.contactsJason").value(DEFAULT_CONTACTS_JASON))
            .andExpect(jsonPath("$.extensionJason").value(DEFAULT_EXTENSION_JASON))
            .andExpect(jsonPath("$.isEnabled").value(DEFAULT_IS_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    void getAlAppleVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        UUID id = alAppleVi.getId();

        defaultAlAppleViFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllAlAppleVisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        // Get all the alAppleViList where name equals to
        defaultAlAppleViFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlAppleVisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        // Get all the alAppleViList where name in
        defaultAlAppleViFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlAppleVisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        // Get all the alAppleViList where name is not null
        defaultAlAppleViFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAlAppleVisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        // Get all the alAppleViList where name contains
        defaultAlAppleViFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlAppleVisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        // Get all the alAppleViList where name does not contain
        defaultAlAppleViFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAlAppleVisByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        // Get all the alAppleViList where description equals to
        defaultAlAppleViFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAlAppleVisByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        // Get all the alAppleViList where description in
        defaultAlAppleViFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllAlAppleVisByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        // Get all the alAppleViList where description is not null
        defaultAlAppleViFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllAlAppleVisByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        // Get all the alAppleViList where description contains
        defaultAlAppleViFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAlAppleVisByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        // Get all the alAppleViList where description does not contain
        defaultAlAppleViFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAlAppleVisByHotlineIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        // Get all the alAppleViList where hotline equals to
        defaultAlAppleViFiltering("hotline.equals=" + DEFAULT_HOTLINE, "hotline.equals=" + UPDATED_HOTLINE);
    }

    @Test
    @Transactional
    void getAllAlAppleVisByHotlineIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        // Get all the alAppleViList where hotline in
        defaultAlAppleViFiltering("hotline.in=" + DEFAULT_HOTLINE + "," + UPDATED_HOTLINE, "hotline.in=" + UPDATED_HOTLINE);
    }

    @Test
    @Transactional
    void getAllAlAppleVisByHotlineIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        // Get all the alAppleViList where hotline is not null
        defaultAlAppleViFiltering("hotline.specified=true", "hotline.specified=false");
    }

    @Test
    @Transactional
    void getAllAlAppleVisByHotlineContainsSomething() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        // Get all the alAppleViList where hotline contains
        defaultAlAppleViFiltering("hotline.contains=" + DEFAULT_HOTLINE, "hotline.contains=" + UPDATED_HOTLINE);
    }

    @Test
    @Transactional
    void getAllAlAppleVisByHotlineNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        // Get all the alAppleViList where hotline does not contain
        defaultAlAppleViFiltering("hotline.doesNotContain=" + UPDATED_HOTLINE, "hotline.doesNotContain=" + DEFAULT_HOTLINE);
    }

    @Test
    @Transactional
    void getAllAlAppleVisByTaxCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        // Get all the alAppleViList where taxCode equals to
        defaultAlAppleViFiltering("taxCode.equals=" + DEFAULT_TAX_CODE, "taxCode.equals=" + UPDATED_TAX_CODE);
    }

    @Test
    @Transactional
    void getAllAlAppleVisByTaxCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        // Get all the alAppleViList where taxCode in
        defaultAlAppleViFiltering("taxCode.in=" + DEFAULT_TAX_CODE + "," + UPDATED_TAX_CODE, "taxCode.in=" + UPDATED_TAX_CODE);
    }

    @Test
    @Transactional
    void getAllAlAppleVisByTaxCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        // Get all the alAppleViList where taxCode is not null
        defaultAlAppleViFiltering("taxCode.specified=true", "taxCode.specified=false");
    }

    @Test
    @Transactional
    void getAllAlAppleVisByTaxCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        // Get all the alAppleViList where taxCode contains
        defaultAlAppleViFiltering("taxCode.contains=" + DEFAULT_TAX_CODE, "taxCode.contains=" + UPDATED_TAX_CODE);
    }

    @Test
    @Transactional
    void getAllAlAppleVisByTaxCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        // Get all the alAppleViList where taxCode does not contain
        defaultAlAppleViFiltering("taxCode.doesNotContain=" + UPDATED_TAX_CODE, "taxCode.doesNotContain=" + DEFAULT_TAX_CODE);
    }

    @Test
    @Transactional
    void getAllAlAppleVisByContactsJasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        // Get all the alAppleViList where contactsJason equals to
        defaultAlAppleViFiltering("contactsJason.equals=" + DEFAULT_CONTACTS_JASON, "contactsJason.equals=" + UPDATED_CONTACTS_JASON);
    }

    @Test
    @Transactional
    void getAllAlAppleVisByContactsJasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        // Get all the alAppleViList where contactsJason in
        defaultAlAppleViFiltering(
            "contactsJason.in=" + DEFAULT_CONTACTS_JASON + "," + UPDATED_CONTACTS_JASON,
            "contactsJason.in=" + UPDATED_CONTACTS_JASON
        );
    }

    @Test
    @Transactional
    void getAllAlAppleVisByContactsJasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        // Get all the alAppleViList where contactsJason is not null
        defaultAlAppleViFiltering("contactsJason.specified=true", "contactsJason.specified=false");
    }

    @Test
    @Transactional
    void getAllAlAppleVisByContactsJasonContainsSomething() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        // Get all the alAppleViList where contactsJason contains
        defaultAlAppleViFiltering("contactsJason.contains=" + DEFAULT_CONTACTS_JASON, "contactsJason.contains=" + UPDATED_CONTACTS_JASON);
    }

    @Test
    @Transactional
    void getAllAlAppleVisByContactsJasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        // Get all the alAppleViList where contactsJason does not contain
        defaultAlAppleViFiltering(
            "contactsJason.doesNotContain=" + UPDATED_CONTACTS_JASON,
            "contactsJason.doesNotContain=" + DEFAULT_CONTACTS_JASON
        );
    }

    @Test
    @Transactional
    void getAllAlAppleVisByExtensionJasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        // Get all the alAppleViList where extensionJason equals to
        defaultAlAppleViFiltering("extensionJason.equals=" + DEFAULT_EXTENSION_JASON, "extensionJason.equals=" + UPDATED_EXTENSION_JASON);
    }

    @Test
    @Transactional
    void getAllAlAppleVisByExtensionJasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        // Get all the alAppleViList where extensionJason in
        defaultAlAppleViFiltering(
            "extensionJason.in=" + DEFAULT_EXTENSION_JASON + "," + UPDATED_EXTENSION_JASON,
            "extensionJason.in=" + UPDATED_EXTENSION_JASON
        );
    }

    @Test
    @Transactional
    void getAllAlAppleVisByExtensionJasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        // Get all the alAppleViList where extensionJason is not null
        defaultAlAppleViFiltering("extensionJason.specified=true", "extensionJason.specified=false");
    }

    @Test
    @Transactional
    void getAllAlAppleVisByExtensionJasonContainsSomething() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        // Get all the alAppleViList where extensionJason contains
        defaultAlAppleViFiltering(
            "extensionJason.contains=" + DEFAULT_EXTENSION_JASON,
            "extensionJason.contains=" + UPDATED_EXTENSION_JASON
        );
    }

    @Test
    @Transactional
    void getAllAlAppleVisByExtensionJasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        // Get all the alAppleViList where extensionJason does not contain
        defaultAlAppleViFiltering(
            "extensionJason.doesNotContain=" + UPDATED_EXTENSION_JASON,
            "extensionJason.doesNotContain=" + DEFAULT_EXTENSION_JASON
        );
    }

    @Test
    @Transactional
    void getAllAlAppleVisByIsEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        // Get all the alAppleViList where isEnabled equals to
        defaultAlAppleViFiltering("isEnabled.equals=" + DEFAULT_IS_ENABLED, "isEnabled.equals=" + UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    void getAllAlAppleVisByIsEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        // Get all the alAppleViList where isEnabled in
        defaultAlAppleViFiltering("isEnabled.in=" + DEFAULT_IS_ENABLED + "," + UPDATED_IS_ENABLED, "isEnabled.in=" + UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    void getAllAlAppleVisByIsEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        // Get all the alAppleViList where isEnabled is not null
        defaultAlAppleViFiltering("isEnabled.specified=true", "isEnabled.specified=false");
    }

    @Test
    @Transactional
    void getAllAlAppleVisByAddressIsEqualToSomething() throws Exception {
        AndreiRightHandVi address;
        if (TestUtil.findAll(em, AndreiRightHandVi.class).isEmpty()) {
            alAppleViRepository.saveAndFlush(alAppleVi);
            address = AndreiRightHandViResourceIT.createEntity();
        } else {
            address = TestUtil.findAll(em, AndreiRightHandVi.class).get(0);
        }
        em.persist(address);
        em.flush();
        alAppleVi.setAddress(address);
        alAppleViRepository.saveAndFlush(alAppleVi);
        Long addressId = address.getId();
        // Get all the alAppleViList where address equals to addressId
        defaultAlAppleViShouldBeFound("addressId.equals=" + addressId);

        // Get all the alAppleViList where address equals to (addressId + 1)
        defaultAlAppleViShouldNotBeFound("addressId.equals=" + (addressId + 1));
    }

    @Test
    @Transactional
    void getAllAlAppleVisByAgencyTypeIsEqualToSomething() throws Exception {
        AlAlexTypeVi agencyType;
        if (TestUtil.findAll(em, AlAlexTypeVi.class).isEmpty()) {
            alAppleViRepository.saveAndFlush(alAppleVi);
            agencyType = AlAlexTypeViResourceIT.createEntity();
        } else {
            agencyType = TestUtil.findAll(em, AlAlexTypeVi.class).get(0);
        }
        em.persist(agencyType);
        em.flush();
        alAppleVi.setAgencyType(agencyType);
        alAppleViRepository.saveAndFlush(alAppleVi);
        UUID agencyTypeId = agencyType.getId();
        // Get all the alAppleViList where agencyType equals to agencyTypeId
        defaultAlAppleViShouldBeFound("agencyTypeId.equals=" + agencyTypeId);

        // Get all the alAppleViList where agencyType equals to UUID.randomUUID()
        defaultAlAppleViShouldNotBeFound("agencyTypeId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlAppleVisByLogoIsEqualToSomething() throws Exception {
        Metaverse logo;
        if (TestUtil.findAll(em, Metaverse.class).isEmpty()) {
            alAppleViRepository.saveAndFlush(alAppleVi);
            logo = MetaverseResourceIT.createEntity();
        } else {
            logo = TestUtil.findAll(em, Metaverse.class).get(0);
        }
        em.persist(logo);
        em.flush();
        alAppleVi.setLogo(logo);
        alAppleViRepository.saveAndFlush(alAppleVi);
        Long logoId = logo.getId();
        // Get all the alAppleViList where logo equals to logoId
        defaultAlAppleViShouldBeFound("logoId.equals=" + logoId);

        // Get all the alAppleViList where logo equals to (logoId + 1)
        defaultAlAppleViShouldNotBeFound("logoId.equals=" + (logoId + 1));
    }

    @Test
    @Transactional
    void getAllAlAppleVisByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alAppleViRepository.saveAndFlush(alAppleVi);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alAppleVi.setApplication(application);
        alAppleViRepository.saveAndFlush(alAppleVi);
        UUID applicationId = application.getId();
        // Get all the alAppleViList where application equals to applicationId
        defaultAlAppleViShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alAppleViList where application equals to UUID.randomUUID()
        defaultAlAppleViShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlAppleViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlAppleViShouldBeFound(shouldBeFound);
        defaultAlAppleViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlAppleViShouldBeFound(String filter) throws Exception {
        restAlAppleViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alAppleVi.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].hotline").value(hasItem(DEFAULT_HOTLINE)))
            .andExpect(jsonPath("$.[*].taxCode").value(hasItem(DEFAULT_TAX_CODE)))
            .andExpect(jsonPath("$.[*].contactsJason").value(hasItem(DEFAULT_CONTACTS_JASON)))
            .andExpect(jsonPath("$.[*].extensionJason").value(hasItem(DEFAULT_EXTENSION_JASON)))
            .andExpect(jsonPath("$.[*].isEnabled").value(hasItem(DEFAULT_IS_ENABLED.booleanValue())));

        // Check, that the count call also returns 1
        restAlAppleViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlAppleViShouldNotBeFound(String filter) throws Exception {
        restAlAppleViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlAppleViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlAppleVi() throws Exception {
        // Get the alAppleVi
        restAlAppleViMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlAppleVi() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alAppleVi
        AlAppleVi updatedAlAppleVi = alAppleViRepository.findById(alAppleVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlAppleVi are not directly saved in db
        em.detach(updatedAlAppleVi);
        updatedAlAppleVi
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .hotline(UPDATED_HOTLINE)
            .taxCode(UPDATED_TAX_CODE)
            .contactsJason(UPDATED_CONTACTS_JASON)
            .extensionJason(UPDATED_EXTENSION_JASON)
            .isEnabled(UPDATED_IS_ENABLED);

        restAlAppleViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlAppleVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlAppleVi))
            )
            .andExpect(status().isOk());

        // Validate the AlAppleVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlAppleViToMatchAllProperties(updatedAlAppleVi);
    }

    @Test
    @Transactional
    void putNonExistingAlAppleVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alAppleVi.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlAppleViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alAppleVi.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alAppleVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlAppleVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlAppleVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alAppleVi.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlAppleViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alAppleVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlAppleVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlAppleVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alAppleVi.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlAppleViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alAppleVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlAppleVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlAppleViWithPatch() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alAppleVi using partial update
        AlAppleVi partialUpdatedAlAppleVi = new AlAppleVi();
        partialUpdatedAlAppleVi.setId(alAppleVi.getId());

        partialUpdatedAlAppleVi
            .description(UPDATED_DESCRIPTION)
            .contactsJason(UPDATED_CONTACTS_JASON)
            .extensionJason(UPDATED_EXTENSION_JASON)
            .isEnabled(UPDATED_IS_ENABLED);

        restAlAppleViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlAppleVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlAppleVi))
            )
            .andExpect(status().isOk());

        // Validate the AlAppleVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlAppleViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlAppleVi, alAppleVi),
            getPersistedAlAppleVi(alAppleVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlAppleViWithPatch() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alAppleVi using partial update
        AlAppleVi partialUpdatedAlAppleVi = new AlAppleVi();
        partialUpdatedAlAppleVi.setId(alAppleVi.getId());

        partialUpdatedAlAppleVi
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .hotline(UPDATED_HOTLINE)
            .taxCode(UPDATED_TAX_CODE)
            .contactsJason(UPDATED_CONTACTS_JASON)
            .extensionJason(UPDATED_EXTENSION_JASON)
            .isEnabled(UPDATED_IS_ENABLED);

        restAlAppleViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlAppleVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlAppleVi))
            )
            .andExpect(status().isOk());

        // Validate the AlAppleVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlAppleViUpdatableFieldsEquals(partialUpdatedAlAppleVi, getPersistedAlAppleVi(partialUpdatedAlAppleVi));
    }

    @Test
    @Transactional
    void patchNonExistingAlAppleVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alAppleVi.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlAppleViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alAppleVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alAppleVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlAppleVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlAppleVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alAppleVi.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlAppleViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alAppleVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlAppleVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlAppleVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alAppleVi.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlAppleViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alAppleVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlAppleVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlAppleVi() throws Exception {
        // Initialize the database
        insertedAlAppleVi = alAppleViRepository.saveAndFlush(alAppleVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alAppleVi
        restAlAppleViMockMvc
            .perform(delete(ENTITY_API_URL_ID, alAppleVi.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alAppleViRepository.count();
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

    protected AlAppleVi getPersistedAlAppleVi(AlAppleVi alAppleVi) {
        return alAppleViRepository.findById(alAppleVi.getId()).orElseThrow();
    }

    protected void assertPersistedAlAppleViToMatchAllProperties(AlAppleVi expectedAlAppleVi) {
        assertAlAppleViAllPropertiesEquals(expectedAlAppleVi, getPersistedAlAppleVi(expectedAlAppleVi));
    }

    protected void assertPersistedAlAppleViToMatchUpdatableProperties(AlAppleVi expectedAlAppleVi) {
        assertAlAppleViAllUpdatablePropertiesEquals(expectedAlAppleVi, getPersistedAlAppleVi(expectedAlAppleVi));
    }
}
