package gmlim.workmanager.sample.workmanagersample.workers

import android.util.Log
import androidx.work.Worker

class UniqueWorker : Worker() {
    override fun doWork(): Result {
        Log.d("Sample", "UniqueWorker start...$id")

        Thread.sleep(5000)

        if (isStopped) {
            Log.d("Sample", "UniqueWorker running stop")

            if (isCancelled) {
                Log.d("Sample", "UniqueWorker running stop AND cancel")
            } else {
                Log.d("Sample", "UniqueWorker running stop BUT not cancel")
            }
        }

        Log.d("Sample", "UniqueWorker end...$id")
        return Result.SUCCESS
    }

}