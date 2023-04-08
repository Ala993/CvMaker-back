package com.cv.maker.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.cv.maker.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Study.class);
        Study study1 = new Study();
        study1.setId("id1");
        Study study2 = new Study();
        study2.setId(study1.getId());
        assertThat(study1).isEqualTo(study2);
        study2.setId("id2");
        assertThat(study1).isNotEqualTo(study2);
        study1.setId(null);
        assertThat(study1).isNotEqualTo(study2);
    }
}
