package com.joshayoung.notemark.note.data.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.joshayoung.notemark.note.data.use_cases.PullRemoteNotesUseCase
import com.joshayoung.notemark.note.data.use_cases.SyncNotesUseCase
import java.lang.Exception

class DataSyncWorker(
    private val context: Context,
    private val params: WorkerParameters,
    private val syncNotesUseCase: SyncNotesUseCase,
    private val pullRemoteNotesUseCase: PullRemoteNotesUseCase,
) : CoroutineWorker(
        context,
        params,
    ) {
    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }

        val frequency = params.inputData.getString(SYNC_FREQUENCY) ?: return Result.failure()

        try {
            // TODO: Return a Result for these use cases?
            syncNotesUseCase.execute()
            pullRemoteNotesUseCase.execute()

            return Result.success()
        } catch (e: Exception) {
            return Result.retry()
        }
    }

    companion object {
        const val SYNC_FREQUENCY = "SYNC_FREQUENCY"
    }
}