package com.example.handlercancellation

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContactsResult(var data: ArrayList<ContactsItem>): Parcelable
@Parcelize
data class ContactsItem(var id: String, var email: String,
                            var address: String, var gender: String,
                                var name: String, var phone: PhoneItem): Parcelable
@Parcelize
data class PhoneItem(var mobile: String, var home: String, var office: String) : Parcelable