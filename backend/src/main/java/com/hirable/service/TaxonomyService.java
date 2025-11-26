package com.hirable.service;

import com.hirable.dto.TaxonomyDTO;
import com.hirable.entity.Category;
import com.hirable.entity.Industry;
import com.hirable.entity.Skill;
import com.hirable.repository.CategoryRepository;
import com.hirable.repository.IndustryRepository;
import com.hirable.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaxonomyService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private IndustryRepository industryRepository;

    @Autowired
    private SkillRepository skillRepository;

    // Category operations
    public List<TaxonomyDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertCategoryToDTO)
                .collect(Collectors.toList());
    }

    public TaxonomyDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return convertCategoryToDTO(category);
    }

    public TaxonomyDTO createCategory(TaxonomyDTO dto) {
        if (categoryRepository.findByName(dto.getName()).isPresent()) {
            throw new RuntimeException("Category with name '" + dto.getName() + "' already exists");
        }

        Category category = Category.builder()
                .name(dto.getName())
                .build();

        Category savedCategory = categoryRepository.save(category);
        return convertCategoryToDTO(savedCategory);
    }

    public TaxonomyDTO updateCategory(Long id, TaxonomyDTO dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!category.getName().equals(dto.getName()) && 
            categoryRepository.findByName(dto.getName()).isPresent()) {
            throw new RuntimeException("Category with name '" + dto.getName() + "' already exists");
        }

        category.setName(dto.getName());
        Category updatedCategory = categoryRepository.save(category);
        return convertCategoryToDTO(updatedCategory);
    }

    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found");
        }
        categoryRepository.deleteById(id);
    }

    // Industry operations
    public List<TaxonomyDTO> getAllIndustries() {
        return industryRepository.findAll().stream()
                .map(this::convertIndustryToDTO)
                .collect(Collectors.toList());
    }

    public TaxonomyDTO getIndustryById(Long id) {
        Industry industry = industryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Industry not found"));
        return convertIndustryToDTO(industry);
    }

    public TaxonomyDTO createIndustry(TaxonomyDTO dto) {
        if (industryRepository.findByName(dto.getName()).isPresent()) {
            throw new RuntimeException("Industry with name '" + dto.getName() + "' already exists");
        }

        Industry industry = Industry.builder()
                .name(dto.getName())
                .build();

        Industry savedIndustry = industryRepository.save(industry);
        return convertIndustryToDTO(savedIndustry);
    }

    public TaxonomyDTO updateIndustry(Long id, TaxonomyDTO dto) {
        Industry industry = industryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Industry not found"));

        if (!industry.getName().equals(dto.getName()) && 
            industryRepository.findByName(dto.getName()).isPresent()) {
            throw new RuntimeException("Industry with name '" + dto.getName() + "' already exists");
        }

        industry.setName(dto.getName());
        Industry updatedIndustry = industryRepository.save(industry);
        return convertIndustryToDTO(updatedIndustry);
    }

    public void deleteIndustry(Long id) {
        if (!industryRepository.existsById(id)) {
            throw new RuntimeException("Industry not found");
        }
        industryRepository.deleteById(id);
    }

    // Skill operations
    public List<TaxonomyDTO> getAllSkills() {
        return skillRepository.findAll().stream()
                .map(this::convertSkillToDTO)
                .collect(Collectors.toList());
    }

    public TaxonomyDTO getSkillById(Long id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found"));
        return convertSkillToDTO(skill);
    }

    public TaxonomyDTO createSkill(TaxonomyDTO dto) {
        if (skillRepository.findByName(dto.getName()).isPresent()) {
            throw new RuntimeException("Skill with name '" + dto.getName() + "' already exists");
        }

        Skill skill = Skill.builder()
                .name(dto.getName())
                .build();

        Skill savedSkill = skillRepository.save(skill);
        return convertSkillToDTO(savedSkill);
    }

    public TaxonomyDTO updateSkill(Long id, TaxonomyDTO dto) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        if (!skill.getName().equals(dto.getName()) && 
            skillRepository.findByName(dto.getName()).isPresent()) {
            throw new RuntimeException("Skill with name '" + dto.getName() + "' already exists");
        }

        skill.setName(dto.getName());
        Skill updatedSkill = skillRepository.save(skill);
        return convertSkillToDTO(updatedSkill);
    }

    public void deleteSkill(Long id) {
        if (!skillRepository.existsById(id)) {
            throw new RuntimeException("Skill not found");
        }
        skillRepository.deleteById(id);
    }

    // Conversion methods
    private TaxonomyDTO convertCategoryToDTO(Category category) {
        return TaxonomyDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .createdAt(category.getCreatedAt())
                .build();
    }

    private TaxonomyDTO convertIndustryToDTO(Industry industry) {
        return TaxonomyDTO.builder()
                .id(industry.getId())
                .name(industry.getName())
                .createdAt(industry.getCreatedAt())
                .build();
    }

    private TaxonomyDTO convertSkillToDTO(Skill skill) {
        return TaxonomyDTO.builder()
                .id(skill.getId())
                .name(skill.getName())
                .createdAt(skill.getCreatedAt())
                .build();
    }
}
