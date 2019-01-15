import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class RaidarMain {

	public static void main(String[] args) throws ParseException, IOException {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String path;
		String[] dates = new String[1];
		File[] paths = new File[1];
		String BossNameAlt;
		String BossNameNeu;
		String[] BossNamen = new String[18];
		String PfadDerNeustenDatei;
		int boss;
		File[] recentPath = new File[17];
		int nzaehler = 0;
		String token = "";
		String neustesDatum;
		Path target = Paths.get("C:\\Users\\Patrick\\Desktop\\Raidar Neuste Dateien\\Test.evtc"); // Platzhalter wird
																									// später mit dem
																									// Datum als
																									// Dateiname
																									// überschrieben!
		
		Scanner input = new Scanner(System.in);

		BossNamen[1] = "Vale Guardian";
		BossNamen[2] = "Gorseval the Multifarious";
		BossNamen[3] = "Sabetha the Saboteur";
		BossNamen[4] = "Slothasor";
		BossNamen[5] = "Matthias Gabrel";
		BossNamen[6] = "Keep Construct";
		BossNamen[7] = "Xera";
		BossNamen[8] = "Cairn the Indomitable";
		BossNamen[9] = "Mursaat Overseer";
		BossNamen[10] = "Samarog";
		BossNamen[11] = "Deimos";
		BossNamen[12] = "Soulless Horror";
		BossNamen[13] = "Dhuum";
		BossNamen[14] = "Conjured Amalgamate";
		BossNamen[15] = "Nikare";
		BossNamen[16] = "Qadim";

		// Initialisierung
		path = "C:\\Users\\Patrick\\Documents\\Guild Wars 2\\addons\\arcdps\\arcdps.cbtlogs\\Vale Guardian";
		BossNameAlt = BossNamen[1];
		BossNameNeu = BossNamen[1];

		boss = 1;
		while (boss <= 16) {

			path = path.replaceAll(BossNameAlt, BossNameNeu);
			System.out.println(path);

			File arcdps = new File(path);
			paths = arcdps.listFiles(); // Alle Pfade zu den jeweiligen Logs werden im Paths Array gespeichert!

			dates = new String[paths.length];
			for (int j = 0; j < paths.length; j++) {
				// System.out.println(paths[j].lastModified());

				// System.out.println("Vale Guardian Datum : " +
				// sdf.format(paths[j].lastModified()));
				dates[j] = sdf.format(paths[j].lastModified()); // dates Array mit knovertiertem Datum
				// System.out.println(dates[j]);

			}

			boss = boss + 1;
			BossNameAlt = BossNamen[boss - 1];
			BossNameNeu = BossNamen[boss];

			Date[] arrayOfDates = new Date[dates.length];
			for (int index = 0; index < dates.length; index++) {
				arrayOfDates[index] = sdf.parse(dates[index]);
			}

			Arrays.sort(arrayOfDates);
			for (int i = 0; i < arrayOfDates.length; i++) {
				System.out.println(arrayOfDates[i]);
			}

			int zaehler = arrayOfDates.length;
			neustesDatum = sdf.format(arrayOfDates[zaehler - 1]);

			for (File i : paths) {
				if (sdf.format(i.lastModified()).equals(neustesDatum)) {
					PfadDerNeustenDatei = i.getAbsolutePath();
					recentPath[nzaehler] = i;
					nzaehler++;
					String date = sdf.format(i.lastModified());
					date = date.replace("/", "");
					date = date.replaceAll(":", "");
					System.out.println(date);
					String newTarget = "C:\\Users\\Patrick\\Desktop\\Raidar Neuste Dateien\\Test" + date + ".evtc";
					target = Paths.get(newTarget);
					Files.copy(i.toPath(), target, StandardCopyOption.REPLACE_EXISTING); // Test Dateien auf Desktop
																							// kopieren
					
					System.out.println(PfadDerNeustenDatei);
					
					System.out.println("IT WORKS");

				}
			}

		}
		
		System.out.println("Bitte Raidar User eingeben: ");
		String username = input.nextLine();
		System.out.println("Bitte Passwort eingeben: ");
		String password = input.nextLine();
		token = getRaidarToken(username, password);

		input.close();
		
		
		for(File x : recentPath) {
			try {
				if (x !=null) {
				DateienUploaden(x , token, username , password);
			}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			}
		}


	public static String getRaidarToken(String username, String password) {
		String token = "";
		String[] command = { "curl", "-F", "\"username=" + username + "\"", "-F", "\"password=" + password + "\"","https://www.gw2raidar.com/api/v2/token" };
		System.out.print("Running Curl with command: ");
		for (String e : command) {
			System.out.print(e);
		}
		System.out.println("");
		ProcessBuilder process = new ProcessBuilder(command);
		Process p;
		try {
			p = process.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			token = reader.readLine(); // Raidar Token

			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
				builder.append(System.getProperty("line.separator"));
			}
			String result = builder.toString();
			System.out.print(result);

		} catch (IOException e) {
			System.out.print("error");
			e.printStackTrace();
		}
		System.out.println("Der abgerufenen Token ist: " + token);
		return token;
	}
	

	private static void DateienUploaden(File path, String token, String username, String password) throws InterruptedException, IOException {  
		
		// String[] command = {"CMD", "/C", "curl" , "-F " + " \"username=" + username + "\"" + "-F " + "\"password=" + password + "\"" + " -F " + "\"file=@" + path.getAbsolutePath() + "\"" + " https://www.gw2raidar.com/api/upload.json" + "--progress-bar >> \"C:\\Users\\Patrick\\Documents\\Guild Wars 2\\addons\\arcdps\\arcdps.cbtlogshttp\""};
		   
		String[] command = {"CMD", "/C", "curl -F \"username=" + username + "\" -F \"password=" + password + "\" -F \"file=@" + path.getAbsolutePath() + "\"  https://www.gw2raidar.com/api/upload.json --progress-bar >> \"C:\\Users\\Patrick\\Documents\\Guild Wars 2\\addons\\arcdps\\arcdps.cbtlogshttp\""};
	        ProcessBuilder probuilder = new ProcessBuilder( command );

	        //Set up your work directory
	        // probuilder.directory(new File("c:\\Test"));
	        
	        Process process = probuilder.start();
	        
	        //Read out dir output
	        java.io.InputStream is = process.getErrorStream();  // Komischerweise liefert der Error Stream die Upload Statistik
	        InputStreamReader isr = new InputStreamReader(is);
	        BufferedReader br = new BufferedReader(isr);
	        String line;
	        System.out.printf("Output of running %s is:\n",
	                Arrays.toString(command));
	        while ((line = br.readLine()) != null) {
	            System.out.println(line);
	        }
	        
	        
	        //Wait to get exit value
	        try {
	            int exitValue = process.waitFor();
	            if (exitValue == 0) {
	            	System.out.println("\n\nLog wurde erfolgreich hochgeladen!");
	            }
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	    }
	}
