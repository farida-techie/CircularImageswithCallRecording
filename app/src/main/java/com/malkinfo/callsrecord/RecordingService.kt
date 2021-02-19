package com.malkinfo.callsrecord

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaRecorder
import android.os.Environment
import android.os.IBinder
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import java.io.File
import java.util.*


class RecordingService:Service (){

    private lateinit var rec:MediaRecorder
    private var recordStrat :Boolean? = null
    private lateinit var file:File
    var path :String = "/callreoding/amd"
    var c = this

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }




    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS)

        val recordingFile = (System.currentTimeMillis()
            .toString() + ".mp3")
        val mFiles = file.toString()+path+recordingFile
        var date =Date()
        var sdf :CharSequence = android.text.format.DateFormat.format("MM-dd-yy-mm-ss", date.time)
        rec = MediaRecorder()
        rec.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL)
        rec.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        rec.setOutputFile(mFiles)
        rec.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        val tManger :TelephonyManager = c.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        tManger.listen(object : PhoneStateListener() {

            override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                if (TelephonyManager.CALL_STATE_IDLE == state && rec == null) {
                    rec.stop()
                    rec.reset()
                    rec.release()
                    recordStrat = false
                    stopSelf()
                } else if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                    rec.prepare()
                    rec.start()
                    recordStrat = true
                }
            }

        }, PhoneStateListener.LISTEN_CALL_STATE)

        return START_STICKY
    }
}