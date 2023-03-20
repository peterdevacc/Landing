package com.peter.landing.ui.util

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class Sound (
    private val context: Context,
): DefaultLifecycleObserver {

    private var mediaPlayer: MediaPlayer? = null
    private var soundFd: AssetFileDescriptor? = null

    private var enabled = false

    fun playAudio(name: String) {
        mediaPlayer?.let { player ->
            if (!player.isPlaying) {
                player.reset()
                soundFd?.close()
                soundFd = context.resources.assets.openFd(name)
                soundFd?.let {
                    player.setDataSource(it.fileDescriptor, it.startOffset, it.length)
                    player.prepare()
                    player.start()
                }
            }
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        mediaPlayer = MediaPlayer()

    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        enabled = false
        mediaPlayer?.release()
        soundFd?.close()
        mediaPlayer = null
    }

}
