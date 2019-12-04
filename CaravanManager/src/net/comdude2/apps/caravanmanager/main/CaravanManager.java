package net.comdude2.apps.caravanmanager.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Scanner;

import net.comdude2.apps.caravanmanager.database.DatabaseConnector;
import net.comdude2.apps.caravanmanager.gui.FrameController;
import net.comdude2.apps.caravanmanager.net.WebDownloader;
import net.comdude2.apps.caravanmanager.net.WebReader;
import net.comdude2.apps.caravanmanager.util.FileSystem;
import net.comdude2.apps.caravanmanager.util.Log;
import net.comdude2.apps.caravanmanager.util.ProgramTools;

public class CaravanManager {
	
	private final static Log log = new Log("CaravanManager", false);
	private final static String version = "1.0";
	private static FileSystem fs = null;
	
	public static void main(String[] args) {
		log.info("CaravanManager V" + CaravanManager.version);
		for (String arg : args){
			if (arg.equalsIgnoreCase("debug")){
				CaravanManager.log.setDebug(true);
			}
		}
		DatabaseConnector.loadJdbcDriver(log);
		
		//Initialise Filesystem
		fs = new FileSystem(log);
		fs.createSubFileSystem();
		
		//Check for updates
		checkForUpdates();
		
		//Start framecontroller
		FrameController controller = new FrameController(log, fs);
		controller.initialise();
		
		//Done
		log.info("Done");
		//log.error("Test", new Exception("Pie"));
	}
	
	@SuppressWarnings("unused")
	public static void checkForUpdates(){
		boolean thisisunused = false;
		log.info("Checking for updates...");
		File f = new File("");
		String path = f.getAbsolutePath() + "/";
		f = new File(path + "md5.txt");
		String myMD5 = null;
		if (f.exists()){
			try {
				Scanner x = new Scanner(f);
				myMD5 = x.nextLine();
				x.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			if (myMD5 == null){
				log.warning("Failed to check for updates due to md5.txt file being corrupt.");
				return;
			}
		}else{
			log.warning("Failed to check for updates due to there being no md5.txt file.");
			return;
		}
		try {
			LinkedList <String> data = WebReader.readData("http://dl.mcviral.net/downloads/hidden/CaravanManager/latest.txt");
			if (exists("http://dl.mcviral.net/downloads/hidden/CaravanManager/latest.txt")){
				log.info("Found latest.txt");
			}else{
				log.info("Couldn't find latest.txt");
			}
			String version = stringStripper(data.get(0));
			String md5 = stringStripper(data.get(1));
			if (Double.parseDouble(CaravanManager.version) < Double.parseDouble(version)){
				//Download
				log.info("New version is available, proceeding to download...");
				download(version, md5);
			} else if (Double.parseDouble(CaravanManager.version) == Double.parseDouble(version)){
				//Do nothing
				log.info("App is up-to-date.");
			} else if (Double.parseDouble(CaravanManager.version) > Double.parseDouble(version)){
				//Do nothing
				
			}
		} catch (MalformedURLException e) {
			log.warning("Failed to check for updates.");
			return;
		} catch (Exception e) {
			log.warning("Failed to check for updates.");
			log.debug(e.getMessage(), e);
		}
	}
	
	private static String stringStripper(String string){
		string.replaceAll("\n", "");
		string.replaceAll("\\n", "");
		string.replaceAll("\r", "");
		string.replaceAll("\\r", "");
		return string;
	}
	
	@SuppressWarnings("unused")
	private static void download(String version, String targetMd5){
		boolean thisisunused = false;
		log.debug("http://dl.mcviral.net/downloads/hidden/CaravanManager/" + version + "/");
		if (exists("http://dl.mcviral.net/downloads/hidden/CaravanManager/" + version + "/")){
			log.debug("Directory found.");
		}else{
			log.debug("Directory not found.");
		}
		if ((exists("http://dl.mcviral.net/downloads/hidden/CaravanManager/" + version + "/CaravanManager.jar")) && (exists("http://dl.mcviral.net/downloads/hidden/CaravanManager/" + version + "/md5.txt"))){
			log.info("Found download, downloading...");
			WebDownloader downloader = new WebDownloader(log);
			try {
				log.debug("");
				log.debug("Target URL: " + "http://dl.mcviral.net/downloads/hidden/CaravanManager/" + version + "/CaravanManager.jar");
				log.debug("Destination: " + fs.getMainPath() + "/downloads/app/CaravanManager.jar");
				log.debug("Target MD5: " + targetMd5);
				log.debug("");
				downloader.downloadFileWithMD5("http://dl.mcviral.net/downloads/hidden/CaravanManager/" + version + "/CaravanManager.jar", fs.getMainPath() + "/downloads/app/CaravanManager.jar", targetMd5);
				downloader.downloadFile("http://dl.mcviral.net/downloads/hidden/CaravanManager/" + version + "/md5.txt", fs.getMainPath() + "/downloads/app/md5.txt");
				log.info("");
				log.info("Relaunching the application.");
				//"cmd /c start C:/Users/Matt/Desktop/Utilities/Backup/backup.bat -wo C:/Users/Matt/Desktop/Utilities/Backup/"
				
				//																								Args location below
				ProgramTools.runProgram("cmd /c start " + fs.getMainPath() + "Installer.bat -wo " + fs.getMainPath() + "");
				System.exit(0);
			} catch (MalformedURLException e) {
				log.warning("Failed to download new version, URl Malformed.");
			} catch (Exception e) {
				log.warning("Failed to download new version.");
				log.debug(e.getMessage(), e);
			}
		}else{
			log.warning("Failed to download new version as it wasn't on the download server.");
		}
	}
	
	@SuppressWarnings("unused")
	private static boolean oldexists(String url){
		File f = new File(url);
		if (f.exists()){
			return true;
		}else{
			return false;
		}
	}
	
	private static boolean exists(String URLName){
		try {
			HttpURLConnection.setFollowRedirects(false);
			// note : you may also need
			//        HttpURLConnection.setInstanceFollowRedirects(false)
			HttpURLConnection con = (HttpURLConnection) new URL(URLName).openConnection();
			con.setInstanceFollowRedirects(false);
			con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
			con.setRequestMethod("HEAD");
			return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean shitexists(String URLName){
	      boolean result = false;
	      try {
	          URL url = new URL(URLName);
	          //url = new URL("ftp://ftp1.freebsd.org/pub/FreeBSD123/");//this will fail

	          InputStream input = url.openStream();
	          input.close();

	           result = true;

	            } catch (Exception ex) {
	               ex.printStackTrace();
	            }
	         return result;
	  }
	
}
