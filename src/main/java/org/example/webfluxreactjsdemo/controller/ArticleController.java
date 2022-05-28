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
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping(path = "/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<Article>> findAll() {
        return articleService.findAll()
                .buffer(2)
                .take(5)
                .delayElements(Duration.ofSeconds(2));
    }

    @GetMapping(path = "/percents", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Integer> getPercents() {
        AtomicInteger percents = new AtomicInteger();

        Runnable runnable = () -> {
            for (int i = 0; i < 100; i++) {
                try {
                    Thread.sleep(new Random().nextInt(1000));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                percents.incrementAndGet();
            }
        };

        new Thread(runnable).start();

        return Flux.interval(Duration.ofSeconds(1))
                .flatMap(interval -> Flux.just(percents.get()));
    }
}
