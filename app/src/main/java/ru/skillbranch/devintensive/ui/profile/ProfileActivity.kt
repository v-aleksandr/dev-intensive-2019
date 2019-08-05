package ru.skillbranch.devintensive.ui.profile

import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_profile.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.utils.Utils
import ru.skillbranch.devintensive.viewmodels.ProfileViewModel

class ProfileActivity : AppCompatActivity() {
    companion object {
        const val IS_EDIT_MODE = "IS_EDIT_MODE"
    }

    private lateinit var viewModel: ProfileViewModel
    var isEditMode = false
    lateinit var viewFields: Map<String, TextView>
    private var initials: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initViews(savedInstanceState)
        initViewModel()
        Log.d("M_ProfileActivity", "onCreate")
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onResume() {
        super.onResume()
        initials = getInitials()
        runShow(initials)
    }

    private fun createInitialsBitmap(initials: String): Bitmap {
        val size = iv_avatar.layoutParams.width / 2f
        val bitmap = Bitmap.createBitmap((2 * size).toInt(), (2 * size).toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        var paint = Paint()
        val typedValue = TypedValue()
        theme.resolveAttribute(R.attr.colorAccent, typedValue, true)
        paint.color = resources.getColor(typedValue.resourceId, theme)
        canvas.drawRect(0f, 0f, (2 * size), (2 * size), paint)
//        val initials = getInitials()
        if (initials != null) {
            paint = Paint()
            paint.color = Color.WHITE
            paint.textSize = size
            paint.textScaleX = 1f
            paint.textAlign = Paint.Align.CENTER
            paint.typeface = Typeface.create("sans-serif-light", Typeface.NORMAL)
            paint.isFakeBoldText = true
            paint.style = Paint.Style.FILL
            canvas.drawText(initials, size, size - ((paint.descent() + paint.ascent()) / 2), paint)
        }
        return bitmap
    }

    private fun getInitials(): String {
/*        return "${et_first_name.text} ${et_last_name.text}"
            .split(' ')
            .mapNotNull { it.firstOrNull()?.toString() }
            .reduce { acc, s -> acc + s }*/
        return if (initials == "") {
            val firstName = et_first_name.text.toString()
            val lastName = et_last_name.text.toString()
            Utils.transliteration(Utils.getInitial(firstName)) + Utils.transliteration(Utils.getInitial(lastName))
        } else initials
    }

    private fun runShow(initials: String) {
        val ivAvatar = iv_avatar
        val initialsBitmap = createInitialsBitmap(initials)
        ivAvatar.setImageBitmap(initialsBitmap)
        ivAvatar.setInitialsPaint(initialsBitmap)
        ivAvatar.showInitials()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean(IS_EDIT_MODE, isEditMode)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        viewModel.getProfileData().observe(this, Observer { updateUI(it) })
        viewModel.getTheme().observe(this, Observer { updateTheme(it) })

    }

    private fun updateTheme(mode: Int) {
        Log.d("M_ProfileActivity", "update theme")
        delegate.setLocalNightMode(mode)
    }

    private fun updateUI(profile: Profile) {
        profile.toMap().also {
            for ((k, v) in viewFields) {
                v.text = it[k].toString()
            }
        }
    }

    private fun initViews(savedInstanceState: Bundle?) {
        viewFields = mapOf(
            "nickName" to tv_nick_name,
            "rank" to tv_rank,
            "firstName" to et_first_name,
            "lastName" to et_last_name,
            "about" to et_about,
            "repository" to et_repository,
            "rating" to tv_rating,
            "respect" to tv_respect
        )

        isEditMode = savedInstanceState?.getBoolean(IS_EDIT_MODE, false) ?: false
        showCurrentMode(isEditMode)

        btn_edit.setOnClickListener(View.OnClickListener {
            if (isEditMode) saveProfileInfo()
            isEditMode = !isEditMode
            showCurrentMode(isEditMode)
        })
        //the same as previous
        /*btn_edit.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                isEditMode = !isEditMode
                showCurrentMode(isEditMode)
            }
        })*/
        btn_switch_theme.setOnClickListener {
            viewModel.switchTheme()
        }
    }

    private fun showCurrentMode(isEdit: Boolean) {
        var newInitials = ""
        val info = viewFields.filter { setOf("firstName", "lastName", "about", "repository").contains(it.key) }
        for ((k, v) in info) {
            v as EditText
            v.isFocusable = isEdit
            v.isFocusableInTouchMode = isEdit
            v.isEnabled = isEdit
            v.background.alpha = if (isEdit) 255 else 0
            newInitials += when (k) {
                "firstName", "lastName" -> Utils.transliteration(Utils.getInitial(v.text.toString()))
                else -> ""
            }
        }
        initials = newInitials
        ic_eye.visibility = if (isEdit) View.GONE else View.VISIBLE
        wr_about.isCounterEnabled = isEdit
// set show here
        runShow(initials)

        with(btn_edit) {
            val filter: ColorFilter? = if (isEdit) {
                PorterDuffColorFilter(resources.getColor(R.color.color_accent, theme), PorterDuff.Mode.SRC_IN)
            } else {
                null
            }
            val icon = if (isEdit) {
                resources.getDrawable(R.drawable.ic_save_black_24dp, theme)
            } else {
                resources.getDrawable(R.drawable.ic_edit_black_24dp, theme)
            }
            background.colorFilter = filter
            setImageDrawable(icon)
        }
    }

    private fun saveProfileInfo() {
        if (!isValidRepository(et_repository.text.toString())) {
            wr_repository.error = "Невалидный адрес репозитория"
            et_repository.setText("")
            isEditMode = !isEditMode
        } else {
            wr_repository.error = ""
            Profile(
                firstName = et_first_name.text.toString(),
                lastName = et_last_name.text.toString(),
                about = et_about.text.toString(),
                repository = et_repository.text.toString()
            ).apply {
                viewModel.saveProfileData(this)
            }
        }
    }

    private fun isValidRepository(repositoryString: String): Boolean {
        if (repositoryString.isNotEmpty()) {
            var arr = repositoryString.split(" ")
            var repString = arr[0]
            if (repString.isNotEmpty()) {
                val nonValidWords = listOf(
                    "enterprise",
                    "features",
                    "topics",
                    "collections",
                    "trending",
                    "events",
                    "marketplace",
                    "pricing",
                    "nonprofit",
                    "customer-stories",
                    "security",
                    "login",
                    "join"
                )
                //"https://" - if (exists) { just remove }
                //"www." - if (exists) { just remove }
                //"github.com/"  - if (not exists) { return NON VALID } else { just remove }
                //split by "/" rest of string, if (array.size!=1)  { return NON VALID } else { check array[0] }
                //if (array[0] empty or in non-valid-list) { return NON VALID } else { return VALID }
                if (repString.startsWith("https://")) {
                    repString = repString.substring(8)
                }
                if (repString.startsWith("www.")) {
                    repString = repString.substring(4)
                }
                if (!repString.startsWith("github.com/")) {
                    return false
                } else {
                    repString = repString.substring(11)
                    arr = repString.split("/")
                    if (arr.size != 1) {
                        return false
                    } else {
                        if (arr[0].isEmpty() || arr[0] in nonValidWords) {
                            return false
                        }
                    }
                }
            }
        }
        return true
    }
}
