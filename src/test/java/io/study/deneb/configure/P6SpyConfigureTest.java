package io.study.deneb.configure;

import com.p6spy.engine.spy.P6SpyDriver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class P6SpyConfigureTest {

  @Autowired
  ApplicationContext context;

  @Autowired
  DataSource dataSource;

  @Test
  void DataSourceLoaded() {
    Assertions.assertNotNull(dataSource);
  }

  @Test
  void P6SpyConfigureLoaded() {
    P6SpySqlFormatConfigure p6SpySqlFormatConfigure = context.getBean(P6SpySqlFormatConfigure.class);
    assertNotNull(p6SpySqlFormatConfigure);
  }

}