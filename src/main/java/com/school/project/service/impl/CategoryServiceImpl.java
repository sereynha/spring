package com.school.project.service.impl;

import com.school.project.dto.CategoryDto;
import com.school.project.dto.ResponseStatusDto;
import com.school.project.exception.DuplicatedException;
import com.school.project.exception.NotFoundException;
import com.school.project.mapper.CategoryMapper;
import com.school.project.model.Category;
import com.school.project.repository.CategoryRepository;
import com.school.project.service.CategoryService;
import com.school.project.service.PageService;
import com.school.project.specifications.CategoryFilter;
import com.school.project.specifications.CategorySpecifications;
import com.school.project.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public ResponseStatusDto create(CategoryDto dto) {
        validateDuplicateName(dto.getName(),null);
        Category category = categoryMapper.toEntity(dto);
        categoryRepository.save(category);
        return new ResponseStatusDto("Create Category", Constants.MESSAGE.SUCCESS_MESSAGE, HttpStatus.CREATED.toString());
    }
    @Override
    public Page<Category> getAll(int page, int limit, String sort, Map<String, String> params) {
        CategoryFilter filter = new CategoryFilter();
        if (params.containsKey("name")){
            String name = params.get("name");
            filter.setName(name);
        }
        if (params.containsKey("id")){
            String id = params.get("id");
            filter.setId(Long.parseLong(id));
        }
        CategorySpecifications specifications = new CategorySpecifications(filter);
        Pageable pageable = PageService.getPageable(page,limit,sort);
        return categoryRepository.findAll(specifications,pageable);
    }

    @Override
    public ResponseStatusDto update(Long id,CategoryDto dto) {
        validateDuplicateName(dto.getName(),id);
        Category found = this.getById(id);
        Category category = new Category();
        category.setName(found.getName());
        category.setDescription(found.getDescription());
        categoryRepository.save(category);
        return new ResponseStatusDto("Update Category", Constants.MESSAGE.SUCCESS_MESSAGE, HttpStatus.OK.toString());
    }

    @Override
    public Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category",id));
    }
    private boolean checkExistedName(String name, Long id) {
        return categoryRepository.findExistedName(name,id) != null;
    }
    private void validateDuplicateName(String name, Long id) {
        if (checkExistedName(name, id)) {
            throw new DuplicatedException(Constants.ERROR_CODE.NAME_ALREADY_EXITED,name);
        }
    }
}
