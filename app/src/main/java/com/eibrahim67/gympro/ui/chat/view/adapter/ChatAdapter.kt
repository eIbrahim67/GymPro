package com.eibrahim67.gympro.ui.chat.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.ui.chat.model.ChatMessage
import com.google.android.material.card.MaterialCardView

class ChatAdapter(private val currentUserId: String?) :
    RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {
    private val messages = mutableListOf<ChatMessage>()

    inner class MessageViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val senderMessage: TextView = view.findViewById(R.id.senderMessage)
        val receiverMessage: TextView = view.findViewById(R.id.receiverMessage)

        val senderImage: ImageView = view.findViewById(R.id.senderImage)
        val senderImageCard: MaterialCardView = view.findViewById(R.id.senderImageCard)

        val receiverMessageCard: LinearLayout = view.findViewById(R.id.receiverMessageCard)
        val senderMessageCard: LinearLayout = view.findViewById(R.id.senderMessageCard)

        val receiverImage: ImageView = view.findViewById(R.id.receiverImage)
        val receiverImageCard: MaterialCardView = view.findViewById(R.id.receiverImageCard)

        val senderAudioCard: MaterialCardView = view.findViewById(R.id.senderAudioCard)
        val btnSenderPlayPause: ImageView = itemView.findViewById(R.id.btnSenderPlayPause)
        val audioSenderSeekBar: SeekBar = itemView.findViewById(R.id.audioSenderSeekBar)
        val txtSenderDuration: TextView = itemView.findViewById(R.id.txtSenderDuration)

        val receiverAudioCard: MaterialCardView = view.findViewById(R.id.receiverAudioCard)
        val btnReceiverPlayPause: ImageView = itemView.findViewById(R.id.btnReceiverPlayPause)
        val audioReceiverSeekBar: SeekBar = itemView.findViewById(R.id.audioReceiverSeekBar)
        val txtReceiverDuration: TextView = itemView.findViewById(R.id.txtReceiverDuration)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_users_chat, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.senderAudioCard.hide()
        holder.receiverAudioCard.hide()
        holder.senderImageCard.hide()
        holder.receiverImageCard.hide()
        holder.senderMessageCard.hide()
        holder.receiverMessageCard.hide()
        val msg = messages[position]
        val isCurrentUser = msg.msgUid == currentUserId

        if (isCurrentUser) {
            if (msg.msgAudio && !msg.audioUrl.isNullOrEmpty()) {
                holder.senderAudioCard.show()

                AudioViewHolder(
                    context = holder.itemView.context,
                    btnPlayPause = holder.btnSenderPlayPause,
                    seekBar = holder.audioSenderSeekBar,
                    txtDuration = holder.txtSenderDuration,
                ).bind(msg.audioUrl)

            } else {
                if (!msg.text.isNullOrEmpty()) {
                    holder.senderMessage.text = msg.text
                    holder.senderMessageCard.show()
                }

                if (!msg.imageUrl.isNullOrEmpty()) {
                    holder.senderImageCard.show()
                    Glide.with(holder.view.context).load(msg.imageUrl).placeholder(R.color.gray_v1)
                        .error(R.drawable.error_ic).centerCrop().into(holder.senderImage)
                }
            }
        } else {
            if (msg.msgAudio && !msg.audioUrl.isNullOrEmpty()) {
                holder.receiverAudioCard.show()

                AudioViewHolder(
                    context = holder.itemView.context,
                    btnPlayPause = holder.btnReceiverPlayPause,
                    seekBar = holder.audioReceiverSeekBar,
                    txtDuration = holder.txtReceiverDuration,
                ).bind(msg.audioUrl)

            } else {
                if (!msg.text.isNullOrEmpty()) {
                    holder.receiverMessageCard.show()
                    holder.receiverMessage.text = msg.text
                }
                if (!msg.imageUrl.isNullOrEmpty()) {
                    holder.receiverImageCard.show()
                    Glide.with(holder.view.context).load(msg.imageUrl)
                        .placeholder(R.color.primary_white).error(R.drawable.error_ic).centerCrop()
                        .into(holder.receiverImage)
                }
            }
        }
    }

    override fun getItemCount() = messages.size

    fun addMessage(message: ChatMessage) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearMessages() {
        messages.clear()
        notifyDataSetChanged()
    }

    fun View.show() {
        visibility = View.VISIBLE
    }

    fun View.hide() {
        visibility = View.GONE
    }


    inner class AudioViewHolder(
        context: Context,
        private val btnPlayPause: ImageView,
        private val seekBar: SeekBar,
        private val txtDuration: TextView
    ) {

        private val exoPlayer = ExoPlayer.Builder(context).build()
        private var currentPlayingHolder: AudioViewHolder? = null

        private var isPlaying = false

        fun bind(url: String) {
            btnPlayPause.setOnClickListener {
                if (isPlaying) {
                    pause()
                } else {
                    play(url)
                }
            }
        }

        private fun play(url: String) {
            currentPlayingHolder?.pause()
            currentPlayingHolder = this

            exoPlayer.setMediaItem(MediaItem.fromUri(url))
            exoPlayer.prepare()
            exoPlayer.play()

            isPlaying = true
            btnPlayPause.setImageResource(R.drawable.ic_pause)

            // Update seekbar & duration
            exoPlayer.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    if (state == Player.STATE_ENDED) {
                        stop()
                    }
                }
            })

            val handler = Handler(Looper.getMainLooper())
            val updateSeekbar = object : Runnable {
                override fun run() {
                    if (isPlaying) {
                        val pos = exoPlayer.currentPosition.toInt()
                        val dur = exoPlayer.duration.toInt()
                        seekBar.max = dur
                        seekBar.progress = pos
                        txtDuration.text = formatTime(pos)
                        handler.postDelayed(this, 500)
                    }
                }
            }
            handler.post(updateSeekbar)

            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) exoPlayer.seekTo(progress.toLong())
                }

                override fun onStartTrackingTouch(sb: SeekBar?) {}
                override fun onStopTrackingTouch(sb: SeekBar?) {}
            })
        }

        private fun pause() {
            exoPlayer.pause()
            isPlaying = false
            btnPlayPause.setImageResource(R.drawable.ic_play)
        }

        private fun stop() {
            exoPlayer.stop()
            isPlaying = false
            btnPlayPause.setImageResource(R.drawable.ic_play)
            seekBar.progress = 0
        }

        private fun formatTime(ms: Int): String {
            val totalSeconds = ms / 1000
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60
            return String.format("%d:%02d", minutes, seconds)
        }
    }

}