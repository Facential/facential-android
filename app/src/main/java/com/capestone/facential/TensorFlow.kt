//package com.capestone.facential
//
//import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
//import org.tensorflow.lite.Interpreter
//
//class TensorFlow {
//    val conditions = CustomModelDownloadConditions.Builder()
//        .requireWifi()  // Also possible: .requireCharging() and .requireDeviceIdle()
//        .build()
//    FirebaseModelDownloader.getInstance()
//    .getModel("your_model", DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND,
//    conditions)
//    .addOnSuccessListener { model: CustomModel? ->
//        // Download complete. Depending on your app, you could enable the ML
//        // feature, or switch from the local model to the remote model, etc.
//
//        // The CustomModel object contains the local path of the model file,
//        // which you can use to instantiate a TensorFlow Lite interpreter.
//        val modelFile = model?.file
//        if (modelFile != null) {
//            interpreter = Interpreter(modelFile)
//        }
//    }
//}