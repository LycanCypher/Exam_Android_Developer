package org.lycancypher.exam_android_developer

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.lycancypher.exam_android_developer.room.NewDb
import org.lycancypher.exam_android_developer.room.User
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserListFragment : Fragment(), ItemListener {
    private lateinit var addButton: FloatingActionButton
    private lateinit var recyclerUser: RecyclerView
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun getListener(): ItemListener{
        return this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_user_list, container, false)

        addButton = view.findViewById(R.id.btnAdd)
        recyclerUser = view.findViewById(R.id.listUsrs)

        addButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_userListFragment_to_addEditFragment
            )
        }
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Lista de usuarios"

        val executor: ExecutorService = Executors.newSingleThreadExecutor()

        executor.execute(Runnable {
            val userArray = NewDb
                .getInstance(context = requireContext())
                ?.userDao()
                ?.getUsers() as MutableList<User>

            Handler(Looper.getMainLooper()).post(Runnable {
                adapter = UserAdapter(userArray, getListener())
                recyclerUser.adapter = adapter
            })
        })

        return view
    }

    override fun onEdit(user: User) {
        //
    }

    override fun onDelete(user: User) {
        val executor: ExecutorService = Executors.newSingleThreadExecutor()

        executor.execute(Runnable {
            val userArray = NewDb
                .getInstance(context = requireContext())
                ?.userDao()
                ?.removeUserById(user.id)

            Handler(Looper.getMainLooper()).post(Runnable {
                adapter.removeItem(user)
                Toast.makeText(requireContext(), "Usuario eliminado!", Toast.LENGTH_LONG).show()
            })
        })
    }
}