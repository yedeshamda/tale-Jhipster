package com.tale.domain;

import static com.tale.domain.CompanyTestSamples.*;
import static com.tale.domain.CompanyUserTestSamples.*;
import static com.tale.domain.UserCompanyRoleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.tale.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CompanyUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompanyUser.class);
        CompanyUser companyUser1 = getCompanyUserSample1();
        CompanyUser companyUser2 = new CompanyUser();
        assertThat(companyUser1).isNotEqualTo(companyUser2);

        companyUser2.setId(companyUser1.getId());
        assertThat(companyUser1).isEqualTo(companyUser2);

        companyUser2 = getCompanyUserSample2();
        assertThat(companyUser1).isNotEqualTo(companyUser2);
    }

    @Test
    void userRolesTest() throws Exception {
        CompanyUser companyUser = getCompanyUserRandomSampleGenerator();
        UserCompanyRole userCompanyRoleBack = getUserCompanyRoleRandomSampleGenerator();

        companyUser.addUserRoles(userCompanyRoleBack);
        assertThat(companyUser.getUserRoles()).containsOnly(userCompanyRoleBack);

        companyUser.removeUserRoles(userCompanyRoleBack);
        assertThat(companyUser.getUserRoles()).doesNotContain(userCompanyRoleBack);

        companyUser.userRoles(new HashSet<>(Set.of(userCompanyRoleBack)));
        assertThat(companyUser.getUserRoles()).containsOnly(userCompanyRoleBack);

        companyUser.setUserRoles(new HashSet<>());
        assertThat(companyUser.getUserRoles()).doesNotContain(userCompanyRoleBack);
    }

    @Test
    void companyTest() throws Exception {
        CompanyUser companyUser = getCompanyUserRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        companyUser.setCompany(companyBack);
        assertThat(companyUser.getCompany()).isEqualTo(companyBack);

        companyUser.company(null);
        assertThat(companyUser.getCompany()).isNull();
    }
}
