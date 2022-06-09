package com.example.todo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.databinding.TodoItemLayoutBinding
import com.example.todo.model.Todo
import com.example.todo.view.ToDoListFragmentDirections
import com.example.todo.view.TodoCheckedChangeListener
import com.example.todo.view.TodoEditClick
import kotlinx.android.synthetic.main.todo_item_layout.view.*

class TodoListAdapter (val todoList:ArrayList<Todo>, val adapterOnClick : (Todo) -> Unit)
    : RecyclerView.Adapter<TodoListAdapter.TodoViewHolder>(), TodoCheckedChangeListener, TodoEditClick {
    class TodoViewHolder(var view: TodoItemLayoutBinding): RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<TodoItemLayoutBinding>(inflater, R.layout.todo_item_layout, parent,
            false)

        // todo_item_layout akan me-create satu2 itemnya

        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.view.todo = todoList[position]
        holder.view.listener = this

//        holder.view.checkTask.setText(todoList[position].title.toString())

        holder.view.imgEdit.setOnClickListener {
            val action = ToDoListFragmentDirections.actionEditToDoFragment(todoList[position].uuid)

            Navigation.findNavController(it).navigate(action)
        }

        holder.view.checkTask.setOnCheckedChangeListener { compoundButton, isChecked ->
            if(isChecked == true) {
                adapterOnClick(todoList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    fun updateTodoList(newTodoList: List<Todo>) {
        todoList.clear()
        todoList.addAll(newTodoList)
        notifyDataSetChanged()
    }

    override fun onCheckChanged(cb: CompoundButton, isChecked: Boolean, obj: Todo) {
        if(isChecked) {
            adapterOnClick(obj)
        }
    }

    override fun onTodoEditClick(v: View) {
        val uuid = v.tag.toString().toInt()
        val action = ToDoListFragmentDirections.actionEditToDoFragment(uuid)

        Navigation.findNavController(v).navigate(action)
    }
}