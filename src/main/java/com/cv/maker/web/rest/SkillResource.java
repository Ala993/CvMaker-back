package com.cv.maker.web.rest;

import com.cv.maker.domain.Skill;
import com.cv.maker.repository.SkillRepository;
import com.cv.maker.service.SkillService;
import com.cv.maker.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.cv.maker.domain.Skill}.
 */
@RestController
@RequestMapping("/api")
public class SkillResource {

    private final Logger log = LoggerFactory.getLogger(SkillResource.class);

    private static final String ENTITY_NAME = "skill";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SkillService skillService;

    private final SkillRepository skillRepository;

    public SkillResource(SkillService skillService, SkillRepository skillRepository) {
        this.skillService = skillService;
        this.skillRepository = skillRepository;
    }

    /**
     * {@code POST  /skills} : Create a new skill.
     *
     * @param skill the skill to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new skill, or with status {@code 400 (Bad Request)} if the skill has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/skills")
    public ResponseEntity<Skill> createSkill(@RequestBody Skill skill) throws URISyntaxException {
        log.debug("REST request to save Skill : {}", skill);
        if (skill.getId() != null) {
            throw new BadRequestAlertException("A new skill cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Skill result = skillService.save(skill);
        return ResponseEntity
            .created(new URI("/api/skills/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /skills/:id} : Updates an existing skill.
     *
     * @param id the id of the skill to save.
     * @param skill the skill to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated skill,
     * or with status {@code 400 (Bad Request)} if the skill is not valid,
     * or with status {@code 500 (Internal Server Error)} if the skill couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/skills/{id}")
    public ResponseEntity<Skill> updateSkill(@PathVariable(value = "id", required = false) final String id, @RequestBody Skill skill)
        throws URISyntaxException {
        log.debug("REST request to update Skill : {}, {}", id, skill);
        if (skill.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, skill.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!skillRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Skill result = skillService.save(skill);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, skill.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /skills/:id} : Partial updates given fields of an existing skill, field will ignore if it is null
     *
     * @param id the id of the skill to save.
     * @param skill the skill to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated skill,
     * or with status {@code 400 (Bad Request)} if the skill is not valid,
     * or with status {@code 404 (Not Found)} if the skill is not found,
     * or with status {@code 500 (Internal Server Error)} if the skill couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/skills/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Skill> partialUpdateSkill(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Skill skill
    ) throws URISyntaxException {
        log.debug("REST request to partial update Skill partially : {}, {}", id, skill);
        if (skill.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, skill.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!skillRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Skill> result = skillService.partialUpdate(skill);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, skill.getId()));
    }

    /**
     * {@code GET  /skills} : get all the skills.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of skills in body.
     */
    @GetMapping("/skills")
    public ResponseEntity<List<Skill>> getAllSkills(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Skills");
        Page<Skill> page = skillService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /skills/:id} : get the "id" skill.
     *
     * @param id the id of the skill to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the skill, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/skills/{id}")
    public ResponseEntity<Skill> getSkill(@PathVariable String id) {
        log.debug("REST request to get Skill : {}", id);
        Optional<Skill> skill = skillService.findOne(id);
        return ResponseUtil.wrapOrNotFound(skill);
    }

    /**
     * {@code DELETE  /skills/:id} : delete the "id" skill.
     *
     * @param id the id of the skill to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/skills/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable String id) {
        log.debug("REST request to delete Skill : {}", id);
        skillService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
