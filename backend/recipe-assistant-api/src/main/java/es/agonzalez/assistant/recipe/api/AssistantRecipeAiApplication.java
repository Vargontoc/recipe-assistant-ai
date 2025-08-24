package es.agonzalez.assistant.recipe.api;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;



@SpringBootApplication
public class AssistantRecipeAiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssistantRecipeAiApplication.class, args);
	}
	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			System.out.println("Let´s inspect the beans privided by Spring Boot:");

			String[] names = ctx.getBeanDefinitionNames();
			Arrays.sort(names);
			for(String n: names) {
				System.out.println(n);
			}

		};
	} 

}
