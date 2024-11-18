package com.tale.domain;

import static com.tale.domain.CampaignTestSamples.*;
import static com.tale.domain.FocusGroupTestSamples.*;
import static com.tale.domain.NormalUserTestSamples.*;
import static com.tale.domain.PointSpendOptionTestSamples.*;
import static com.tale.domain.PointTransactionTestSamples.*;
import static com.tale.domain.QuizProgressTestSamples.*;
import static com.tale.domain.SurveyParticipantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.tale.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class NormalUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NormalUser.class);
        NormalUser normalUser1 = getNormalUserSample1();
        NormalUser normalUser2 = new NormalUser();
        assertThat(normalUser1).isNotEqualTo(normalUser2);

        normalUser2.setId(normalUser1.getId());
        assertThat(normalUser1).isEqualTo(normalUser2);

        normalUser2 = getNormalUserSample2();
        assertThat(normalUser1).isNotEqualTo(normalUser2);
    }

    @Test
    void participatedSurveysTest() throws Exception {
        NormalUser normalUser = getNormalUserRandomSampleGenerator();
        SurveyParticipant surveyParticipantBack = getSurveyParticipantRandomSampleGenerator();

        normalUser.addParticipatedSurveys(surveyParticipantBack);
        assertThat(normalUser.getParticipatedSurveys()).containsOnly(surveyParticipantBack);
        assertThat(surveyParticipantBack.getNormalUser()).isEqualTo(normalUser);

        normalUser.removeParticipatedSurveys(surveyParticipantBack);
        assertThat(normalUser.getParticipatedSurveys()).doesNotContain(surveyParticipantBack);
        assertThat(surveyParticipantBack.getNormalUser()).isNull();

        normalUser.participatedSurveys(new HashSet<>(Set.of(surveyParticipantBack)));
        assertThat(normalUser.getParticipatedSurveys()).containsOnly(surveyParticipantBack);
        assertThat(surveyParticipantBack.getNormalUser()).isEqualTo(normalUser);

        normalUser.setParticipatedSurveys(new HashSet<>());
        assertThat(normalUser.getParticipatedSurveys()).doesNotContain(surveyParticipantBack);
        assertThat(surveyParticipantBack.getNormalUser()).isNull();
    }

    @Test
    void focusGroupsTest() throws Exception {
        NormalUser normalUser = getNormalUserRandomSampleGenerator();
        FocusGroup focusGroupBack = getFocusGroupRandomSampleGenerator();

        normalUser.addFocusGroups(focusGroupBack);
        assertThat(normalUser.getFocusGroups()).containsOnly(focusGroupBack);
        assertThat(focusGroupBack.getNormalUser()).isEqualTo(normalUser);

        normalUser.removeFocusGroups(focusGroupBack);
        assertThat(normalUser.getFocusGroups()).doesNotContain(focusGroupBack);
        assertThat(focusGroupBack.getNormalUser()).isNull();

        normalUser.focusGroups(new HashSet<>(Set.of(focusGroupBack)));
        assertThat(normalUser.getFocusGroups()).containsOnly(focusGroupBack);
        assertThat(focusGroupBack.getNormalUser()).isEqualTo(normalUser);

        normalUser.setFocusGroups(new HashSet<>());
        assertThat(normalUser.getFocusGroups()).doesNotContain(focusGroupBack);
        assertThat(focusGroupBack.getNormalUser()).isNull();
    }

    @Test
    void pointTransactionsTest() throws Exception {
        NormalUser normalUser = getNormalUserRandomSampleGenerator();
        PointTransaction pointTransactionBack = getPointTransactionRandomSampleGenerator();

        normalUser.addPointTransactions(pointTransactionBack);
        assertThat(normalUser.getPointTransactions()).containsOnly(pointTransactionBack);
        assertThat(pointTransactionBack.getNormalUser()).isEqualTo(normalUser);

        normalUser.removePointTransactions(pointTransactionBack);
        assertThat(normalUser.getPointTransactions()).doesNotContain(pointTransactionBack);
        assertThat(pointTransactionBack.getNormalUser()).isNull();

        normalUser.pointTransactions(new HashSet<>(Set.of(pointTransactionBack)));
        assertThat(normalUser.getPointTransactions()).containsOnly(pointTransactionBack);
        assertThat(pointTransactionBack.getNormalUser()).isEqualTo(normalUser);

        normalUser.setPointTransactions(new HashSet<>());
        assertThat(normalUser.getPointTransactions()).doesNotContain(pointTransactionBack);
        assertThat(pointTransactionBack.getNormalUser()).isNull();
    }

    @Test
    void quizProgressesTest() throws Exception {
        NormalUser normalUser = getNormalUserRandomSampleGenerator();
        QuizProgress quizProgressBack = getQuizProgressRandomSampleGenerator();

        normalUser.addQuizProgresses(quizProgressBack);
        assertThat(normalUser.getQuizProgresses()).containsOnly(quizProgressBack);
        assertThat(quizProgressBack.getNormalUser()).isEqualTo(normalUser);

        normalUser.removeQuizProgresses(quizProgressBack);
        assertThat(normalUser.getQuizProgresses()).doesNotContain(quizProgressBack);
        assertThat(quizProgressBack.getNormalUser()).isNull();

        normalUser.quizProgresses(new HashSet<>(Set.of(quizProgressBack)));
        assertThat(normalUser.getQuizProgresses()).containsOnly(quizProgressBack);
        assertThat(quizProgressBack.getNormalUser()).isEqualTo(normalUser);

        normalUser.setQuizProgresses(new HashSet<>());
        assertThat(normalUser.getQuizProgresses()).doesNotContain(quizProgressBack);
        assertThat(quizProgressBack.getNormalUser()).isNull();
    }

    @Test
    void redemptionOptionsTest() throws Exception {
        NormalUser normalUser = getNormalUserRandomSampleGenerator();
        PointSpendOption pointSpendOptionBack = getPointSpendOptionRandomSampleGenerator();

        normalUser.addRedemptionOptions(pointSpendOptionBack);
        assertThat(normalUser.getRedemptionOptions()).containsOnly(pointSpendOptionBack);
        assertThat(pointSpendOptionBack.getNormalUser()).isEqualTo(normalUser);

        normalUser.removeRedemptionOptions(pointSpendOptionBack);
        assertThat(normalUser.getRedemptionOptions()).doesNotContain(pointSpendOptionBack);
        assertThat(pointSpendOptionBack.getNormalUser()).isNull();

        normalUser.redemptionOptions(new HashSet<>(Set.of(pointSpendOptionBack)));
        assertThat(normalUser.getRedemptionOptions()).containsOnly(pointSpendOptionBack);
        assertThat(pointSpendOptionBack.getNormalUser()).isEqualTo(normalUser);

        normalUser.setRedemptionOptions(new HashSet<>());
        assertThat(normalUser.getRedemptionOptions()).doesNotContain(pointSpendOptionBack);
        assertThat(pointSpendOptionBack.getNormalUser()).isNull();
    }

    @Test
    void campaignTest() throws Exception {
        NormalUser normalUser = getNormalUserRandomSampleGenerator();
        Campaign campaignBack = getCampaignRandomSampleGenerator();

        normalUser.setCampaign(campaignBack);
        assertThat(normalUser.getCampaign()).isEqualTo(campaignBack);

        normalUser.campaign(null);
        assertThat(normalUser.getCampaign()).isNull();
    }
}
