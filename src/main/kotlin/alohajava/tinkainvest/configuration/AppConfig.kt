package alohajava.tinkainvest.configuration

import alohajava.tinkainvest.configuration.properties.AccountProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import ru.tinkoff.piapi.core.InvestApi

@Configuration
class AppConfig {

    @Bean
    fun investApi(accountProperties: AccountProperties) =
        InvestApi.create(accountProperties.token)

    @Bean
    fun taskScheduler() = ThreadPoolTaskScheduler()

}
