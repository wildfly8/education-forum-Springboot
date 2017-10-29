package com.gslf.model.category;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface CategoryRepository extends CrudRepository<CategoryTO, Integer> {

}
