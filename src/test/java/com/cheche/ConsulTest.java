package com.cheche;

import com.cheche.domain.RegisterServiceModel;
import com.cheche.service.ConsulService;
import com.orbitz.consul.model.agent.ImmutableRegCheck;
import com.orbitz.consul.model.health.Service;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

/**
 * ConsulTest
 *
 * @author fudy
 * @date 2023/3/2
 */
@SpringBootTest(classes = SpringBootConsulApplication.class)
public class ConsulTest {

  @Autowired
  private ConsulService consulService;

  @Test
  public void test() {
    RegisterServiceModel registerServiceModel = new RegisterServiceModel();
    registerServiceModel.setId("consul-service-register-test");
    registerServiceModel.setName("consul-service-register-test");
    registerServiceModel.setAddress("10.81.86.129");
    registerServiceModel.setPort(18080);
    registerServiceModel.setTags(new String[]{"v2", "v1"});
    registerServiceModel.setCheck(RegisterServiceModel.buildImmutableRegCheck("http://10.81.86.129:18080/actuator/health", "5s"));
    consulService.serviceRegister(registerServiceModel);

    Map<String, Service> map = consulService.getAllService();
    for (Map.Entry<String, Service> entry : map.entrySet()) {
      System.out.println(entry.getValue().toString());
    }

  }


}
