package com.tale.domain;

import static com.tale.domain.CampaignTestSamples.*;
import static com.tale.domain.CompanyTestSamples.*;
import static com.tale.domain.OurDatabasesTestSamples.*;
import static com.tale.domain.SurveyInsightTestSamples.*;
import static com.tale.domain.SurveyParticipantTestSamples.*;
import static com.tale.domain.SurveyRequirementTestSamples.*;
import static com.tale.domain.SurveyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.tale.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SurveyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Survey.class);
        Survey survey1 = getSurveySample1();
        Survey survey2 = new Survey();
        assertThat(survey1).isNotEqualTo(survey2);

        survey2.setId(survey1.getId());
        assertThat(survey1).isEqualTo(survey2);

        survey2 = getSurveySample2();
        assertThat(survey1).isNotEqualTo(survey2);
    }

    @Test
    void surveyRequirementsTest() throws Exception {
        Survey survey = getSurveyRandomSampleGenerator();
        SurveyRequirement surveyRequirementBack = getSurveyRequirementRandomSampleGenerator();

        survey.setSurveyRequirements(surveyRequirementBack);
        assertThat(survey.getSurveyRequirements()).isEqualTo(surveyRequirementBack);

        survey.surveyRequirements(null);
        assertThat(survey.getSurveyRequirements()).isNull();
    }

    @Test
    void surveyInsightsTest() throws Exception {
        Survey survey = getSurveyRandomSampleGenerator();
        SurveyInsight surveyInsightBack = getSurveyInsightRandomSampleGenerator();

        survey.setSurveyInsights(surveyInsightBack);
        assertThat(survey.getSurveyInsights()).isEqualTo(surveyInsightBack);

        survey.surveyInsights(null);
        assertThat(survey.getSurveyInsights()).isNull();
    }

    @Test
    void participantsTest() throws Exception {
        Survey survey = getSurveyRandomSampleGenerator();
        SurveyParticipant surveyParticipantBack = getSurveyParticipantRandomSampleGenerator();

        survey.addParticipants(surveyParticipantBack);
        assertThat(survey.getParticipants()).containsOnly(surveyParticipantBack);

        survey.removeParticipants(surveyParticipantBack);
        assertThat(survey.getParticipants()).doesNotContain(surveyParticipantBack);

        survey.participants(new HashSet<>(Set.of(surveyParticipantBack)));
        assertThat(survey.getParticipants()).containsOnly(surveyParticipantBack);

        survey.setParticipants(new HashSet<>());
        assertThat(survey.getParticipants()).doesNotContain(surveyParticipantBack);
    }

    @Test
    void campaignTest() throws Exception {
        Survey survey = getSurveyRandomSampleGenerator();
        Campaign campaignBack = getCampaignRandomSampleGenerator();

        survey.setCampaign(campaignBack);
        assertThat(survey.getCampaign()).isEqualTo(campaignBack);

        survey.campaign(null);
        assertThat(survey.getCampaign()).isNull();
    }

    @Test
    void companyTest() throws Exception {
        Survey survey = getSurveyRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        survey.setCompany(companyBack);
        assertThat(survey.getCompany()).isEqualTo(companyBack);

        survey.company(null);
        assertThat(survey.getCompany()).isNull();
    }

    @Test
    void ourDatabasesTest() throws Exception {
        Survey survey = getSurveyRandomSampleGenerator();
        OurDatabases ourDatabasesBack = getOurDatabasesRandomSampleGenerator();

        survey.setOurDatabases(ourDatabasesBack);
        assertThat(survey.getOurDatabases()).isEqualTo(ourDatabasesBack);

        survey.ourDatabases(null);
        assertThat(survey.getOurDatabases()).isNull();
    }
}
