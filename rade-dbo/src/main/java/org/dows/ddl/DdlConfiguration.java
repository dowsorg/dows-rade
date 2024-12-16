package org.dows.ddl;

import org.dows.ddl.store.DdlBuildrRepository;
import org.dows.ddl.store.DdlWrappersRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DdlConfiguration {
    @Bean
    public DdlBuildrRepository ddlBuildrRepository() {
        return new DdlBuildrRepository();
    }

    @Bean
    public DdlWrappersRepository ddlWrappersRepository() {
        return new DdlWrappersRepository();
    }
}
