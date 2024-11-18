package com.tale.domain;

import static com.tale.domain.OurDatabasesTestSamples.*;
import static com.tale.domain.SurveyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.tale.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OurDatabasesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OurDatabases.class);
        OurDatabases ourDatabases1 = getOurDatabasesSample1();
        OurDatabases ourDatabases2 = new OurDatabases();
        assertThat(ourDatabases1).isNotEqualTo(ourDatabases2);

        ourDatabases2.setId(ourDatabases1.getId());
        assertThat(ourDatabases1).isEqualTo(ourDatabases2);

        ourDatabases2 = getOurDatabasesSample2();
        assertThat(ourDatabases1).isNotEqualTo(ourDatabases2);
    }

    @Test
    void surveyDatabaseTest() throws Exception {
        OurDatabases ourDatabases = getOurDatabasesRandomSampleGenerator();
        Survey surveyBack = getSurveyRandomSampleGenerator();

        ourDatabases.addSurveyDatabase(surveyBack);
        assertThat(ourDatabases.getSurveyDatabases()).containsOnly(surveyBack);
        assertThat(surveyBack.getOurDatabases()).isEqualTo(ourDatabases);

        ourDatabases.removeSurveyDatabase(surveyBack);
        assertThat(ourDatabases.getSurveyDatabases()).doesNotContain(surveyBack);
        assertThat(surveyBack.getOurDatabases()).isNull();

        ourDatabases.surveyDatabases(new HashSet<>(Set.of(surveyBack)));
        assertThat(ourDatabases.getSurveyDatabases()).containsOnly(surveyBack);
        assertThat(surveyBack.getOurDatabases()).isEqualTo(ourDatabases);

        ourDatabases.setSurveyDatabases(new HashSet<>());
        assertThat(ourDatabases.getSurveyDatabases()).doesNotContain(surveyBack);
        assertThat(surveyBack.getOurDatabases()).isNull();
    }
}
