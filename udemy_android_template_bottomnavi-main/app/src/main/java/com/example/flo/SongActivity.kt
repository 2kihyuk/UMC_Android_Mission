package com.example.flo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() { //안드로이드에서 액티비티의 기능들을 사용할수 있도록 만들어둔 클래스.  코틀린에서 다른 클래스를 생성자로 받을때는 ()반드시 써야함.
    //lateinit var는 전방선언이다. 초기화는 나중에 해줄게. 선언 먼저 할게.
    //코틀린에서 변수 선언 방법은 var, val

    //var은 먼저 초기화하고 나중에 수정 가능
    //val은 선언하고 초기화하면 수정 불가
    lateinit var binding : ActivitySongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySongBinding.inflate(layoutInflater)
        //inflate는 xml에 표기된 layout들을 메모리에 객체화 시키는 행동.
        setContentView(binding.root) //activity_song xml 파일의 뷰들을 마음대로  쓰기 위한 함수

        binding.songDownIb.setOnClickListener{
            finish() //Activity를 꺼주는 메소드
        }
        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(false)

        }
        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(true)
        }
        if(intent.hasExtra("title") && intent.hasExtra("singer")){
            binding.songMusicTitleTv.text= intent.getStringExtra("title")
            binding.songSingerNameTv.text = intent.getStringExtra("singer")
        }

    }

    fun setPlayerStatus(isPlaying : Boolean){
        if(isPlaying){
            binding.songMiniplayerIv.visibility=View.VISIBLE
            binding.songPauseIv.visibility=View.GONE
        }
        else{
            binding.songMiniplayerIv.visibility=View.GONE
            binding.songPauseIv.visibility=View.VISIBLE
        }
    }

}