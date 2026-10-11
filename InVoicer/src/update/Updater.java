package update;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

import org.kohsuke.github.GHAsset;
import org.kohsuke.github.GHRelease;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import inv.Invoicer;

public class Updater {
	//independent jar file
	public static void main(String[] args) {
		//0: absolute path of downloaded invoicah
		//1: absolute path of invoicer
		//2: version num
		//3: invoicer pid(idk)
		if(args.length<4) {
			JOptionPane.showMessageDialog(null, "Must run Updater through InVoicer.");
			System.exit(1);
		}
		for(String s: args) {
			System.out.println(s);
		}
		long originalPid = Long.parseLong(args[3]);

		while (ProcessHandle.of(originalPid)
		        .map(ProcessHandle::isAlive)
		        .orElse(false)) {

		    try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		File location = new File(args[0]);
		File newLocation = new File(args[1]);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Files.move(
			        location.toPath(),
			        newLocation.toPath(),
			        StandardCopyOption.REPLACE_EXISTING
			    );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error moving file: "+e);
			JOptionPane.showMessageDialog(null, "Try going to your InVoicer folder and renaming Invoicah.jar to InVoicer.jar after deleting the original.");
		}
		
        	JOptionPane.showMessageDialog(null, "InVoicer successfully updated to v"+args[2]+".");
        	try {
				ProcessBuilder pb = new ProcessBuilder("java","-jar",args[1]);
				pb.inheritIO();
				pb.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        
      }

	public static void checkForUpdate() {
		String versionNum=Invoicer.versionNum;
		String downloadLoc = FileSystemView.getFileSystemView().getDefaultDirectory().getPath()+"/InVoicer/";
		String tempName="Invoicah.jar";
		String finalName="InVoicer.jar";
		String repo = "evank2008/InVoicer";
		
		File location = new File(downloadLoc+tempName);
		System.out.println(location.getPath());
		try {
			GitHub gh = new GitHubBuilder().build();
			GHRelease release = gh.getRepository(repo).getLatestRelease();
			String s = release.getTagName();
			System.out.println(s);
			if(!s.equals(versionNum)) {
				if(JOptionPane.showConfirmDialog(null, "Version "+s+" has been released. Update?", "UpDater", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
					
					System.out.println("update");
					for(GHAsset ass: release.getAssets()) {
						if(ass.getName().equals("InVoicer.jar")) {
							String url = ass.getBrowserDownloadUrl();
							System.out.println(url);
							URL downloadUrl = URI.create(url).toURL();
					        InputStream in = downloadUrl.openStream(); 
					        Files.copy(in, location.toPath(), StandardCopyOption.REPLACE_EXISTING);
					        //now run the Updater
					        ProcessBuilder pb = new ProcessBuilder("java", "-jar", downloadLoc+"Updater.jar", 
					        		downloadLoc+tempName, 
					        		downloadLoc+finalName,
					        		s,
					        		Long.toString(ProcessHandle.current().pid()));
					       //s is the new version num
					        pb.inheritIO();
					        pb.start();
					        System.out.println("exiting updater");
					        System.exit(0);
						}
					}
					
				}	
			}
			else JOptionPane.showMessageDialog(null, "Your InVoicer is up to date at version "+s+".");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error updating: "+e.getMessage());
		}
	}
	
}