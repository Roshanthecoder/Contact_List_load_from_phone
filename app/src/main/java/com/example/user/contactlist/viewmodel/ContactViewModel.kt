package com.example.user.contactlist.viewmodel

import android.content.Context
import androidx.databinding.BaseObservable
import androidx.databinding.ObservableArrayList
import com.example.user.contactlist.model.Contact
import com.example.user.contactlist.model.ContactRepository

class ContactViewModel(context: Context?) : BaseObservable() {
    val contacts: ObservableArrayList<Contact>
    private val repository: ContactRepository

    init {
        contacts = ObservableArrayList()
        repository = ContactRepository(context!!)
    }

    fun getContact(): List<Contact> {
        contacts.addAll(repository.fetchContacts())
        return contacts
    }
}