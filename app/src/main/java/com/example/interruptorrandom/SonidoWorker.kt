package com.example.interruptorrandom

import android.content.Context
import android.media.MediaPlayer
import androidx.work.Worker
import androidx.work.WorkerParameters

class SonidoWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        return try {
            val letra = inputData.getString("letra") ?: "a"
            val rawResId = when (letra) {
                "a" -> R.raw.a
                "b" -> R.raw.b
                "c" -> R.raw.c
                "d" -> R.raw.d
                "e" -> R.raw.e
                else -> R.raw.a
            }

            val mediaPlayer = MediaPlayer.create(applicationContext, rawResId)
            mediaPlayer?.start()

            mediaPlayer?.setOnCompletionListener { it.release() }

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}