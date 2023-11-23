package com.example.flo

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {

    //소괄호 : 클래스를 다른 클래스로 상속을 진행할 때는 소괄호를 넣어줘야 한다.

    //전역 변수
    lateinit var binding : ActivitySongBinding
//    lateinit var song : Song

    lateinit var timer : Timer

    private var mediaPlayer :MediaPlayer? = null
    private var gson:Gson = Gson()

    val songs = arrayListOf<Song>()

    lateinit var  songDB:SongDatabase
    var nowPos =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initPlayList()
        initSong()

        initClickListener()
//        setPlayer(song)

//        if(intent.hasExtra("title") && intent.hasExtra("singer")){
//            binding.songMusicTitleTv.text = intent.getStringExtra("title")
//            binding.songSingerNameTv.text = intent.getStringExtra("singer")
//        }

//        binding.songDownIb.setOnClickListener {
//            finish()
//        }
//
//        binding.songMiniplayerIv.setOnClickListener {
//            setPlayerStatus(true)
//        }
//
//        binding.songPauseIv.setOnClickListener {
//            setPlayerStatus(false)
//        }


    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
    }

    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun initClickListener(){
        binding.songDownIb.setOnClickListener {
            finish()
        }

        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(true)
        }

        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }

        binding.songNextIv.setOnClickListener {
            moveSong(+1)
        }
        binding.songPreviousIv.setOnClickListener {
            moveSong(-1)
        }

        binding.songLikeIv.setOnClickListener {
            songs[nowPos].isLike?.let { it1 -> setLike(it1) }

        }
    }

    private fun initSong(){
//        if(intent.hasExtra("title") && intent.hasExtra("singer")){
//            song = Song(
//                intent.getStringExtra("title")!!,
//                intent.getStringExtra("singer")!!,
//                intent.getIntExtra("second",0),
//                intent.getIntExtra("playTime",0),
//                intent.getBooleanExtra("isPlaying",false),
//                intent.getStringExtra("music")!!
//
//
//            )
//        }
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId",0)
        nowPos = getPlayingSongPosition(songId)
        Log.d("now Song ID",songs[nowPos].id.toString())
        startTimer()
        setPlayer(songs[nowPos])
    }

    private fun setLike(isLike:Boolean){
        songs[nowPos].isLike=!isLike
        songDB.songDao().updateIsLikeById(!isLike,songs[nowPos].id)

        if(!isLike == true){
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        }else{
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private fun moveSong(direct:Int){
        if(nowPos+ direct<0){
            Toast.makeText(this,"first song",Toast.LENGTH_SHORT).show()
            return
        }
        if(nowPos+direct>=songs.size){
            Toast.makeText(this,"last song",Toast.LENGTH_SHORT).show()
            return
        }
        nowPos+=direct

        timer.interrupt()
        startTimer()
        mediaPlayer?.release()
        mediaPlayer=null

        setPlayer(songs[nowPos])
    }

    private fun getPlayingSongPosition(songId:Int):Int{
        for(i in 0 until songs.size){
            if(songs[i].id==songId){
                return i
            }
        }
        return 0
    }

    private fun setPlayer(song:Song){
        binding.songMusicTitleTv.text = song.title
        binding.songSingerNameTv.text = song.singer
        binding.songStartTimeTv.text = String.format("%02d:%02d",song.second /60 , song.second%60)
        binding.songEndTimeTv.text = String.format("%02d:%02d",song.playTime /60 , song.playTime%60)
        binding.songProgressbarBackgroudView.progress = (song.second * 1000 / song.playTime)
        binding.songAlbumIv.setImageResource(song.coverImg!!)

        val music = resources.getIdentifier(song.music,"raw",this.packageName)
        mediaPlayer = MediaPlayer.create(this,music)

        if(song.isLike == true){
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        }else{
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }

        setPlayerStatus(song.isPlaying)


    }

    fun setPlayerStatus (isPlaying : Boolean){
        songs[nowPos].isPlaying = isPlaying
        timer.isPlaying =isPlaying

        if(isPlaying){
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            mediaPlayer?.start()
        } else {
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            if(mediaPlayer?.isPlaying==true){
                mediaPlayer?.pause()
            }
        }
    }

    private fun startTimer(){
        timer = Timer(songs[nowPos].playTime,songs[nowPos].isPlaying)
        timer.start()
    }

    inner class Timer(private val playTime : Int , var isPlaying : Boolean = true) : Thread(){
        private var second : Int = 0
        private var mills : Float =0f

        override fun run(){
            super.run()
            try{

                while(true){
                    if(second >= playTime){
                        break
                    }
                    if(isPlaying){
                        sleep(50)
                        mills+= 50

                        runOnUiThread{
                            binding.songProgressbarBackgroudView.progress =((mills / playTime)*10).toInt()
                        }
                        if(mills % 1000 == 0f){
                            runOnUiThread {
                                binding.songStartTimeTv.text = String.format("%02d:%02d",second /60 , second % 60)
                            }
                            second++
                        }
                    }
                }
            }catch(e: InterruptedException){
                Log.d("Song","쓰레드가 죽었습니다. ${e.message}")
            }

        }

    }
    //사용자가 포커스를 잃었을때 음악을 중지.
    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)
        songs[nowPos].second=((binding.songProgressbarBackgroudView.progress*songs[nowPos].playTime)/100)/1000
        songs[nowPos].isPlaying=false

        //sharedpreperence 를 통해 데이터저장.
        val sharedpreferences = getSharedPreferences("song", MODE_PRIVATE)
        var editor = sharedpreferences.edit() // 에디터. 이 에디터에게 인텐트처럼 put()을 사용해 데이터를 저장시킨다.
//        editor.putString("title",song.title) // key,value형태로 저장.

//        val songJson = gson.toJson(songs[nowPos])

//        editor.putString("songData",songJson)
        editor.putInt("songId",songs[nowPos].id)
        editor.apply() // 실제 저장.


    }



}