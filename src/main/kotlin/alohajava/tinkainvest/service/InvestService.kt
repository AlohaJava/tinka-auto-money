package alohajava.tinkainvest.service

import alohajava.tinkainvest.exception.TradeException
import alohajava.tinkainvest.configuration.properties.CronSettings
import alohajava.tinkainvest.configuration.properties.toRandomIntInRange
import alohajava.tinkainvest.configuration.properties.toRandomLongInRange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.future.await
import kotlinx.coroutines.launch
import mu.KLogging
import org.springframework.stereotype.Service
import ru.tinkoff.piapi.contract.v1.OrderDirection
import ru.tinkoff.piapi.contract.v1.OrderType
import ru.tinkoff.piapi.contract.v1.Quotation
import ru.tinkoff.piapi.core.InvestApi
import java.util.*

@Service
class InvestService(
    private val investApi: InvestApi,
    private val telegramService: TelegramService?
) {
    companion object : KLogging() {
        private val DEFAULT_QUOTATION = Quotation.newBuilder().setUnits(0).build()
    }

    suspend fun makeProkrut(cronSettings: CronSettings) {
        val buyCount = cronSettings.buyCount.toRandomLongInRange()
        val repeatCount = cronSettings.repeatCount.toRandomIntInRange()
        val figi = cronSettings.figi

        telegramService?.sendMessage(
            "Начал прокрутку с $figi, количество: $buyCount, прокрутов: $repeatCount"
        )

        delay(cronSettings.delayBefore.toRandomLongInRange())

        val accountId = investApi.userService
            .accounts
            .await()
            .first()
            .id

        repeat(repeatCount) {
            createOrder(figi, buyCount, OrderDirection.ORDER_DIRECTION_BUY, accountId)
            delay(cronSettings.delayBetween.toRandomLongInRange())
            createOrder(figi, buyCount, OrderDirection.ORDER_DIRECTION_SELL, accountId)
        }
        telegramService?.sendMessage(
            "Сделал прокрутку с $figi, количество: $buyCount, прокрутов: $repeatCount"
        )
    }

    private suspend fun createOrder(
        figi: String,
        quantity: Long,
        direction: OrderDirection,
        accountId: String
    ) {
        investApi.ordersService.postOrder(
            figi,
            quantity,
            DEFAULT_QUOTATION,
            direction,
            accountId,
            OrderType.ORDER_TYPE_MARKET,
            UUID.randomUUID().toString()
        )
            .exceptionally {
                CoroutineScope(Dispatchers.Default).launch {
                    telegramService?.sendMessage("Ошибка создания order для figi: $figi, ${it.message}")
                }
                throw TradeException(it.message);
            }.await()
    }

}
