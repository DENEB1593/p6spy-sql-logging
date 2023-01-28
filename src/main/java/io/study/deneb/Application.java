package io.study.deneb;

import io.study.deneb.model.Post;
import io.study.deneb.repository.PostRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  CommandLineRunner commandLineRunner(PostRepository postRepository) {
    return args -> {
      postRepository.save(new Post("this is content"));
      postRepository.save(new Post("this is content2"));
      postRepository.save(new Post("this is content3"));
      postRepository.save(new Post("this is content4"));
    };
  }

}
