package com.raja;

import com.raja.model.Employee;
import com.raja.repository.EmployeeRepository;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.InetSocketAddress;

@SpringBootApplication
public class SpringbootElasticsearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootElasticsearchApplication.class, args);
	}


	private static final String CLUSTER_NAME = "419cf41421304041b4c6f149ccf4bd55";
	private static final String USERNAME = "elastic";
	private static final String PASSWORD = "AVxlJM2pVHhSmK9MNavpcRQl";

	private static final String CLUSTER_URL = CLUSTER_NAME + ".us-east-1.aws.found.io";
	private static final String CREDENTIALS = USERNAME + ":" + PASSWORD;

	@Bean
	public ApplicationRunner runner(TransportClient client, EmployeeRepository repository) {


		return (args) -> {
			ClusterHealthResponse response = client.admin().cluster().prepareHealth().get();
			System.out.println("response = " + response.getClusterName());

			repository.deleteAll();
			repository.save(new Employee(1L, "Alice", "Smith", ""));
			repository.save(new Employee(2L, "Bob", "Smith", ""));
			System.out.println("Customers found with findAll():");
			System.out.println("-------------------------------");
			for (Employee employee : repository.findAll()) {
				System.out.println(employee);
			}
			System.out.println();

		};
	}

	@Bean
	public TransportClient elasticsearchSecuredClient() throws Exception {
		// Based on https://github.com/elastic/found-shield-example/blob/master/src/main/java/org/elasticsearch/cloud/transport/example/TransportExample.java
		Settings settings = Settings.builder()
				.put("client.transport.nodes_sampler_interval", "5s")
				.put("client.transport.sniff", false)
				.put("transport.tcp.compress", true)
				.put("cluster.name", CLUSTER_NAME)
				.put("xpack.security.transport.ssl.enabled", true)
				.put("request.headers.X-Found-Cluster", CLUSTER_NAME)
				.put("xpack.security.user", CREDENTIALS)
				.build();

		TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
				.addTransportAddress(new TransportAddress(new InetSocketAddress(CLUSTER_URL, 9343)));
		return client;
		/*return new PreBuiltXPackTransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress(CLUSTER_URL, 9343)));*/
	}
}
