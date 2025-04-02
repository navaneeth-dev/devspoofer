package com.rizexor.devspoofer

import android.R.attr.classLoader
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage


class MainHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedBridge.log("Dev Spoofer Loaded!")

        // Filtering unnecessary applications
//        if (lpparam.packageName != "com.digilocker.android") return

        // Execute Hook
        hook(lpparam)
    }

    private fun hook(lpparam: XC_LoadPackage.LoadPackageParam) {
//        var targetClass = XposedHelpers.findClass("android.provider.Settings.Global", lpparam.classLoader)
//        for (method in targetClass.declaredMethods) {
//            XposedBridge.log("Hooking method: ${method.name} -> Params: ${method.parameterTypes.joinToString()}")
//
//            XposedHelpers.findAndHookMethod(
//                targetClass,
//                method.name,
//                *method.parameterTypes,  // Dynamically use all parameters
//                object : XC_MethodHook() {
//                    override fun beforeHookedMethod(param: MethodHookParam) {
//                        XposedBridge.log("[+] Hooked ${method.name}")
//                        param.args.forEachIndexed { index, arg ->
//                            XposedBridge.log("Arg[$index]: Type=${arg?.javaClass?.name}, Value=$arg")
//                        }
//                    }
//                }
//            )
//        }

        var classLoader = lpparam.classLoader

        XposedHelpers.findAndHookMethod(
            "android.provider.Settings.Global",
            classLoader,
            "getInt",
            android.content.ContentResolver::class.java,
            String::class.java,
            Integer::class.javaPrimitiveType,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    XposedBridge.log("[+] Global.getInt called -> ${param.args[1] as String}")

                    var key = param.args[1] as String
                    if (key == "development_settings_enabled") {
                        param.setResult(0);
                    } else if (key == "adb_wifi_enabled") {
                        param.setResult(0);
                    } else if (key == "adb_enabled") {
                        param.setResult(0);
                    }
                }
            }
        )
    }
}