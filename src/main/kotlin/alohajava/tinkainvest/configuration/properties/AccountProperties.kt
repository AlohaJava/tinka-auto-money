package alohajava.tinkainvest.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import kotlin.random.Random

@ConfigurationProperties(prefix = "account")
data class AccountProperties(
    val token: String,
    val cronSettings: List<CronSettings>
)

data class CronSettings(
    val cron: String,
    val buyCount: String,
    val figi: String,
    val repeatCount: String,
    val delayBetween: String,
    val delayBefore: String
)

fun String.toRandomLongInRange(): Long {
    val (start, end) = this.split("-").map { it.toLong() }
    return Random.nextLong(start, end + 1)
}

fun String.toRandomIntInRange(): Int {
    val (start, end) = this.split("-").map { it.toInt() }
    return Random.nextInt(start, end + 1)
}