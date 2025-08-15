package com.joshayoung.notemark.core.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DebounceNoteSave {
    fun <T1, T2> debounce(
        scope: CoroutineScope,
        delay: Long,
        function: (T1, T2) -> Unit
    ): (T1, T2) -> Unit {
        var job: Job? = null
        return { one: T1, two: T2 ->
            job?.cancel()
            job = scope.launch {
                delay(delay)
                function(one, two)
            }
        }
    }
}