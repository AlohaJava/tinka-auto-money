package alohajava.tinkainvest.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class DontSleepController {

    @GetMapping("/dont-sleep")
    suspend fun dontSleep(): LocalDateTime {
        return LocalDateTime.now();
    }
}