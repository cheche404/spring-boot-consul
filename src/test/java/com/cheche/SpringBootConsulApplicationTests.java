package com.cheche;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.ConsulRawClient;
import com.google.common.net.HostAndPort;
import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.model.agent.ImmutableRegCheck;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.health.ServiceHealth;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SpringBootConsulApplicationTests {

  private static final String SERVICE_NAME = "spring-boot-consul";

  private static final String CONSUL_IP = "10.81.86.129";

  private static final Integer CONSUL_PORT = 8500;

  static Consul consul = Consul.builder()
    .withHostAndPort(HostAndPort.fromParts(CONSUL_IP, CONSUL_PORT))
    .withPing(false)
    .build();

  /**
   * service registry
   */
  public static void serviceRegister() {
    AgentClient agent = consul.agentClient();
    // health detection
    ImmutableRegCheck check = ImmutableRegCheck.builder().http("http://10.81.86.129:18080/actuator/health").interval("3s").build();
    ImmutableRegistration.Builder builder = ImmutableRegistration.builder();
    builder.id(SERVICE_NAME)
      .name(SERVICE_NAME)
      .addTags("v1")
      .address(CONSUL_IP)
      .port(18080)
      .addChecks(check);
    agent.register(builder.build());
  }

  /**
   * service acquisition
   */
  public static void serviceGet() {
    HealthClient client = consul.healthClient();

    // get all services
    System.out.println(client.getAllServiceInstances(SERVICE_NAME).getResponse().size());

    // get all normal services (health check passed)
    List<ServiceHealth> responses = client.getHealthyServiceInstances(SERVICE_NAME).getResponse();
    for(ServiceHealth sh : responses ) {
      System.out.println(sh.getService());
    }
  }

  public static void main(String[] args) {
    serviceRegister();
    serviceGet();
    System.exit(0);
  }

}
