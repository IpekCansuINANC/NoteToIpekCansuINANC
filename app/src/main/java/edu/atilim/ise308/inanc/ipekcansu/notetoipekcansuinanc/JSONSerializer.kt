package edu.atilim.ise308.inanc.ipekcansu.notetoipekcansuinanc

import android.content.Context
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONTokener
import java.io.*

class JSONSerializer(private val filename : String, private val context: Context) {
    //this class ->store the data

    @Throws(IOException::class,JSONException::class)
    fun save (noteList: List<Note>){
        val jsonArray = JSONArray()
        for(note in noteList){
            jsonArray.put(note.convertTOJSON())
        }
        var writer: Writer? = null
        try {
            val outFile = context.openFileOutput(filename, Context.MODE_PRIVATE)
            writer = OutputStreamWriter(outFile)
            writer.write(jsonArray.toString())

        }finally {
            if (writer != null){
                writer.close()
            }
        }
    }

    //load the file to app
    @Throws(IOException::class, JSONException::class)
    fun load():ArrayList<Note>{
        var noteList = ArrayList<Note>()
        var reader : BufferedReader? =null

        try {
            val inFile = context.openFileInput(filename)
            reader = BufferedReader(InputStreamReader(inFile))
            val jsonString = StringBuilder()

            for (line in reader.readLine()){
                jsonString.append(line)
            }

            val jsonArray = JSONTokener(jsonString.toString()).nextValue() as JSONArray

            for (i in 0 until jsonArray.length()){
                noteList.add(Note(jsonArray.getJSONObject(i)))
            }
        }catch (e : FileNotFoundException){
            // we will ignore this one, since it happens
            //when we start fresh. You could a log statement here.
        }finally {
            reader!!.close()
        }
        return noteList
    }
}