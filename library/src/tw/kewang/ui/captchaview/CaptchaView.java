package tw.kewang.ui.captchaview;

import java.security.SecureRandom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author kewang
 */
public class CaptchaView extends TextView {
	private static final int LENGTH_DEFAULT = 4;
	private static final String PATTERN_DEFAULT = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static final Typeface[] TYPEFACES = { Typeface.DEFAULT, Typeface.DEFAULT_BOLD, Typeface.MONOSPACE, Typeface.SANS_SERIF, Typeface.SERIF };
	private static final Style[] STYLES = { Style.FILL, Style.FILL_AND_STROKE, Style.STROKE };

	private String pattern;
	private StringBuffer captcha;
	private int length;
	private Paint paint;
	private SecureRandom random;
	private float mTextSize = 100;
	private boolean mEnableLineBlur = true;
	private boolean mEnableDotBlur = true;

	/*
	 * Cache bitmap
	 */
	private Bitmap mBitmap;
	/*
	 * Cache canvas
	 */
	private Canvas mBitmapCanvas;

	public CaptchaView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init();
	}

	public CaptchaView(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	public CaptchaView(Context context) {
		super(context);

		init();
	}

	public void enableLineBlur(boolean enable) {
		mEnableLineBlur = enable;
	}

	public void enableDotBlur(boolean enable) {
		mEnableDotBlur = enable;
	}

	private void init() {
		paint = new Paint();
		random = new SecureRandom();
		captcha = new StringBuffer();

		pattern = PATTERN_DEFAULT;
		length = LENGTH_DEFAULT;

		buildCaptcha();
	}

	/**
	 * get captcha string
	 * 
	 * @return
	 */
	public String getCaptcha() {
		return captcha.toString();
	}

	/**
	 * refresh captcha string
	 */
	public void refresh() {
		buildCaptcha();
		invalidate();
	}

	/**
	 * set captcha pattern
	 * 
	 * @param pattern
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * set length
	 * 
	 * @param length
	 */
	public void setLength(int length) {
		this.length = length;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// redraw cache bitmap
		if (mBitmap != null) {
			canvas.drawBitmap(mBitmap, 0, 0, null);
			return;
		}

		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		int times;

		if (mBitmap == null) {
			mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
			mBitmapCanvas = new Canvas(mBitmap);
		}

		mBitmapCanvas.drawColor(getColor());

		paint.setAntiAlias(true);

		double avgWidth = width / length;// Average width for text region 
		double offsetX = avgWidth / length;// The offset to draw text position
		
		mTextSize = (float) avgWidth / 2;
		
		float x_pos = 0;
		float y_pos = height / 2;
		// text
		for (int i = 0; i < length; i++) {
			String s = String.valueOf(captcha.charAt(i));

			x_pos = (int) (avgWidth * i + offsetX);

			mBitmapCanvas.save();

			mBitmapCanvas.rotate(random.nextInt(90) - 45, x_pos, y_pos);
			// canvas.skew(random.nextFloat() - 0.5f, random.nextFloat() -
			// 0.5f);

			paint.setColor(getColor());
			paint.setTextSize(mTextSize);
			paint.setStyle(STYLES[random.nextInt(STYLES.length)]);
			paint.setTypeface(TYPEFACES[random.nextInt(TYPEFACES.length)]);

			mBitmapCanvas.drawText(s, x_pos, randomPos(mTextSize, y_pos), paint);

			mBitmapCanvas.restore();
		}

		// line
		if (mEnableLineBlur) {
			times = (int) (width * height * 0.00005);
			for (int i = 0; i < times; i++) {
				paint.setColor(getColor());
				paint.setStrokeWidth(random.nextInt(5));
				paint.setStyle(STYLES[random.nextInt(STYLES.length)]);

				mBitmapCanvas.drawLine(random.nextInt(width), random.nextInt(height), random.nextInt(width), random.nextInt(height), paint);
			}
		}

		// dot
		if (mEnableDotBlur) {
			times = (int) (width * height * 0.005);
			for (int i = 0; i < times; i++) {
				paint.setColor(getColor());
				paint.setStrokeWidth(3);
				paint.setStyle(STYLES[random.nextInt(STYLES.length)]);

				mBitmapCanvas.drawPoint(random.nextInt(width), random.nextInt(height), paint);
			}
		}

		canvas.drawBitmap(mBitmap, 0, 0, null);
	}
	
	private float randomPos(float lowBound, float uperBound) {
		return (float) (random.nextDouble() * uperBound) + lowBound;
	}

	private int getColor() {
		return Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
	}

	private void buildCaptcha() {
		if (mBitmap != null && !mBitmap.isRecycled()) {
			mBitmap.recycle();
			mBitmap = null;
		}
		System.gc();

		captcha.setLength(0);

		for (int i = 0; i < length; i++) {
			captcha.append(pattern.charAt(random.nextInt(pattern.length())));
		}
	}
}