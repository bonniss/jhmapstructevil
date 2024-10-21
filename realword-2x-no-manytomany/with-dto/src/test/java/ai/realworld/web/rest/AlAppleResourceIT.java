package ai.realworld.web.rest;

import static ai.realworld.domain.AlAppleAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlAlexType;
import ai.realworld.domain.AlApple;
import ai.realworld.domain.AndreiRightHand;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.repository.AlAppleRepository;
import ai.realworld.service.dto.AlAppleDTO;
import ai.realworld.service.mapper.AlAppleMapper;
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
 * Integration tests for the {@link AlAppleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlAppleResourceIT {

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

    private static final String ENTITY_API_URL = "/api/al-apples";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlAppleRepository alAppleRepository;

    @Autowired
    private AlAppleMapper alAppleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlAppleMockMvc;

    private AlApple alApple;

    private AlApple insertedAlApple;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlApple createEntity(EntityManager em) {
        AlApple alApple = new AlApple()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .hotline(DEFAULT_HOTLINE)
            .taxCode(DEFAULT_TAX_CODE)
            .contactsJason(DEFAULT_CONTACTS_JASON)
            .extensionJason(DEFAULT_EXTENSION_JASON)
            .isEnabled(DEFAULT_IS_ENABLED);
        // Add required entity
        AlAlexType alAlexType;
        if (TestUtil.findAll(em, AlAlexType.class).isEmpty()) {
            alAlexType = AlAlexTypeResourceIT.createEntity();
            em.persist(alAlexType);
            em.flush();
        } else {
            alAlexType = TestUtil.findAll(em, AlAlexType.class).get(0);
        }
        alApple.setAgencyType(alAlexType);
        return alApple;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlApple createUpdatedEntity(EntityManager em) {
        AlApple updatedAlApple = new AlApple()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .hotline(UPDATED_HOTLINE)
            .taxCode(UPDATED_TAX_CODE)
            .contactsJason(UPDATED_CONTACTS_JASON)
            .extensionJason(UPDATED_EXTENSION_JASON)
            .isEnabled(UPDATED_IS_ENABLED);
        // Add required entity
        AlAlexType alAlexType;
        if (TestUtil.findAll(em, AlAlexType.class).isEmpty()) {
            alAlexType = AlAlexTypeResourceIT.createUpdatedEntity();
            em.persist(alAlexType);
            em.flush();
        } else {
            alAlexType = TestUtil.findAll(em, AlAlexType.class).get(0);
        }
        updatedAlApple.setAgencyType(alAlexType);
        return updatedAlApple;
    }

    @BeforeEach
    public void initTest() {
        alApple = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlApple != null) {
            alAppleRepository.delete(insertedAlApple);
            insertedAlApple = null;
        }
    }

    @Test
    @Transactional
    void createAlApple() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlApple
        AlAppleDTO alAppleDTO = alAppleMapper.toDto(alApple);
        var returnedAlAppleDTO = om.readValue(
            restAlAppleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alAppleDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlAppleDTO.class
        );

        // Validate the AlApple in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlApple = alAppleMapper.toEntity(returnedAlAppleDTO);
        assertAlAppleUpdatableFieldsEquals(returnedAlApple, getPersistedAlApple(returnedAlApple));

        insertedAlApple = returnedAlApple;
    }

    @Test
    @Transactional
    void createAlAppleWithExistingId() throws Exception {
        // Create the AlApple with an existing ID
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);
        AlAppleDTO alAppleDTO = alAppleMapper.toDto(alApple);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlAppleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alAppleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlApple in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alApple.setName(null);

        // Create the AlApple, which fails.
        AlAppleDTO alAppleDTO = alAppleMapper.toDto(alApple);

        restAlAppleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alAppleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlApples() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        // Get all the alAppleList
        restAlAppleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alApple.getId().toString())))
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
    void getAlApple() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        // Get the alApple
        restAlAppleMockMvc
            .perform(get(ENTITY_API_URL_ID, alApple.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alApple.getId().toString()))
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
    void getAlApplesByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        UUID id = alApple.getId();

        defaultAlAppleFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllAlApplesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        // Get all the alAppleList where name equals to
        defaultAlAppleFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlApplesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        // Get all the alAppleList where name in
        defaultAlAppleFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlApplesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        // Get all the alAppleList where name is not null
        defaultAlAppleFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAlApplesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        // Get all the alAppleList where name contains
        defaultAlAppleFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlApplesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        // Get all the alAppleList where name does not contain
        defaultAlAppleFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAlApplesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        // Get all the alAppleList where description equals to
        defaultAlAppleFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAlApplesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        // Get all the alAppleList where description in
        defaultAlAppleFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllAlApplesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        // Get all the alAppleList where description is not null
        defaultAlAppleFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllAlApplesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        // Get all the alAppleList where description contains
        defaultAlAppleFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAlApplesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        // Get all the alAppleList where description does not contain
        defaultAlAppleFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAlApplesByHotlineIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        // Get all the alAppleList where hotline equals to
        defaultAlAppleFiltering("hotline.equals=" + DEFAULT_HOTLINE, "hotline.equals=" + UPDATED_HOTLINE);
    }

    @Test
    @Transactional
    void getAllAlApplesByHotlineIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        // Get all the alAppleList where hotline in
        defaultAlAppleFiltering("hotline.in=" + DEFAULT_HOTLINE + "," + UPDATED_HOTLINE, "hotline.in=" + UPDATED_HOTLINE);
    }

    @Test
    @Transactional
    void getAllAlApplesByHotlineIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        // Get all the alAppleList where hotline is not null
        defaultAlAppleFiltering("hotline.specified=true", "hotline.specified=false");
    }

    @Test
    @Transactional
    void getAllAlApplesByHotlineContainsSomething() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        // Get all the alAppleList where hotline contains
        defaultAlAppleFiltering("hotline.contains=" + DEFAULT_HOTLINE, "hotline.contains=" + UPDATED_HOTLINE);
    }

    @Test
    @Transactional
    void getAllAlApplesByHotlineNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        // Get all the alAppleList where hotline does not contain
        defaultAlAppleFiltering("hotline.doesNotContain=" + UPDATED_HOTLINE, "hotline.doesNotContain=" + DEFAULT_HOTLINE);
    }

    @Test
    @Transactional
    void getAllAlApplesByTaxCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        // Get all the alAppleList where taxCode equals to
        defaultAlAppleFiltering("taxCode.equals=" + DEFAULT_TAX_CODE, "taxCode.equals=" + UPDATED_TAX_CODE);
    }

    @Test
    @Transactional
    void getAllAlApplesByTaxCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        // Get all the alAppleList where taxCode in
        defaultAlAppleFiltering("taxCode.in=" + DEFAULT_TAX_CODE + "," + UPDATED_TAX_CODE, "taxCode.in=" + UPDATED_TAX_CODE);
    }

    @Test
    @Transactional
    void getAllAlApplesByTaxCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        // Get all the alAppleList where taxCode is not null
        defaultAlAppleFiltering("taxCode.specified=true", "taxCode.specified=false");
    }

    @Test
    @Transactional
    void getAllAlApplesByTaxCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        // Get all the alAppleList where taxCode contains
        defaultAlAppleFiltering("taxCode.contains=" + DEFAULT_TAX_CODE, "taxCode.contains=" + UPDATED_TAX_CODE);
    }

    @Test
    @Transactional
    void getAllAlApplesByTaxCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        // Get all the alAppleList where taxCode does not contain
        defaultAlAppleFiltering("taxCode.doesNotContain=" + UPDATED_TAX_CODE, "taxCode.doesNotContain=" + DEFAULT_TAX_CODE);
    }

    @Test
    @Transactional
    void getAllAlApplesByContactsJasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        // Get all the alAppleList where contactsJason equals to
        defaultAlAppleFiltering("contactsJason.equals=" + DEFAULT_CONTACTS_JASON, "contactsJason.equals=" + UPDATED_CONTACTS_JASON);
    }

    @Test
    @Transactional
    void getAllAlApplesByContactsJasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        // Get all the alAppleList where contactsJason in
        defaultAlAppleFiltering(
            "contactsJason.in=" + DEFAULT_CONTACTS_JASON + "," + UPDATED_CONTACTS_JASON,
            "contactsJason.in=" + UPDATED_CONTACTS_JASON
        );
    }

    @Test
    @Transactional
    void getAllAlApplesByContactsJasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        // Get all the alAppleList where contactsJason is not null
        defaultAlAppleFiltering("contactsJason.specified=true", "contactsJason.specified=false");
    }

    @Test
    @Transactional
    void getAllAlApplesByContactsJasonContainsSomething() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        // Get all the alAppleList where contactsJason contains
        defaultAlAppleFiltering("contactsJason.contains=" + DEFAULT_CONTACTS_JASON, "contactsJason.contains=" + UPDATED_CONTACTS_JASON);
    }

    @Test
    @Transactional
    void getAllAlApplesByContactsJasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        // Get all the alAppleList where contactsJason does not contain
        defaultAlAppleFiltering(
            "contactsJason.doesNotContain=" + UPDATED_CONTACTS_JASON,
            "contactsJason.doesNotContain=" + DEFAULT_CONTACTS_JASON
        );
    }

    @Test
    @Transactional
    void getAllAlApplesByExtensionJasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        // Get all the alAppleList where extensionJason equals to
        defaultAlAppleFiltering("extensionJason.equals=" + DEFAULT_EXTENSION_JASON, "extensionJason.equals=" + UPDATED_EXTENSION_JASON);
    }

    @Test
    @Transactional
    void getAllAlApplesByExtensionJasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        // Get all the alAppleList where extensionJason in
        defaultAlAppleFiltering(
            "extensionJason.in=" + DEFAULT_EXTENSION_JASON + "," + UPDATED_EXTENSION_JASON,
            "extensionJason.in=" + UPDATED_EXTENSION_JASON
        );
    }

    @Test
    @Transactional
    void getAllAlApplesByExtensionJasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        // Get all the alAppleList where extensionJason is not null
        defaultAlAppleFiltering("extensionJason.specified=true", "extensionJason.specified=false");
    }

    @Test
    @Transactional
    void getAllAlApplesByExtensionJasonContainsSomething() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        // Get all the alAppleList where extensionJason contains
        defaultAlAppleFiltering("extensionJason.contains=" + DEFAULT_EXTENSION_JASON, "extensionJason.contains=" + UPDATED_EXTENSION_JASON);
    }

    @Test
    @Transactional
    void getAllAlApplesByExtensionJasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        // Get all the alAppleList where extensionJason does not contain
        defaultAlAppleFiltering(
            "extensionJason.doesNotContain=" + UPDATED_EXTENSION_JASON,
            "extensionJason.doesNotContain=" + DEFAULT_EXTENSION_JASON
        );
    }

    @Test
    @Transactional
    void getAllAlApplesByIsEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        // Get all the alAppleList where isEnabled equals to
        defaultAlAppleFiltering("isEnabled.equals=" + DEFAULT_IS_ENABLED, "isEnabled.equals=" + UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    void getAllAlApplesByIsEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        // Get all the alAppleList where isEnabled in
        defaultAlAppleFiltering("isEnabled.in=" + DEFAULT_IS_ENABLED + "," + UPDATED_IS_ENABLED, "isEnabled.in=" + UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    void getAllAlApplesByIsEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        // Get all the alAppleList where isEnabled is not null
        defaultAlAppleFiltering("isEnabled.specified=true", "isEnabled.specified=false");
    }

    @Test
    @Transactional
    void getAllAlApplesByAddressIsEqualToSomething() throws Exception {
        AndreiRightHand address;
        if (TestUtil.findAll(em, AndreiRightHand.class).isEmpty()) {
            alAppleRepository.saveAndFlush(alApple);
            address = AndreiRightHandResourceIT.createEntity();
        } else {
            address = TestUtil.findAll(em, AndreiRightHand.class).get(0);
        }
        em.persist(address);
        em.flush();
        alApple.setAddress(address);
        alAppleRepository.saveAndFlush(alApple);
        Long addressId = address.getId();
        // Get all the alAppleList where address equals to addressId
        defaultAlAppleShouldBeFound("addressId.equals=" + addressId);

        // Get all the alAppleList where address equals to (addressId + 1)
        defaultAlAppleShouldNotBeFound("addressId.equals=" + (addressId + 1));
    }

    @Test
    @Transactional
    void getAllAlApplesByAgencyTypeIsEqualToSomething() throws Exception {
        AlAlexType agencyType;
        if (TestUtil.findAll(em, AlAlexType.class).isEmpty()) {
            alAppleRepository.saveAndFlush(alApple);
            agencyType = AlAlexTypeResourceIT.createEntity();
        } else {
            agencyType = TestUtil.findAll(em, AlAlexType.class).get(0);
        }
        em.persist(agencyType);
        em.flush();
        alApple.setAgencyType(agencyType);
        alAppleRepository.saveAndFlush(alApple);
        UUID agencyTypeId = agencyType.getId();
        // Get all the alAppleList where agencyType equals to agencyTypeId
        defaultAlAppleShouldBeFound("agencyTypeId.equals=" + agencyTypeId);

        // Get all the alAppleList where agencyType equals to UUID.randomUUID()
        defaultAlAppleShouldNotBeFound("agencyTypeId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlApplesByLogoIsEqualToSomething() throws Exception {
        Metaverse logo;
        if (TestUtil.findAll(em, Metaverse.class).isEmpty()) {
            alAppleRepository.saveAndFlush(alApple);
            logo = MetaverseResourceIT.createEntity();
        } else {
            logo = TestUtil.findAll(em, Metaverse.class).get(0);
        }
        em.persist(logo);
        em.flush();
        alApple.setLogo(logo);
        alAppleRepository.saveAndFlush(alApple);
        Long logoId = logo.getId();
        // Get all the alAppleList where logo equals to logoId
        defaultAlAppleShouldBeFound("logoId.equals=" + logoId);

        // Get all the alAppleList where logo equals to (logoId + 1)
        defaultAlAppleShouldNotBeFound("logoId.equals=" + (logoId + 1));
    }

    @Test
    @Transactional
    void getAllAlApplesByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alAppleRepository.saveAndFlush(alApple);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alApple.setApplication(application);
        alAppleRepository.saveAndFlush(alApple);
        UUID applicationId = application.getId();
        // Get all the alAppleList where application equals to applicationId
        defaultAlAppleShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alAppleList where application equals to UUID.randomUUID()
        defaultAlAppleShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlAppleFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlAppleShouldBeFound(shouldBeFound);
        defaultAlAppleShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlAppleShouldBeFound(String filter) throws Exception {
        restAlAppleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alApple.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].hotline").value(hasItem(DEFAULT_HOTLINE)))
            .andExpect(jsonPath("$.[*].taxCode").value(hasItem(DEFAULT_TAX_CODE)))
            .andExpect(jsonPath("$.[*].contactsJason").value(hasItem(DEFAULT_CONTACTS_JASON)))
            .andExpect(jsonPath("$.[*].extensionJason").value(hasItem(DEFAULT_EXTENSION_JASON)))
            .andExpect(jsonPath("$.[*].isEnabled").value(hasItem(DEFAULT_IS_ENABLED.booleanValue())));

        // Check, that the count call also returns 1
        restAlAppleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlAppleShouldNotBeFound(String filter) throws Exception {
        restAlAppleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlAppleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlApple() throws Exception {
        // Get the alApple
        restAlAppleMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlApple() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alApple
        AlApple updatedAlApple = alAppleRepository.findById(alApple.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlApple are not directly saved in db
        em.detach(updatedAlApple);
        updatedAlApple
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .hotline(UPDATED_HOTLINE)
            .taxCode(UPDATED_TAX_CODE)
            .contactsJason(UPDATED_CONTACTS_JASON)
            .extensionJason(UPDATED_EXTENSION_JASON)
            .isEnabled(UPDATED_IS_ENABLED);
        AlAppleDTO alAppleDTO = alAppleMapper.toDto(updatedAlApple);

        restAlAppleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alAppleDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alAppleDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlApple in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlAppleToMatchAllProperties(updatedAlApple);
    }

    @Test
    @Transactional
    void putNonExistingAlApple() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alApple.setId(UUID.randomUUID());

        // Create the AlApple
        AlAppleDTO alAppleDTO = alAppleMapper.toDto(alApple);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlAppleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alAppleDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alAppleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlApple in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlApple() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alApple.setId(UUID.randomUUID());

        // Create the AlApple
        AlAppleDTO alAppleDTO = alAppleMapper.toDto(alApple);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlAppleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alAppleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlApple in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlApple() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alApple.setId(UUID.randomUUID());

        // Create the AlApple
        AlAppleDTO alAppleDTO = alAppleMapper.toDto(alApple);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlAppleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alAppleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlApple in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlAppleWithPatch() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alApple using partial update
        AlApple partialUpdatedAlApple = new AlApple();
        partialUpdatedAlApple.setId(alApple.getId());

        partialUpdatedAlApple.hotline(UPDATED_HOTLINE).taxCode(UPDATED_TAX_CODE).extensionJason(UPDATED_EXTENSION_JASON);

        restAlAppleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlApple.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlApple))
            )
            .andExpect(status().isOk());

        // Validate the AlApple in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlAppleUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAlApple, alApple), getPersistedAlApple(alApple));
    }

    @Test
    @Transactional
    void fullUpdateAlAppleWithPatch() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alApple using partial update
        AlApple partialUpdatedAlApple = new AlApple();
        partialUpdatedAlApple.setId(alApple.getId());

        partialUpdatedAlApple
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .hotline(UPDATED_HOTLINE)
            .taxCode(UPDATED_TAX_CODE)
            .contactsJason(UPDATED_CONTACTS_JASON)
            .extensionJason(UPDATED_EXTENSION_JASON)
            .isEnabled(UPDATED_IS_ENABLED);

        restAlAppleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlApple.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlApple))
            )
            .andExpect(status().isOk());

        // Validate the AlApple in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlAppleUpdatableFieldsEquals(partialUpdatedAlApple, getPersistedAlApple(partialUpdatedAlApple));
    }

    @Test
    @Transactional
    void patchNonExistingAlApple() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alApple.setId(UUID.randomUUID());

        // Create the AlApple
        AlAppleDTO alAppleDTO = alAppleMapper.toDto(alApple);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlAppleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alAppleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alAppleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlApple in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlApple() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alApple.setId(UUID.randomUUID());

        // Create the AlApple
        AlAppleDTO alAppleDTO = alAppleMapper.toDto(alApple);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlAppleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alAppleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlApple in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlApple() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alApple.setId(UUID.randomUUID());

        // Create the AlApple
        AlAppleDTO alAppleDTO = alAppleMapper.toDto(alApple);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlAppleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alAppleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlApple in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlApple() throws Exception {
        // Initialize the database
        insertedAlApple = alAppleRepository.saveAndFlush(alApple);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alApple
        restAlAppleMockMvc
            .perform(delete(ENTITY_API_URL_ID, alApple.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alAppleRepository.count();
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

    protected AlApple getPersistedAlApple(AlApple alApple) {
        return alAppleRepository.findById(alApple.getId()).orElseThrow();
    }

    protected void assertPersistedAlAppleToMatchAllProperties(AlApple expectedAlApple) {
        assertAlAppleAllPropertiesEquals(expectedAlApple, getPersistedAlApple(expectedAlApple));
    }

    protected void assertPersistedAlAppleToMatchUpdatableProperties(AlApple expectedAlApple) {
        assertAlAppleAllUpdatablePropertiesEquals(expectedAlApple, getPersistedAlApple(expectedAlApple));
    }
}
