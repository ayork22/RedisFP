package apm.redis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Telnet {
	@SuppressWarnings({ "unchecked" })
	public static void main(String[] args) throws IOException {

		Socket sock = null;
		PrintWriter out = null;
		BufferedReader in = null;

		sock = new Socket("localhost", 6379);
		out = new PrintWriter(sock.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

		out.println("INFO");
		// StringBuilder sb = new StringBuilder();
		ArrayList<String> data = new ArrayList<>();
		JSONArray metricArray = new JSONArray();
		// while ((data = in.readLine()) != null) {
		while (data.add(in.readLine())) {

			// System.out.println(data.get(data.size() -1));
			if (data.get(data.size() - 1).equals("# Keyspace"))
				break;
		}
		out.close();
		in.close();
		sock.close();
		// System.out.println("TEST***************");
		for (String s : data) {

			if (!s.contains(":"))
				continue;

			// System.out.println(s);

			if (s.split(":")[0].equals("redis_version")) {

				System.out.println("MetricName = " + s.split(":")[0]);
				String metricValue = s.split(":")[1];
				System.out.println("MetricValue = " + metricValue);

				// JSONArray metricArray = new JSONArray();
				metricArray.add(createMetric("StringEvent", "Redis:" + s.split(":")[0], metricValue));
			}

		}
		// System.out.println("TEST***************");
		JSONObject metricsToEPAgent = new JSONObject();
		metricsToEPAgent.put("metrics", metricArray);
		System.out.println("All Metrics = " + metricsToEPAgent.toString());
		WebServiceHandler.sendMetric(metricsToEPAgent.toString(), "localhost", 9080);
	}

	@SuppressWarnings("unchecked")
	public static JSONObject createMetric(String type, String name, Object value) {

		JSONObject metric = new JSONObject();
		metric.put("type", type);
		metric.put("name", name);
		metric.put("value", value);
		System.out.println("metricINFO = " + metric.toString());
		return metric;
	}
}
