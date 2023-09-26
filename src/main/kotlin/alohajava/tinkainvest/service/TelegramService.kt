package alohajava.tinkainvest.service

import alohajava.tinkainvest.configuration.properties.TelegramProperties
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
@ConditionalOnProperty(prefix = "telegram", name = ["enabled"], havingValue = "true")
class TelegramService(
    telegramProperties: TelegramProperties
) {
    private val chatId = telegramProperties.chatId
    private val webClient = WebClient.builder()
        .baseUrl("https://api.telegram.org/bot${telegramProperties.token}")
        .build()

    suspend fun sendMessage(text: String) {
        val uri = "/sendMessage?chat_id=$chatId&text=$text"
        webClient.get()
            .uri(uri)
            .retrieve()
            .bodyToMono(Void::class.java)
            .awaitSingleOrNull()
    }

    @PostConstruct
    fun sendInitMessage() {
        CoroutineScope(Dispatchers.Default).launch {
            sendMessage("Бот запущен")
        }
    }
}
