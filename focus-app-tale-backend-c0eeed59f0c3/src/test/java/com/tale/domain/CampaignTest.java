package com.tale.domain;

import static com.tale.domain.AdminUserTestSamples.*;
import static com.tale.domain.AnalyticssTestSamples.*;
import static com.tale.domain.CampaignTestSamples.*;
import static com.tale.domain.NormalUserTestSamples.*;
import static com.tale.domain.SurveyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.tale.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CampaignTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Campaign.class);
        Campaign campaign1 = getCampaignSample1();
        Campaign campaign2 = new Campaign();
        assertThat(campaign1).isNotEqualTo(campaign2);

        campaign2.setId(campaign1.getId());
        assertThat(campaign1).isEqualTo(campaign2);

        campaign2 = getCampaignSample2();
        assertThat(campaign1).isNotEqualTo(campaign2);
    }

    @Test
    void surveyTest() throws Exception {
        Campaign campaign = getCampaignRandomSampleGenerator();
        Survey surveyBack = getSurveyRandomSampleGenerator();

        campaign.addSurvey(surveyBack);
        assertThat(campaign.getSurveys()).containsOnly(surveyBack);
        assertThat(surveyBack.getCampaign()).isEqualTo(campaign);

        campaign.removeSurvey(surveyBack);
        assertThat(campaign.getSurveys()).doesNotContain(surveyBack);
        assertThat(surveyBack.getCampaign()).isNull();

        campaign.surveys(new HashSet<>(Set.of(surveyBack)));
        assertThat(campaign.getSurveys()).containsOnly(surveyBack);
        assertThat(surveyBack.getCampaign()).isEqualTo(campaign);

        campaign.setSurveys(new HashSet<>());
        assertThat(campaign.getSurveys()).doesNotContain(surveyBack);
        assertThat(surveyBack.getCampaign()).isNull();
    }

    @Test
    void analyticsTest() throws Exception {
        Campaign campaign = getCampaignRandomSampleGenerator();
        Analyticss analyticssBack = getAnalyticssRandomSampleGenerator();

        campaign.addAnalytics(analyticssBack);
        assertThat(campaign.getAnalytics()).containsOnly(analyticssBack);
        assertThat(analyticssBack.getCampaign()).isEqualTo(campaign);

        campaign.removeAnalytics(analyticssBack);
        assertThat(campaign.getAnalytics()).doesNotContain(analyticssBack);
        assertThat(analyticssBack.getCampaign()).isNull();

        campaign.analytics(new HashSet<>(Set.of(analyticssBack)));
        assertThat(campaign.getAnalytics()).containsOnly(analyticssBack);
        assertThat(analyticssBack.getCampaign()).isEqualTo(campaign);

        campaign.setAnalytics(new HashSet<>());
        assertThat(campaign.getAnalytics()).doesNotContain(analyticssBack);
        assertThat(analyticssBack.getCampaign()).isNull();
    }

    @Test
    void userTest() throws Exception {
        Campaign campaign = getCampaignRandomSampleGenerator();
        NormalUser normalUserBack = getNormalUserRandomSampleGenerator();

        campaign.addUser(normalUserBack);
        assertThat(campaign.getUsers()).containsOnly(normalUserBack);
        assertThat(normalUserBack.getCampaign()).isEqualTo(campaign);

        campaign.removeUser(normalUserBack);
        assertThat(campaign.getUsers()).doesNotContain(normalUserBack);
        assertThat(normalUserBack.getCampaign()).isNull();

        campaign.users(new HashSet<>(Set.of(normalUserBack)));
        assertThat(campaign.getUsers()).containsOnly(normalUserBack);
        assertThat(normalUserBack.getCampaign()).isEqualTo(campaign);

        campaign.setUsers(new HashSet<>());
        assertThat(campaign.getUsers()).doesNotContain(normalUserBack);
        assertThat(normalUserBack.getCampaign()).isNull();
    }

    @Test
    void adminUserTest() throws Exception {
        Campaign campaign = getCampaignRandomSampleGenerator();
        AdminUser adminUserBack = getAdminUserRandomSampleGenerator();

        campaign.setAdminUser(adminUserBack);
        assertThat(campaign.getAdminUser()).isEqualTo(adminUserBack);

        campaign.adminUser(null);
        assertThat(campaign.getAdminUser()).isNull();
    }
}
