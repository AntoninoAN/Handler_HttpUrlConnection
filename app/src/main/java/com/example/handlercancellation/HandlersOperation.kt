package com.example.handlercancellation

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import org.json.JSONObject
import java.lang.StringBuilder
import java.util.*


class HandlerSignalNetwork(var progress: ProgressBar,
                           var textView: TextView): Handler(){
    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        //todo get the type of message [Cancel, Result]
        when(msg.obj){
            SignalTypes.CANCEL -> {
                //todo hide PB, TV
                progress.visibility = View.GONE
                textView.text =
                    msg.data.getString(ConstantValues.EXTRA_CANCEL_MESSAGE)
            }
            SignalTypes.RESULT ->{
                progress.visibility= View.GONE
                textView.visibility = View.VISIBLE

                val data= msg.data.getParcelable<ContactsResult>(ConstantValues.EXTRA_NETWORK_MESSAGE)
                textView.text = StringBuilder(
                    data!!.data[0].name +"\n"+
                            data!!.data[0].phone.mobile +"\n"
                ).toString()
            }
        }
    }
}

enum class SignalTypes{
    CANCEL,
    RESULT
}

class NetworkTask(var handler: HandlerSignalNetwork): Runnable{
    override fun run() {
        //todo call Network to init
        var stringResponse=
            Network.initNetwork("https://api.androidhive.info/contacts/")
        var bundle= Bundle()

        val dataContacts: ContactsResult
        dataContacts = stringResponse.parseResult(stringResponse)

        bundle.putParcelable(ConstantValues.EXTRA_NETWORK_MESSAGE,
                                dataContacts)

        var message: Message = Message()
        message.data = bundle
        message.obj = SignalTypes.RESULT
        handler.sendMessage(message)
    }

}

class CancelTask(var handler: HandlerSignalNetwork): Runnable{
    override fun run() {
        val bundle= Bundle()
        bundle.putString(ConstantValues.EXTRA_CANCEL_MESSAGE,
            "Cancelled!")
        var message = Message()
        message.obj = SignalTypes.CANCEL
        message.data = bundle
        handler.sendMessage(message)
    }
}

object ConstantValues{
    const val EXTRA_NETWORK_MESSAGE=
        "com.example.handlercancellation.EXTRA_NETWORK_MESSAGE"
    const val EXTRA_CANCEL_MESSAGE=
        "com.example.handlercancellation.EXTRA_CANCEL_MESSAGE"
}

fun String.parseResult(value: String): ContactsResult{
    //todo json parsing
    val jsonObject = JSONObject(value)
    var random = Random().nextInt(5)
    val contactsDetail =
        jsonObject.getJSONArray("contacts").getJSONObject(random)
    val contactsDetailPhone = contactsDetail.getJSONObject("phone")
    val pojoDetailsPhone= PhoneItem(
        contactsDetailPhone.getString("mobile"),
        contactsDetailPhone.getString("home"),
        contactsDetailPhone.getString("office")
    )
    val pojoContactsItem = ContactsItem(
        contactsDetail.getString("id"),
        contactsDetail.getString("email"),
        contactsDetail.getString("address"),
        contactsDetail.getString("gender"),
        contactsDetail.getString("name"),
        pojoDetailsPhone
    )
    return ContactsResult(arrayListOf(pojoContactsItem))
}