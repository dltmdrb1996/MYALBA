package com.bottotop.myalba

import android.content.Context
import android.graphics.Rect
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
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
import com.bottotop.model.NavigationTable
import com.bottotop.model.UserInfo
import com.bottotop.myalba.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.navigation.NavDestination
import com.bottotop.core.MainNavGraphDirections
import com.bottotop.model.NavigationRouter
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ToFlowNavigatable, ShowLoading {

    private val navigator: Navigator = Navigator()
    private var backPressedTime: Long = 0
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setNavigation()
        setSupportActionBar(binding.appToolbar)
        getDivice()
    }

    fun setNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navigator.navController = navController
        binding.navViewFragment.setupWithNavController(navController)
        setBottomNavAction(navController)
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


    override fun navigateToFlow(flow: NavigationFlow) {
        navigator.navigateToFlow(flow)
    }

    override fun showLoading(isLoading: Boolean) {
        binding.loadingView.isInProgress = isLoading
    }

    fun setBottomNavAction(navController: NavController) {
        binding.navViewFragment.setOnItemSelectedListener {
            showLoading(true)
            binding.ToolbarLayout.setExpanded(true)
            when (it.itemId) {
                R.id.member_flow -> {
                    if(NavigationRouter.currentState!=NavigationTable.Member) {
                        navigateToFlow(NavigationFlow.MemberFlow("test"))
                    }
                    return@setOnItemSelectedListener true
                }
                R.id.community_flow -> {
                    if(NavigationRouter.currentState!=NavigationTable.Community) {
                        navigateToFlow(NavigationFlow.CommunityFlow("test"))
                    }
                    return@setOnItemSelectedListener true
                }
                R.id.home_flow -> {
                    if(NavigationRouter.currentState!=NavigationTable.Home) {
                        navigateToFlow(NavigationFlow.HomeFlow("test"))
                    }
                    return@setOnItemSelectedListener true
                }
                R.id.schedule_flow -> {
                    if(NavigationRouter.currentState!=NavigationTable.Schedule) {
                        navigateToFlow(NavigationFlow.ScheduleFlow("test"))
                    }
                    return@setOnItemSelectedListener true
                }
                R.id.asset_flow -> {
                    if(NavigationRouter.currentState!=NavigationTable.Asset) {
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
            Log.e(TAG, "setNavAction: ${NavigationRouter.currentState}  , " +
                    "${destination.label}" )
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

    fun getDivice() {
        val android_id =
            Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID)
        UserInfo.android_Id = android_id
    }

//    override fun onBackPressed() {
//        if (NavigationRouter.currentState == NavigationTable.ScheduleDetail) {
//            super.onBackPressed()
//        } else {
//            if (System.currentTimeMillis() > backPressedTime + 2000) {
//                backPressedTime = System.currentTimeMillis()
//                Toast.makeText(this, "뒤로 버튼을 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()
//            } else if (System.currentTimeMillis() <= backPressedTime + 2000) {
//                finish()
//            }
//        }
//    }

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