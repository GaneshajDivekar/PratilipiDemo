package pratilipi.demo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import pratilipi.demo.database.CustomerListEntity
import pratilipi.demo.databinding.ContactLayoutBinding
import pratilipi.demo.interfaces.ItemClick


class ContactAdapter(
    var context: Context,
    var allPosts: List<CustomerListEntity>,
    var itemClickEvent: ItemClick
) : RecyclerView.Adapter<ContactAdapter.RecyclerViewHolder>() {

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (allPosts == null) {
            0
        } else {
            allPosts!!.size
        }
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val currentUser = allPosts!![position]

        if (currentUser.call_status.equals("0")) {
            holder.binding.block.setChecked(false)
        } else {
            holder.binding.block.setChecked(true)
        }
        holder.binding.block.setOnClickListener {
            if (holder.binding.block.isChecked) {
                holder.binding.block.setChecked(true)
           //     currentUser.call_status="1"
            } else {
                holder.binding.block.setChecked(false)
             //   currentUser.call_status="0"
            }
            itemClickEvent.onClick(allPosts.get(position),position)
        }
        holder.binding.contactdb = currentUser
        holder.binding.executePendingBindings()

    }

    class RecyclerViewHolder(val binding: ContactLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val binding =
            ContactLayoutBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
        return RecyclerViewHolder(binding)
    }

    fun removeItem(adapterPosition: Int) {
        allPosts.drop(adapterPosition);
        notifyItemRemoved(adapterPosition);
    }

    fun updateList(displayContact: List<CustomerListEntity>) {
        allPosts = displayContact
        notifyDataSetChanged()
    }

}