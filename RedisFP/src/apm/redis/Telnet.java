package apm.redis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Telnet {
	@SuppressWarnings({ "unchecked" })
	public static void main(String[] args) throws IOException {

		Runnable servers = new Runnable() {

			public void run() {

				Socket sock = null;
				PrintWriter out = null;
				BufferedReader in = null;

				try {
					sock = new Socket((GetPropertiesFile.getPropertyValue("RedisServer")), Integer.parseInt(GetPropertiesFile.getPropertyValue("RedisPort")));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					out = new PrintWriter(sock.getOutputStream(), true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				out.println("INFO");
				// StringBuilder sb = new StringBuilder();
				ArrayList<String> data = new ArrayList<>();
				JSONArray metricArray = new JSONArray();
				// while ((data = in.readLine()) != null) {
				try {
					while (data.add(in.readLine())) {

						// System.out.println(data.get(data.size() -1));
						if (data.get(data.size() - 1).equals("# Keyspace"))
							break;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				out.close();
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					sock.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				for (String s : data) {

					if (!s.contains(":"))
						continue;

					if (s.split(":")[0].equals("redis_version")) {

						System.out.println("MetricName = " + s.split(":")[0]);
						String metricValue = s.split(":")[1];
						System.out.println("MetricValue = " + metricValue);

						// JSONArray metricArray = new JSONArray();
						metricArray.add(createMetric("StringEvent", "RedisTest1:" + s.split(":")[0], metricValue));
					}

					if (s.split(":")[0].equals("used_memory")) {

						System.out.println("MetricName = " + s.split(":")[0]);
						String metricValue = s.split(":")[1];
						System.out.println("MetricValue = " + metricValue);

						// JSONArray metricArray = new JSONArray();
						metricArray.add(createMetric("LongCounter", "RedisTest1:" + s.split(":")[0], metricValue));
					}

					if (s.split(":")[0].equals("instantaneous_ops_per_sec")) {

						System.out.println("MetricName = " + s.split(":")[0]);
						String metricValue = s.split(":")[1];
						System.out.println("MetricValue = " + metricValue);

						// JSONArray metricArray = new JSONArray();
						metricArray.add(createMetric("LongCounter", "RedisTest1:" + s.split(":")[0], metricValue));
					}

					if (s.split(":")[0].equals("evicted_keys")) {

						System.out.println("MetricName = " + s.split(":")[0]);
						String metricValue = s.split(":")[1];
						System.out.println("MetricValue = " + metricValue);

						// JSONArray metricArray = new JSONArray();
						metricArray.add(createMetric("LongCounter", "RedisTest1:" + s.split(":")[0], metricValue));
					}

					if (s.split(":")[0].equals("mem_fragmentation_ratio")) {

						System.out.println("MetricName = " + s.split(":")[0]);
						String metricValue = s.split(":")[1];
						System.out.println("MetricValue = " + metricValue);

						// JSONArray metricArray = new JSONArray();
						metricArray.add(createMetric("StringEvent", "RedisTest1:" + s.split(":")[0], metricValue));
					}

					if (s.split(":")[0].equals("connected_clients")) {

						System.out.println("MetricName = " + s.split(":")[0]);
						String metricValue = s.split(":")[1];
						System.out.println("MetricValue = " + metricValue);

						// JSONArray metricArray = new JSONArray();
						metricArray.add(createMetric("LongCounter", "RedisTest1:" + s.split(":")[0], metricValue));
					}
					if (s.split(":")[0].equals("connected_slaves")) {

						System.out.println("MetricName = " + s.split(":")[0]);
						String metricValue = s.split(":")[1];
						System.out.println("MetricValue = " + metricValue);

						// JSONArray metricArray = new JSONArray();
						metricArray.add(createMetric("LongCounter", "RedisTest1:" + s.split(":")[0], metricValue));
					}
					if (s.split(":")[0].equals("master_last_io_seconds_ago")) {

						System.out.println("MetricName = " + s.split(":")[0]);
						String metricValue = s.split(":")[1];
						System.out.println("MetricValue = " + metricValue);

						// JSONArray metricArray = new JSONArray();
						metricArray.add(createMetric("LongCounter", "RedisTest1:" + s.split(":")[0], metricValue));
					}
					if (s.split(":")[0].equals("keyspace_misses")) {

						System.out.println("MetricName = " + s.split(":")[0]);
						String metricValue = s.split(":")[1];
						System.out.println("MetricValue = " + metricValue);

						// JSONArray metricArray = new JSONArray();
						metricArray.add(createMetric("LongCounter", "RedisTest1:" + s.split(":")[0], metricValue));
					}
					if (s.split(":")[0].equals("total_commands_processed")) {

						System.out.println("MetricName = " + s.split(":")[0]);
						String metricValue = s.split(":")[1];
						System.out.println("MetricValue = " + metricValue);

						// JSONArray metricArray = new JSONArray();
						metricArray.add(createMetric("LongCounter", "RedisTest1:" + s.split(":")[0], metricValue));
					}

				}
				// System.out.println("TEST***************");
				JSONObject metricsToEPAgent = new JSONObject();
				metricsToEPAgent.put("metrics", metricArray);
				System.out.println("All Metrics = " + metricsToEPAgent.toString());
				WebServiceHandler.sendMetric(metricsToEPAgent.toString(), (GetPropertiesFile.getPropertyValue("EPAgentHost")),Integer.parseInt(GetPropertiesFile.getPropertyValue("EPAgentPort")) );
			}

			public JSONObject createMetric(String type, String name, Object value) {

				JSONObject metric = new JSONObject();
				metric.put("type", type);
				metric.put("name", name);
				metric.put("value", value);
				System.out.println("metricINFO = " + metric.toString());
				return metric;
			}
		};

		ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
		// executor.scheduleAtFixedRate(servers, 0, 15, TimeUnit.SECONDS);
		if ((GetPropertiesFile.getPropertyValue("delaytime").isEmpty())
				|| Integer.parseInt(GetPropertiesFile.getPropertyValue("delaytime")) < 5) {
			executor.scheduleAtFixedRate(servers, 0, 5, TimeUnit.MINUTES);

		} else {
			executor.scheduleAtFixedRate(servers, 0, Integer.parseInt(GetPropertiesFile.getPropertyValue("delaytime")),
					TimeUnit.MINUTES);

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
