package com.ar.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
//import java.text.DecimalFormat;

import com.jfoenix.controls.JFXProgressBar;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class DownnloadTask extends Task<Void> {

	private String url;
	private String downloadPath;
	private HBox hbox;
	private JFXProgressBar progressBar;
	private Label label;

	public DownnloadTask(String url, String downloadPath, HBox hbox, JFXProgressBar progressBar, Label label) {
		super();
		this.url = url;
		this.hbox = hbox;
		this.downloadPath = downloadPath;
		this.progressBar = progressBar;
		this.label = label;
	}

	@Override
	protected Void call() throws Exception {
		System.out.println("THREAD :" + Thread.currentThread().getName());

		URLConnection connection = new URL(url).openConnection();
		long fileLength = connection.getContentLength();

		try (InputStream is = connection.getInputStream();
				OutputStream os = Files.newOutputStream(Paths.get(downloadPath))) {

			long nread = 0L;
			byte[] but = new byte[8192];
			int n;
			while ((n = is.read(but)) > 0) {
				os.write(but, 0, n);
				nread += n;
				updateProgress(nread, fileLength);
//				DecimalFormat df = new DecimalFormat("#.##");
//				updateMessage( Float.valueOf(df.format(nread / 1000000f)) + " MB / " + Float.valueOf(df.format(fileLength / 1000000f)) + " MB");
				///example:- float f = (float)11/3; System.out.print(String.format("%.2f",f));////
				updateMessage( String.format("%.2f",nread/1000000f) + " MB / " + String.format("%.2f",fileLength/1000000f) + " MB");
			}
		}
		return null;
	}

	@Override
	protected void failed() {
		System.out.println("Failed");
		progressBar.getStyleClass().add("jfx-progress-bar-error");
		progressBar.setStyle("-fx-background-color:red;");
		updateMessage("failed");
		label.setStyle("-fx-background-color:#61210B; -fx-text-fill:#FF0000; "
				+ "-fx-background-radius:10; -fx-border-radius:10; -fx-border-color:#FF8000;");

		Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.seconds(3), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
//						System.out.println("AFTER 3 SECONDS");
				hbox.getChildren().remove(progressBar);
			}
		}), new KeyFrame(Duration.seconds(6), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
//						System.out.println("AFTER 6 SECONDS");
				hbox.getChildren().remove(label);
			}
		})
	    );
		// fiveSecondsWonder.setCycleCount(1);// 1 - oru tavana [ee vari illengilum oru
		// tavana] //Timeline.INDEFINITE - nilkilla tudarnnu kondirikum
		fiveSecondsWonder.play();
//		System.out.println("START PLAYING");
	}

	@Override
	protected void succeeded() {
		System.out.println("Downloaded");
		updateMessage("completed");
		label.setStyle("-fx-background-color:#0B610B; -fx-text-fill:#40FF00; "
				+ "-fx-background-radius:10; -fx-border-radius:10; -fx-border-color:#0B173B;");
		Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
			hbox.getChildren().remove(progressBar);
		}), new KeyFrame(Duration.seconds(5), e -> {
			hbox.getChildren().remove(label);
		}));
		fiveSecondsWonder.play();
	}

}
