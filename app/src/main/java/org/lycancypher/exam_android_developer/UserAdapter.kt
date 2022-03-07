package org.lycancypher.exam_android_developer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import org.lycancypher.exam_android_developer.room.User

class UserAdapter (
    private val userArray: MutableList<User>?,
    val itemListener : ItemListener
    ) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val id: TextView = view.findViewById(R.id.tvId)
        val pic: ImageView = view.findViewById(R.id.ivUserThumb)
        val name: TextView = view.findViewById(R.id.tvName)
        val appat: TextView = view.findViewById(R.id.tvApelPat)
        val apmat: TextView = view.findViewById(R.id.tvApelMat)
        val editBtn: ImageButton = view.findViewById(R.id.btnEdit)
        val deleteBtn: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.user_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sb = StringBuilder()
        val reg = "Registro: "
        sb.append(reg).append((userArray?.get(position)?.id ?: "").toString())
        holder.id.text = sb
        holder.name.text = userArray?.get(position)?.name ?: ""
        holder.appat.text = userArray?.get(position)?.apPat ?: ""
        holder.apmat.text = userArray?.get(position)?.apMat ?: ""
        holder.pic.setImageURI(userArray?.get(position)?.usrPic?.toUri())
        /*holder.phone.text = userArray?.get(position)?.phone ?: ""
        holder.mail.text = userArray?.get(position)?.mail ?: ""
        holder.lat.text = (userArray?.get(position)?.lat ?: "").toString()
        holder.long.text = (userArray?.get(position)?.longitude ?: "").toString()*/

        holder.deleteBtn.setOnClickListener {
            userArray?.get(position)?.let { user ->
                itemListener.onDelete(user)
            }
        }
    }

    override fun getItemCount() = userArray?.size ?: 0

    fun removeItem(user: User){
        userArray?.let {
            val position = it.indexOf(user)
            userArray.remove(user)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, userArray.size)
        }
    }
}