package apm.redis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class GetData {

	public static void main(String[] args) throws IOException {

		// processData();

		// }

		Runnable servers = new Runnable() {

			@SuppressWarnings("unchecked")
			public void run() {
				// public static void processData() throws IOException {
				Process p = null;
				try {
					p = Runtime.getRuntime().exec(GetPropertiesFile.getPropertyValue("CLI") + " info");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

				String data;
				JSONArray metricArray = new JSONArray();

				try {
					while ((data = input.readLine()) != null) {

						String parts[] = data.split(":");
						// System.out.println("Array Length = " + parts.length);

						// Grab only potential metrics
						if (parts.length == 2) {
							String metricName = parts[0];
							// Specifically grab desired metrics using if
							// statements
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
							// ***This is a decimal value***
							if (metricName.equals("mem_fragmentation_ratio")) {

								System.out.println("MetricName = " + metricName);
								String metricValue = parts[1];
								System.out.println("MetricValue = " + metricValue);

								// JSONArray metricArray = new JSONArray();
								metricArray
										.add(createMetric("StringEvent", "Redis:" + metricName + "test", metricValue));
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
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// System.out.println("Agent List" + "\n" + result);
				// Close connections
				// bw.close();
				try {
					input.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				JSONObject metricsToEPAgent = new JSONObject();
				metricsToEPAgent.put("metrics", metricArray);
				System.out.println("All Metrics = " + metricsToEPAgent.toString());

				// Location & Port of EPAgent
				WebServiceHandler.sendMetric(metricsToEPAgent.toString(),
						GetPropertiesFile.getPropertyValue("EPAgentHost"),
						Integer.valueOf(GetPropertiesFile.getPropertyValue("EPAgentPort")));

			}

			@SuppressWarnings("unchecked")
			public JSONObject createMetric(String type, String name, Object value) {

				JSONObject metric = new JSONObject();
				metric.put("type", type);
				metric.put("name", name);
				metric.put("value", value);
				System.out.println("metricINFO = " + metric.toString());
				return metric;
			}
		};
		// Executer to trigger runnable thread. Delay time is defined in the
		// redis.properties DEFAULTS to 5 minutes if no value provided or below
		// 5 minutes
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
		if ((GetPropertiesFile.getPropertyValue("delaytime").isEmpty())
				|| Integer.parseInt(GetPropertiesFile.getPropertyValue("delaytime")) < 15) {
			executor.scheduleAtFixedRate(servers, 0, 15, TimeUnit.MINUTES);
		} else {
			executor.scheduleAtFixedRate(servers, 0, 1, TimeUnit.MINUTES);

			executor.scheduleAtFixedRate(servers, 0, 5, TimeUnit.SECONDS);
			// }
			// Loop to keep Main Thread alive
			while (true) {
				try {
					Thread.sleep(15000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
