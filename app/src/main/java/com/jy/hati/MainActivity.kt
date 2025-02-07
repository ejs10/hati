package com.jy.hati

import android.content.Intent
import android.content.SharedPreferences
import android.icu.text.Transliterator.Position
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var todoListView: ListView
    private lateinit var addTodoFab: FloatingActionButton
    private lateinit var sharedPreferences: SharedPreferences
    private val todoList = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sharedPreferences = getSharedPreferences("todoPrefs", MODE_PRIVATE)

        todoListView = findViewById(R.id.todoListView)
        addTodoFab = findViewById(R.id.fabAddTodo)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, todoList)
        todoListView.adapter = adapter

        //앱 시작 시 할일 목록 로드
        loadTodoList()

        //할일추가 버튼 클릭
        addTodoFab.setOnClickListener {
            showAddTodoDialog()
        }

        todoListView.setOnItemClickListener { _, _, position, _ ->
            val selectedTodo = todoList[position]

            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("todoDetail", selectedTodo)
            intent.putExtra("position", position)
            startActivity(intent)

        }


    }

    private fun showAddTodoDialog() {
        val builder = AlertDialog.Builder(this)
        val input = EditText(this)
        builder.setTitle("새 할일 추가")
        builder.setView(input)
        builder.setPositiveButton("추가"){_, _, ->
            val todo = input.text.toString()
            if(todo.isNotEmpty()){
                todoList.add(todo)
                adapter.notifyDataSetChanged()
                saveTodoList()
            }
        }
        builder.setNegativeButton("취소",null)
        builder.show()
    }


    private fun saveTodoList() {
        val editor = sharedPreferences.edit()
        editor.putStringSet("todos", todoList.toSet())
        editor.apply()
    }

    //할일리스트
    private fun loadTodoList() {
        val savedTodos = sharedPreferences.getStringSet("todos", mutableSetOf())?.toList() ?: emptyList()
        todoList.addAll(savedTodos)
        adapter.notifyDataSetChanged()
    }
}