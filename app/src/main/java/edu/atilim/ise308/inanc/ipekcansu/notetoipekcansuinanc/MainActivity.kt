package edu.atilim.ise308.inanc.ipekcansu.notetoipekcansuinanc

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import java.lang.Exception
import javax.security.auth.login.LoginException
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    //private val noteList = ArrayList<Note>()
    private var noteList : ArrayList<Note>? = null
    private var jsonSerializer: JSONSerializer?= null
    private var recyclerView: RecyclerView? = null //val
    private var adapter: NoteAdapter? = null   //val
    private var showDividers: Boolean =false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fabNewNote = findViewById<FloatingActionButton>(R.id.fab_new_note)
        fabNewNote.setOnClickListener{
            val dialogNewNote = DialogNewNote()
            dialogNewNote.show(supportFragmentManager,"124")
        }
        jsonSerializer = JSONSerializer("NoteToIpek",applicationContext)
        try {
            noteList = jsonSerializer!!.load()
        }catch (e: Exception){
            noteList = ArrayList()
            Log.e("Error Loading data...","",e)
        }

        recyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView?
        adapter = NoteAdapter(this,noteList!!)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView!!.layoutManager=layoutManager
        recyclerView!!.itemAnimator= DefaultItemAnimator()
        recyclerView!!.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        recyclerView!!.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        val prefs = getSharedPreferences("Note to Self", Context.MODE_PRIVATE)
        showDividers = prefs.getBoolean("dividers",true)
        if(showDividers){
            recyclerView!!.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        }
        else{
            if(recyclerView!!.itemDecorationCount>0)
                recyclerView!!.removeItemDecorationAt(0)
        }
    }
    fun showNote(noteToShow: Int)  // main activity show the data
    {
        val dialog = DialogShowNote()
        noteList?.get(noteToShow)?.let { dialog.sendNoteSelected(it) }
        dialog.show(supportFragmentManager,"")

    }

    fun createNewNote(note: Note){
        noteList!!.add(note)
        adapter!!.notifyDataSetChanged()   //everytime you changed a note, it will check

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean { //settings
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { //if an item selected from settings
        val id =item.itemId
        val b= when (id){
            R.id.settings -> {
                val intentToSettings = Intent(this,SettingsActivity::class.java)
                startActivity(intentToSettings)
                true
            }
        else ->super.onOptionsItemSelected(item)
        }
        return b
    }

    private fun saveNotes(){
        try {
            jsonSerializer!!.save(this.noteList!!)
        }catch (e:Exception){
            Log.e("Error saving data","",e)
        }
    }

    override fun onPause() {
        super.onPause()
        saveNotes()
    }
}
