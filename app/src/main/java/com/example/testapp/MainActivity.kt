package com.example.testapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(checkDeviceRoot())
            return
    }

    fun Activity.checkDeviceRoot(): Boolean {
        return if (DeviceUtils.isDeviceRooted(this)) {
            AlertDialog.Builder(this)
                .setMessage("Your device is rooted. you can not use this app into rooted device.")
                .setCancelable(false)
                .setPositiveButton("Click") { _, _ ->
                    exitProcess(0)
                }.show()
            true
        } else {
            false
        }
    }

    object DeviceUtils {
        fun isDeviceRooted(context: Context?): Boolean {
            return isRooted1 || isRooted2
        }

        private val isRooted1: Boolean
            get() {
                val file = File("/system/app/Superuser.apk")
                return file.exists()
            }

        // try executing commands
        private val isRooted2: Boolean
            get() = (canExecuteCommand("/system/xbin/which su")
                    || canExecuteCommand("/system/bin/which su")
                    || canExecuteCommand("which su"))

        private fun canExecuteCommand(command: String): Boolean {
            return try {
                Runtime.getRuntime().exec(command)
                true
            } catch (e: Exception) {
                false
            }
        }
    }
}