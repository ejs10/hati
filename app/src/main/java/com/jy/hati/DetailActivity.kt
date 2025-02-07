package com.jy.hati

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var tvTodoContent: TextView
    private lateinit var deleteButton: Button
    private var todo: String? =null
    private var position: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        //val todoDetil = intent.getStringExtra("todoDetail")

        sharedPreferences = getSharedPreferences("todoPrefs", MODE_PRIVATE)

        tvTodoContent = findViewById(R.id.tvTodoContent)
        deleteButton = findViewById(R.id.deleteButton)

        //val tvTodoContent = findViewById<TextView>(R.id.tvTodoContent)
        //tvTodoContent.text = todoDetil

        supportActionBar?.setDisplayUseLogoEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        todo = intent.getStringExtra("todoDetail")
        position = intent.getIntExtra("position", -1)


        tvTodoContent.text = todo


        //삭제버튼
        deleteButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }

    }

    private fun showDeleteConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(" 삭제확인")
        builder.setMessage("\"$todo\" 을(를) 삭제하시겠습니까?")
        builder.setPositiveButton("삭제"){_, _, ->
            deleteTodoItem()
        }
        builder.setNegativeButton("취소",null)
        builder.show()
    }

    //할일삭제
    private fun deleteTodoItem() {
        val todoList = mutableListOf<String>()
        val editor = sharedPreferences.edit()

        // SharedPreferences에서 저장된 할 일 목록 불러오기
        val savedTodos = sharedPreferences.getStringSet("todos", mutableSetOf())?.toList() ?: emptyList()
        todoList.addAll(savedTodos)

        // 해당 항목 삭제
        todoList.removeAt(position)

        // 삭제된 목록을 다시 SharedPreferences에 저장
        editor.putStringSet("todos", todoList.toSet()) // Set으로 저장
        editor.apply()

        // 삭제 후 MainActivity로 돌아가기
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // 현재 액티비티 종료

    }

    //뒤로가기
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}