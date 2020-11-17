package com.xxAMIDOxx.xxSTACKSxx.provider.gcp;

import com.xxAMIDOxx.xxSTACKSxx.menu.repository.MenuRepositoryAdapter;
import com.xxAMIDOxx.xxSTACKSxx.provider.gcp.repository.GcpMenuRepositoryAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GcpConfig {

  @Bean("menuRepositoryAdapter")
  @ConditionalOnProperty(name = "cloud-provider", havingValue = "gcp")
  public MenuRepositoryAdapter gcpMenuAdapter() {
    return new GcpMenuRepositoryAdapter();
  }
}
