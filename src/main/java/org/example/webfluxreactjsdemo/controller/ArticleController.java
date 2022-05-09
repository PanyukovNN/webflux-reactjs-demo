package org.example.webfluxreactjsdemo.controller;

import lombok.RequiredArgsConstructor;
import org.example.webfluxreactjsdemo.model.Article;
import org.example.webfluxreactjsdemo.service.ArticleService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping(path = "/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<Article>> findAll() {
        return articleService.findAll()
                .buffer(2)
                .delayElements(Duration.ofSeconds(2));
    }
}
