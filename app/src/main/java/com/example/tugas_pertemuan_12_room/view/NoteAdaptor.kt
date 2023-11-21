package com.example.tugas_pertemuan_12_room.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tugas_pertemuan_12_room.database.Note
import com.example.tugas_pertemuan_12_room.databinding.ItemNoteBinding

class ListNoteAdapter(private var listNote: List<Note>) :
    RecyclerView.Adapter<ListNoteAdapter.ItemNoteViewHolder>() {

    inner class ItemNoteViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnHapus.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onDeleteClickListener?.onDeleteClick(listNote[position])
                }
            }

            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.onItemClick(listNote[position])
                }
            }
        }

        fun bind(note: Note) {
            with(binding) {
                titleNote.text = note.title
                descriptionNote.text = note.description
                dateNote.text = note.date
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newNotes: List<Note>) {
        listNote = newNotes
        notifyDataSetChanged()
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(note: Note)
    }

    interface OnItemClickListener {
        fun onItemClick(note: Note)
    }

    private var onDeleteClickListener: OnDeleteClickListener? = null
    private var onItemClickListener: OnItemClickListener? = null

    fun setOnDeleteClickListener(listener: OnDeleteClickListener) {
        onDeleteClickListener = listener
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemNoteViewHolder {
        val binding = ItemNoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ItemNoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemNoteViewHolder, position: Int) {
        holder.bind(listNote[position])
    }

    override fun getItemCount(): Int = listNote.size
}
