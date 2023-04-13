package com.cv.maker.service.dto;

import com.cv.maker.domain.Skill;

import java.util.List;

public class CvFilter {

    List<Skill> skills;

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }
}
