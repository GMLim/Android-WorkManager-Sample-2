package gmlim.workmanager.sample.workmanagersample.workers

import android.util.Log
import androidx.work.Worker

class CancelWorker: Worker() {
    override fun doWork(): Result {
        Log.d("Sample", "CancelWorker start...")

        for (i in 1..10) {
            Log.d("Sample", "CancelWorker running $i")
            Thread.sleep(1000)

            if (isStopped) {
                // 작업이 중단되었습니다.
                // 명시적 중단 인지 시스템에 의한 중단인지는 isCancelled 로 판단 됩니다.
                Log.d("Sample", "CancelWorker running stop")

                if (isCancelled) {
                    // 사용자에 의해 작업이 취소 되어 작업이 중단되었습니다.
                    // 이 작업은 다시 실행 되지 않습니다.
                    Log.d("Sample", "CancelWorker running stop AND cancel")
                } else {
                    // 시스템에 의해 작업이 중단되었지만 취소되지 않았으므로 적당한 시점에 다시 실행 됩니다.
                    Log.d("Sample", "CancelWorker running stop BUT not cancel")
                }

                break
            }
        }

        Log.d("Sample", "CancelWorker end...")

        return Result.SUCCESS
    }
}