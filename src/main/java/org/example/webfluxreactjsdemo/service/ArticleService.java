package org.example.webfluxreactjsdemo.service;

import lombok.RequiredArgsConstructor;
import org.example.webfluxreactjsdemo.model.Article;
import org.example.webfluxreactjsdemo.repository.ArticleRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    @PostConstruct
    public void postConstruct() {
//        Flux.range(0, 100)
//                .flatMap(i -> save(new Article(null, "header-" + i, "content-" + i)))
//                .subscribe();
    }

    public Flux<Article> findAll() {
        return articleRepository.findAll();
    }

    public Mono<Article> save(Article article) {
        Objects.requireNonNull(article, "Article must not be null");

        if (article.getId() == null) {
            return articleRepository.save(article);
        }

        return articleRepository.findById(article.getId())
                .flatMap(a -> {
                    a.setHeader(article.getHeader());
                    a.setContent(article.getContent());

                    return articleRepository.save(a);
                })
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Article is not found")));
    }

    public Mono<Article> getById(Long id) {
        return articleRepository.findById(id);
    }

    public Mono<Void> delete(Long id) {
        return articleRepository.deleteById(id);
    }
}
