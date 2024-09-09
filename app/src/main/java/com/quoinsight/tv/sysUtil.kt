
// https://kotlinlang.org/docs/basic-syntax.html
// https://github.com/QuoInsight/minimal.apk/blob/master/app/src/main/java/com/quoinsight/minimal/sysUtil.java

package com.quoinsight.tv

public class sysUtil { companion object { // to allow static functions, e.g. sysUtil.launchUrl(...) instead of sysUtil().launchUrl(...)

  // https://developer.android.com/guide/components/intents-common#ViewUrl
  //fun openWebPage(url: String) {
  //static final public void launchUrl(android.content.Context parentContext, String url) {
  public fun launchUrl(parentContext: android.content.Context, url: String) {
    // https://sooka.my/en/watch/channel/aec-hd/1497824
    try {
      val webpage: android.net.Uri = android.net.Uri.parse(url)
      val intent = android.content.Intent(
        android.content.Intent.ACTION_VIEW, webpage
      )
      //if (intent.resolveActivity(packageManager) != null) {
        parentContext.startActivity(intent)
      //}
    } catch(e: Exception) {
      //commonGui().writeMessage(parentContext, "sysUtil.launchUrl", e.message.toString())
      commonGui.writeMessage(parentContext, "sysUtil.launchUrl", e.message.toString())
    }
  }


  //////////////////////////////////////////////////////////////////////

  public fun launchAppMgr(parentActivity: android.app.Activity) {
    try {
      parentActivity.startActivityForResult( android.content.Intent(
        android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS
      ), 0)
    } catch(e: Exception) {
      commonGui.writeMessage(parentActivity as android.content.Context, "sysUtil.launchAppMgr", e.message.toString())
      return
    }
  }

  public fun launchAppInfo(parentContext: android.content.Context, pkgName: String) {
    try {
      val intent = android.content.Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
      intent.setData(android.net.Uri.parse("package:" + pkgName))
      parentContext.startActivity(intent)
    } catch(e: Exception) {
      commonGui.writeMessage(parentContext, "sysUtil.launchAppInfo", e.message.toString())
      return
    }
  }

  public fun launchApp(parentContext: android.content.Context, pkgName: String) {
    try {
      val intent = parentContext.getPackageManager().getLaunchIntentForPackage(pkgName)
      parentContext.startActivity(intent)
    } catch(e: Exception) {
      commonGui.writeMessage(parentContext, "sysUtil.launchApp", "[" + pkgName + "] " +e.message.toString())
      return
    }
  }

  //////////////////////////////////////////////////////////////////////

}}
