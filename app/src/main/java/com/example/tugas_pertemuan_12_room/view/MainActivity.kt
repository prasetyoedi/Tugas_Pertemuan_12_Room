package com.example.tugas_pertemuan_12_room.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugas_pertemuan_12_room.database.Note
import com.example.tugas_pertemuan_12_room.database.NoteRoomDatabase
import com.example.tugas_pertemuan_12_room.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var listNoteAdapter: ListNoteAdapter
    private val UPDATE_FILM_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        val db = NoteRoomDatabase.getDatabase(this)
        val noteDao = db?.noteDao()

        val allNotes: LiveData<List<Note>>? = noteDao?.allNotes

        allNotes?.observe(this) { notes ->
            notes?.let { listNoteAdapter.setData(it) }
        }

        listNoteAdapter.setOnDeleteClickListener(object : ListNoteAdapter.OnDeleteClickListener {
            override fun onDeleteClick(note: Note) {
                deleteNoteInBackground(note)
            }
        })

        binding.btnPlus.setOnClickListener {
            val intent = Intent(this, Form::class.java)
            startActivity(intent)
        }

        listNoteAdapter.setOnItemClickListener(object : ListNoteAdapter.OnItemClickListener {
            override fun onItemClick(note: Note) {
                val intent = Intent(this@MainActivity, Form::class.java).apply {
                    putExtra(Form.EXTRA_ID, note.id)
                    putExtra(Form.EXTRA_TITLE, note.title)
                    putExtra(Form.EXTRA_DESC, note.description)
                    putExtra(Form.EXTRA_DATE, note.date)
                }
                startActivityForResult(intent, UPDATE_FILM_REQUEST)
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == UPDATE_FILM_REQUEST && resultCode == RESULT_OK) {
            fetchDataAndUpdateList()
        }
    }

    private fun fetchDataAndUpdateList() {
        val db = NoteRoomDatabase.getDatabase(this)
        val noteDao = db?.noteDao()

        val allNotes: LiveData<List<Note>>? = noteDao?.allNotes

        allNotes?.observe(this) { notes ->
            notes?.let { listNoteAdapter.setData(it) }
        }
    }

    private fun deleteNoteInBackground(note: Note) {
        val noteDao = NoteRoomDatabase.getDatabase(this)?.noteDao()
        noteDao?.let {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    it.delete(note)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        listNoteAdapter = ListNoteAdapter(emptyList())
        binding.rvNote.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = listNoteAdapter
        }
    }
}
