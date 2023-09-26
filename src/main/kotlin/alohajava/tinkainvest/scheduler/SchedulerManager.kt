package alohajava.tinkainvest.scheduler

import alohajava.tinkainvest.configuration.properties.AccountProperties
import alohajava.tinkainvest.configuration.properties.CronSettings
import alohajava.tinkainvest.service.InvestService
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch
import mu.KLogging
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.support.CronTrigger
import org.springframework.stereotype.Component
import java.util.*

@Component
class SchedulerManager(
    private val accountProperties: AccountProperties,
    private val taskScheduler: TaskScheduler,
    private val investService: InvestService
) {

    @PostConstruct
    fun initCronTasks() {
        registerCrons()
    }

    private fun registerCrons() {
        logger.info { "Registering crons..." }
        val timeZone = TimeZone.getTimeZone("Europe/Moscow")
        accountProperties.cronSettings.forEach { cronSetting ->
            scheduleTask(cronSetting, timeZone)
            logger.info { "Registered cron: ${cronSetting.cron}" }
        }
    }

    private fun scheduleTask(cronSetting: CronSettings, timeZone: TimeZone) {

        val cronTask = Runnable {
            CoroutineScope(Dispatchers.Default).launch {
                investService.makeProkrut(cronSetting)
            }
        }

        val cronTrigger = CronTrigger(cronSetting.cron, timeZone)
        taskScheduler.schedule(cronTask, cronTrigger)
    }

    companion object : KLogging()
}