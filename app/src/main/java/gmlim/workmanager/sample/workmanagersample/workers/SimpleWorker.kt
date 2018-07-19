package gmlim.workmanager.sample.workmanagersample.workers

import androidx.work.Worker

class SimpleWorker : Worker() {
    override fun doWork(): Result {
        Thread.sleep(3000)

        return Result.SUCCESS
    }
}