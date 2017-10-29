package com.wst.model.category;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	
	public List<CategoryTO> getAllCategories() {
		List<CategoryTO> categories = new ArrayList<CategoryTO>();
		categoryRepository.findAll().forEach(categories::add);
		return categories;
	}
	
	public CategoryTO getCategory(Integer id) {
		return categoryRepository.findOne(id);
	}
	
	public void addCategory(CategoryTO category) {
		categoryRepository.save(category);
	}
	
	public void updateCategory(Integer id, CategoryTO category) {
		categoryRepository.save(category);
	}
	
	public void deleteCategory(Integer id) {
		categoryRepository.delete(id);
	}

}