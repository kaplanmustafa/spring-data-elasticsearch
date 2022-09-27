package com.mustafakaplan.springbootelasticsearch.controller;

import com.mustafakaplan.springbootelasticsearch.dao.CompanyRepository;
import com.mustafakaplan.springbootelasticsearch.model.Company;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.Operator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {

    private static final String INDEX_NAME = "erp";

    private final CompanyRepository companyRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    public CompanyController(CompanyRepository companyRepository, ElasticsearchOperations elasticsearchOperations) {
        this.companyRepository = companyRepository;
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @GetMapping
    List<Company> getCompaniesByEmployeesName(@RequestParam("name") String name) {
        final Page<Company> employeesPage = companyRepository.findByEmployeesName(name, PageRequest.of(0, 20));
        return employeesPage.getContent();
    }

    @PostMapping
    Company postCompany(@RequestBody Company company) {
        return companyRepository.save(company);
    }

    @GetMapping("/search")
    List<SearchHit<Company>> getCompaniesByDescription(@RequestParam("search") String searchTerm) {
        final NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("description", searchTerm))
                .build();

        return elasticsearchOperations.search(searchQuery, Company.class, IndexCoordinates.of(INDEX_NAME)).getSearchHits();
    }

    //    Full-text Search
    @GetMapping("/full-search")
    List<SearchHit<Company>> getCompaniesByFullDescription(@RequestParam("search") String searchTerm) {
        final NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("description", searchTerm).operator(Operator.AND))
                .build();

        return elasticsearchOperations.search(searchQuery, Company.class, IndexCoordinates.of(INDEX_NAME)).getSearchHits();
    }

    //    Fuziness
    @GetMapping("/fuzzy-search")
    List<SearchHit<Company>> getCompaniesByFuzzyDescription(@RequestParam("search") String searchTerm) {
        final NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("description", searchTerm)
                        .operator(Operator.AND)
                        .fuzziness(Fuzziness.ONE) // Fuziness.ONE ile arama eşleme işleminin bir harf değiştirerek de denenmesi belirtilmektedir.
                        .prefixLength(2)) // prefixLength(2) ile kelimelerin ilk iki harfinde bir değişikliklik yapılmaması belirtilmekte ve böylelikle kombinasyon sayısı azaltılmaktadır.
                .build();

        return elasticsearchOperations.search(searchQuery, Company.class, IndexCoordinates.of(INDEX_NAME)).getSearchHits();
    }
}
