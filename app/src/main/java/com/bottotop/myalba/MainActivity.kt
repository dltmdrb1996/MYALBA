package com.bottotop.myalba

import android.app.Dialog
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bottotop.core.global.ShowLoading
import com.bottotop.core.ext.isVisible
import com.bottotop.core.navigation.NavigationFlow
import com.bottotop.core.navigation.Navigator
import com.bottotop.core.navigation.ToFlowNavigatable
import com.bottotop.myalba.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import com.bottotop.core.global.SharedViewModel
import com.bottotop.core.ext.showToast
import com.bottotop.core.global.NavigationRouter
import com.bottotop.core.global.NavigationTable
import com.bottotop.core.global.SocialInfo
import kotlinx.coroutines.delay
import java.util.*
import kotlin.concurrent.timer
import android.content.Intent
import android.net.*
import android.os.Looper
import com.bottotop.core.ext.connectivityManager
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import androidx.core.content.ContentProviderCompat.requireContext
import com.bottotop.core.MainNavGraphDirections
import com.bottotop.core.ext.withDelayOnMain


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ToFlowNavigatable, ShowLoading {

    private val navigator: Navigator = Navigator()
    private var networkCheck: Boolean = true
    lateinit var binding: ActivityMainBinding
    private val viewModel: SharedViewModel by viewModels()
    private var backPressedTime: Long = 0
    lateinit var cm: ConnectivityManager
    lateinit var mNetworkCallback: NetworkCallback


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        firstNetworkCheck()
        observeSharedViewModel()
        initBottomNavigation()
        setSupportActionBar(binding.appToolbar)
        getDivice()
    }

    override fun onResume() {
        observeNetwork()
        super.onResume()
    }

    override fun onPause() {
        cm.unregisterNetworkCallback(mNetworkCallback)
        super.onPause()
    }

    override fun navigateToFlow(flow: NavigationFlow) {
        navigator.navigateToFlow(flow)
    }

    override fun showLoading(isLoading: Boolean) {
        if(isLoading) binding.mainLayout.alpha = 0.5f
        else binding.mainLayout.alpha = 1f
        binding.mainLayout.isClickable = isLoading
        binding.loadingView.isInProgress = isLoading
    }

    private fun observeSharedViewModel() {
        viewModel.sample.observe(this, {
            showToast("공유성공")
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.schedule_menu, menu)
        return true
    }

    // 딥링크로 설정이랑 알림 QR연결
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.menu_right1 -> {
                //검색 버튼 눌렀을 때
                Toast.makeText(applicationContext, "첫번쨰 실행", Toast.LENGTH_LONG).show()
                return super.onOptionsItemSelected(item)
            }
            R.id.menu_right2 -> {
                Toast.makeText(applicationContext, "두번쨰 실행", Toast.LENGTH_LONG).show()
                return super.onOptionsItemSelected(item)
            }
            R.id.menu_right3 -> {
                Toast.makeText(applicationContext, "세번째 실행", Toast.LENGTH_LONG).show()
                return super.onOptionsItemSelected(item)
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun initBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navigator.navController = navController
        binding.navViewFragment.setupWithNavController(navController)
        setBottomNavAction(navController)
    }

    fun setBottomNavAction(navController: NavController) {
        binding.navViewFragment.setOnItemSelectedListener {
            binding.ToolbarLayout.setExpanded(true)
            when (it.itemId) {
                R.id.member_flow -> {
                    if (NavigationRouter.currentState != NavigationTable.Member) {
                        navigateToFlow(NavigationFlow.MemberFlow("test"))
                    }
                    return@setOnItemSelectedListener true
                }
                R.id.community_flow -> {
                    if (NavigationRouter.currentState != NavigationTable.Community) {
                        navigateToFlow(NavigationFlow.CommunityFlow("test"))
                    }
                    return@setOnItemSelectedListener true
                }
                R.id.home_flow -> {
                    if (NavigationRouter.currentState != NavigationTable.Home) {
                        navigateToFlow(NavigationFlow.HomeFlow("test"))
                    }
                    return@setOnItemSelectedListener true
                }
                R.id.schedule_flow -> {
                    if (NavigationRouter.currentState != NavigationTable.Schedule) {
                        navigateToFlow(NavigationFlow.ScheduleFlow("test"))
                    }
                    return@setOnItemSelectedListener true
                }
                R.id.asset_flow -> {
                    if (NavigationRouter.currentState != NavigationTable.Asset) {
                        navigateToFlow(NavigationFlow.AssetFlow("test"))
                    }
                    return@setOnItemSelectedListener true
                }
                else -> return@setOnItemSelectedListener true
            }
        }
        setNavAction(navController)
    }

    fun setNavAction(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            NavigationRouter.saveState(destination.label as String)
//            Log.e(TAG, "setNavAction: ${NavigationRouter.currentState}  , ")
            when (NavigationRouter.currentState) {
                NavigationTable.Member -> binding.appToolbar.title = "직원관리"
                NavigationTable.Community -> binding.appToolbar.title = "커뮤니티"
                NavigationTable.Home -> {
                    binding.appToolbar.isVisible()
                    binding.navViewFragment.isVisible()
                    binding.appToolbar.title = "내알빠"
                }
                NavigationTable.Schedule -> binding.appToolbar.title = "일정관리"
                NavigationTable.Asset -> binding.appToolbar.title = "자산관리"
            }
        }
    }

    fun firstNetworkCheck() {
        cm = getSystemService(ConnectivityManager::class.java)
        val check = cm.activeNetworkInfo?.isConnectedOrConnecting
        if (check == false || check == null) {
            showToast("네트워크 연결을 확인해주세요")
            finish()
        }
    }

    private fun observeNetwork() {
        mNetworkCallback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                Log.e(TAG, "네트워크 활성화")
                networkCheck = true
            }
            // 와이파이 연결해제시 lost에서 다시 연결까지의 약간의 텀이있음
            override fun onLost(network: Network) {
                networkCheck = false
                withDelayOnMain(1000){
                    if (networkCheck) {
                        Log.e(TAG, "onLost: 연결되있음")
                    } else {
                        showToast("네트워크 연결이 끊어졌습니다.")
                    }
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cm.registerDefaultNetworkCallback(mNetworkCallback)
        }
    }


    // 사용자 고유id값
    fun getDivice() {
        val android_id =
            Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID)
        SocialInfo.id = android_id
    }

    override fun onBackPressed() {
        when (NavigationRouter.currentState) {
            is NavigationTable.Asset, NavigationTable.Community ,
                NavigationTable.Member, NavigationTable.Schedule -> navigateToFlow(NavigationFlow.HomeFlow("back"))
            is NavigationTable.Login, NavigationTable.Register , NavigationTable.Home -> {
                if (System.currentTimeMillis() > backPressedTime + 3000) {
                    backPressedTime = System.currentTimeMillis()
                    Toast.makeText(this, "한번더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
                } else if (System.currentTimeMillis() <= backPressedTime + 3000) {
                    finish()
                }
            }
            else -> super.onBackPressed()
        }
    }

    // 키보드내리기
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val focusView: View? = currentFocus
        if (focusView != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev.x.toInt()
            val y = ev.y.toInt()
            if (!rect.contains(x, y)) {
                val imm: InputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }


    companion object {
        const val TAG = "메인액티비티"
    }


}