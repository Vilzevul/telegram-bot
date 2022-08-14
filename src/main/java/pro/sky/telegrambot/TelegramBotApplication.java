package pro.sky.telegrambot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import pro.sky.telegrambot.listener.TelegramBotUpdatesListener;

@SpringBootApplication
@EnableScheduling
public class TelegramBotApplication {
	private static Logger logger = LoggerFactory.getLogger(TelegramBotApplication.class);

	public static void main(String[] args) {
		logger.info("- Processing main");
	SpringApplication.run(TelegramBotApplication.class, args);
	}

}
