package submission.submission2_3

import Alarm.AlarmReceiver
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.title = "Set a Reminder"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() , SharedPreferences.OnSharedPreferenceChangeListener {

        private lateinit var  alarmReceiver: AlarmReceiver
        private lateinit var isNotification: SwitchPreferenceCompat
        private lateinit var mcontext: Context

        override fun onAttach(context: Context) {
            super.onAttach(context)
            mcontext = context
        }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            alarmReceiver = AlarmReceiver()
            init()
            setSum()
        }

        private fun init() {
            isNotification =
                findPreference<SwitchPreferenceCompat>("sync") as SwitchPreferenceCompat
        }

        override fun onResume() {
            super.onResume()
            preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onPause() {
            super.onPause()
            preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        }

        private fun setSum() {
            val shared = preferenceManager.sharedPreferences
            isNotification.isChecked = shared.getBoolean("sync", false)
            Log.d("isChecked ", isNotification.isChecked.toString())
        }

        override fun onSharedPreferenceChanged(sp: SharedPreferences, key: String) {
            if (key == "sync") {
                isNotification.isChecked = sp.getBoolean("sync", false)

                if (isNotification.isChecked){
                    alarmReceiver.setRepeat(mcontext, getString(R.string.notification_msg))
                    Log.d("setAlarm ", isNotification.isChecked.toString())
                } else {
                    alarmReceiver.cancelAlarm(mcontext)
                    Log.d("cancelAlarm ", isNotification.isChecked.toString())
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}
