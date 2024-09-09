package com.quoinsight.tv


public class commonGui { companion object { // to allow static functions, e.g. commonGui.writeMessage(...) instead of commonGui().writeMessage(...)

  public fun writeMessage(parentContext: android.content.Context, tag: String, msg: String) { // (String...args) // varargs
    android.widget.Toast.makeText(
      parentContext, "$tag: $msg",
        android.widget.Toast.LENGTH_LONG
    ).show()  // .setDuration(int duration)
    //android.util.Log.e(tag, msg)
    //MainActivity.getInstance().edit1.setText("$tag: $msg")
  }

}}

