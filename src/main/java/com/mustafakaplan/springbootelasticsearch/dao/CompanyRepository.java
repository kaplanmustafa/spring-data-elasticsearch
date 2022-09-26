package com.mustafakaplan.springbootelasticsearch.dao;

import com.mustafakaplan.springbootelasticsearch.model.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CompanyRepository extends ElasticsearchRepository<Company, String> {

    Page<Company> findByEmployeesName(String name, Pageable pageable);

//    @Query("{\"bool\": {\"must\": [{\"match\": {\"employees.name\": \"?0\"}}]}}")
//    Page<Company> findByEmployeesNameUsingCustomQuery(String name, Pageable pageable);
}
