package com.placeholders.mindquest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class MindquestApplication {

	private static final String URL = "http://localhost:8081/quiz/getQuiz";
	private static final Logger logger = LoggerFactory.getLogger(MindquestApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MindquestApplication.class, args);
		openApp();
	}

	//automatically opens browser after startup
	private static void openApp(){

		Runtime rt = Runtime.getRuntime();

		String osName = System.getProperty("os.name").toLowerCase();

		try {
			//windows
			if (osName.contains("windows")){
				rt.exec("rundll32 url, FileProtocolHandler " + URL);
			}else {
				//unix
				rt.exec("Open " + URL);
			}
		}catch (IOException ioxe){
			logger.info("ERROR: COULDN'T OPEN WEBPAGE VIA URL " + URL);
		}
	}

}
