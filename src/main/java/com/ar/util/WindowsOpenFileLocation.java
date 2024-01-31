package com.ar.util;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WindowsOpenFileLocation {

	/**
	 * Opens the file with the System default file explorer.
	 *
	 * @param path the path
	 */
	public static void openFileLocation(String path) {
		try {
//			Runtime.getRuntime().exec("explorer.exe /select," + path);
			Runtime.getRuntime().exec("explorer.exe /open," + path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void openFileLocation() {
		Path partPath = Paths.get("yourFileLocation" + "\\" + "yourFileName.ext");
		System.out.println(partPath);
	}

	/*
	 * Note that you could directly open the file by making a string to the file
	 * location and the file name with its extension, such as:
	 * 
	 * Path partPath = Paths.get("yourFileLocation"+"\\"+"yourFileName.ext");
	 */

	/**
	 * import java.awt.Desktop; import java.io.File; Opens the file with the System
	 * default file explorer.
	 *
	 * @param path the path
	 */
	public static void openFileLocation3(String path) {
		Desktop.getDesktop().browseFileDirectory(new File(path));
	}

//	Windows
//	Runtime.getRuntime().exec("explorer /select, <file path>")
//
//	MacOS
//	Runtime.getRuntime().exec("open -R <file path>");
}
