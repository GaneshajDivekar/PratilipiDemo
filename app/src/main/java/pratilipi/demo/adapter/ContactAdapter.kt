package pratilipi.demo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
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

        holder.binding.block.setChecked(allPosts.get(position).call_status)

        holder.binding.block.setOnClickListener {
            if (holder.binding.block.isChecked()) {
                holder.binding.block.setChecked(false);
                holder.binding.block.setChecked(allPosts.get(position).call_status)
            } else {
                holder.binding.block.setChecked(allPosts.get(position).call_status)
                holder.binding.block.setChecked(true);
            }
            itemClickEvent.onClick(allPosts.get(position))
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