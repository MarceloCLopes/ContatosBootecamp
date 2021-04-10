package com.mcl.contatosbootecamp

import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val requestContact = 1
    val linearLayoutVertical = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.READ_CONTACTS),
                requestContact
            )
        } else {
            setContacts()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == requestContact) setContacts()
    }

    private fun setContacts() {
        val contactList: ArrayList<Contact> = ArrayList()

        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        if (cursor != null) {
            while (cursor.moveToNext()) {
                contactList.add(
                    Contact(
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    )
                )
            }
            cursor.close()
        }

        val adapter = ContactsAdapter(contactList)
        val contactRecyclerView = findViewById<RecyclerView>(R.id.contacts_recycler_view)

        contactRecyclerView.layoutManager = LinearLayoutManager(this, linearLayoutVertical, false)

        contactRecyclerView.adapter = adapter
    }
}