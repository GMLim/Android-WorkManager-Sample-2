package gmlim.workmanager.sample.workmanagersample.workers

import android.util.Log
import androidx.work.Worker
import androidx.work.toWorkData

class CompressWorker : Worker() {
    override fun doWork(): Result {
        val fileName = inputData.getString("file_name", "")

        Log.d("Sample", "CompressWorker Target : ${fileName!!}")

        // 전달받은 파일이름의 이미지를 압축합니다.

        // 결과물을 compress.jpg 로 저장합니다.

        // 이제 compress.jpg 를 UploadWorker 로 전달합니다.
        outputData = mapOf("compress_file_name" to "compress.jpg").toWorkData()

        return Result.SUCCESS
    }
}

class UploadWorker : Worker() {
    override fun doWork(): Result {
        val fileName = inputData.getString("compress_file_name", "")

        Log.d("Sample", "UploadWorker Target : ${fileName!!}")

        return Result.SUCCESS
    }
}