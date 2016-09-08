package ch.sbb.releasetrain.webui;

import java.io.IOException;
import java.util.TimeZone;

import org.apache.commons.lang3.SystemUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan("ch.sbb")
@EnableAutoConfiguration
@EnableAsync
@EnableScheduling
public class Application {

	private static final String URL = "http://localhost:8080";

	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);

		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Zurich"));

		if(SystemUtils.IS_OS_WINDOWS){
			Runtime rt = Runtime.getRuntime();
			try {
				rt.exec("rundll32 url.dll,FileProtocolHandler " + URL);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if(SystemUtils.IS_OS_MAC){
				Runtime runtime = Runtime.getRuntime();
				String[] args2 = { "osascript", "-e", "open location \"" + URL + "\"" };
				try
				{
					Process process = runtime.exec(args2);
				}
				catch (IOException e)
				{
					// do what you want with this
				}
			}
		}

}
