package com.joshayoung.notemark.note.data

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.await
import com.joshayoung.notemark.note.data.workers.DataSyncWorker
import com.joshayoung.notemark.note.domain.SyncNotesScheduler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.toJavaDuration

class SyncNoteWorkerScheduler(
    private val context: Context,
) : SyncNotesScheduler {
    private val workManager = WorkManager.getInstance(context)

    override suspend fun scheduleSync(interval: Duration) {
        scheduleNotesSync(interval = interval)
    }

    private suspend fun scheduleNotesSync(interval: Duration) {
        val isSyncScheduled =
            withContext(Dispatchers.IO) {
                workManager
                    .getWorkInfosByTag("sync_work")
                    .get()
                    .isNotEmpty()
            }

        if (isSyncScheduled) {
            return
        }

        val workRequest =
            PeriodicWorkRequestBuilder<DataSyncWorker>(
                repeatInterval = interval.toJavaDuration(),
            ).setConstraints(
                Constraints
                    .Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build(),
            ).setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 2000L,
                timeUnit = TimeUnit.MILLISECONDS,
            )
//            .setInitialDelay()
                .addTag("sync_work")
                .build()

        workManager.enqueue(workRequest).await()
    }

    override suspend fun cancelSyncs() {
        WorkManager
            .getInstance(context)
            .cancelAllWork()
            .await()
    }
}