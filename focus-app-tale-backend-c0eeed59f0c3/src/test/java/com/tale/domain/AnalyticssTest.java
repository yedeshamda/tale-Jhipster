package com.tale.domain;

import static com.tale.domain.AnalyticssTestSamples.*;
import static com.tale.domain.CampaignTestSamples.*;
import static com.tale.domain.ResponsesTestSamples.*;
import static com.tale.domain.SectionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.tale.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AnalyticssTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Analyticss.class);
        Analyticss analyticss1 = getAnalyticssSample1();
        Analyticss analyticss2 = new Analyticss();
        assertThat(analyticss1).isNotEqualTo(analyticss2);

        analyticss2.setId(analyticss1.getId());
        assertThat(analyticss1).isEqualTo(analyticss2);

        analyticss2 = getAnalyticssSample2();
        assertThat(analyticss1).isNotEqualTo(analyticss2);
    }

    @Test
    void campaignTest() throws Exception {
        Analyticss analyticss = getAnalyticssRandomSampleGenerator();
        Campaign campaignBack = getCampaignRandomSampleGenerator();

        analyticss.setCampaign(campaignBack);
        assertThat(analyticss.getCampaign()).isEqualTo(campaignBack);

        analyticss.campaign(null);
        assertThat(analyticss.getCampaign()).isNull();
    }

    @Test
    void sectionTest() throws Exception {
        Analyticss analyticss = getAnalyticssRandomSampleGenerator();
        Section sectionBack = getSectionRandomSampleGenerator();

        analyticss.setSection(sectionBack);
        assertThat(analyticss.getSection()).isEqualTo(sectionBack);

        analyticss.section(null);
        assertThat(analyticss.getSection()).isNull();
    }

    @Test
    void responsesTest() throws Exception {
        Analyticss analyticss = getAnalyticssRandomSampleGenerator();
        Responses responsesBack = getResponsesRandomSampleGenerator();

        analyticss.setResponses(responsesBack);
        assertThat(analyticss.getResponses()).isEqualTo(responsesBack);

        analyticss.responses(null);
        assertThat(analyticss.getResponses()).isNull();
    }
}
