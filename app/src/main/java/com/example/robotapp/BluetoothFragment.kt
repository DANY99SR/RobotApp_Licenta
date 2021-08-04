package com.example.robotapp

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_bluetooth.*
import kotlinx.android.synthetic.main.fragment_bluetooth.view.*
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class BluetoothFragment : Fragment() {
    private lateinit var firebaseUser: FirebaseUser
    private val DEVICE_ADDRESS = "00:20:10:08:37:EF"
    private val PORT_UUID =
        UUID.fromString("00001101-0000-1000-8000-00805f9b34fb") //Serial Port Service ID
    private var device: BluetoothDevice? = null
    private var socket: BluetoothSocket? = null
    private var outputStream: OutputStream? = null
    private var inputStream: InputStream? = null
    var startButton: Button? = null
    var sendButton: Button? = null
    var clearButton: Button? = null
    var stopButton: Button? = null
    var textView: TextView? = null
    var editText: EditText? = null
    var deviceConnected = false

    var thread: Thread? = null
    lateinit var buffer: ByteArray
    var bufferPosition = 0
    var stopThread = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bluetooth, container, false)

        startButton = view.findViewById(R.id.buttonStart)
        sendButton = view.findViewById(R.id.buttonSend)
        clearButton = view.findViewById(R.id.buttonClear)
        stopButton = view.findViewById(R.id.buttonStop)
        editText = view.findViewById(R.id.editText)
        textView = view.findViewById(R.id.textView)
        setUiEnabled(false)

        view.buttonStart.setOnClickListener {
            onClickStart(view)
        }
        view.buttonSend.setOnClickListener {
            onClickSend(view)
        }
        view.buttonClear.setOnClickListener {
            onClickClear(view)
        }
        view.buttonStop.setOnClickListener {
            onClickStop(view)
        }

        return view
    }

    fun setUiEnabled(bool: Boolean) {
        startButton!!.isEnabled = !bool
        sendButton!!.isEnabled = bool
        stopButton!!.isEnabled = bool
        textView!!.isEnabled = bool
    }

    fun BTinit(): Boolean {
        var found = false
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            Toast.makeText(activity, "Device doesn't Support Bluetooth", Toast.LENGTH_SHORT).show()
        }
        if (!bluetoothAdapter!!.isEnabled) {
            val enableAdapter = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableAdapter, 0)
            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        val bondedDevices = bluetoothAdapter.bondedDevices //paired
        if (bondedDevices.isEmpty()) {
            Toast.makeText(activity, "Please Pair the Device first", Toast.LENGTH_SHORT).show()
        } else {
            for (iterator in bondedDevices) {
                if (iterator.address == DEVICE_ADDRESS) {
                    device = iterator
                    found = true
                    break
                }
            }
        }
        return found
    }

    fun BTconnect(): Boolean {
        var connected = true
        try {
            socket = device!!.createRfcommSocketToServiceRecord(PORT_UUID)
            socket?.connect()
        } catch (e: IOException) {
            e.printStackTrace()
            connected = false
        }
        if (connected) {
            try {
                outputStream = socket!!.outputStream
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                inputStream = socket!!.inputStream
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return connected
    }

    fun onClickStart(view: View?) {
        if (BTinit()) {
            if (BTconnect()) {
                setUiEnabled(true)
                deviceConnected = true
                beginListenForData()
                textView!!.append("\nConnection Opened!\n")
            }
        }
    }

    fun beginListenForData() {
        val handler = Handler()
        var bundle = Bundle()
        val delimitator = " "
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        stopThread = false
        buffer = ByteArray(1024)
        val thread = Thread(Runnable {
            while (!Thread.currentThread().isInterrupted && !stopThread) {
                try {
                    val byteCount = inputStream!!.available()
                    if (byteCount > 0) {
                        val rawBytes = ByteArray(byteCount)
                        inputStream!!.read(rawBytes)
                        val string = String(rawBytes, Charsets.UTF_8)
                        val arr = string.split(delimitator).toTypedArray()
                        val dataRef = FirebaseDatabase.getInstance().reference
                            .child("Data").child(FirebaseAuth.getInstance().currentUser!!.uid)
                        val dataHash = HashMap<String, Any>()
                        dataHash["battery"] = arr[0]
                        dataHash["temperature"] = arr[1]
                        dataHash["humidity"] = arr[2]
                        dataHash["airquality"] = arr[3]
                        dataRef.setValue(dataHash)
                        handler.post { textView!!.append(string) }
                    }
                } catch (ex: IOException) {
                    stopThread = true
                }
            }
        })
        thread.start()
    }

    fun onClickSend(view: View?) {
        val string = editText!!.text.toString()
        """
     $string
     
     """.trimIndent()
        try {
            outputStream!!.write(string.toByteArray())
        } catch (e: IOException) {
            e.printStackTrace()
        }
        textView!!.append("\nSent Data:\n")
    }

    @Throws(IOException::class)
    fun onClickStop(view: View?) {
        stopThread = true
        outputStream!!.close()
        inputStream!!.close()
        socket!!.close()
        setUiEnabled(false)
        deviceConnected = false
        textView!!.append("\nConnection Closed!\n")
    }

    fun onClickClear(view: View?) {
        textView!!.text = ""
    }


}