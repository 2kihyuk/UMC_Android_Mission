package com.example.flo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.flo.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private var gson:Gson =Gson()
    private var song:Song =Song()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.Theme_FLO)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val song = Song(binding.mainMiniplayerTitleTv.text.toString(),binding.mainMiniplayerSingerTv.text.toString(),0,60,false,"music_lilac")

       binding.mainPlayerCl.setOnClickListener {
//           startActivity(Intent(this,SongActivity::class.java))
           val intent = Intent(this,SongActivity::class.java)
           intent.putExtra("title",song.title)
           intent.putExtra("singer",song.singer)
           intent.putExtra("second",song.second)
           intent.putExtra("playTime",song.playTime)
           intent.putExtra("isPlaying",song.isPlaying)
           intent.putExtra("music",song.music)
           startActivity(intent)


       }
        initBottomNavigation()



        Log.d("Song",song.title + song.singer)

    }

    private fun initBottomNavigation(){

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener{ item ->
            when (item.itemId) {

                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    private fun setMiniplayer(song :Song){
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text= song.singer
        binding.seekBar.progress = (song.second*100000)/song.playTime
    }

    override fun onStart() {
        super.onStart()
        //onstart에서 sharedpreference에 저장되었던 값 읽어오기
        val sharedpreference  = getSharedPreferences("song", MODE_PRIVATE)
        val songJson = sharedpreference.getString("songData",null)

        song = if(songJson==null) {
            Song("라일락", "아이유(IU)", 0, 60, false, "music_lilac")
        }
        else{
            gson.fromJson(songJson,Song::class.java)
        }
        setMiniplayer(song)
    }
}