package alohajava.tinkainvest

import alohajava.tinkainvest.configuration.properties.AccountProperties
import alohajava.tinkainvest.configuration.properties.TelegramProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableConfigurationProperties(value = [
    AccountProperties::class,
    TelegramProperties::class
])
@EnableScheduling
@SpringBootApplication
class TinkaInvestApplication

fun main(args: Array<String>) {
    runApplication<TinkaInvestApplication>(*args)
}
