package com.example.tugas_pertemuan_12_room.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.tugas_pertemuan_12_room.database.Note
import com.example.tugas_pertemuan_12_room.database.NoteDao
import com.example.tugas_pertemuan_12_room.database.NoteRoomDatabase
import com.example.tugas_pertemuan_12_room.databinding.ActivityFormBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Form : AppCompatActivity() {
    private lateinit var binding: ActivityFormBinding
    private lateinit var noteDao: NoteDao
    private lateinit var executorService: ExecutorService

    companion object {
        const val EXTRA_TITLE = "com.example.tugas_pertemuan_12_room.EXTRA_TITLE"
        const val EXTRA_DESC = "com.example.tugas_pertemuan_12_room.EXTRA_DESC"
        const val EXTRA_DATE = "com.example.tugas_pertemuan_12_room.EXTRA_DATE"
        const val EXTRA_ID = "com.example.tugas_pertemuan_12_room.EXTRA_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        executorService = Executors.newSingleThreadExecutor()

        // Retrieve the intent data
        val title = intent.getStringExtra(EXTRA_TITLE)
        val description = intent.getStringExtra(EXTRA_DESC)
        val date = intent.getStringExtra(EXTRA_DATE)
        val id = intent.getIntExtra(EXTRA_ID, 0)

        val db = NoteRoomDatabase.getDatabase(this)
        if (db != null) {
            noteDao = db.noteDao()!!
        }

        binding.txtTitle.setText(title)
        binding.txtDescription.setText(description)
        binding.txtDate.setText(date)

        if (id == 0) {
            binding.btnSave.setOnClickListener {
                saveData()
            }
            binding.btnUpdate.visibility = View.GONE
        } else {
            binding.btnSave.setOnClickListener {
                showToast("Klik Update untuk melakukan perubahan data")
            }
            binding.btnUpdate.setOnClickListener {
                updateData(id)
            }
        }
    }

    private fun saveData() {
        val newTitle = binding.txtTitle.text.toString()
        val newDescription = binding.txtDescription.text.toString()
        val newDate = binding.txtDate.text.toString()

        if (newTitle.isNotEmpty() && newDescription.isNotEmpty() && newDate.isNotEmpty()) {
            val newNote = Note(
                title = newTitle,
                description = newDescription,
                date = newDate
            )

            insert(newNote)
        } else {
            showToast("Isi form dengan benar")
        }
    }

    private fun updateData(id: Int) {
        val updatedTitle = binding.txtTitle.text.toString()
        val updatedDescription = binding.txtDescription.text.toString()
        val updatedDate = binding.txtDate.text.toString()

        if (updatedTitle.isNotEmpty() && updatedDescription.isNotEmpty() && updatedDate.isNotEmpty()) {
            val updatedNote = Note(
                id = id,
                title = updatedTitle,
                description = updatedDescription,
                date = updatedDate
            )

            update(updatedNote)

            val resultIntent = Intent().apply {
                putExtra(EXTRA_ID, updatedNote.id)
                putExtra(EXTRA_TITLE, updatedNote.title)
                putExtra(EXTRA_DESC, updatedNote.description)
                putExtra(EXTRA_DATE, updatedNote.date)
            }

            setResult(RESULT_OK, resultIntent)
            finish()
        } else {
            showToast("Please fill the form correctly")
        }
    }

    private fun insert(note: Note) {
        executorService.execute {
            noteDao.insert(note)
            setResult(RESULT_OK)
            finish()
        }
    }

    private fun update(note: Note) {
        executorService.execute {
            noteDao.update(note)
            setResult(RESULT_OK)
            finish()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}
