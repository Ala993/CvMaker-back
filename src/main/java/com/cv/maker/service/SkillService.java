package com.cv.maker.service;

import com.cv.maker.domain.Skill;
import com.cv.maker.repository.SkillRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Skill}.
 */
@Service
public class SkillService {

    private final Logger log = LoggerFactory.getLogger(SkillService.class);

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    /**
     * Save a skill.
     *
     * @param skill the entity to save.
     * @return the persisted entity.
     */
    public Skill save(Skill skill) {
        log.debug("Request to save Skill : {}", skill);
        return skillRepository.save(skill);
    }

    /**
     * Partially update a skill.
     *
     * @param skill the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Skill> partialUpdate(Skill skill) {
        log.debug("Request to partially update Skill : {}", skill);

        return skillRepository
            .findById(skill.getId())
            .map(existingSkill -> {
                if (skill.getName() != null) {
                    existingSkill.setName(skill.getName());
                }

                return existingSkill;
            })
            .map(skillRepository::save);
    }

    /**
     * Get all the skills.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<Skill> findAll(Pageable pageable) {
        log.debug("Request to get all Skills");
        return skillRepository.findAll(pageable);
    }

    /**
     * Get one skill by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<Skill> findOne(String id) {
        log.debug("Request to get Skill : {}", id);
        return skillRepository.findById(id);
    }

    /**
     * Delete the skill by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Skill : {}", id);
        skillRepository.deleteById(id);
    }
}
