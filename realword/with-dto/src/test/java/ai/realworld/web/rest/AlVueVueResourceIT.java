package ai.realworld.web.rest;

import static ai.realworld.domain.AlVueVueAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static ai.realworld.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlVueVue;
import ai.realworld.domain.AlVueVueUsage;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.domain.enumeration.AlcountScopy;
import ai.realworld.domain.enumeration.AlcountTypo;
import ai.realworld.domain.enumeration.PaoloStatus;
import ai.realworld.repository.AlVueVueRepository;
import ai.realworld.service.dto.AlVueVueDTO;
import ai.realworld.service.mapper.AlVueVueMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link AlVueVueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlVueVueResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_HEITIGA = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_HEITIGA = "BBBBBBBBBB";

    private static final AlcountTypo DEFAULT_DISCOUNT_TYPE = AlcountTypo.BY_FIXED_AMOUNT;
    private static final AlcountTypo UPDATED_DISCOUNT_TYPE = AlcountTypo.BY_PERCENT;

    private static final BigDecimal DEFAULT_DISCOUNT_RATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_DISCOUNT_RATE = new BigDecimal(2);
    private static final BigDecimal SMALLER_DISCOUNT_RATE = new BigDecimal(1 - 1);

    private static final AlcountScopy DEFAULT_SCOPE = AlcountScopy.ALL_PRODUCTS;
    private static final AlcountScopy UPDATED_SCOPE = AlcountScopy.SPECIFIED_PRODUCTS;

    private static final Boolean DEFAULT_IS_INDIVIDUALLY_USED_ONLY = false;
    private static final Boolean UPDATED_IS_INDIVIDUALLY_USED_ONLY = true;

    private static final Integer DEFAULT_USAGE_LIFE_TIME_LIMIT = 1;
    private static final Integer UPDATED_USAGE_LIFE_TIME_LIMIT = 2;
    private static final Integer SMALLER_USAGE_LIFE_TIME_LIMIT = 1 - 1;

    private static final Integer DEFAULT_USAGE_LIMIT_PER_USER = 1;
    private static final Integer UPDATED_USAGE_LIMIT_PER_USER = 2;
    private static final Integer SMALLER_USAGE_LIMIT_PER_USER = 1 - 1;

    private static final Integer DEFAULT_USAGE_QUANTITY = 1;
    private static final Integer UPDATED_USAGE_QUANTITY = 2;
    private static final Integer SMALLER_USAGE_QUANTITY = 1 - 1;

    private static final BigDecimal DEFAULT_MINIMUM_SPEND = new BigDecimal(1);
    private static final BigDecimal UPDATED_MINIMUM_SPEND = new BigDecimal(2);
    private static final BigDecimal SMALLER_MINIMUM_SPEND = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_MAXIMUM_SPEND = new BigDecimal(1);
    private static final BigDecimal UPDATED_MAXIMUM_SPEND = new BigDecimal(2);
    private static final BigDecimal SMALLER_MAXIMUM_SPEND = new BigDecimal(1 - 1);

    private static final Boolean DEFAULT_CAN_BE_COLLECTED_BY_USER = false;
    private static final Boolean UPDATED_CAN_BE_COLLECTED_BY_USER = true;

    private static final LocalDate DEFAULT_SALE_PRICE_FROM_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SALE_PRICE_FROM_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_SALE_PRICE_FROM_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_SALE_PRICE_TO_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SALE_PRICE_TO_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_SALE_PRICE_TO_DATE = LocalDate.ofEpochDay(-1L);

    private static final PaoloStatus DEFAULT_PUBLICATION_STATUS = PaoloStatus.DRAFT;
    private static final PaoloStatus UPDATED_PUBLICATION_STATUS = PaoloStatus.PUBLISHED;

    private static final Instant DEFAULT_PUBLISHED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PUBLISHED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/al-vue-vues";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlVueVueRepository alVueVueRepository;

    @Autowired
    private AlVueVueMapper alVueVueMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlVueVueMockMvc;

    private AlVueVue alVueVue;

    private AlVueVue insertedAlVueVue;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlVueVue createEntity() {
        return new AlVueVue()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .contentHeitiga(DEFAULT_CONTENT_HEITIGA)
            .discountType(DEFAULT_DISCOUNT_TYPE)
            .discountRate(DEFAULT_DISCOUNT_RATE)
            .scope(DEFAULT_SCOPE)
            .isIndividuallyUsedOnly(DEFAULT_IS_INDIVIDUALLY_USED_ONLY)
            .usageLifeTimeLimit(DEFAULT_USAGE_LIFE_TIME_LIMIT)
            .usageLimitPerUser(DEFAULT_USAGE_LIMIT_PER_USER)
            .usageQuantity(DEFAULT_USAGE_QUANTITY)
            .minimumSpend(DEFAULT_MINIMUM_SPEND)
            .maximumSpend(DEFAULT_MAXIMUM_SPEND)
            .canBeCollectedByUser(DEFAULT_CAN_BE_COLLECTED_BY_USER)
            .salePriceFromDate(DEFAULT_SALE_PRICE_FROM_DATE)
            .salePriceToDate(DEFAULT_SALE_PRICE_TO_DATE)
            .publicationStatus(DEFAULT_PUBLICATION_STATUS)
            .publishedDate(DEFAULT_PUBLISHED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlVueVue createUpdatedEntity() {
        return new AlVueVue()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .contentHeitiga(UPDATED_CONTENT_HEITIGA)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .discountRate(UPDATED_DISCOUNT_RATE)
            .scope(UPDATED_SCOPE)
            .isIndividuallyUsedOnly(UPDATED_IS_INDIVIDUALLY_USED_ONLY)
            .usageLifeTimeLimit(UPDATED_USAGE_LIFE_TIME_LIMIT)
            .usageLimitPerUser(UPDATED_USAGE_LIMIT_PER_USER)
            .usageQuantity(UPDATED_USAGE_QUANTITY)
            .minimumSpend(UPDATED_MINIMUM_SPEND)
            .maximumSpend(UPDATED_MAXIMUM_SPEND)
            .canBeCollectedByUser(UPDATED_CAN_BE_COLLECTED_BY_USER)
            .salePriceFromDate(UPDATED_SALE_PRICE_FROM_DATE)
            .salePriceToDate(UPDATED_SALE_PRICE_TO_DATE)
            .publicationStatus(UPDATED_PUBLICATION_STATUS)
            .publishedDate(UPDATED_PUBLISHED_DATE);
    }

    @BeforeEach
    public void initTest() {
        alVueVue = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlVueVue != null) {
            alVueVueRepository.delete(insertedAlVueVue);
            insertedAlVueVue = null;
        }
    }

    @Test
    @Transactional
    void createAlVueVue() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlVueVue
        AlVueVueDTO alVueVueDTO = alVueVueMapper.toDto(alVueVue);
        var returnedAlVueVueDTO = om.readValue(
            restAlVueVueMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alVueVueDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlVueVueDTO.class
        );

        // Validate the AlVueVue in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlVueVue = alVueVueMapper.toEntity(returnedAlVueVueDTO);
        assertAlVueVueUpdatableFieldsEquals(returnedAlVueVue, getPersistedAlVueVue(returnedAlVueVue));

        insertedAlVueVue = returnedAlVueVue;
    }

    @Test
    @Transactional
    void createAlVueVueWithExistingId() throws Exception {
        // Create the AlVueVue with an existing ID
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);
        AlVueVueDTO alVueVueDTO = alVueVueMapper.toDto(alVueVue);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlVueVueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alVueVueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlVueVue in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alVueVue.setName(null);

        // Create the AlVueVue, which fails.
        AlVueVueDTO alVueVueDTO = alVueVueMapper.toDto(alVueVue);

        restAlVueVueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alVueVueDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlVueVues() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList
        restAlVueVueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alVueVue.getId().toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contentHeitiga").value(hasItem(DEFAULT_CONTENT_HEITIGA)))
            .andExpect(jsonPath("$.[*].discountType").value(hasItem(DEFAULT_DISCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].discountRate").value(hasItem(sameNumber(DEFAULT_DISCOUNT_RATE))))
            .andExpect(jsonPath("$.[*].scope").value(hasItem(DEFAULT_SCOPE.toString())))
            .andExpect(jsonPath("$.[*].isIndividuallyUsedOnly").value(hasItem(DEFAULT_IS_INDIVIDUALLY_USED_ONLY.booleanValue())))
            .andExpect(jsonPath("$.[*].usageLifeTimeLimit").value(hasItem(DEFAULT_USAGE_LIFE_TIME_LIMIT)))
            .andExpect(jsonPath("$.[*].usageLimitPerUser").value(hasItem(DEFAULT_USAGE_LIMIT_PER_USER)))
            .andExpect(jsonPath("$.[*].usageQuantity").value(hasItem(DEFAULT_USAGE_QUANTITY)))
            .andExpect(jsonPath("$.[*].minimumSpend").value(hasItem(sameNumber(DEFAULT_MINIMUM_SPEND))))
            .andExpect(jsonPath("$.[*].maximumSpend").value(hasItem(sameNumber(DEFAULT_MAXIMUM_SPEND))))
            .andExpect(jsonPath("$.[*].canBeCollectedByUser").value(hasItem(DEFAULT_CAN_BE_COLLECTED_BY_USER.booleanValue())))
            .andExpect(jsonPath("$.[*].salePriceFromDate").value(hasItem(DEFAULT_SALE_PRICE_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].salePriceToDate").value(hasItem(DEFAULT_SALE_PRICE_TO_DATE.toString())))
            .andExpect(jsonPath("$.[*].publicationStatus").value(hasItem(DEFAULT_PUBLICATION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].publishedDate").value(hasItem(DEFAULT_PUBLISHED_DATE.toString())));
    }

    @Test
    @Transactional
    void getAlVueVue() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get the alVueVue
        restAlVueVueMockMvc
            .perform(get(ENTITY_API_URL_ID, alVueVue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alVueVue.getId().toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.contentHeitiga").value(DEFAULT_CONTENT_HEITIGA))
            .andExpect(jsonPath("$.discountType").value(DEFAULT_DISCOUNT_TYPE.toString()))
            .andExpect(jsonPath("$.discountRate").value(sameNumber(DEFAULT_DISCOUNT_RATE)))
            .andExpect(jsonPath("$.scope").value(DEFAULT_SCOPE.toString()))
            .andExpect(jsonPath("$.isIndividuallyUsedOnly").value(DEFAULT_IS_INDIVIDUALLY_USED_ONLY.booleanValue()))
            .andExpect(jsonPath("$.usageLifeTimeLimit").value(DEFAULT_USAGE_LIFE_TIME_LIMIT))
            .andExpect(jsonPath("$.usageLimitPerUser").value(DEFAULT_USAGE_LIMIT_PER_USER))
            .andExpect(jsonPath("$.usageQuantity").value(DEFAULT_USAGE_QUANTITY))
            .andExpect(jsonPath("$.minimumSpend").value(sameNumber(DEFAULT_MINIMUM_SPEND)))
            .andExpect(jsonPath("$.maximumSpend").value(sameNumber(DEFAULT_MAXIMUM_SPEND)))
            .andExpect(jsonPath("$.canBeCollectedByUser").value(DEFAULT_CAN_BE_COLLECTED_BY_USER.booleanValue()))
            .andExpect(jsonPath("$.salePriceFromDate").value(DEFAULT_SALE_PRICE_FROM_DATE.toString()))
            .andExpect(jsonPath("$.salePriceToDate").value(DEFAULT_SALE_PRICE_TO_DATE.toString()))
            .andExpect(jsonPath("$.publicationStatus").value(DEFAULT_PUBLICATION_STATUS.toString()))
            .andExpect(jsonPath("$.publishedDate").value(DEFAULT_PUBLISHED_DATE.toString()));
    }

    @Test
    @Transactional
    void getAlVueVuesByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        UUID id = alVueVue.getId();

        defaultAlVueVueFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllAlVueVuesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where code equals to
        defaultAlVueVueFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAlVueVuesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where code in
        defaultAlVueVueFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAlVueVuesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where code is not null
        defaultAlVueVueFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVuesByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where code contains
        defaultAlVueVueFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAlVueVuesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where code does not contain
        defaultAlVueVueFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllAlVueVuesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where name equals to
        defaultAlVueVueFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlVueVuesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where name in
        defaultAlVueVueFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlVueVuesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where name is not null
        defaultAlVueVueFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVuesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where name contains
        defaultAlVueVueFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlVueVuesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where name does not contain
        defaultAlVueVueFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAlVueVuesByContentHeitigaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where contentHeitiga equals to
        defaultAlVueVueFiltering("contentHeitiga.equals=" + DEFAULT_CONTENT_HEITIGA, "contentHeitiga.equals=" + UPDATED_CONTENT_HEITIGA);
    }

    @Test
    @Transactional
    void getAllAlVueVuesByContentHeitigaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where contentHeitiga in
        defaultAlVueVueFiltering(
            "contentHeitiga.in=" + DEFAULT_CONTENT_HEITIGA + "," + UPDATED_CONTENT_HEITIGA,
            "contentHeitiga.in=" + UPDATED_CONTENT_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByContentHeitigaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where contentHeitiga is not null
        defaultAlVueVueFiltering("contentHeitiga.specified=true", "contentHeitiga.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVuesByContentHeitigaContainsSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where contentHeitiga contains
        defaultAlVueVueFiltering(
            "contentHeitiga.contains=" + DEFAULT_CONTENT_HEITIGA,
            "contentHeitiga.contains=" + UPDATED_CONTENT_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByContentHeitigaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where contentHeitiga does not contain
        defaultAlVueVueFiltering(
            "contentHeitiga.doesNotContain=" + UPDATED_CONTENT_HEITIGA,
            "contentHeitiga.doesNotContain=" + DEFAULT_CONTENT_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByDiscountTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where discountType equals to
        defaultAlVueVueFiltering("discountType.equals=" + DEFAULT_DISCOUNT_TYPE, "discountType.equals=" + UPDATED_DISCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllAlVueVuesByDiscountTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where discountType in
        defaultAlVueVueFiltering(
            "discountType.in=" + DEFAULT_DISCOUNT_TYPE + "," + UPDATED_DISCOUNT_TYPE,
            "discountType.in=" + UPDATED_DISCOUNT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByDiscountTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where discountType is not null
        defaultAlVueVueFiltering("discountType.specified=true", "discountType.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVuesByDiscountRateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where discountRate equals to
        defaultAlVueVueFiltering("discountRate.equals=" + DEFAULT_DISCOUNT_RATE, "discountRate.equals=" + UPDATED_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllAlVueVuesByDiscountRateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where discountRate in
        defaultAlVueVueFiltering(
            "discountRate.in=" + DEFAULT_DISCOUNT_RATE + "," + UPDATED_DISCOUNT_RATE,
            "discountRate.in=" + UPDATED_DISCOUNT_RATE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByDiscountRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where discountRate is not null
        defaultAlVueVueFiltering("discountRate.specified=true", "discountRate.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVuesByDiscountRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where discountRate is greater than or equal to
        defaultAlVueVueFiltering(
            "discountRate.greaterThanOrEqual=" + DEFAULT_DISCOUNT_RATE,
            "discountRate.greaterThanOrEqual=" + UPDATED_DISCOUNT_RATE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByDiscountRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where discountRate is less than or equal to
        defaultAlVueVueFiltering(
            "discountRate.lessThanOrEqual=" + DEFAULT_DISCOUNT_RATE,
            "discountRate.lessThanOrEqual=" + SMALLER_DISCOUNT_RATE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByDiscountRateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where discountRate is less than
        defaultAlVueVueFiltering("discountRate.lessThan=" + UPDATED_DISCOUNT_RATE, "discountRate.lessThan=" + DEFAULT_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllAlVueVuesByDiscountRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where discountRate is greater than
        defaultAlVueVueFiltering("discountRate.greaterThan=" + SMALLER_DISCOUNT_RATE, "discountRate.greaterThan=" + DEFAULT_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    void getAllAlVueVuesByScopeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where scope equals to
        defaultAlVueVueFiltering("scope.equals=" + DEFAULT_SCOPE, "scope.equals=" + UPDATED_SCOPE);
    }

    @Test
    @Transactional
    void getAllAlVueVuesByScopeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where scope in
        defaultAlVueVueFiltering("scope.in=" + DEFAULT_SCOPE + "," + UPDATED_SCOPE, "scope.in=" + UPDATED_SCOPE);
    }

    @Test
    @Transactional
    void getAllAlVueVuesByScopeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where scope is not null
        defaultAlVueVueFiltering("scope.specified=true", "scope.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVuesByIsIndividuallyUsedOnlyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where isIndividuallyUsedOnly equals to
        defaultAlVueVueFiltering(
            "isIndividuallyUsedOnly.equals=" + DEFAULT_IS_INDIVIDUALLY_USED_ONLY,
            "isIndividuallyUsedOnly.equals=" + UPDATED_IS_INDIVIDUALLY_USED_ONLY
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByIsIndividuallyUsedOnlyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where isIndividuallyUsedOnly in
        defaultAlVueVueFiltering(
            "isIndividuallyUsedOnly.in=" + DEFAULT_IS_INDIVIDUALLY_USED_ONLY + "," + UPDATED_IS_INDIVIDUALLY_USED_ONLY,
            "isIndividuallyUsedOnly.in=" + UPDATED_IS_INDIVIDUALLY_USED_ONLY
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByIsIndividuallyUsedOnlyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where isIndividuallyUsedOnly is not null
        defaultAlVueVueFiltering("isIndividuallyUsedOnly.specified=true", "isIndividuallyUsedOnly.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVuesByUsageLifeTimeLimitIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where usageLifeTimeLimit equals to
        defaultAlVueVueFiltering(
            "usageLifeTimeLimit.equals=" + DEFAULT_USAGE_LIFE_TIME_LIMIT,
            "usageLifeTimeLimit.equals=" + UPDATED_USAGE_LIFE_TIME_LIMIT
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByUsageLifeTimeLimitIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where usageLifeTimeLimit in
        defaultAlVueVueFiltering(
            "usageLifeTimeLimit.in=" + DEFAULT_USAGE_LIFE_TIME_LIMIT + "," + UPDATED_USAGE_LIFE_TIME_LIMIT,
            "usageLifeTimeLimit.in=" + UPDATED_USAGE_LIFE_TIME_LIMIT
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByUsageLifeTimeLimitIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where usageLifeTimeLimit is not null
        defaultAlVueVueFiltering("usageLifeTimeLimit.specified=true", "usageLifeTimeLimit.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVuesByUsageLifeTimeLimitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where usageLifeTimeLimit is greater than or equal to
        defaultAlVueVueFiltering(
            "usageLifeTimeLimit.greaterThanOrEqual=" + DEFAULT_USAGE_LIFE_TIME_LIMIT,
            "usageLifeTimeLimit.greaterThanOrEqual=" + UPDATED_USAGE_LIFE_TIME_LIMIT
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByUsageLifeTimeLimitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where usageLifeTimeLimit is less than or equal to
        defaultAlVueVueFiltering(
            "usageLifeTimeLimit.lessThanOrEqual=" + DEFAULT_USAGE_LIFE_TIME_LIMIT,
            "usageLifeTimeLimit.lessThanOrEqual=" + SMALLER_USAGE_LIFE_TIME_LIMIT
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByUsageLifeTimeLimitIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where usageLifeTimeLimit is less than
        defaultAlVueVueFiltering(
            "usageLifeTimeLimit.lessThan=" + UPDATED_USAGE_LIFE_TIME_LIMIT,
            "usageLifeTimeLimit.lessThan=" + DEFAULT_USAGE_LIFE_TIME_LIMIT
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByUsageLifeTimeLimitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where usageLifeTimeLimit is greater than
        defaultAlVueVueFiltering(
            "usageLifeTimeLimit.greaterThan=" + SMALLER_USAGE_LIFE_TIME_LIMIT,
            "usageLifeTimeLimit.greaterThan=" + DEFAULT_USAGE_LIFE_TIME_LIMIT
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByUsageLimitPerUserIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where usageLimitPerUser equals to
        defaultAlVueVueFiltering(
            "usageLimitPerUser.equals=" + DEFAULT_USAGE_LIMIT_PER_USER,
            "usageLimitPerUser.equals=" + UPDATED_USAGE_LIMIT_PER_USER
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByUsageLimitPerUserIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where usageLimitPerUser in
        defaultAlVueVueFiltering(
            "usageLimitPerUser.in=" + DEFAULT_USAGE_LIMIT_PER_USER + "," + UPDATED_USAGE_LIMIT_PER_USER,
            "usageLimitPerUser.in=" + UPDATED_USAGE_LIMIT_PER_USER
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByUsageLimitPerUserIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where usageLimitPerUser is not null
        defaultAlVueVueFiltering("usageLimitPerUser.specified=true", "usageLimitPerUser.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVuesByUsageLimitPerUserIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where usageLimitPerUser is greater than or equal to
        defaultAlVueVueFiltering(
            "usageLimitPerUser.greaterThanOrEqual=" + DEFAULT_USAGE_LIMIT_PER_USER,
            "usageLimitPerUser.greaterThanOrEqual=" + UPDATED_USAGE_LIMIT_PER_USER
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByUsageLimitPerUserIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where usageLimitPerUser is less than or equal to
        defaultAlVueVueFiltering(
            "usageLimitPerUser.lessThanOrEqual=" + DEFAULT_USAGE_LIMIT_PER_USER,
            "usageLimitPerUser.lessThanOrEqual=" + SMALLER_USAGE_LIMIT_PER_USER
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByUsageLimitPerUserIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where usageLimitPerUser is less than
        defaultAlVueVueFiltering(
            "usageLimitPerUser.lessThan=" + UPDATED_USAGE_LIMIT_PER_USER,
            "usageLimitPerUser.lessThan=" + DEFAULT_USAGE_LIMIT_PER_USER
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByUsageLimitPerUserIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where usageLimitPerUser is greater than
        defaultAlVueVueFiltering(
            "usageLimitPerUser.greaterThan=" + SMALLER_USAGE_LIMIT_PER_USER,
            "usageLimitPerUser.greaterThan=" + DEFAULT_USAGE_LIMIT_PER_USER
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByUsageQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where usageQuantity equals to
        defaultAlVueVueFiltering("usageQuantity.equals=" + DEFAULT_USAGE_QUANTITY, "usageQuantity.equals=" + UPDATED_USAGE_QUANTITY);
    }

    @Test
    @Transactional
    void getAllAlVueVuesByUsageQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where usageQuantity in
        defaultAlVueVueFiltering(
            "usageQuantity.in=" + DEFAULT_USAGE_QUANTITY + "," + UPDATED_USAGE_QUANTITY,
            "usageQuantity.in=" + UPDATED_USAGE_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByUsageQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where usageQuantity is not null
        defaultAlVueVueFiltering("usageQuantity.specified=true", "usageQuantity.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVuesByUsageQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where usageQuantity is greater than or equal to
        defaultAlVueVueFiltering(
            "usageQuantity.greaterThanOrEqual=" + DEFAULT_USAGE_QUANTITY,
            "usageQuantity.greaterThanOrEqual=" + UPDATED_USAGE_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByUsageQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where usageQuantity is less than or equal to
        defaultAlVueVueFiltering(
            "usageQuantity.lessThanOrEqual=" + DEFAULT_USAGE_QUANTITY,
            "usageQuantity.lessThanOrEqual=" + SMALLER_USAGE_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByUsageQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where usageQuantity is less than
        defaultAlVueVueFiltering("usageQuantity.lessThan=" + UPDATED_USAGE_QUANTITY, "usageQuantity.lessThan=" + DEFAULT_USAGE_QUANTITY);
    }

    @Test
    @Transactional
    void getAllAlVueVuesByUsageQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where usageQuantity is greater than
        defaultAlVueVueFiltering(
            "usageQuantity.greaterThan=" + SMALLER_USAGE_QUANTITY,
            "usageQuantity.greaterThan=" + DEFAULT_USAGE_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByMinimumSpendIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where minimumSpend equals to
        defaultAlVueVueFiltering("minimumSpend.equals=" + DEFAULT_MINIMUM_SPEND, "minimumSpend.equals=" + UPDATED_MINIMUM_SPEND);
    }

    @Test
    @Transactional
    void getAllAlVueVuesByMinimumSpendIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where minimumSpend in
        defaultAlVueVueFiltering(
            "minimumSpend.in=" + DEFAULT_MINIMUM_SPEND + "," + UPDATED_MINIMUM_SPEND,
            "minimumSpend.in=" + UPDATED_MINIMUM_SPEND
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByMinimumSpendIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where minimumSpend is not null
        defaultAlVueVueFiltering("minimumSpend.specified=true", "minimumSpend.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVuesByMinimumSpendIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where minimumSpend is greater than or equal to
        defaultAlVueVueFiltering(
            "minimumSpend.greaterThanOrEqual=" + DEFAULT_MINIMUM_SPEND,
            "minimumSpend.greaterThanOrEqual=" + UPDATED_MINIMUM_SPEND
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByMinimumSpendIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where minimumSpend is less than or equal to
        defaultAlVueVueFiltering(
            "minimumSpend.lessThanOrEqual=" + DEFAULT_MINIMUM_SPEND,
            "minimumSpend.lessThanOrEqual=" + SMALLER_MINIMUM_SPEND
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByMinimumSpendIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where minimumSpend is less than
        defaultAlVueVueFiltering("minimumSpend.lessThan=" + UPDATED_MINIMUM_SPEND, "minimumSpend.lessThan=" + DEFAULT_MINIMUM_SPEND);
    }

    @Test
    @Transactional
    void getAllAlVueVuesByMinimumSpendIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where minimumSpend is greater than
        defaultAlVueVueFiltering("minimumSpend.greaterThan=" + SMALLER_MINIMUM_SPEND, "minimumSpend.greaterThan=" + DEFAULT_MINIMUM_SPEND);
    }

    @Test
    @Transactional
    void getAllAlVueVuesByMaximumSpendIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where maximumSpend equals to
        defaultAlVueVueFiltering("maximumSpend.equals=" + DEFAULT_MAXIMUM_SPEND, "maximumSpend.equals=" + UPDATED_MAXIMUM_SPEND);
    }

    @Test
    @Transactional
    void getAllAlVueVuesByMaximumSpendIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where maximumSpend in
        defaultAlVueVueFiltering(
            "maximumSpend.in=" + DEFAULT_MAXIMUM_SPEND + "," + UPDATED_MAXIMUM_SPEND,
            "maximumSpend.in=" + UPDATED_MAXIMUM_SPEND
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByMaximumSpendIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where maximumSpend is not null
        defaultAlVueVueFiltering("maximumSpend.specified=true", "maximumSpend.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVuesByMaximumSpendIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where maximumSpend is greater than or equal to
        defaultAlVueVueFiltering(
            "maximumSpend.greaterThanOrEqual=" + DEFAULT_MAXIMUM_SPEND,
            "maximumSpend.greaterThanOrEqual=" + UPDATED_MAXIMUM_SPEND
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByMaximumSpendIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where maximumSpend is less than or equal to
        defaultAlVueVueFiltering(
            "maximumSpend.lessThanOrEqual=" + DEFAULT_MAXIMUM_SPEND,
            "maximumSpend.lessThanOrEqual=" + SMALLER_MAXIMUM_SPEND
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByMaximumSpendIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where maximumSpend is less than
        defaultAlVueVueFiltering("maximumSpend.lessThan=" + UPDATED_MAXIMUM_SPEND, "maximumSpend.lessThan=" + DEFAULT_MAXIMUM_SPEND);
    }

    @Test
    @Transactional
    void getAllAlVueVuesByMaximumSpendIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where maximumSpend is greater than
        defaultAlVueVueFiltering("maximumSpend.greaterThan=" + SMALLER_MAXIMUM_SPEND, "maximumSpend.greaterThan=" + DEFAULT_MAXIMUM_SPEND);
    }

    @Test
    @Transactional
    void getAllAlVueVuesByCanBeCollectedByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where canBeCollectedByUser equals to
        defaultAlVueVueFiltering(
            "canBeCollectedByUser.equals=" + DEFAULT_CAN_BE_COLLECTED_BY_USER,
            "canBeCollectedByUser.equals=" + UPDATED_CAN_BE_COLLECTED_BY_USER
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByCanBeCollectedByUserIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where canBeCollectedByUser in
        defaultAlVueVueFiltering(
            "canBeCollectedByUser.in=" + DEFAULT_CAN_BE_COLLECTED_BY_USER + "," + UPDATED_CAN_BE_COLLECTED_BY_USER,
            "canBeCollectedByUser.in=" + UPDATED_CAN_BE_COLLECTED_BY_USER
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByCanBeCollectedByUserIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where canBeCollectedByUser is not null
        defaultAlVueVueFiltering("canBeCollectedByUser.specified=true", "canBeCollectedByUser.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVuesBySalePriceFromDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where salePriceFromDate equals to
        defaultAlVueVueFiltering(
            "salePriceFromDate.equals=" + DEFAULT_SALE_PRICE_FROM_DATE,
            "salePriceFromDate.equals=" + UPDATED_SALE_PRICE_FROM_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesBySalePriceFromDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where salePriceFromDate in
        defaultAlVueVueFiltering(
            "salePriceFromDate.in=" + DEFAULT_SALE_PRICE_FROM_DATE + "," + UPDATED_SALE_PRICE_FROM_DATE,
            "salePriceFromDate.in=" + UPDATED_SALE_PRICE_FROM_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesBySalePriceFromDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where salePriceFromDate is not null
        defaultAlVueVueFiltering("salePriceFromDate.specified=true", "salePriceFromDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVuesBySalePriceFromDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where salePriceFromDate is greater than or equal to
        defaultAlVueVueFiltering(
            "salePriceFromDate.greaterThanOrEqual=" + DEFAULT_SALE_PRICE_FROM_DATE,
            "salePriceFromDate.greaterThanOrEqual=" + UPDATED_SALE_PRICE_FROM_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesBySalePriceFromDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where salePriceFromDate is less than or equal to
        defaultAlVueVueFiltering(
            "salePriceFromDate.lessThanOrEqual=" + DEFAULT_SALE_PRICE_FROM_DATE,
            "salePriceFromDate.lessThanOrEqual=" + SMALLER_SALE_PRICE_FROM_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesBySalePriceFromDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where salePriceFromDate is less than
        defaultAlVueVueFiltering(
            "salePriceFromDate.lessThan=" + UPDATED_SALE_PRICE_FROM_DATE,
            "salePriceFromDate.lessThan=" + DEFAULT_SALE_PRICE_FROM_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesBySalePriceFromDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where salePriceFromDate is greater than
        defaultAlVueVueFiltering(
            "salePriceFromDate.greaterThan=" + SMALLER_SALE_PRICE_FROM_DATE,
            "salePriceFromDate.greaterThan=" + DEFAULT_SALE_PRICE_FROM_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesBySalePriceToDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where salePriceToDate equals to
        defaultAlVueVueFiltering(
            "salePriceToDate.equals=" + DEFAULT_SALE_PRICE_TO_DATE,
            "salePriceToDate.equals=" + UPDATED_SALE_PRICE_TO_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesBySalePriceToDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where salePriceToDate in
        defaultAlVueVueFiltering(
            "salePriceToDate.in=" + DEFAULT_SALE_PRICE_TO_DATE + "," + UPDATED_SALE_PRICE_TO_DATE,
            "salePriceToDate.in=" + UPDATED_SALE_PRICE_TO_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesBySalePriceToDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where salePriceToDate is not null
        defaultAlVueVueFiltering("salePriceToDate.specified=true", "salePriceToDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVuesBySalePriceToDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where salePriceToDate is greater than or equal to
        defaultAlVueVueFiltering(
            "salePriceToDate.greaterThanOrEqual=" + DEFAULT_SALE_PRICE_TO_DATE,
            "salePriceToDate.greaterThanOrEqual=" + UPDATED_SALE_PRICE_TO_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesBySalePriceToDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where salePriceToDate is less than or equal to
        defaultAlVueVueFiltering(
            "salePriceToDate.lessThanOrEqual=" + DEFAULT_SALE_PRICE_TO_DATE,
            "salePriceToDate.lessThanOrEqual=" + SMALLER_SALE_PRICE_TO_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesBySalePriceToDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where salePriceToDate is less than
        defaultAlVueVueFiltering(
            "salePriceToDate.lessThan=" + UPDATED_SALE_PRICE_TO_DATE,
            "salePriceToDate.lessThan=" + DEFAULT_SALE_PRICE_TO_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesBySalePriceToDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where salePriceToDate is greater than
        defaultAlVueVueFiltering(
            "salePriceToDate.greaterThan=" + SMALLER_SALE_PRICE_TO_DATE,
            "salePriceToDate.greaterThan=" + DEFAULT_SALE_PRICE_TO_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByPublicationStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where publicationStatus equals to
        defaultAlVueVueFiltering(
            "publicationStatus.equals=" + DEFAULT_PUBLICATION_STATUS,
            "publicationStatus.equals=" + UPDATED_PUBLICATION_STATUS
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByPublicationStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where publicationStatus in
        defaultAlVueVueFiltering(
            "publicationStatus.in=" + DEFAULT_PUBLICATION_STATUS + "," + UPDATED_PUBLICATION_STATUS,
            "publicationStatus.in=" + UPDATED_PUBLICATION_STATUS
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByPublicationStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where publicationStatus is not null
        defaultAlVueVueFiltering("publicationStatus.specified=true", "publicationStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVuesByPublishedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where publishedDate equals to
        defaultAlVueVueFiltering("publishedDate.equals=" + DEFAULT_PUBLISHED_DATE, "publishedDate.equals=" + UPDATED_PUBLISHED_DATE);
    }

    @Test
    @Transactional
    void getAllAlVueVuesByPublishedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where publishedDate in
        defaultAlVueVueFiltering(
            "publishedDate.in=" + DEFAULT_PUBLISHED_DATE + "," + UPDATED_PUBLISHED_DATE,
            "publishedDate.in=" + UPDATED_PUBLISHED_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlVueVuesByPublishedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        // Get all the alVueVueList where publishedDate is not null
        defaultAlVueVueFiltering("publishedDate.specified=true", "publishedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAlVueVuesByImageIsEqualToSomething() throws Exception {
        Metaverse image;
        if (TestUtil.findAll(em, Metaverse.class).isEmpty()) {
            alVueVueRepository.saveAndFlush(alVueVue);
            image = MetaverseResourceIT.createEntity();
        } else {
            image = TestUtil.findAll(em, Metaverse.class).get(0);
        }
        em.persist(image);
        em.flush();
        alVueVue.setImage(image);
        alVueVueRepository.saveAndFlush(alVueVue);
        Long imageId = image.getId();
        // Get all the alVueVueList where image equals to imageId
        defaultAlVueVueShouldBeFound("imageId.equals=" + imageId);

        // Get all the alVueVueList where image equals to (imageId + 1)
        defaultAlVueVueShouldNotBeFound("imageId.equals=" + (imageId + 1));
    }

    @Test
    @Transactional
    void getAllAlVueVuesByAlVueVueUsageIsEqualToSomething() throws Exception {
        AlVueVueUsage alVueVueUsage;
        if (TestUtil.findAll(em, AlVueVueUsage.class).isEmpty()) {
            alVueVueRepository.saveAndFlush(alVueVue);
            alVueVueUsage = AlVueVueUsageResourceIT.createEntity();
        } else {
            alVueVueUsage = TestUtil.findAll(em, AlVueVueUsage.class).get(0);
        }
        em.persist(alVueVueUsage);
        em.flush();
        alVueVue.setAlVueVueUsage(alVueVueUsage);
        alVueVueRepository.saveAndFlush(alVueVue);
        UUID alVueVueUsageId = alVueVueUsage.getId();
        // Get all the alVueVueList where alVueVueUsage equals to alVueVueUsageId
        defaultAlVueVueShouldBeFound("alVueVueUsageId.equals=" + alVueVueUsageId);

        // Get all the alVueVueList where alVueVueUsage equals to UUID.randomUUID()
        defaultAlVueVueShouldNotBeFound("alVueVueUsageId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlVueVuesByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alVueVueRepository.saveAndFlush(alVueVue);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alVueVue.setApplication(application);
        alVueVueRepository.saveAndFlush(alVueVue);
        UUID applicationId = application.getId();
        // Get all the alVueVueList where application equals to applicationId
        defaultAlVueVueShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alVueVueList where application equals to UUID.randomUUID()
        defaultAlVueVueShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultAlVueVueFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlVueVueShouldBeFound(shouldBeFound);
        defaultAlVueVueShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlVueVueShouldBeFound(String filter) throws Exception {
        restAlVueVueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alVueVue.getId().toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contentHeitiga").value(hasItem(DEFAULT_CONTENT_HEITIGA)))
            .andExpect(jsonPath("$.[*].discountType").value(hasItem(DEFAULT_DISCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].discountRate").value(hasItem(sameNumber(DEFAULT_DISCOUNT_RATE))))
            .andExpect(jsonPath("$.[*].scope").value(hasItem(DEFAULT_SCOPE.toString())))
            .andExpect(jsonPath("$.[*].isIndividuallyUsedOnly").value(hasItem(DEFAULT_IS_INDIVIDUALLY_USED_ONLY.booleanValue())))
            .andExpect(jsonPath("$.[*].usageLifeTimeLimit").value(hasItem(DEFAULT_USAGE_LIFE_TIME_LIMIT)))
            .andExpect(jsonPath("$.[*].usageLimitPerUser").value(hasItem(DEFAULT_USAGE_LIMIT_PER_USER)))
            .andExpect(jsonPath("$.[*].usageQuantity").value(hasItem(DEFAULT_USAGE_QUANTITY)))
            .andExpect(jsonPath("$.[*].minimumSpend").value(hasItem(sameNumber(DEFAULT_MINIMUM_SPEND))))
            .andExpect(jsonPath("$.[*].maximumSpend").value(hasItem(sameNumber(DEFAULT_MAXIMUM_SPEND))))
            .andExpect(jsonPath("$.[*].canBeCollectedByUser").value(hasItem(DEFAULT_CAN_BE_COLLECTED_BY_USER.booleanValue())))
            .andExpect(jsonPath("$.[*].salePriceFromDate").value(hasItem(DEFAULT_SALE_PRICE_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].salePriceToDate").value(hasItem(DEFAULT_SALE_PRICE_TO_DATE.toString())))
            .andExpect(jsonPath("$.[*].publicationStatus").value(hasItem(DEFAULT_PUBLICATION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].publishedDate").value(hasItem(DEFAULT_PUBLISHED_DATE.toString())));

        // Check, that the count call also returns 1
        restAlVueVueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlVueVueShouldNotBeFound(String filter) throws Exception {
        restAlVueVueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlVueVueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlVueVue() throws Exception {
        // Get the alVueVue
        restAlVueVueMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlVueVue() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alVueVue
        AlVueVue updatedAlVueVue = alVueVueRepository.findById(alVueVue.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlVueVue are not directly saved in db
        em.detach(updatedAlVueVue);
        updatedAlVueVue
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .contentHeitiga(UPDATED_CONTENT_HEITIGA)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .discountRate(UPDATED_DISCOUNT_RATE)
            .scope(UPDATED_SCOPE)
            .isIndividuallyUsedOnly(UPDATED_IS_INDIVIDUALLY_USED_ONLY)
            .usageLifeTimeLimit(UPDATED_USAGE_LIFE_TIME_LIMIT)
            .usageLimitPerUser(UPDATED_USAGE_LIMIT_PER_USER)
            .usageQuantity(UPDATED_USAGE_QUANTITY)
            .minimumSpend(UPDATED_MINIMUM_SPEND)
            .maximumSpend(UPDATED_MAXIMUM_SPEND)
            .canBeCollectedByUser(UPDATED_CAN_BE_COLLECTED_BY_USER)
            .salePriceFromDate(UPDATED_SALE_PRICE_FROM_DATE)
            .salePriceToDate(UPDATED_SALE_PRICE_TO_DATE)
            .publicationStatus(UPDATED_PUBLICATION_STATUS)
            .publishedDate(UPDATED_PUBLISHED_DATE);
        AlVueVueDTO alVueVueDTO = alVueVueMapper.toDto(updatedAlVueVue);

        restAlVueVueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alVueVueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alVueVueDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlVueVue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlVueVueToMatchAllProperties(updatedAlVueVue);
    }

    @Test
    @Transactional
    void putNonExistingAlVueVue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alVueVue.setId(UUID.randomUUID());

        // Create the AlVueVue
        AlVueVueDTO alVueVueDTO = alVueVueMapper.toDto(alVueVue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlVueVueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alVueVueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alVueVueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlVueVue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlVueVue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alVueVue.setId(UUID.randomUUID());

        // Create the AlVueVue
        AlVueVueDTO alVueVueDTO = alVueVueMapper.toDto(alVueVue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlVueVueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alVueVueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlVueVue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlVueVue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alVueVue.setId(UUID.randomUUID());

        // Create the AlVueVue
        AlVueVueDTO alVueVueDTO = alVueVueMapper.toDto(alVueVue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlVueVueMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alVueVueDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlVueVue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlVueVueWithPatch() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alVueVue using partial update
        AlVueVue partialUpdatedAlVueVue = new AlVueVue();
        partialUpdatedAlVueVue.setId(alVueVue.getId());

        partialUpdatedAlVueVue
            .name(UPDATED_NAME)
            .contentHeitiga(UPDATED_CONTENT_HEITIGA)
            .discountRate(UPDATED_DISCOUNT_RATE)
            .scope(UPDATED_SCOPE)
            .usageQuantity(UPDATED_USAGE_QUANTITY)
            .minimumSpend(UPDATED_MINIMUM_SPEND)
            .canBeCollectedByUser(UPDATED_CAN_BE_COLLECTED_BY_USER)
            .salePriceFromDate(UPDATED_SALE_PRICE_FROM_DATE);

        restAlVueVueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlVueVue.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlVueVue))
            )
            .andExpect(status().isOk());

        // Validate the AlVueVue in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlVueVueUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAlVueVue, alVueVue), getPersistedAlVueVue(alVueVue));
    }

    @Test
    @Transactional
    void fullUpdateAlVueVueWithPatch() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alVueVue using partial update
        AlVueVue partialUpdatedAlVueVue = new AlVueVue();
        partialUpdatedAlVueVue.setId(alVueVue.getId());

        partialUpdatedAlVueVue
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .contentHeitiga(UPDATED_CONTENT_HEITIGA)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .discountRate(UPDATED_DISCOUNT_RATE)
            .scope(UPDATED_SCOPE)
            .isIndividuallyUsedOnly(UPDATED_IS_INDIVIDUALLY_USED_ONLY)
            .usageLifeTimeLimit(UPDATED_USAGE_LIFE_TIME_LIMIT)
            .usageLimitPerUser(UPDATED_USAGE_LIMIT_PER_USER)
            .usageQuantity(UPDATED_USAGE_QUANTITY)
            .minimumSpend(UPDATED_MINIMUM_SPEND)
            .maximumSpend(UPDATED_MAXIMUM_SPEND)
            .canBeCollectedByUser(UPDATED_CAN_BE_COLLECTED_BY_USER)
            .salePriceFromDate(UPDATED_SALE_PRICE_FROM_DATE)
            .salePriceToDate(UPDATED_SALE_PRICE_TO_DATE)
            .publicationStatus(UPDATED_PUBLICATION_STATUS)
            .publishedDate(UPDATED_PUBLISHED_DATE);

        restAlVueVueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlVueVue.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlVueVue))
            )
            .andExpect(status().isOk());

        // Validate the AlVueVue in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlVueVueUpdatableFieldsEquals(partialUpdatedAlVueVue, getPersistedAlVueVue(partialUpdatedAlVueVue));
    }

    @Test
    @Transactional
    void patchNonExistingAlVueVue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alVueVue.setId(UUID.randomUUID());

        // Create the AlVueVue
        AlVueVueDTO alVueVueDTO = alVueVueMapper.toDto(alVueVue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlVueVueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alVueVueDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alVueVueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlVueVue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlVueVue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alVueVue.setId(UUID.randomUUID());

        // Create the AlVueVue
        AlVueVueDTO alVueVueDTO = alVueVueMapper.toDto(alVueVue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlVueVueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alVueVueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlVueVue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlVueVue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alVueVue.setId(UUID.randomUUID());

        // Create the AlVueVue
        AlVueVueDTO alVueVueDTO = alVueVueMapper.toDto(alVueVue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlVueVueMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alVueVueDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlVueVue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlVueVue() throws Exception {
        // Initialize the database
        insertedAlVueVue = alVueVueRepository.saveAndFlush(alVueVue);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alVueVue
        restAlVueVueMockMvc
            .perform(delete(ENTITY_API_URL_ID, alVueVue.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alVueVueRepository.count();
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

    protected AlVueVue getPersistedAlVueVue(AlVueVue alVueVue) {
        return alVueVueRepository.findById(alVueVue.getId()).orElseThrow();
    }

    protected void assertPersistedAlVueVueToMatchAllProperties(AlVueVue expectedAlVueVue) {
        assertAlVueVueAllPropertiesEquals(expectedAlVueVue, getPersistedAlVueVue(expectedAlVueVue));
    }

    protected void assertPersistedAlVueVueToMatchUpdatableProperties(AlVueVue expectedAlVueVue) {
        assertAlVueVueAllUpdatablePropertiesEquals(expectedAlVueVue, getPersistedAlVueVue(expectedAlVueVue));
    }
}
