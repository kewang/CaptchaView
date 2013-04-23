package tw.kewang.ui.captchaview;

import java.security.SecureRandom;

import android.content.Context;
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
	private static final Typeface[] TYPEFACES = { Typeface.DEFAULT,
			Typeface.DEFAULT_BOLD, Typeface.MONOSPACE, Typeface.SANS_SERIF,
			Typeface.SERIF };
	private static final Style[] STYLES = { Style.FILL, Style.FILL_AND_STROKE,
			Style.STROKE };

	private String pattern;
	private StringBuffer captcha;
	private int length;
	private Paint paint;
	private SecureRandom random;

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
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		int times;

		canvas.drawColor(getColor());

		paint.setAntiAlias(true);

		// text
		for (int i = 0; i < length; i++) {
			String s = String.valueOf(captcha.charAt(i));
			int x = width / length * i;
			int y = height / 2;

			canvas.save();

			canvas.rotate(random.nextInt(180) - 90, x, y);
			canvas.skew(random.nextFloat() - 0.5f, random.nextFloat() - 0.5f);

			paint.setColor(getColor());
			paint.setTextSize(100);
			paint.setStyle(STYLES[random.nextInt(STYLES.length)]);
			paint.setTypeface(TYPEFACES[random.nextInt(TYPEFACES.length)]);

			canvas.drawText(s, x, y, paint);

			canvas.restore();
		}

		// line
		times = (int) (width * height * 0.00005);
		for (int i = 0; i < times; i++) {
			paint.setColor(getColor());
			paint.setStrokeWidth(random.nextInt(5));
			paint.setStyle(STYLES[random.nextInt(STYLES.length)]);

			canvas.drawLine(random.nextInt(width), random.nextInt(height),
					random.nextInt(width), random.nextInt(height), paint);
		}

		// dot
		times = (int) (width * height * 0.005);
		for (int i = 0; i < times; i++) {
			paint.setColor(getColor());
			paint.setStrokeWidth(3);
			paint.setStyle(STYLES[random.nextInt(STYLES.length)]);

			canvas.drawPoint(random.nextInt(width), random.nextInt(height),
					paint);
		}
	}

	private int getColor() {
		return Color.rgb(random.nextInt(256), random.nextInt(256),
				random.nextInt(256));
	}

	private void buildCaptcha() {
		captcha.setLength(0);

		for (int i = 0; i < length; i++) {
			captcha.append(pattern.charAt(random.nextInt(pattern.length())));
		}
	}
}