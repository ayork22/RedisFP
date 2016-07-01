package apm.redis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class GetData {

	private static Object metricArray;

	public static void main(String[] args) throws IOException {

		processData();

	}

	@SuppressWarnings("unchecked")
	public static String processData() throws IOException {
		Process p = null;
		try {
			p = Runtime.getRuntime().exec("/usr/local/opt/redis/bin/redis-cli info");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String data;
		JSONArray metricArray = new JSONArray();
		try {
			while ((data = input.readLine()) != null) {

				String parts[] = data.split(":");
				// System.out.println("Array Length = " + parts.length);

				// Grab only potential metrics
				if (parts.length == 2) {
					String metricName = parts[0];
					// Specifically grab desired metrics using if statements
					if (metricName.equals("redis_version")) {

						System.out.println("MetricName = " + metricName);
						String metricValue = parts[1];
						System.out.println("MetricValue = " + metricValue);

						// JSONArray metricArray = new JSONArray();
						metricArray.add(createMetric("StringEvent", "Redis:" + metricName, metricValue));
					}
					if (metricName.equals("used_memory")) {

						System.out.println("MetricName = " + metricName);
						String metricValue = parts[1];
						System.out.println("MetricValue = " + metricValue);

						// JSONArray metricArray = new JSONArray();
						metricArray.add(createMetric("LongCounter", "Redis:" + metricName, metricValue));
					}
					// metricRootLocation + ":Number of Repos"
					
					if (metricName.equals("instantaneous_ops_per_sec")) {

						System.out.println("MetricName = " + metricName);
						String metricValue = parts[1];
						System.out.println("MetricValue = " + metricValue);

						// JSONArray metricArray = new JSONArray();
						metricArray.add(createMetric("LongCounter", "Redis:" + metricName, metricValue));
					}
					if (metricName.equals("evicted_keys")) {

						System.out.println("MetricName = " + metricName);
						String metricValue = parts[1];
						System.out.println("MetricValue = " + metricValue);

						// JSONArray metricArray = new JSONArray();
						metricArray.add(createMetric("LongCounter", "Redis:" + metricName, metricValue));
					}
//					***This is a decimal value***
					if (metricName.equals("mem_fragmentation_ratio")) {

						System.out.println("MetricName = " + metricName);
						String metricValue = parts[1];
						System.out.println("MetricValue = " + metricValue);

						// JSONArray metricArray = new JSONArray();
						metricArray.add(createMetric("StringEvent", "Redis:" + metricName + "test", metricValue));
					}
					if (metricName.equals("connected_clients")) {

						System.out.println("MetricName = " + metricName);
						String metricValue = parts[1];
						System.out.println("MetricValue = " + metricValue);

						// JSONArray metricArray = new JSONArray();
						metricArray.add(createMetric("LongCounter", "Redis:" + metricName, metricValue));
					}
					if (metricName.equals("connected_slaves")) {

						System.out.println("MetricName = " + metricName);
						String metricValue = parts[1];
						System.out.println("MetricValue = " + metricValue);

						// JSONArray metricArray = new JSONArray();
						metricArray.add(createMetric("LongCounter", "Redis:" + metricName, metricValue));
					}
					if (metricName.equals("master_last_io_seconds_ago")) {

						System.out.println("MetricName = " + metricName);
						String metricValue = parts[1];
						System.out.println("MetricValue = " + metricValue);

						// JSONArray metricArray = new JSONArray();
						metricArray.add(createMetric("LongCounter", "Redis:" + metricName, metricValue));
					}
					if (metricName.equals("keyspace_misses")) {

						System.out.println("MetricName = " + metricName);
						String metricValue = parts[1];
						System.out.println("MetricValue = " + metricValue);

						// JSONArray metricArray = new JSONArray();
						metricArray.add(createMetric("LongCounter", "Redis:" + metricName, metricValue));
					}
					if (metricName.equals("total_commands_processed")) {

						System.out.println("MetricName = " + metricName);
						String metricValue = parts[1];
						System.out.println("MetricValue = " + metricValue);

						// JSONArray metricArray = new JSONArray();
						metricArray.add(createMetric("LongCounter", "Redis:" + metricName, metricValue));
					}
					

				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Print FINAL String of all Agents
		String result = sb.toString();
		// System.out.println("Agent List" + "\n" + result);
		// Close connections
		// bw.close();
		input.close();

		JSONObject metricsToEPAgent = new JSONObject();
		metricsToEPAgent.put("metrics", metricArray);
		System.out.println("All Metrics = " + metricsToEPAgent.toString());

		// Location & Port of EPAgent
		WebServiceHandler.sendMetric(metricsToEPAgent.toString(), "localhost", 9080);

		return result;
	}

	@SuppressWarnings("unchecked")
	public static JSONObject createMetric(String type, String name, Object value) {

		JSONObject metric = new JSONObject();
		metric.put("type", type);
		metric.put("name", name);
		metric.put("value", value);
		System.out.println("metric = " + metric.toString());
		return metric;
	}
}
