package alohajava.tinkainvest.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "telegram")
data class TelegramProperties(
    val token: String? = null,
    val chatId: String? = null
)