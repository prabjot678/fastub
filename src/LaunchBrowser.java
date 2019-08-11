package com.fastub.api;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.fastub.api.controller.APIController;

@Component
public class LaunchBrowser {
	
	private static final Logger logger = LogManager.getLogger(APIController.class);
	
	@Autowired
	Environment environment;

	

	@EventListener({ ApplicationReadyEvent.class })
	void applicationReadyEvent() {
		logger.info("Application started ... launching default browser now");
		String port = environment.getProperty("local.server.port");
		Browse("http://localhost:" + port);
	}

	public static void Browse(String url) {
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			try {
				desktop.browse(new URI(url));
			} catch (IOException | URISyntaxException e) {
				logger.error("IOException|URISyntaxException occured while launching the browser..");
			} catch (Exception exception) {
				logger.error("Exception occured while launching the browser..");
			}
		} else {
			Runtime runtime = Runtime.getRuntime();
			try {
				runtime.exec("rundll32 url.dll,FileProtocolHandler " + url);
			} catch (IOException e) {
				logger.error("IOException occured while launching the browser..");
			} catch (Exception exception) {
				logger.error("Exception occured while launching the browser..");
			}
		}
	}
}
