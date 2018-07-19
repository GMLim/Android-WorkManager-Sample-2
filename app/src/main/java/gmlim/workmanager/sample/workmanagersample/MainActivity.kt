package gmlim.workmanager.sample.workmanagersample

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import androidx.work.*
import gmlim.workmanager.sample.workmanagersample.workers.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btRunOneTime.setOnClickListener {
            doWorkOneTime()
        }

        btRunChaningWork.setOnClickListener {
            doWorkChaining()
        }

        btRunChaningWork2.setOnClickListener {
            doWorkChaining2()
        }

        btRunCancelWork.setOnClickListener {
            doCancelWork()
        }

        btCancelWorkUseId.setOnClickListener {
            cancelWorkUseId()
        }

        btCancelWorkUseTag.setOnClickListener {
            cancelWorkUseTag()
        }

        btRunUniqueWorkUseKEEP.setOnClickListener {
            doUniqueWorkUseKEEP()
        }

        btRunUniqueWorkUseREPLACE.setOnClickListener {
            doUniqueWorkUseREPLACE()
        }

        btRunUniqueWorkUseAPPEND.setOnClickListener {
            doUniqueWorkUseAPPEND()
        }
    }

    private fun doWorkOneTime() {
        val workRequest = OneTimeWorkRequestBuilder<SimpleWorker>().build()

        val workManager = WorkManager.getInstance()

        workManager?.let{
            // WorkManager 의 큐에 작업을 추가 한다
            it.enqueue(workRequest)

            // 작업의 id 를 이용해서 WorkManager 로 부터 LiveData 를 반환 받는다
            val statusLiveData = it.getStatusById(workRequest.id)

            // 이 LiveData 에 Observer 를 설정하면 해당 작업의 상태를 추적할수 있다
            statusLiveData.observe(this, Observer {
                Log.d("Sample", "state : ${it?.state}")
            })
        }
    }

    private fun doWorkChaining() {
        // 전달할 정보를 담은 Map 객체를 생성합니다.
        val input = mapOf("file_name" to "sdcard/user_choice_picture.jpg")

        // Data 클래스의 Builder 를 사용해서 input 을 담고 있는 Data 객체를 생성합니다.
        val inputData = Data.Builder().putAll(input).build()

        // 코틀린의 경우 Map 에 제공되는 인라인 함수 toWorkData() 를 이용해 쉽게 Data 객체를 생성할수도 있습니다.
//        val inputData = input.toWorkData()

        // WorkRequest 의 setInputData() 메서드로 작업자에 정보를 전달합니다.
        val compressWork = OneTimeWorkRequestBuilder<CompressWorker>()
                .setInputData(inputData)
                .build()

        val uploadWork = OneTimeWorkRequestBuilder<UploadWorker>().build()

        WorkManager.getInstance()?.apply {
            beginWith(compressWork).then(uploadWork).enqueue()
        }
    }

    private fun doWorkChaining2() {
        val countWordWorker1 = OneTimeWorkRequestBuilder<CountWordWorker>()
                .setInputData(mapOf("count" to 10).toWorkData())
                .build()

        val countWordWorker2 = OneTimeWorkRequestBuilder<CountWordWorker>()
                .setInputData(mapOf("count" to 20).toWorkData())
                .build()

        val countWordWorker3 = OneTimeWorkRequestBuilder<CountWordWorker>()
                .setInputData(mapOf("count" to 30).toWorkData())
                .build()

        // ArrayCreatingInputMerger 를 사용하도록 변경, 기본은 OverwritingInputMerger
        val sortWordWorker = OneTimeWorkRequestBuilder<SortWorker>()
                .setInputMerger(ArrayCreatingInputMerger::class)
                .build()

        WorkManager.getInstance()?.apply {
            beginWith(countWordWorker1, countWordWorker2, countWordWorker3).then(sortWordWorker).enqueue()
        }
    }

    // 취소 시킬 작업의 ID 를 기억합니다.
    var targetWorkId = UUID.randomUUID()

    // 취소 시킬 작업의 Tag
    val targetWorkTag = "cancel work tag"

    private fun doCancelWork() {
        val cancelWork = OneTimeWorkRequestBuilder<CancelWorker>()
                .addTag(targetWorkTag)
                .build()

        targetWorkId = cancelWork.id

        WorkManager.getInstance()?.apply {
            enqueue(cancelWork)
            getStatusById(cancelWork.id).observe(this@MainActivity, Observer {
                Log.d("Sample", "state : ${it?.state}")
            })
        }
    }

    private fun cancelWorkUseId() {
        // 주어진 ID 를 가진 작업을 취소 합니다.
        WorkManager.getInstance()?.cancelWorkById(targetWorkId)
    }

    private fun cancelWorkUseTag() {
        // 주어진 Tag 를 가진 작업들을 모두 취소 합니다.
        WorkManager.getInstance()?.cancelAllWorkByTag(targetWorkTag)
    }

    private fun doUniqueWorkUseKEEP() {
        val work = OneTimeWorkRequestBuilder<UniqueWorker>().build()

        WorkManager.getInstance()?.apply {
            beginUniqueWork("simple", ExistingWorkPolicy.KEEP, work).enqueue()

            getStatusById(work.id).observe(this@MainActivity, Observer {
                Log.d("Sample", "id : ${it?.id} , state : ${it?.state}")
            })
        }
    }

    private fun doUniqueWorkUseREPLACE() {
        val work = OneTimeWorkRequestBuilder<UniqueWorker>().build()

        WorkManager.getInstance()?.apply {
            beginUniqueWork("simple", ExistingWorkPolicy.REPLACE, work).enqueue()

            getStatusById(work.id).observe(this@MainActivity, Observer {
                Log.d("Sample", "id : ${it?.id} , state : ${it?.state}")
            })
        }
    }

    private fun doUniqueWorkUseAPPEND() {
        val work = OneTimeWorkRequestBuilder<UniqueWorker>().build()

        WorkManager.getInstance()?.apply {
            beginUniqueWork("simple", ExistingWorkPolicy.APPEND, work).enqueue()

            getStatusById(work.id).observe(this@MainActivity, Observer {
                Log.d("Sample", "id : ${it?.id} , state : ${it?.state}")
            })
        }
    }
}
