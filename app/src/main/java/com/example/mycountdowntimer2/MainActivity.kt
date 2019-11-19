package com.example.mycountdowntimer2

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var soundPool: SoundPool
    private var soundResId = 0

    inner class MyCountdownTimer2(millisInFuture: Long,countDownInterval: Long) :
        CountDownTimer(millisInFuture, countDownInterval){

        var isRunning=false

        override fun onTick(millisUntiFinished: Long){
            val minute = millisUntiFinished / 1000L / 60L
            val second = millisUntiFinished / 1000L % 60L
            timerText.text = "%1d:%2$02d".format(minute, second)
        }

        override fun onFinish(){
            timerText.text="0:00"
            soundPool.play(soundResId, 1.0f, 100f, 0, 0, 1.0f)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timerText.text = "3:00"
        val timer = MyCountdownTimer2(3*60*1000, 100)
        playstop.setOnClickListener{
            timer.isRunning = when (timer.isRunning){
                true ->{
                    timer.cancel()
                    playstop.setImageResource(R.drawable.ic_play_arrow_black_24dp)
                    false
                }
                false ->{
                    timer.start()
                    playstop.setImageResource(R.drawable.ic_play_arrow_black_24dp)
                    true
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        soundPool =
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                @Suppress("DEPRECATION")
                SoundPool(2, AudioManager.STREAM_ALARM, 0)
            } else{
                val audioAttributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build()
                SoundPool.Builder().setMaxStreams(1).setAudioAttributes(audioAttributes).build()
            }
        soundResId = soundPool.load(this, R.raw.bellsound, 1)
    }

    override fun onPause() {
        super.onPause()
        soundPool.release()
    }
}
