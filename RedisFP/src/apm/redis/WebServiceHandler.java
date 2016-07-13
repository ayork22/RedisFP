package apm.redis;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebServiceHandler {

	/**
	 * @param metrics
	 *            = JSON formatted metrics
	 * @param hostname
	 *            = EPA Host
	 * @param port
	 *            = EPA Port
	 * @return String = is what we're returning - Metrics posted successfully or
	 *         NOT
	 */
	public static String sendMetric(String metrics, String hostname, int port) {

		String output = "";
		try {

			// Pass JSON File Data to REST Service
			try {
				URL url = new URL("http://" + hostname + ":" + Integer.toString(port) + "/apm/metricFeed");
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setDoOutput(true);
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setConnectTimeout(5000);
				connection.setReadTimeout(5000);

				// int code = connection.getResponseCode();

				OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
				out.write(metrics.toString());
				out.close();

				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

				// *****This returns output from EPAgent after sending it
				while ((output = in.readLine()) != null) {
					output = output + "\n";
				}
				in.close();
				// StashFPwrapper.logger.debug("Metrics Posted Successfully..");
				System.out.println("Metrics Posted Successfully..");
			} catch (Exception e) {
				System.out.println("Metrics NOT Posted Successfully..");
				System.out.println(e);
				// StashFPwrapper.logger.error("Error while Posting Metrics : "
				// + e);
			}

		} catch (Exception e) {
			e.printStackTrace();
			// StashFPwrapper.logger.error(e);
		}
		// returning output from EPAgent
		return output;
	}

}