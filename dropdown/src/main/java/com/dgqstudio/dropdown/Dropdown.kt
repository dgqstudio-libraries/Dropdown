package com.dgqstudio.dropdown

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.dgqstudio.dropdown.helpers.DeviceUIMode.getDeviceUIMode
import com.dgqstudio.dropdown.helpers.DeviceUIModes

class Dropdown @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    // Views
    private var dropdownHeader: View
    private var dropdownHeaderTitle: TextView
    private var dropdownHeaderButton: ImageButton
    private var dropdownHeaderDivider: View
    private var dropdownContent: FrameLayout

    // Attrs
    private var dropdownCollapsedByDefault: Boolean

    private var dropdownTitle: String
    private var dropdownTitleStyle: Int = 0
    private var dropdownButtonTint: ColorStateList?
    private var dropdownExpandedButtonSrc: Drawable?
    private var dropdownCollapsedButtonSrc: Drawable?
    private var dropdownShowDivider: Boolean

    private var headerPadding: Float = 0f
    private var headerTopPadding: Float = 0f
    private var headerStartPadding: Float = 0f
    private var headerEndPadding: Float = 0f
    private var headerBottomPadding: Float = 0f
    private var headerBackgroundColor: ColorStateList?

    private var contentPadding: Float = 0f
    private var contentTopPadding: Float = 0f
    private var contentStartPadding: Float = 0f
    private var contentEndPadding: Float = 0f
    private var contentBottomPadding: Float = 0f
    private var contentBackgroundColor: ColorStateList?

    // Variables
    private var isContentVisible = true
    private var dropdownContentHeight: Int = 0

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.dropdown, this, true)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.Dropdown,
            0,
            0
        ).apply {
            try {
                // Views
                dropdownHeader = findViewById(R.id.dropdownHeader)
                dropdownHeaderTitle = findViewById(R.id.dropdownHeaderTitle)
                dropdownHeaderButton = findViewById(R.id.dropdownHeaderButton)
                dropdownHeaderDivider = findViewById(R.id.dropdownHeaderDivider)
                dropdownContent = findViewById(R.id.dropdownContent)

                // Attrs
                dropdownCollapsedByDefault =
                    getBoolean(R.styleable.Dropdown_dropdownCollapsedByDefault, false)

                dropdownTitle = getString(R.styleable.Dropdown_dropdownTitle) ?: ""
                dropdownTitleStyle = getResourceId(R.styleable.Dropdown_dropdownTitleStyle, 0)
                dropdownButtonTint = getColorStateList(R.styleable.Dropdown_dropdownButtonTint)
                dropdownExpandedButtonSrc =
                    getDrawable(R.styleable.Dropdown_dropdownExpandedButtonSrc)
                dropdownCollapsedButtonSrc =
                    getDrawable(R.styleable.Dropdown_dropdownCollapsedButtonSrc)
                dropdownShowDivider = getBoolean(R.styleable.Dropdown_dropdownShowDivider, true)

                headerPadding = getDimension(R.styleable.Dropdown_headerPadding, 15f)
                headerTopPadding = getDimension(R.styleable.Dropdown_headerTopPadding, 0f)
                headerStartPadding =
                    getDimension(R.styleable.Dropdown_headerStartPadding, 0f)
                headerEndPadding = getDimension(R.styleable.Dropdown_headerEndPadding, 0f)
                headerBottomPadding =
                    getDimension(R.styleable.Dropdown_headerBottomPadding, 0f)
                headerBackgroundColor =
                    getColorStateList(R.styleable.Dropdown_headerBackgroundColor)

                contentPadding = getDimension(R.styleable.Dropdown_contentPadding, 15f)
                contentTopPadding = getDimension(R.styleable.Dropdown_contentTopPadding, 0f)
                contentStartPadding =
                    getDimension(R.styleable.Dropdown_contentStartPadding, 0f)
                contentEndPadding = getDimension(R.styleable.Dropdown_contentEndPadding, 0f)
                contentBottomPadding =
                    getDimension(R.styleable.Dropdown_contentBottomPadding, 0f)
                contentBackgroundColor =
                    getColorStateList(R.styleable.Dropdown_contentBackgroundColor)

                init()
                initBinding()
            } finally {
                recycle()
            }
        }
    }

    private fun init() {
        setHeaderPadding()
        setupHeaderBackgroundColor()
        setupDropdownTitle()
        setupCollapsedByDefault()
        setupDropdownButton()
        setupDropdownDivider()
        setContentPadding()
        setupContentBackgroundColor()
    }

    private fun initBinding() {
        dropdownHeader.setOnClickListener {
            toggleContentVisibility()
        }
    }

    private fun setupCollapsedByDefault() {
        if (dropdownCollapsedByDefault) {
            isContentVisible = false
            toggleContentVisibility()
        }
    }

    private fun setupDropdownTitle() {
        dropdownHeaderTitle.text = dropdownTitle
        if (dropdownTitleStyle != 0) dropdownHeaderTitle.setTextAppearance(dropdownTitleStyle)
    }

    private fun setupDropdownButton() {
        if (dropdownButtonTint != null) {
            val color: Int = dropdownButtonTint!!.getColorForState(
                dropdownHeaderButton.drawableState,
                dropdownButtonTint!!.defaultColor
            )

            dropdownHeaderButton.setColorFilter(color)
        } else {
            if (dropdownExpandedButtonSrc == null || dropdownCollapsedButtonSrc == null) {
                val color: Int = when (getDeviceUIMode(context)) {
                    DeviceUIModes.DAY -> Color.BLACK
                    DeviceUIModes.NIGHT -> Color.WHITE
                }

                dropdownHeaderButton.setColorFilter(color)
            }
        }

        setDropdownButtonSrc()
    }

    private fun setupDropdownDivider() {
        if (dropdownShowDivider) {
            dropdownHeaderDivider.visibility = View.VISIBLE
        } else {
            dropdownHeaderDivider.visibility = View.GONE
        }
    }

    private fun setHeaderPadding() {
        dropdownHeader.setPadding(
            headerPadding.toInt(),
            headerPadding.toInt(),
            headerPadding.toInt(),
            headerPadding.toInt()
        )

        if (headerTopPadding != 0f) {
            dropdownHeader.setPadding(
                dropdownHeader.paddingStart,
                headerTopPadding.toInt(),
                dropdownHeader.paddingEnd,
                dropdownHeader.paddingBottom
            )
        }

        if (headerStartPadding != 0f) {
            dropdownHeader.setPadding(
                headerStartPadding.toInt(),
                dropdownHeader.paddingTop,
                dropdownHeader.paddingEnd,
                dropdownHeader.paddingBottom
            )
        }

        if (headerEndPadding != 0f) {
            dropdownHeader.setPadding(
                dropdownHeader.paddingStart,
                dropdownHeader.paddingTop,
                headerEndPadding.toInt(),
                dropdownHeader.paddingBottom
            )
        }

        if (headerBottomPadding != 0f) {
            dropdownHeader.setPadding(
                dropdownHeader.paddingStart,
                dropdownHeader.paddingTop,
                dropdownHeader.paddingEnd,
                headerBottomPadding.toInt()
            )
        }
    }

    private fun setupHeaderBackgroundColor() {
        headerBackgroundColor?.let {
            val color: Int = it.getColorForState(dropdownHeader.drawableState, it.defaultColor)

            dropdownHeader.setBackgroundColor(color)
        }
    }

    private fun setContentPadding() {
        dropdownContent.setPadding(
            contentPadding.toInt(),
            contentPadding.toInt(),
            contentPadding.toInt(),
            contentPadding.toInt()
        )

        if (headerTopPadding != 0f) {
            dropdownContent.setPadding(
                dropdownContent.paddingStart,
                contentTopPadding.toInt(),
                dropdownContent.paddingEnd,
                dropdownContent.paddingBottom
            )
        }

        if (contentStartPadding != 0f) {
            dropdownContent.setPadding(
                contentStartPadding.toInt(),
                dropdownContent.paddingTop,
                dropdownContent.paddingEnd,
                dropdownContent.paddingBottom
            )
        }

        if (contentEndPadding != 0f) {
            dropdownContent.setPadding(
                dropdownContent.paddingStart,
                dropdownContent.paddingTop,
                contentEndPadding.toInt(),
                dropdownContent.paddingBottom
            )
        }

        if (contentBottomPadding != 0f) {
            dropdownContent.setPadding(
                dropdownContent.paddingStart,
                dropdownContent.paddingTop,
                dropdownContent.paddingEnd,
                contentBottomPadding.toInt()
            )
        }
    }

    private fun setupContentBackgroundColor() {
        contentBackgroundColor?.let {
            val color: Int = it.getColorForState(dropdownContent.drawableState, it.defaultColor)

            dropdownContent.setBackgroundColor(color)
        }
    }

    private fun toggleContentVisibility() {
        val animationDuration: Long = 600

        if (isContentVisible) {
            isContentVisible = false

            dropdownContentHeight = dropdownContent.height

            val animator = ValueAnimator.ofInt(dropdownContentHeight, 0)
            animator.duration = animationDuration
            animator.interpolator = AccelerateInterpolator(1.3f)

            animator.addUpdateListener { animation ->
                val animatedValue = animation.animatedValue as Int

                val layoutParams = dropdownContent.layoutParams
                layoutParams.height = animatedValue
                dropdownContent.layoutParams = layoutParams

                dropdownContent.requestLayout()
            }

            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    setDropdownButtonSrc()
                    dropdownContent.visibility = View.GONE
                }
            })

            animator.start()
        } else {
            isContentVisible = true

            val animator = ValueAnimator.ofInt(0, dropdownContentHeight)
            animator.duration = animationDuration
            animator.interpolator = AccelerateInterpolator(1.3f)

            animator.addUpdateListener { animation ->
                val animatedValue = animation.animatedValue as Int

                val layoutParams = dropdownContent.layoutParams
                layoutParams.height = animatedValue
                dropdownContent.layoutParams = layoutParams

                dropdownContent.requestLayout()
            }

            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    setDropdownButtonSrc()
                    dropdownContent.visibility = View.VISIBLE
                }
            })

            animator.start()
        }
    }

    private fun setDropdownButtonSrc() {
        if (isContentVisible) {
            val imageResource: Drawable =
                (dropdownExpandedButtonSrc ?: ContextCompat.getDrawable(
                    context,
                    R.drawable.baseline_keyboard_arrow_up_24
                )) as Drawable

            dropdownHeaderButton.setImageDrawable(imageResource)
        } else {
            val imageResource: Drawable =
                (dropdownCollapsedButtonSrc ?: ContextCompat.getDrawable(
                    context,
                    R.drawable.baseline_keyboard_arrow_down_24
                )) as Drawable

            dropdownHeaderButton.setImageDrawable(imageResource)
        }
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (childCount != 0) {
            val layoutParams = params as LayoutParams
            layoutParams.gravity = Gravity.TOP

            dropdownContent.addView(child, index, params)
        } else {
            super.addView(child, index, params)
        }
    }
}