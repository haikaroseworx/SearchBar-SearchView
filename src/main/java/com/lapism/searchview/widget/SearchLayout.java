package com.lapism.searchview.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lapism.searchview.R;
import com.lapism.searchview.Search;
import com.lapism.searchview.graphics.SearchArrowDrawable;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

@RestrictTo(LIBRARY_GROUP)
public abstract class SearchLayout extends FrameLayout implements View.OnClickListener {

    @Search.Logo
    protected int mLogo;
    @Search.Shape
    protected int mShape;
    @Search.Theme
    protected int mTheme;

    protected CharSequence mQueryText = "";
    protected int mTextStyle = Typeface.NORMAL;
    protected boolean mIconAnimation = true;
    protected Typeface mTextFont = Typeface.DEFAULT;

    protected Context mContext;
    protected CardView mCardView;
    protected ImageView mImageViewLogo;
    protected ImageView mImageViewMic;
    protected ImageView mImageViewClear;
    protected ImageView mImageViewMenu;
    protected LinearLayout mLinearLayout;
    protected SearchEditText mSearchEditText;
    protected SearchArrowDrawable mSearchArrowDrawable;

    protected Search.OnMicClickListener mOnMicClickListener;
    protected Search.OnMenuClickListener mOnMenuClickListener;
    protected Search.OnQueryTextListener mOnQueryTextListener;

    // ---------------------------------------------------------------------------------------------
    public SearchLayout(@NonNull Context context) {
        super(context);
    }

    public SearchLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SearchLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    // ---------------------------------------------------------------------------------------------
    protected abstract void onTextChanged(CharSequence s);

    protected abstract void addFocus();

    protected abstract void removeFocus();

    protected abstract boolean isView();

    protected abstract int getLayout();

    public abstract void open();

    public abstract void close();

    // ---------------------------------------------------------------------------------------------
    @CallSuper
    protected void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        mContext = context;

        mCardView = findViewById(R.id.search_cardView);

        mLinearLayout = findViewById(R.id.search_linearLayout);

        mImageViewLogo = findViewById(R.id.search_imageView_logo);
        mImageViewLogo.setOnClickListener(this);

        mImageViewMic = findViewById(R.id.search_imageView_mic);
        mImageViewMic.setVisibility(View.GONE);
        mImageViewMic.setOnClickListener(this);

        mImageViewMenu = findViewById(R.id.search_imageView_menu);
        mImageViewMenu.setVisibility(View.GONE);
        mImageViewMenu.setOnClickListener(this);

        mSearchEditText = findViewById(R.id.search_searchEditText);
        mSearchEditText.setVisibility(View.GONE);
        mSearchEditText.setLayout(this);
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SearchLayout.this.onTextChanged(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                onSubmitQuery();
                return true;
            }
        });
        mSearchEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    addFocus();
                } else {
                    removeFocus();
                }
            }
        });
    }

    // ---------------------------------------------------------------------------------------------
    @Search.Logo
    public int getLogo() {
        return mLogo;
    }

    public void setLogo(@Search.Logo int logo) {
        mLogo = logo;

        switch (mLogo) {
            case Search.Logo.GOOGLE:
                int left = getResources().getDimensionPixelSize(R.dimen.search_logo_padding_left);
                int top = getContext().getResources().getDimensionPixelSize(R.dimen.search_logo_padding_top);
                int right = mContext.getResources().getDimensionPixelSize(R.dimen.search_logo_padding_right);
                int bottom = 0;

                mImageViewLogo.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.search_ic_google_color));
                mImageViewLogo.setPadding(left, top, right, bottom);
                break;
            case Search.Logo.G:
                mImageViewLogo.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.search_ic_g_color_24dp));
                break;
            case Search.Logo.HAMBURGER_ARROW:
                mSearchArrowDrawable = new SearchArrowDrawable(mContext);
                mImageViewLogo.setImageDrawable(mSearchArrowDrawable);
                break;
            case Search.Logo.ARROW:
                mImageViewLogo.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.search_ic_arrow_back_black_24dp));
                break;
        }
    }

    @Search.Shape
    public int getShape() {
        return mShape;
    }

    public void setShape(@Search.Shape int shape) {
        mShape = shape;

        switch (mShape) {
            case Search.Shape.CLASSIC:
                mCardView.setRadius(getResources().getDimensionPixelSize(R.dimen.search_shape_classic));
                break;
            case Search.Shape.ROUNDED:
                mCardView.setRadius(getResources().getDimensionPixelSize(R.dimen.search_shape_rounded));
                break;
            case Search.Shape.OVAL:
                if (!isView()) {
                    mCardView.setRadius(getResources().getDimensionPixelSize(R.dimen.search_shape_oval));
                }
                break;
        }
    }

    @Search.Theme
    public int getTheme() {
        return mTheme;
    }

    public void setTheme(@Search.Theme int theme) {
        mTheme = theme;

        switch (mTheme) {
            case Search.Theme.PLAY:
                setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_play_background));
                setDividerColor(ContextCompat.getColor(mContext, R.color.search_play_divider));
                setLogoColor(ContextCompat.getColor(mContext, R.color.search_play_icon));
                setMicColor(ContextCompat.getColor(mContext, R.color.search_play_icon));
                setClearColor(ContextCompat.getColor(mContext, R.color.search_play_icon));
                setMenuColor(ContextCompat.getColor(mContext, R.color.search_play_icon));
                setHintColor(ContextCompat.getColor(mContext, R.color.search_play_hint));
                setTextColor(ContextCompat.getColor(mContext, R.color.search_play_title));
                break;
            case Search.Theme.COLOR:
                setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_color_background));
                setDividerColor(ContextCompat.getColor(mContext, R.color.search_color_divider));
                clearIconsColor();
                setClearColor(ContextCompat.getColor(mContext, R.color.search_color_icon));
                setMenuColor(ContextCompat.getColor(mContext, R.color.search_color_menu));
                setHintColor(ContextCompat.getColor(mContext, R.color.search_color_hint));
                setTextColor(ContextCompat.getColor(mContext, R.color.search_color_title));
                break;
            case Search.Theme.LIGHT:
                setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_light_background));
                setDividerColor(ContextCompat.getColor(mContext, R.color.search_light_divider));
                setLogoColor(ContextCompat.getColor(mContext, R.color.search_light_icon));
                setMicColor(ContextCompat.getColor(mContext, R.color.search_light_icon));
                setClearColor(ContextCompat.getColor(mContext, R.color.search_light_icon));
                setMenuColor(ContextCompat.getColor(mContext, R.color.search_light_icon));
                setHintColor(ContextCompat.getColor(mContext, R.color.search_light_hint));
                setTextColor(ContextCompat.getColor(mContext, R.color.search_light_title));
                break;
            case Search.Theme.DARK:
                setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_dark_background));
                setDividerColor(ContextCompat.getColor(mContext, R.color.search_dark_divider));
                setLogoColor(ContextCompat.getColor(mContext, R.color.search_dark_icon));
                setMicColor(ContextCompat.getColor(mContext, R.color.search_dark_icon));
                setClearColor(ContextCompat.getColor(mContext, R.color.search_dark_icon));
                setMenuColor(ContextCompat.getColor(mContext, R.color.search_dark_icon));
                setHintColor(ContextCompat.getColor(mContext, R.color.search_dark_hint));
                setTextColor(ContextCompat.getColor(mContext, R.color.search_dark_title));
                break;
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Logo
    public void setLogoIcon(@DrawableRes int resource) {
        mImageViewLogo.setImageResource(resource);
    }

    public void setLogoIcon(@Nullable Drawable drawable) {
        if (drawable != null) {
            mImageViewLogo.setImageDrawable(drawable);
        } else {
            mImageViewLogo.setVisibility(GONE);
        }
    }

    public void setLogoColor(@ColorInt int color) {
        mImageViewLogo.setColorFilter(color);
    }

    // Mic
    public void setMicIcon(@DrawableRes int resource) {
        mImageViewMic.setImageResource(resource);
    }

    public void setMicIcon(@Nullable Drawable drawable) {
        mImageViewMic.setImageDrawable(drawable);
    }

    public void setMicColor(@ColorInt int color) {
        mImageViewMic.setColorFilter(color);
    }

    // Menu
    public void setMenuIcon(@DrawableRes int resource) {
        mImageViewMenu.setImageResource(resource);
    }

    public void setMenuIcon(@Nullable Drawable drawable) {
        mImageViewMenu.setImageDrawable(drawable);
    }

    public void setMenuColor(@ColorInt int color) {
        mImageViewMenu.setColorFilter(color);
    }

    // Text
    public void setTextImeOptions(int imeOptions) {
        mSearchEditText.setImeOptions(imeOptions);
    }

    public void setTextInputType(int inputType) {
        mSearchEditText.setInputType(inputType);
    }

    public Editable getText() {
        return mSearchEditText.getText();
    }

    public void setText(CharSequence text) {
        mSearchEditText.setText(text);
    }

    public void setText(@StringRes int text) {
        mSearchEditText.setText(text);
    }

    public void setTextColor(@ColorInt int color) {
        mSearchEditText.setTextColor(color);
    }

    public void setTextSize(float size) {
        mSearchEditText.setTextSize(size);
    }

    /**
     * Typeface.NORMAL
     * Typeface.BOLD
     * Typeface.ITALIC
     * Typeface.BOLD_ITALIC
     */
    public void setTextStyle(int style) {
        mTextStyle = style;
        mSearchEditText.setTypeface((Typeface.create(mTextFont, mTextStyle)));
    }

    /**
     * Typeface.DEFAULT
     * Typeface.DEFAULT_BOLD
     * Typeface.SANS_SERIF
     * Typeface.SERIF
     * Typeface.MONOSPACE
     */
    public void setTextFont(Typeface font) {
        mTextFont = font;
        mSearchEditText.setTypeface((Typeface.create(mTextFont, mTextStyle)));
    }

    public void setTextGravity(int gravity) {
        mSearchEditText.setGravity(gravity);
    }

    // Hint
    public void setHint(CharSequence hint) {
        mSearchEditText.setHint(hint);
    }

    public void setHint(@StringRes int hint) {
        mSearchEditText.setHint(hint);
    }

    public void setHintColor(@ColorInt int color) {
        mSearchEditText.setHintTextColor(color);
    }

    // Query
    public Editable getQuery() {
        return mSearchEditText.getText();
    }

    public void setQuery(CharSequence query, boolean submit) {
        mSearchEditText.setText(query);
        if (query != null) {
            mSearchEditText.setSelection(mSearchEditText.length());
            mQueryText = query;
        }

        if (submit && !TextUtils.isEmpty(query)) {
            onSubmitQuery();
        }
    }

    public void setQuery(@StringRes int query, boolean submit) {
        mSearchEditText.setText(query);
        if (query != 0) {
            mSearchEditText.setSelection(mSearchEditText.length());
            mQueryText = String.valueOf(query);
        }

        if (submit && !(String.valueOf(query).isEmpty())) {
            onSubmitQuery();
        }
    }

    // Height
    public int getCustomHeight() {
        ViewGroup.LayoutParams params = mLinearLayout.getLayoutParams();
        return params.height;
    }

    public void setCustomHeight(int height) {
        ViewGroup.LayoutParams params = mLinearLayout.getLayoutParams();
        params.height = height;
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        mLinearLayout.setLayoutParams(params);
    }

    // Overrides
    @Override
    public void setElevation(float elevation) {
        mCardView.setMaxCardElevation(elevation);
        mCardView.setCardElevation(elevation);
    }

    @Override
    public void setBackgroundColor(@ColorInt int color) {
        mCardView.setCardBackgroundColor(color);
    }

    @Override
    public void setAlpha(@FloatRange(from = 0.5, to = 1.0) float alpha) {
        super.setAlpha(alpha);
    }

    // Others
    public boolean isOpen() {
        return getVisibility() == View.VISIBLE;
    }

    public void open(boolean iconAnimation) {
        mIconAnimation = iconAnimation;
        open();
    }

    public void close(boolean iconAnimation) {
        mIconAnimation = iconAnimation;
        close();
    }

    // Listeners
    public void setOnMicClickListener(Search.OnMicClickListener listener) {
        mOnMicClickListener = listener;
        if (mOnMicClickListener != null) {
            mImageViewMic.setVisibility(View.VISIBLE);
            if (mTheme == Search.Theme.COLOR) {
                mImageViewMic.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.search_ic_mic_color_24dp));
            } else {
                mImageViewMic.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.search_ic_mic_black_24dp));
            }
        } else {
            mImageViewMic.setVisibility(View.GONE);
        }
    }

    public void setOnMenuClickListener(Search.OnMenuClickListener listener) {
        mOnMenuClickListener = listener;
        if (mOnMenuClickListener != null) {
            mImageViewMenu.setVisibility(View.VISIBLE);
            mImageViewMenu.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.search_ic_menu_black_24dp));
        } else {
            mImageViewMenu.setVisibility(View.GONE);
        }
    }

    public void setOnQueryTextListener(Search.OnQueryTextListener listener) {
        mOnQueryTextListener = listener;
    }

    // ---------------------------------------------------------------------------------------------
    protected void setDividerColor(@ColorInt int color) {

    }

    protected void setClearColor(@ColorInt int color) {

    }

    protected void showKeyboard() {
        if (!isInEditMode()) {
            InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.showSoftInput(mSearchEditText, 0);
            }
        }
    }

    protected void hideKeyboard() {
        if (!isInEditMode()) {
            InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            }
        }
    }

    // ---------------------------------------------------------------------------------------------
    private void clearIconsColor() {
        mImageViewLogo.clearColorFilter();
        mImageViewMic.clearColorFilter();
        if (mImageViewClear != null) {
            mImageViewClear.clearColorFilter();
        }
    }

    private void onSubmitQuery() {
        CharSequence query = mSearchEditText.getText();
        if (query != null && TextUtils.getTrimmedLength(query) > 0) {
            // dispatchFilters(); todo
            if (mOnQueryTextListener == null || !mOnQueryTextListener.onQueryTextSubmit(query.toString())) {
                mSearchEditText.setText(query);
                // dismissSuggestions();
            }
        }
    }

}
