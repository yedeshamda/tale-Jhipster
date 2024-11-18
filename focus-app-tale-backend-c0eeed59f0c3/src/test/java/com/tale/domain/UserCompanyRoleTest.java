package com.tale.domain;

import static com.tale.domain.CompanyUserTestSamples.*;
import static com.tale.domain.UserCompanyRoleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.tale.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UserCompanyRoleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserCompanyRole.class);
        UserCompanyRole userCompanyRole1 = getUserCompanyRoleSample1();
        UserCompanyRole userCompanyRole2 = new UserCompanyRole();
        assertThat(userCompanyRole1).isNotEqualTo(userCompanyRole2);

        userCompanyRole2.setId(userCompanyRole1.getId());
        assertThat(userCompanyRole1).isEqualTo(userCompanyRole2);

        userCompanyRole2 = getUserCompanyRoleSample2();
        assertThat(userCompanyRole1).isNotEqualTo(userCompanyRole2);
    }

    @Test
    void companyUsersTest() throws Exception {
        UserCompanyRole userCompanyRole = getUserCompanyRoleRandomSampleGenerator();
        CompanyUser companyUserBack = getCompanyUserRandomSampleGenerator();

        userCompanyRole.addCompanyUsers(companyUserBack);
        assertThat(userCompanyRole.getCompanyUsers()).containsOnly(companyUserBack);
        assertThat(companyUserBack.getUserRoles()).containsOnly(userCompanyRole);

        userCompanyRole.removeCompanyUsers(companyUserBack);
        assertThat(userCompanyRole.getCompanyUsers()).doesNotContain(companyUserBack);
        assertThat(companyUserBack.getUserRoles()).doesNotContain(userCompanyRole);

        userCompanyRole.companyUsers(new HashSet<>(Set.of(companyUserBack)));
        assertThat(userCompanyRole.getCompanyUsers()).containsOnly(companyUserBack);
        assertThat(companyUserBack.getUserRoles()).containsOnly(userCompanyRole);

        userCompanyRole.setCompanyUsers(new HashSet<>());
        assertThat(userCompanyRole.getCompanyUsers()).doesNotContain(companyUserBack);
        assertThat(companyUserBack.getUserRoles()).doesNotContain(userCompanyRole);
    }
}
