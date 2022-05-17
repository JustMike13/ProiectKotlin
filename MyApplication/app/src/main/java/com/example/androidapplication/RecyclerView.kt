package com.example.androidapplication


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class RecyclerView : AppCompatActivity(){
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<Subjects>
    lateinit var textId : Array<String>
    lateinit var imageId : Array<Int>

    //for searchBar
    private lateinit var searchArrayList: ArrayList<Subjects>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        imageId = arrayOf(
            R.drawable.lightofthejedi,
            R.drawable.atestofcourage,
            R.drawable.intothedark,
            R.drawable.therisingstorm,
            R.drawable.thrawn,
            R.drawable.thrawnalliances,
            R.drawable.thrawntreason

        )

        textId = arrayOf(
            "Light of The Jedi",
            "A Test Of Courage",
            "Into The Dark",
            "The Rising Storm",
            "Thrawn",
            "Thrawn: Alliances",
            "Thrawn: Treason"
        )

        newRecyclerView = findViewById(R.id.recyclerView)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)
        newArrayList = arrayListOf<Subjects>()
        searchArrayList = arrayListOf<Subjects>()
        getData()

    }
    private fun getData(){

        for(i in imageId.indices){
            val subjects = Subjects(imageId[i],textId[i])
            newArrayList.add(subjects)
        }

        searchArrayList.addAll(newArrayList)
        newRecyclerView.adapter = NewAdapter(searchArrayList)

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.search_bar,menu)
        val item = menu?.findItem(R.id.search)
        val searchView = item?.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query!= null){
                    getData()
                }
                return true
            }

            override fun onQueryTextChange(newSearchText: String?): Boolean {

                searchArrayList.clear()
                val searchText = newSearchText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()){
                    newArrayList.forEach{
                        if (it.text_title .lowercase(Locale.getDefault()).contains(searchText)){

                            searchArrayList.add(it)

                        }
                    }
                    println(searchArrayList.toString())
                    newRecyclerView.adapter = NewAdapter(searchArrayList)

                }else{
                    searchArrayList.clear()
                    searchArrayList.addAll(newArrayList)
                    newRecyclerView.adapter = NewAdapter(searchArrayList)

                }


                return false

            }


        })


        return super.onCreateOptionsMenu(menu)
    }


}