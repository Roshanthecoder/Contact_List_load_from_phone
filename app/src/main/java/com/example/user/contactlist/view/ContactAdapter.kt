package com.example.user.contactlist.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Filter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.user.contactlist.R
import com.example.user.contactlist.databinding.ItemContactBinding
import com.example.user.contactlist.model.Contact
import com.example.user.contactlist.model.loadImage1

import com.example.user.contactlist.view.ContactAdapter.ContactViewHolder
import java.util.Locale
import java.util.Random

class ContactAdapter internal constructor(private val context: Context) :
    RecyclerView.Adapter<ContactViewHolder>() {
    private var contacts: List<Contact>? = null
    private var binding: ItemContactBinding? = null

    // Allows to remember the last item shown on screen
    private var lastPosition = -1
    private var filtered_icontacts: List<Contact> = ArrayList()
    private val mFilter = ItemFilter()
    private var mOnItemClickListener: OnItemClickListener? = null

    /**
     * The interface On item click listener.
     */
    interface OnItemClickListener {
        /**
         * On item click.
         *
         * @param view     the view
         * @param obj      the obj
         * @param position the position
         */
        fun onItemClick(view: View?, obj: Contact?, position: Int)
    }

    /**
     * Sets on item click listener.
     *
     * @param mItemClickListener the m item click listener
     */
    fun setOnItemClickListener(mItemClickListener: OnItemClickListener?) {
        mOnItemClickListener = mItemClickListener
    }

    fun setContacts(contacts: List<Contact>) {
        this.contacts = contacts
        filtered_icontacts = contacts
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ContactViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(viewGroup.context), R.layout.item_contact,
            viewGroup, false
        )
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(contactViewHolder: ContactViewHolder, i: Int) {
        contactViewHolder.onBind(filtered_icontacts[i])
        // Here you apply the animation when the view is bound
        setAnimation(contactViewHolder.itemView, i)
    }

    override fun getItemCount(): Int {
        // return contacts != null ? contacts.size() : 0;
        return filtered_icontacts.size
    }

    inner class ContactViewHolder(private val binding: ItemContactBinding?) :
        RecyclerView.ViewHolder(binding!!.root) {
        fun onBind(contact: Contact) {
            //binding.setVariable(BR.contact, contact);
            // binding.setContact(contact);
            binding!!.contactName.text = contact.name
            binding.contactNumber.text = contact.phoneNumber
            binding.selectedContactLayout.setOnClickListener { view ->
                mOnItemClickListener!!.onItemClick(
                    view,
                    contact,
                    adapterPosition
                )
            }
            if (contact.photoUri != null) {
                binding.drawableTextView.visibility = View.GONE
                binding.contactPhoto.visibility = View.VISIBLE
                loadImage1(binding.contactPhoto, contact.photoUri)
            } else {
                binding.contactPhoto.visibility = View.GONE
                binding.drawableTextView.visibility = View.VISIBLE
                val gradientDrawable = binding.drawableTextView.background as GradientDrawable
                gradientDrawable.setColor(randomColor)
                val serviceSubString = contact.name!!.substring(0, 2)
                binding.drawableTextView.text = serviceSubString.uppercase(Locale.getDefault())
            }
            binding.executePendingBindings()
        }
    }

    /*        *
     * Here is the key method to apply the animation*/
    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.item_animation_from_right)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    private val randomColor: Int
        /**
         * @return a random color which is used a background by
         * services initial letters
         */
        private get() {
            val rnd = Random()
            return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        }
    val filter: Filter
        /**
         * Gets filter.
         *
         * @return the filter
         */
        get() = mFilter

    private inner class ItemFilter : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val query = constraint.toString().lowercase(Locale.getDefault())
            val results = FilterResults()
            val list = contacts
            val result_list: MutableList<Contact> = ArrayList(
                list!!.size
            )
            for (i in list.indices) {
                val str_title = list[i].name
                if (str_title!!.lowercase(Locale.getDefault()).contains(query)) {
                    result_list.add(list[i])
                }
            }
            results.values = result_list
            results.count = result_list.size
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            filtered_icontacts = results.values as List<Contact>
            notifyDataSetChanged()
        }
    }
}