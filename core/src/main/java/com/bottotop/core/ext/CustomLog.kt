package com.bottotop.core.ext

import android.util.Log

object Logg {
    fun v(thread: Thread,msg: String) {
        Log.v(tag(thread), msg)
    }

    fun d(thread: Thread,msg: String) {
        Log.d(tag(thread), msg)
    }

    fun i(thread: Thread,msg: String) {
        Log.i(tag(thread), msg)
    }

    fun w(thread: Thread,msg: String) {
        Log.w(tag(thread), msg)
    }

    fun e(thread: Thread , msg: String) {
        Log.e(tag(thread), msg)
    }

    private fun tag(thread: Thread): String {
        val level = 4
        val trace = thread.stackTrace[level]
        val fileName = trace.fileName
        val classPath = trace.className
        val className = classPath.substring(classPath.lastIndexOf(".") + 1)
        val methodName = trace.methodName
        val lineNumber = trace.lineNumber
        return "MyJob# $className.$methodName($fileName:$lineNumber)"
    }
}