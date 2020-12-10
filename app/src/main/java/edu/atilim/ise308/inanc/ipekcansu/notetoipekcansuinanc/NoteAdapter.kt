package edu.atilim.ise308.inanc.ipekcansu.notetoipekcansuinanc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter(private val mainActivity: MainActivity, private val noteList: List<Note>): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(noteItemView: View) : RecyclerView.ViewHolder(noteItemView), View.OnClickListener {
        internal var title = noteItemView.findViewById<TextView>(R.id.textViewTitle)
        internal var description = noteItemView.findViewById<TextView>(R.id.textViewDescription)
        internal var status = noteItemView.findViewById<TextView>(R.id.textViewStatus)

        init {
            noteItemView.isClickable = true
            noteItemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            mainActivity.showNote(adapterPosition)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        if (noteList != null) {
            return noteList.size
        }
        return -1
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = noteList[position]
        holder.title.text = note.title
        val substringLength = if(note.description!!.length <15) note.description!!.length else 15
        val shortDesc = "${note.description!!.substring(0,substringLength)}..."
        holder.description.text =shortDesc

        when {
            note.idea -> holder.status.text = mainActivity.resources.getString(R.string.idea_text)
            note.important -> holder.status.text = mainActivity.resources.getString(R.string.important_text)
            note.todo -> holder.status.text = mainActivity.resources.getString(R.string.todo_text)
        }
    }
}