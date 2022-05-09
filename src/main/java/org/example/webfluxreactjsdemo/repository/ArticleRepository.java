package org.example.webfluxreactjsdemo.repository;

import org.example.webfluxreactjsdemo.model.Article;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends ReactiveCrudRepository<Article, Long> {

}
