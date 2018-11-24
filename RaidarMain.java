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
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.omg.CORBA.NameValuePair;

public class RaidarMain {

	public static void main(String[] args) throws ParseException, IOException {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String path;
		String[] dates = new String[1];
		File[] paths = new File[1];
		String BossNameAlt;
		String BossNameNeu;
		String[] BossNamen = new String[17];
		String PfadDerNeustenDatei;
		int boss;
		String token = "";
		String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:63.0) Gecko/20100101 Firefox/63.0";
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
		while (boss < 16) {

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
		String url = "https://www.gw2raidar.com/api/v2/token";
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

	
	}
}
