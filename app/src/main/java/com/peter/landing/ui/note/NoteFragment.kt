package com.peter.landing.ui.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.snackbar.Snackbar
import com.peter.landing.R
import com.peter.landing.data.local.word.Word
import com.peter.landing.databinding.FragmentNoteBinding
import com.peter.landing.ui.common.showDefinitionZoomDialog
import com.peter.landing.ui.note.adapter.NoteAdapter
import com.peter.landing.ui.note.adapter.NoteViewHolder
import com.peter.landing.ui.note.adapter.SwipeCallback
import com.peter.landing.ui.note.adapter.SwipeDeleteCallback
import com.peter.landing.ui.util.Sound
import com.peter.landing.ui.util.isDarkTheme
import com.peter.landing.ui.util.listStateChangeListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NoteFragment : Fragment() {

    private var noteBinding: FragmentNoteBinding? = null
    private val binding get() = noteBinding!!
    private val viewModel: NoteViewModel by viewModels()
    private lateinit var sound: Sound
    private val adapter = NoteAdapter()
    private var noteJob: Job? = null
    private var snackBar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        noteBinding = FragmentNoteBinding
            .inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isDarkTheme()) {
            binding.noteEmptyImg.setImageResource(R.drawable.empty_img_dark)
        } else {
            binding.noteEmptyImg.setImageResource(R.drawable.empty_img_light)
        }

        sound = Sound(view.context)

        snackBar = Snackbar.make(view, "", Snackbar.LENGTH_LONG)

        val touchCallback = SwipeDeleteCallback(requireContext())
        touchCallback.setSwipeEnable(true)
        val itemTouchHelper = ItemTouchHelper(touchCallback)
        itemTouchHelper.attachToRecyclerView(binding.noteList)

        val callback = object : SwipeCallback {
            override fun onSwipe(word: Word) {
                viewModel.removeNote(word.id)
                showDeleteItemUndoSnackBar(word)
            }
        }

        adapter.setItemSwipeCallback(callback)
        adapter.setItemOnClickListener(itemListener)
        adapter.setSoundOnClickListener(soundListener)

        binding.noteList.setHasFixedSize(true)
        binding.noteList.adapter = adapter
        binding.noteList.addItemDecoration(
            DividerItemDecoration(
                binding.noteList.context, DividerItemDecoration.VERTICAL
            )
        )
        binding.noteList.addOnAttachStateChangeListener(listStateChangeListener)

        viewModel.totalNoteNum.observe(viewLifecycleOwner) { totalNum ->
            if (totalNum > 0) {
                getNote()
                binding.noteEmptyMsg.visibility = View.GONE
                binding.noteEmptyCard.visibility = View.GONE
                binding.noteList.visibility = View.VISIBLE
            } else {
                binding.noteList.visibility = View.GONE
                binding.noteEmptyCard.visibility = View.VISIBLE
                binding.noteEmptyMsg.visibility = View.VISIBLE
            }
            binding.noteLoading.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        viewLifecycleOwner.lifecycle.addObserver(sound)
    }

    override fun onStop() {
        super.onStop()
        viewLifecycleOwner.lifecycle.removeObserver(sound)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        snackBar?.dismiss()
        noteBinding = null
    }

    private fun getNote() {
        noteJob?.cancel()
        noteJob = viewLifecycleOwner.lifecycleScope.launch {
            viewModel.noteWordList.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private val itemListener = View.OnClickListener {
        val viewHolder = it.tag as NoteViewHolder.ItemNoteViewHolder
        val word = viewHolder.getData()
        showDefinitionZoomDialog(word, childFragmentManager)
    }

    private val soundListener = View.OnClickListener {
        val viewHolder = it.tag as NoteViewHolder.ItemNoteViewHolder
        val word = viewHolder.getData()
        sound.playAudio(word.pronName)
    }

    private fun showDeleteItemUndoSnackBar(word: Word) {
        snackBar?.setText(getString(R.string.note_delete_toast, word.spelling))
        snackBar?.setAction(getString(R.string.note_undo_delete)) {
            viewModel.addNote(word.id)
        }
        snackBar?.show()
    }

}
