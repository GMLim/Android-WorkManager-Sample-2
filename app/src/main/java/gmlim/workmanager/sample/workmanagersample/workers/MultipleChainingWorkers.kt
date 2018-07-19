package gmlim.workmanager.sample.workmanagersample.workers

import android.util.Log
import androidx.work.Worker
import java.util.*

class CountWordWorker : Worker() {
    override fun doWork(): Result {
        outputData = inputData

        return Result.SUCCESS
    }
}

class SortWorker : Worker() {
    override fun doWork(): Result {
        // ArrayCreatingInputMerger , 같은 key 를 가지는 value 를 배열로 묶어서 반환합니다.
        val counts = inputData.getIntArray("count")
        Log.d("Sample", "counts : ${Arrays.toString(counts)}")

        return Result.SUCCESS
    }
}