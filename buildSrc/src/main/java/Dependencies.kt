
object Apps {
    const val APP_ID = "com.bottotop.myalba"
    const val COMPILE_SDK = 30
    const val BUILD_TOOLS_VERSION = "30.0.2"
    const val MIN_SDK = 23
    const val TARGET_SDK = 30
    const val VERSION_CODE = 2
    const val VERSION_NAME = "1.0.1"
}

object Versions {
    //Project Setting
    const val BUILD_GRADLE = "4.2.2"
    const val KOTLIN_VERSION = "1.5.31"
    const val CORE_KTX = "1.6.0"
    const val APP_COMPAT = "1.3.1"
    const val ACTIVITY_KTX = "1.2.3"
    const val MATERIAL = "1.4.0"
    const val CONSTRAINT_LAYOUT = "2.1.0"

    //Kotlin
    const val SERIALIZATION = "1.3.0"

    //jetpack
    const val FRAGMENT_KTX = "1.3.6"
    const val LIFECYCLE_KTX = "2.3.1"
    const val ROOM = "2.3.0"
    const val HILT = "2.35"
    const val NAVIGATION = "2.3.5"

    //network
    const val RETROFIT = "2.9.0"
    const val OKHTTP = "4.9.1"

    //coroutines
    const val KOTLINX_COROUTINES = "1.5.0"

    //Glide
    const val GLIDE = "4.12.0"
    const val GLIDE_COMPILER ="4.12.0"

    //Lottie
    const val LOTTIE = "3.1.0"

    //test
    const val JUNIT = "4.13.2"
    const val ANDROID_JUNIT = "1.1.3"
    const val ESPRESSO_CORE = "3.4.0"
}

object Kotlin {
    const val SERIALIZATION = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.SERIALIZATION}"
    const val KOTLIN_STDLIB = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN_VERSION}"
    const val COROUTINES_CORE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.KOTLINX_COROUTINES}"
    const val COROUTINES_ANDROID = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.KOTLINX_COROUTINES}"
}

object AndroidX {
    const val CORE_KTX = "androidx.core:core-ktx:${Versions.CORE_KTX}"
    const val APP_COMPAT = "androidx.appcompat:appcompat:${Versions.APP_COMPAT}"
    const val ACTIVITY_KTX = "androidx.activity:activity-ktx:${Versions.ACTIVITY_KTX}"
    const val FRAGMENT_KTX = "androidx.fragment:fragment-ktx:${Versions.FRAGMENT_KTX}"
    const val MATERIAL = "com.google.android.material:material:${Versions.MATERIAL}"
    const val CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT_LAYOUT}"
}

object View{
    const val CARD_VIEW = "androidx.cardview:cardview:1.0.0"
    const val VIEWPAGER = "androidx.viewpager2:viewpager2:1.0.0"
    const val insetter = "dev.chrisbanes.insetter:insetter:0.6.0"
}

object Android_Libraries {
    const val HILT_ANDROID = "com.google.dagger:hilt-android:${Versions.HILT}"
    const val HILT_ANDROID_COMPILER = "com.google.dagger:hilt-android-compiler:${Versions.HILT}"
    const val LIFECYCLE_VIEWMODEL_KTX = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LIFECYCLE_KTX}"
    const val LIFECYCLE_LIVEDATA_KTX = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.LIFECYCLE_KTX}"
    const val ROOM_RUNTIME = "androidx.room:room-runtime:${Versions.ROOM}"
    const val ROOM_KTX = "androidx.room:room-ktx:${Versions.ROOM}"
    const val ROOM_COMPILER = "androidx.room:room-compiler:${Versions.ROOM}"
    const val GSON = "com.google.code.gson:gson:2.8.8"

}

object Libraries {

    const val GLIDE = "com.github.bumptech.glide:glide:${Versions.GLIDE}"
    const val GLIDE_COMPILER = "com.github.bumptech.glide:compiler:${Versions.GLIDE_COMPILER}"
    const val LOTTIE = "com.airbnb.android:lottie:${Versions.LOTTIE}"

}

object Network {
    const val RETROFIT = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}"
    const val RETROFIT_CONVERTER_GSON = "com.squareup.retrofit2:converter-gson:${Versions.RETROFIT}"
    const val SERIALIZATION = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0"
    const val OKHTTP = "com.squareup.okhttp3:okhttp:${Versions.OKHTTP}"
    const val OKHTTP_LOGGING_INTERCEPTOR = "com.squareup.okhttp3:logging-interceptor:${Versions.OKHTTP}"

}

object Test {
    const val JUNIT = "junit:junit:4.13.2"
    const val ANDROID_JUNIT = "androidx.test:runner:1.1.0"
    const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:3.2.0"
}

object Navigation{
    const val NAVIGATION_FRAGMENT = "androidx.navigation:navigation-fragment-ktx:${Versions.NAVIGATION}"
    const val NAVIGATION_UI = "androidx.navigation:navigation-ui-ktx:${Versions.NAVIGATION}"
    const val NAVIGATION_DYNAMIC = "androidx.navigation:navigation-dynamic-features-fragment:${Versions.NAVIGATION}"
    const val NAVIGATION_TEST = "androidx.navigation:navigation-testing:${Versions.NAVIGATION}"
}
